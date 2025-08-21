package org.polyfrost.colorsaturation.mixin.client.compat;

import org.polyfrost.colorsaturation.client.ColorSaturationConfig;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "Config", remap = false)
public class OptifineConfigMixin {
    @Dynamic("OptiFine")
    @Inject(method = "isFastRender", at = @At("HEAD"), cancellable = true)
    private static void cancelFastRender(CallbackInfoReturnable<Boolean> cir) {
        if (ColorSaturationConfig.isEnabled() && ColorSaturationConfig.getForceDisableFastRender()) {
            cir.setReturnValue(false);
        }
    }
}
