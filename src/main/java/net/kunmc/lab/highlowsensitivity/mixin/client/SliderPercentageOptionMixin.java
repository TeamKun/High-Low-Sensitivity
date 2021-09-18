package net.kunmc.lab.highlowsensitivity.mixin.client;

import net.kunmc.lab.highlowsensitivity.client.gui.SensitivityOptionSlider;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.settings.SliderPercentageOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SliderPercentageOption.class)
public class SliderPercentageOptionMixin {
    @Inject(method = "createButton", at = @At("HEAD"), cancellable = true)
    private void createButton(GameSettings p_216586_1_, int p_216586_2_, int p_216586_3_, int p_216586_4_, CallbackInfoReturnable<Widget> cir) {
        if ((Object) this == AbstractOption.SENSITIVITY) {
            cir.setReturnValue(new SensitivityOptionSlider(p_216586_1_, p_216586_2_, p_216586_3_, p_216586_4_, 20, (SliderPercentageOption) (Object) this));
        }
    }
}
