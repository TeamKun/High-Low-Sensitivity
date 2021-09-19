package net.kunmc.lab.highlowsensitivity.data;

import net.kunmc.lab.highlowsensitivity.packet.PacketHandler;
import net.kunmc.lab.highlowsensitivity.packet.SensitivityMessage;
import net.kunmc.lab.highlowsensitivity.util.NBTUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.SharedConstants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

public class SensitivityManager implements INBTSerializable<CompoundNBT> {
    private static final SensitivityManager INSTANCE = new SensitivityManager();
    private static final Map<UUID, SensitivityState> playerStates = new HashMap<>();
    private static final Random random = new Random();
    private Mode mode = Mode.NONE;

    public static SensitivityManager getInstance() {
        return INSTANCE;
    }

    public SensitivityState getSensitivityState(UUID uuid) {
        if (!playerStates.containsKey(uuid))
            playerStates.put(uuid, new SensitivityState(false, -1));
        return playerStates.get(uuid);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public void reset() {
        this.mode = Mode.NONE;
        playerStates.clear();
    }
    public void sendUpdatePacket(ServerPlayerEntity player) {
        SensitivityState state = getSensitivityState(player.getGameProfile().getId());
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SensitivityMessage(state.isLocked(), state.getFixedSensitivity()));
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        NBTUtil.writeSensitivityData(tag, "Data", playerStates);
        tag.putInt("Mode", mode.getNum());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        NBTUtil.readSensitivityData(tag, "Data", playerStates);
        this.mode = Mode.getByNum(tag.getInt("Mode"));
    }

    public Path getPath() {
        return Paths.get("SensitivityStateData.dat");
    }


    public void saveFile(File file) {
        CompoundNBT compoundnbt = new CompoundNBT();
        compoundnbt.put("data", this.serializeNBT());
        compoundnbt.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());

        try {
            CompressedStreamTools.writeCompressed(compoundnbt, file.toPath().resolve(getPath()).toFile());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void unload() {
        playerStates.clear();
    }

    public void load(File file) {
        try {
            File saveFile = file.toPath().resolve(getPath()).toFile();
            if (saveFile.exists())
                deserializeNBT(CompressedStreamTools.readCompressed(saveFile).getCompound("data"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static enum Mode {
        NONE(0, () -> -1d), HIGH(1, () -> 1d), LOW(2, () -> 0d), RANDOM(3, () -> Double.valueOf(random.nextInt(2)));
        private final int num;
        private final Supplier<Double> sensitivity;

        Mode(int num, Supplier<Double> sensitivity) {
            this.num = num;
            this.sensitivity = sensitivity;
        }

        public int getNum() {
            return num;
        }

        public Supplier<Double> getSensitivity() {
            return sensitivity;
        }

        public static Mode getByNum(int num) {
            for (Mode value : values()) {
                if (value.getNum() == num)
                    return value;
            }
            return NONE;
        }
    }
}
