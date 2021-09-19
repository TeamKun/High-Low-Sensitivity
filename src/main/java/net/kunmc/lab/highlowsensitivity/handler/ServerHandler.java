package net.kunmc.lab.highlowsensitivity.handler;

import net.kunmc.lab.highlowsensitivity.command.HLSCommands;
import net.kunmc.lab.highlowsensitivity.data.SensitivityManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerHandler {
    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent e) {
        HLSCommands.registerCommand(e.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (!e.getPlayer().level.isClientSide()) {
            SensitivityManager.getInstance().sendUpdatePacket((ServerPlayerEntity) e.getPlayer());
        }
    }
}
