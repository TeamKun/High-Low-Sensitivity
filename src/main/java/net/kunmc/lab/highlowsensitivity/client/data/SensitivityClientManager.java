package net.kunmc.lab.highlowsensitivity.client.data;


public class SensitivityClientManager {
    private static final SensitivityClientManager INSTANCE = new SensitivityClientManager();

    public static SensitivityClientManager getInstance() {
        return INSTANCE;
    }

    public boolean isLocked() {
        return true;
    }

}
