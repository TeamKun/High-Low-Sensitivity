package net.kunmc.lab.highlowsensitivity.data;

import net.kunmc.lab.highlowsensitivity.util.NBTUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.SharedConstants;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SensitivityManager implements INBTSerializable<CompoundNBT> {
    private static final SensitivityManager INSTANCE = new SensitivityManager();
    private static Map<UUID, SensitivityState> playerStates = new HashMap<>();

    public static SensitivityManager getInstance() {
        return INSTANCE;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        NBTUtil.writeSensitivityData(tag, "Data", playerStates);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        NBTUtil.readSensitivityData(tag, "Data", playerStates);
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
