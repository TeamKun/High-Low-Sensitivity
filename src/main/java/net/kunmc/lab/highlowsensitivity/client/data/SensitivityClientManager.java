package net.kunmc.lab.highlowsensitivity.client.data;


import net.kunmc.lab.highlowsensitivity.client.ClientConfig;
import net.minecraft.client.Minecraft;

public class SensitivityClientManager {
    private static final SensitivityClientManager INSTANCE = new SensitivityClientManager();
    private static final Minecraft mc = Minecraft.getInstance();
    private boolean locked;

    public static SensitivityClientManager getInstance() {
        return INSTANCE;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setOldSensitivity(double oldSensitivity) {
        ClientConfig.OLD_SENSITIVITY.set(oldSensitivity);
    }

    public double getOldSensitivity() {
        return ClientConfig.OLD_SENSITIVITY.get();
    }

    public void reset() {
        if (locked && getOldSensitivity() >= 0) {
            mc.options.sensitivity = getOldSensitivity();
            setOldSensitivity(-1);
        }
        locked = false;
    }
}
