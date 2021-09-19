package net.kunmc.lab.highlowsensitivity;

import net.kunmc.lab.highlowsensitivity.client.ClientConfig;
import net.kunmc.lab.highlowsensitivity.client.handler.ClientHandler;
import net.kunmc.lab.highlowsensitivity.handler.ServerHandler;
import net.kunmc.lab.highlowsensitivity.packet.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HighLowSensitivity.MODID)
public class HighLowSensitivity {
    public static final String MODID = "highlowsensitivity";

    public HighLowSensitivity() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        PacketHandler.init();
        MinecraftForge.EVENT_BUS.register(ServerHandler.class);
        ClientConfig.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(ClientHandler.class);
    }
}
