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

public class SensitivityManager implements INBTSerializable<CompoundNBT> {
    private static final SensitivityManager INSTANCE = new SensitivityManager();
    private static final Map<UUID, SensitivityState> playerStates = new HashMap<>();
    private static final Random random = new Random();
    private boolean enableMode;

    public static SensitivityManager getInstance() {
        return INSTANCE;
    }

    public SensitivityState getSensitivityState(UUID uuid) {
        if (!playerStates.containsKey(uuid))
            playerStates.put(uuid, new SensitivityState(false, -1));
        return playerStates.get(uuid);
    }

    public void setEnableMode(boolean enableMode) {
        this.enableMode = enableMode;
    }

    public boolean isEnableMode() {
        return enableMode;
    }

    public void reset() {
        this.enableMode = false;
        playerStates.clear();
    }

    public void sendUpdatePacket(ServerPlayerEntity player) {
        SensitivityState state = getSensitivityState(player.getGameProfile().getId());
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SensitivityMessage(state.isLocked(), state.getFixedSensitivity()));
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("EnableMode", enableMode);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        NBTUtil.readSensitivityData(tag, "Data", playerStates);
        this.enableMode = tag.getBoolean("EnableMode");
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
}
