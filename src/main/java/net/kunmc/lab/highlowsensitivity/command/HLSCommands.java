package net.kunmc.lab.highlowsensitivity.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

public class HLSCommands {
    public static void registerCommand(CommandDispatcher<CommandSource> d) {
        SensitivityCommand.register(d);
    }
}
