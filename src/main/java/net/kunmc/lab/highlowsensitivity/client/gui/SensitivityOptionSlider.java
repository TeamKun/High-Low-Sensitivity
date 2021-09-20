package net.kunmc.lab.highlowsensitivity.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.kunmc.lab.highlowsensitivity.HighLowSensitivity;
import net.kunmc.lab.highlowsensitivity.client.data.SensitivityClientManager;
import net.kunmc.lab.highlowsensitivity.client.util.RenderUtil;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.OptionSlider;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SensitivityOptionSlider extends OptionSlider {
    public static final List<SensitivityOptionSlider> SLIDERS = new ArrayList<>();
    private static final ResourceLocation SENS_TEXTURE = new ResourceLocation(HighLowSensitivity.MODID, "textures/gui/sensitivity_widgets.png");

    public SensitivityOptionSlider(GameSettings p_i51129_1_, int p_i51129_2_, int p_i51129_3_, int p_i51129_4_, int p_i51129_5_, SliderPercentageOption p_i51129_6_) {
        super(p_i51129_1_, p_i51129_2_, p_i51129_3_, p_i51129_4_, p_i51129_5_, p_i51129_6_);
        SLIDERS.add(this);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, Minecraft p_230441_2_, int p_230441_3_, int p_230441_4_) {
        super.renderBg(matrixStack, p_230441_2_, p_230441_3_, p_230441_4_);
    }


    @Override
    public void renderButton(MatrixStack matrixStack, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        super.renderButton(matrixStack, p_230431_2_, p_230431_3_, p_230431_4_);
        if (SensitivityClientManager.getInstance().isLocked()) {
            int col = 0xFF810000;
            fill(matrixStack, x, y, x + width, y + 1, col);
            fill(matrixStack, x, y + height - 1, x + width, y + height, col);
            fill(matrixStack, x, y + 1, x + 1, y + height - 1, col);
            fill(matrixStack, x + width - 1, y + 1, x + width, y + height - 1, col);

            int le = (int) Math.sqrt(Math.pow(height - 1, 2) + Math.pow(width, 2));
            float dr = (float) Math.toDegrees(Math.tanh((double) (height - 1) / (double) width));

            matrixStack.pushPose();
            matrixStack.translate(x, y, 0);
            RenderUtil.matrixRotateDegreefZ(matrixStack, dr);
            matrixStack.translate(-x, -y, 0);
            fill(matrixStack, x, y, x + le, y + 1, col);
            matrixStack.popPose();

            matrixStack.pushPose();
            matrixStack.translate(x, y + height, 0);
            RenderUtil.matrixRotateDegreefZ(matrixStack, -dr);
            matrixStack.translate(-x, -(y + height), 0);
            fill(matrixStack, x, y + height - 1, x + le, y + height, col);
            matrixStack.popPose();

            RenderUtil.guiBindAndBlit(SENS_TEXTURE, matrixStack, x + (width / 2 - 5), y + (height / 2 - 7), 0, 0, 10, 14, 256, 256);
        }
    }

    @Override
    protected boolean clicked(double p_230992_1_, double p_230992_3_) {
        return super.clicked(p_230992_1_, p_230992_3_) && !SensitivityClientManager.getInstance().isLocked();
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        if (SensitivityClientManager.getInstance().isLocked())
            return false;

        return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
    }

    public void update() {
        this.value = Minecraft.getInstance().options.sensitivity;
        updateMessage();
    }
}
