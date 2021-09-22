package net.kunmc.lab.highlowsensitivity;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerConfig {
    private static final Logger LOGGER = LogManager.getLogger(ServerConfig.class);
    public static ForgeConfigSpec.ConfigValue<Double> HIGH_SENSITIVITY;
    public static ForgeConfigSpec.ConfigValue<Double> LOW_SENSITIVITY;
    public static ForgeConfigSpec.ConfigValue<Integer> HIGH_SENSITIVITY_CONT;
    public static ForgeConfigSpec.ConfigValue<Integer> LOW_SENSITIVITY_CONT;

    public static void init() {
        Pair<ConfigLoder, ForgeConfigSpec> server_config = new ForgeConfigSpec.Builder().configure(ConfigLoder::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server_config.getRight());
    }

    public static class ConfigLoder {
        public ConfigLoder(ForgeConfigSpec.Builder builder) {
            LOGGER.info("Loading Server Config");
            builder.push(HighLowSensitivity.MODID);
            HIGH_SENSITIVITY = builder.define("high sensitivity", 200d);
            LOW_SENSITIVITY = builder.define("low sensitivity", 0d);
            HIGH_SENSITIVITY_CONT = builder.define("high sensitivity cont", 3);
            LOW_SENSITIVITY_CONT = builder.define("low sensitivity cont", 3);
            builder.pop();
        }
    }
}
