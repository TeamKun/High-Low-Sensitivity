package net.kunmc.lab.highlowsensitivity.client.handler;

import net.kunmc.lab.highlowsensitivity.client.data.SensitivityClientManager;
import net.kunmc.lab.highlowsensitivity.packet.SensitivityMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SensitivityMessageHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void reversiveMessage(SensitivityMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        SensitivityClientManager scm = SensitivityClientManager.getInstance();

        if (message.reset) {
            scm.reset();
            return;
        }

        scm.setLocked(message.locked);
        if (message.sensitivity >= 0) {
            if (scm.getOldSensitivity() < 0)
                scm.setOldSensitivity(mc.options.sensitivity);
            mc.options.sensitivity = message.sensitivity;
        } else if (scm.getOldSensitivity() >= 0) {
            mc.options.sensitivity = scm.getOldSensitivity();
            scm.setOldSensitivity(-1);
        }
    }
}
