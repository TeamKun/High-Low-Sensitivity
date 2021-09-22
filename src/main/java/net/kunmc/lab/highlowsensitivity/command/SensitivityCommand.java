package net.kunmc.lab.highlowsensitivity.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.kunmc.lab.highlowsensitivity.ServerConfig;
import net.kunmc.lab.highlowsensitivity.data.SensitivityManager;
import net.kunmc.lab.highlowsensitivity.data.SensitivityState;
import net.kunmc.lab.highlowsensitivity.packet.PacketHandler;
import net.kunmc.lab.highlowsensitivity.packet.SensitivityMessage;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.*;

public class SensitivityCommand {
    private static final Random random = new Random();

    public static void register(CommandDispatcher<CommandSource> d) {
        d.register(Commands.literal("sensitivity").requires((source) -> source.hasPermission(2))
                .then(Commands.literal("lock").then(Commands.argument("target", EntityArgument.players()).executes((context -> lockedPlayer(context.getSource(), EntityArgument.getPlayers(context, "target"), true)))))
                .then(Commands.literal("unlock").then(Commands.argument("target", EntityArgument.players()).executes((context -> lockedPlayer(context.getSource(), EntityArgument.getPlayers(context, "target"), false)))))
                .then(Commands.literal("set").then(Commands.argument("value", DoubleArgumentType.doubleArg(0, 0x114514)).then(Commands.argument("target", EntityArgument.players()).executes((context -> setSensitivity(context.getSource(), EntityArgument.getPlayers(context, "target"), DoubleArgumentType.getDouble(context, "value")))))))
                .then(Commands.literal("reset").executes((context -> reset(context.getSource()))))
                .then(Commands.literal("random").then(Commands.argument("target", EntityArgument.players()).executes((context -> setRandom(context.getSource(), EntityArgument.getPlayers(context, "target"))))))
                .then(Commands.literal("mode").executes((context -> reset(context.getSource())))
                                .then(Commands.literal("off").executes(context -> setMode(context.getSource(), false)))
                                .then(Commands.literal("on").executes(context -> setMode(context.getSource(), true)))
                        //        .then(Commands.literal("low").then(Commands.argument("value", IntegerArgumentType.integer()).executes(context -> setMode(context.getSource(), SensitivityManager.Mode.LOW, IntegerArgumentType.getInteger(context, "value")))))
                        //        .then(Commands.literal("random").then(Commands.argument("value", IntegerArgumentType.integer()).executes(context -> setMode(context.getSource(), SensitivityManager.Mode.RANDOM, IntegerArgumentType.getInteger(context, "value")))))
                ));
    }

    private static int setRandom(CommandSource src, Collection<ServerPlayerEntity> players) {
        SensitivityManager sm = SensitivityManager.getInstance();
        sm.reset();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        for (ServerPlayerEntity player : players) {
            double hs = ServerConfig.HIGH_SENSITIVITY.get() / 200d;
            double ls = ServerConfig.LOW_SENSITIVITY.get() / 200d;
            boolean r = random.nextBoolean();
            sm.getSensitivityState(player.getGameProfile().getId()).setFixedSensitivity(r ? hs : ls);
            sm.getSensitivityState(player.getGameProfile().getId()).setLocked(true);
            sm.sendUpdatePacket(player);
            sb.append(player.getGameProfile().getName());
            sb.append("[").append(r ? "HIGH" : "LOW").append("]");
            sb.append(" ");
            i++;
        }
        src.sendSuccess(new StringTextComponent(sb + "にしました"), true);
        return i;
    }

