package net.kunmc.lab.highlowsensitivity.packet;

import net.kunmc.lab.highlowsensitivity.HighLowSensitivity;
import net.kunmc.lab.highlowsensitivity.client.handler.SensitivityMessageHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(HighLowSensitivity.MODID, HighLowSensitivity.MODID + "_channel")).clientAcceptedVersions(a -> true).serverAcceptedVersions(a -> true).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
    private static int next = 0;

    public static void init() {
        INSTANCE.registerMessage(next++, SensitivityMessage.class, SensitivityMessage::encodeMessege, SensitivityMessage::decodeMessege, SensitivityMessageHandler::reversiveMessage);
    }
}
