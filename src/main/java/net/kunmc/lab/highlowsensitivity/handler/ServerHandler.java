package net.kunmc.lab.highlowsensitivity.handler;

import com.google.common.collect.ImmutableList;
import io.netty.util.AttributeKey;
import net.kunmc.lab.highlowsensitivity.HighLowSensitivity;
import net.kunmc.lab.highlowsensitivity.command.HLSCommands;
import net.kunmc.lab.highlowsensitivity.data.SensitivityManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.FMLConnectionData;
import net.minecraftforge.fml.network.NetworkHooks;

public class ServerHandler {
    public static final AttributeKey<FMLConnectionData> FML_CONNECTION_DATA = AttributeKey.valueOf("fml:conndata");

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent e) {
        HLSCommands.registerCommand(e.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (!e.getPlayer().level.isClientSide()) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) e.getPlayer();

            if (NetworkHooks.getConnectionType(() -> serverPlayer.connection.getConnection()).isVanilla()) {
                serverPlayer.connection.disconnect(new StringTextComponent("申し訳ないがバニラでログインはNG"));
                return;
            }

            ImmutableList<String> st = serverPlayer.connection.getConnection().channel().attr(FML_CONNECTION_DATA).get().getModList();
            if (!st.contains(HighLowSensitivity.MODID)) {
                serverPlayer.connection.disconnect(new StringTextComponent("(MODが足り)ないです。"));
                return;
            }

            SensitivityManager sm = SensitivityManager.getInstance();
            if (sm.isEnableMode())
                sm.getSensitivityState(e.getPlayer().getGameProfile().getId()).setLocked(true);
            sm.sendUpdatePacket((ServerPlayerEntity) e.getPlayer());
        }
    }
}
