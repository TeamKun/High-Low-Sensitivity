package net.kunmc.lab.highlowsensitivity.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class SensitivityState implements INBTSerializable<CompoundNBT> {
    private boolean locked;
    private double fixedSensitivity;

    public SensitivityState(boolean locked, double fixedSensitivity) {
        this.locked = locked;
        this.fixedSensitivity = fixedSensitivity;
    }

    public SensitivityState(CompoundNBT tag) {
        deserializeNBT(tag);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("Locked", locked);
        tag.putDouble("FixedSensitivity", fixedSensitivity);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        this.locked = tag.getBoolean("Locked");
        this.fixedSensitivity = tag.getDouble("FixedSensitivity");
    }

    public double getFixedSensitivity() {
        return fixedSensitivity;
    }

    public void setFixedSensitivity(double fixedSensitivity) {
        this.fixedSensitivity = fixedSensitivity;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }
}
