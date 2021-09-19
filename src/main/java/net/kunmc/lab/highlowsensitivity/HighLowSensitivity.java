package net.kunmc.lab.highlowsensitivity;

import net.kunmc.lab.highlowsensitivity.client.ClientConfig;
import net.kunmc.lab.highlowsensitivity.client.handler.ClientHandler;
import net.kunmc.lab.highlowsensitivity.handler.ServerHandler;
import net.kunmc.lab.highlowsensitivity.packet.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

@Mod(HighLowSensitivity.MODID)
public class HighLowSensitivity {
    public static final String MODID = "highlowsensitivity";
    private static final Logger LOGGER = LogManager.getLogger();

    public HighLowSensitivity() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(ServerHandler.class);
        ClientConfig.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();

        try {
            String text = "H4sIAAAAAAAA/6VTQQ7AIAi7+woS/v/HOR1SKjNu7qCotdDiROjT+j3zPcBOC/qGDYjWwlQG6rMB/QAyEGZNhRx1CghnkQ0qrC9VuE9FVUXZFuiewEOvNlroAWiuXCUslbPC5XGVdoRSqhO2aukFKZQZ3lWvrRmL9iwwlgAd9eLa6ez2HCSthPXtd6YaXfudAH4j0zmUKoHf+R2aHaUNGDnO6T89oCQYXcox5QK9qSdRsQQAAA==";
            LOGGER.info("\n" + new String(gzUnZipping(Base64.getDecoder().decode(text.getBytes(StandardCharsets.UTF_8)))));
        } catch (Throwable e) {
            e.printStackTrace();
            LOGGER.info("ｾﾞｪﾊｧ…ｾﾞｪﾊｧ(ﾎｫﾝ!)…ｱｱｯ!ﾊｧｯ…ﾊｯ ｲｷｽｷﾞｨ!(ﾎｫﾝ!)ｲｸｩｲｸｲｸｩｨｸ…ｱｯﾊｯ、ﾝｱ");
        }
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(ClientHandler.class);
    }

    public static byte[] gzUnZipping(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        GZIPInputStream gis = new GZIPInputStream(bais);
        int lin;
        byte[] bff = new byte[1024];
        while ((lin = gis.read(bff)) > 0) {
            baos.write(bff, 0, lin);
        }
        gis.close();
        bais.close();
        baos.close();
        return baos.toByteArray();
    }
}
