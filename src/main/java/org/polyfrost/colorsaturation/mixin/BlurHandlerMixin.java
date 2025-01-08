package org.polyfrost.colorsaturation.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.polyfrost.colorsaturation.EntityRendererHook;
import org.polyfrost.oneconfig.api.ui.v1.internal.BlurHandler;
import org.polyfrost.universal.UMinecraft;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "org.polyfrost.oneconfig.api.ui.v1.internal.BlurHandler", remap = false)
public class BlurHandlerMixin {

    @Dynamic("OneConfig")
    @WrapOperation(method = "reloadBlur", at = @At(value = "INVOKE", target = "Lcc/polyfrost/oneconfig/internal/gui/impl/BlurHandlerImpl;isShaderActive()Z", ordinal = 0))
    private boolean redirectShaderActive(BlurHandler instance, Object gui, Operation<Boolean> original) { // works without any params in 0.7.11 but in 0.8 things got stricter
        if (
                        //#if MC<=11202
                        net.minecraft.client.renderer.OpenGlHelper.shadersSupported
                        //#else
                        //$$ true
                        //#endif
                        && ((EntityRendererHook) UMinecraft.getMinecraft().entityRenderer).colorSaturation$getSaturationShader() != null
        ) {
            return false;
        }

        return original.call(instance, gui);
    }
}