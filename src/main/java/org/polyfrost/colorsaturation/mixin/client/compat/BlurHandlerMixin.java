package org.polyfrost.colorsaturation.mixin.client.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.polyfrost.colorsaturation.client.SaturationHandler;
import org.polyfrost.oneconfig.api.ui.v1.internal.BlurHandler;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "org.polyfrost.oneconfig.api.ui.v1.internal.BlurHandler", remap = false)
public class BlurHandlerMixin {

    @Dynamic("OneConfig")
    @WrapOperation(method = "reloadBlur", at = @At(value = "INVOKE", target = "Lorg/polyfrost/oneconfig/api/ui/v1/internal/BlurHandler;isShaderActive()Z", ordinal = 0))
    private boolean redirectShaderActive(BlurHandler instance, Operation<Boolean> original, Object gui) { // works without any params in 0.7.11 but in 0.8 things got stricter
        if (SaturationHandler.isActive()) {
            return false;
        }

        return original.call(instance, gui);
    }
}
