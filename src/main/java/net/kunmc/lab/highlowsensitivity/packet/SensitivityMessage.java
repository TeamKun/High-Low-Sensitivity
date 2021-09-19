package net.kunmc.lab.highlowsensitivity.packet;

import net.minecraft.network.PacketBuffer;

public class SensitivityMessage {
    public boolean reset;
    public boolean locked;
    public double sensitivity;

    public SensitivityMessage(boolean reset) {
        this(reset, false, -1);
    }

    public SensitivityMessage(boolean reset, boolean locked, double sensitivity) {
        this.reset = reset;
        this.locked = locked;
        this.sensitivity = sensitivity;
    }

    public SensitivityMessage(boolean locked, double sensitivity) {
        this(false, locked, sensitivity);
    }

    public static SensitivityMessage decodeMessege(PacketBuffer buffer) {
        return new SensitivityMessage(buffer.readBoolean(), buffer.readBoolean(), buffer.readDouble());
    }

    public static void encodeMessege(SensitivityMessage messegeIn, PacketBuffer buffer) {
        buffer.writeBoolean(messegeIn.reset);
        buffer.writeBoolean(messegeIn.locked);
        buffer.writeDouble(messegeIn.sensitivity);
    }
}