    private static int setMode(CommandSource src, boolean enable) {
        SensitivityManager sm = SensitivityManager.getInstance();
        String str = enable ? "有効" : "無効";
        if (sm.isEnableMode() == enable) {
            src.sendFailure(new StringTextComponent("すでに" + str + "です"));
            return 1;
        }
        sm.reset();
        sm.setEnableMode(enable);
        List<ServerPlayerEntity> players = new ArrayList<>(src.getServer().getPlayerList().getPlayers());
        if (enable) {
            Collections.shuffle(players, random);

            int hc = ServerConfig.HIGH_SENSITIVITY_CONT.get();
            int lc = ServerConfig.LOW_SENSITIVITY_CONT.get();

            double hs = ServerConfig.HIGH_SENSITIVITY.get() / 200d;
            double ls = ServerConfig.LOW_SENSITIVITY.get() / 200d;
            StringBuilder sb = new StringBuilder();
            sb.append(" ");
            for (int i = 0; i < Math.min(hc + lc, players.size()); i++) {
                ServerPlayerEntity player = players.get(i);
                double sensy;
                if (hc > 0 && lc > 0) {
                    if (random.nextBoolean()) {
                        sensy = hs;
                        hc--;
                    } else {
                        sensy = ls;
                        lc--;
                    }
                } else if (hc > 0) {
                    sensy = hs;
                    hc--;
                } else {
                    sensy = ls;
                    lc--;
                }
                sb.append(player.getGameProfile().getName());
                sb.append("[").append(sensy == ls ? "LOW" : "HIGH").append("]");
                sb.append(" ");

                sm.getSensitivityState(player.getGameProfile().getId()).setFixedSensitivity(sensy);
            }
            src.sendSuccess(new StringTextComponent("モードを有効し" + sb + "を対象に選びました"), true);
        } else {
            src.sendSuccess(new StringTextComponent("モードを無効にしました"), true);
        }
        for (ServerPlayerEntity player : players) {
            sm.getSensitivityState(player.getGameProfile().getId()).setLocked(enable);
            sm.sendUpdatePacket(player);
        }
        return 1;
    }
/*
    private static int setMode(CommandSource src, SensitivityManager.Mode mode, int num) {
        SensitivityManager sm = SensitivityManager.getInstance();
        SensitivityManager.Mode old = sm.getMode();
        if (old == mode & mode == SensitivityManager.Mode.NONE) {
            src.sendFailure(new StringTextComponent("すでに通常モードです"));
            return 1;
        }
        List<ServerPlayerEntity> players = new ArrayList<>(src.getServer().getPlayerList().getPlayers());
        if (mode != SensitivityManager.Mode.NONE) {
            if (players.size() < num) {
                src.sendFailure(new StringTextComponent("指定した人数が現在のプレイヤー数を越しています"));
                return 1;
            }
            if (num == 0) {
                src.sendFailure(new StringTextComponent("指定した人数が0です"));
                return 1;
            }
        }

        sm.reset();
        if (mode == SensitivityManager.Mode.NONE) {
            src.sendSuccess(new StringTextComponent("モードを通常にしました"), true);
            for (ServerPlayerEntity player : players) {
                sm.sendUpdatePacket(player);
            }
            return 1;
        }
        sm.setMode(mode);

        Collections.shuffle(players, random);
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        for (int i = 0; i < num; i++) {
            ServerPlayerEntity player = players.get(i);
            double sens = mode.getSensitivity().get();
            sm.getSensitivityState(player.getGameProfile().getId()).setFixedSensitivity(sens);
            sb.append(player.getGameProfile().getName());
            if (mode == SensitivityManager.Mode.RANDOM)
                sb.append("[").append(sens == SensitivityManager.Mode.LOW.getSensitivity().get() ? SensitivityManager.Mode.LOW.name() : SensitivityManager.Mode.HIGH.name()).append("]");
            sb.append(" ");
        }

        for (ServerPlayerEntity player : players) {
            sm.getSensitivityState(player.getGameProfile().getId()).setLocked(true);
            sm.sendUpdatePacket(player);
        }
        src.sendSuccess(new StringTextComponent("モードを" + mode.name() + "に変更し" + sb + "を対象に選びました"), true);

        return 1;
    }*/

    private static int lockedPlayer(CommandSource src, Collection<ServerPlayerEntity> players, boolean locked) {
        int i = 0;
        SensitivityManager sm = SensitivityManager.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        for (ServerPlayerEntity player : players) {
            SensitivityState state = sm.getSensitivityState(player.getGameProfile().getId());
            if (state.isLocked() == locked) {
                continue;
            }

            state.setLocked(locked);

            if (!locked)
                state.setFixedSensitivity(-1);
            sm.sendUpdatePacket(player);
            i++;
            sb.append(player.getGameProfile().getName()).append(" ");
        }
        String str = (locked ? "固定" : "固定を解除");
        if (i > 0)
            src.sendSuccess(new StringTextComponent(sb + "の感度を" + str + "しました。"), true);
        else
            src.sendSuccess(new StringTextComponent(str + "できるプレイヤーが存在しません"), true);

        return i;
    }

    private static int setSensitivity(CommandSource src, Collection<ServerPlayerEntity> players, double sensitivity) {
        int i = 0;
        SensitivityManager sm = SensitivityManager.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        for (ServerPlayerEntity player : players) {
            SensitivityState state = sm.getSensitivityState(player.getGameProfile().getId());
            state.setLocked(true);
            state.setFixedSensitivity(sensitivity / 200d);
            sm.sendUpdatePacket(player);
            i++;
            sb.append(player.getGameProfile().getName()).append(" ");
        }
        if (i > 0)
            src.sendSuccess(new StringTextComponent(sb + "の感度を" + sensitivity + "%に固定しました。"), true);
        else
            src.sendSuccess(new StringTextComponent("感度を変更できるプレイヤーが存在しません"), true);

        return i;
    }

    private static int reset(CommandSource src) {
        SensitivityManager.getInstance().reset();
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SensitivityMessage(true));
        src.sendSuccess(new StringTextComponent("感度をすべてリセットしました"), true);
        return 1;
    }
}
