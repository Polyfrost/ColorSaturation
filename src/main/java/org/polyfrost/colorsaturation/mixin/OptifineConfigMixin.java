package org.polyfrost.colorsaturation.mixin;

import org.polyfrost.colorsaturation.ColorSaturation;
import org.polyfrost.colorsaturation.config.SaturationConfig;
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
        if (ColorSaturation.getConfig() != null && ColorSaturation.getConfig().enabled && SaturationConfig.forceDisableFastRender) {
            cir.setReturnValue(false);
        }
    }
}