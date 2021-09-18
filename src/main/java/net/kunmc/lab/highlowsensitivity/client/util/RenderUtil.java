package net.kunmc.lab.highlowsensitivity.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class RenderUtil {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void guiBindAndBlit(ResourceLocation location, MatrixStack matx, int x, int y, int textureStartX, int textureStartY, int textureFinishWidth, int textureFinishHeight, int textureSizeX, int textureSizeY) {
        matx.pushPose();
        mc.getTextureManager().bind(location);
        guiBlit(matx, x, y, textureStartX, textureStartY, textureFinishWidth, textureFinishHeight, textureSizeX, textureSizeY);
        matx.popPose();
    }

    private static void guiBlit(MatrixStack matx, int x, int y, int textureStartX, int textureStartY, int textureFinishWidth, int textureFinishHeight, int textureSizeX, int textureSizeY) {
        AbstractGui.blit(matx, x, y, textureStartX, textureStartY, textureFinishWidth, textureFinishHeight, textureSizeX, textureSizeY);
    }

    public static void matrixRotateDegreefX(MatrixStack ms, float x) {
        ms.mulPose(Vector3f.XP.rotationDegrees(x));
    }

    public static void matrixRotateDegreefY(MatrixStack ms, float y) {
        ms.mulPose(Vector3f.YP.rotationDegrees(y));
    }

    public static void matrixRotateDegreefZ(MatrixStack ms, float z) {
        ms.mulPose(Vector3f.ZP.rotationDegrees(z));
    }

}
