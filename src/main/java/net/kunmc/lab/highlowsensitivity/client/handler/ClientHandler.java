package net.kunmc.lab.highlowsensitivity.client.handler;

import net.kunmc.lab.highlowsensitivity.client.data.SensitivityClientManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientHandler {
    @SubscribeEvent
    public static void onLogout(WorldEvent.Unload e) {
        if (e.getWorld().isClientSide()) {
            SensitivityClientManager.getInstance().reset();
        }
    }
}
