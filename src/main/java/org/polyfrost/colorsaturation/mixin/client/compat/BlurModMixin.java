package org.polyfrost.colorsaturation.mixin.client.compat;

import net.minecraft.client.renderer.EntityRenderer;
import org.polyfrost.colorsaturation.client.SaturationHandler;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "com.tterrag.blur.Blur", remap = false)
public class BlurModMixin {
    @Dynamic("Blur Mod")
    @Redirect(method = "onGuiChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;isShaderActive()Z", ordinal = 0, remap = true), remap = false)
    private boolean isShaderActive(EntityRenderer er) {
        if (SaturationHandler.isActive()) {
            return false;
        }

        return er.isShaderActive();
    }
}