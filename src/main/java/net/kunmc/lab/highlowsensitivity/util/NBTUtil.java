package net.kunmc.lab.highlowsensitivity.util;

import net.kunmc.lab.highlowsensitivity.data.SensitivityState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.util.*;
import java.util.function.Function;

public class NBTUtil {
    public static <T> CompoundNBT writeList(CompoundNBT tag, String name, List<T> list, Function<T, INBT> writer) {
        ListNBT listTag = new ListNBT();
        list.forEach(n -> listTag.add(writer.apply(n)));
        tag.put(name, listTag);
        return tag;
    }

    public static <T> void readList(CompoundNBT tag, String name, List<T> list, Function<INBT, T> reader, int num) {
        list.clear();
        ListNBT listTag = tag.getList(name, num);
        for (INBT lstag : listTag) {
            list.add(reader.apply(lstag));
        }
    }

    public static <T> void readList(CompoundNBT tag, String name, List<T> list, Function<INBT, T> reader) {
        readList(tag, name, list, reader, 10);
    }

    public static <T, M> CompoundNBT writeMap(CompoundNBT tag, String name, Map<T, M> map, Function<T, INBT> writerKey, Function<M, INBT> writerEntry) {

        Function<Map.Entry<T, M>, INBT> writer = n -> {
            CompoundNBT it = new CompoundNBT();
            it.put("K", writerKey.apply(n.getKey()));
            it.put("E", writerEntry.apply(n.getValue()));
            return it;
        };
        return writeList(tag, name, new ArrayList<>(map.entrySet()), writer);
    }

    public static <T, M> void readMap(CompoundNBT tag, String name, Map<T, M> map, Function<INBT, T> readerKey, Function<INBT, M> readerEntry, int num) {
        List<Map.Entry<T, M>> entries = new ArrayList<>();
        Function<INBT, Map.Entry<T, M>> reader = n -> {
            CompoundNBT it = (CompoundNBT) n;
            return new AbstractMap.SimpleEntry<>(readerKey.apply(it.get("K")), readerEntry.apply(it.get("E")));
        };
        readList(tag, name, entries, reader, num);
        map.clear();
        entries.forEach(n -> map.put(n.getKey(), n.getValue()));
    }


    public static CompoundNBT writeSensitivityData(CompoundNBT tag, String name, Map<UUID, SensitivityState> data) {
        return writeMap(tag, name, data, net.minecraft.nbt.NBTUtil::createUUID, SensitivityState::serializeNBT);
    }

    public static void readSensitivityData(CompoundNBT tag, String name, Map<UUID, SensitivityState> data) {
        readMap(tag, name, data, net.minecraft.nbt.NBTUtil::loadUUID, n -> new SensitivityState((CompoundNBT) n), 10);
    }
}
