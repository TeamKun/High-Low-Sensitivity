package net.kunmc.lab.highlowsensitivity.client;

import net.kunmc.lab.highlowsensitivity.HighLowSensitivity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientConfig {
    private static final Logger LOGGER = LogManager.getLogger(ClientConfig.class);
    public static ForgeConfigSpec.ConfigValue<Double> OLD_SENSITIVITY;

    public static void init() {
        Pair<ConfigLoder, ForgeConfigSpec> client_config = new ForgeConfigSpec.Builder().configure(ConfigLoder::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, client_config.getRight());
    }

    public static class ConfigLoder {
        public ConfigLoder(ForgeConfigSpec.Builder builder) {
            LOGGER.info("Loading Client Config");
            builder.push(HighLowSensitivity.MODID);
            OLD_SENSITIVITY = builder.define("old sensitivity", -1d);
            builder.pop();
        }
    }
}
