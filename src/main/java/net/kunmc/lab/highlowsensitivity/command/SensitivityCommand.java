package net.kunmc.lab.highlowsensitivity.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
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

import java.util.Collection;

public class SensitivityCommand {
    public static void register(CommandDispatcher<CommandSource> d) {
        d.register(Commands.literal("sensitivity").requires((source) -> source.hasPermission(2))
                .then(Commands.literal("lock").then(Commands.argument("target", EntityArgument.players()).executes((context -> lockedPlayer(context.getSource(), EntityArgument.getPlayers(context, "target"), true)))))
                .then(Commands.literal("unlock").then(Commands.argument("target", EntityArgument.players()).executes((context -> lockedPlayer(context.getSource(), EntityArgument.getPlayers(context, "target"), false)))))
                .then(Commands.literal("set").then(Commands.argument("value", DoubleArgumentType.doubleArg(0, 1)).then(Commands.argument("target", EntityArgument.players()).executes((context -> setSensitivity(context.getSource(), EntityArgument.getPlayers(context, "target"), DoubleArgumentType.getDouble(context, "value")))))))
                .then(Commands.literal("reset").executes((context -> reset(context.getSource())))
                ));
    }

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
            state.setFixedSensitivity(sensitivity);
            sm.sendUpdatePacket(player);
            i++;
            sb.append(player.getGameProfile().getName()).append(" ");
        }
        if (i > 0)
            src.sendSuccess(new StringTextComponent(sb + "の感度を" + (sensitivity * 200) + "%に固定しました。"), true);
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
