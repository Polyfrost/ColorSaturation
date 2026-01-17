package org.polyfrost.colorsaturation.mixin.client;

import dev.deftu.omnicore.api.client.OmniClient;
import dev.deftu.omnicore.api.client.OmniClientProfiler;
import net.minecraft.client.renderer.GameRenderer;
import org.polyfrost.colorsaturation.client.ColorSaturationConfig;
import org.polyfrost.colorsaturation.client.SaturationHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >= 1.21.4 {
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
//?} else {
/*import dev.deftu.omnicore.api.client.render.OmniRenderTicks;
*///?}

import net.minecraft.client.DeltaTracker;

@Mixin(GameRenderer.class)
public class Mixin_ApplySaturation {
    //? if >= 1.21.4 {
    @Final @Shadow private Minecraft minecraft;
    @Shadow @Final private CrossFrameResourcePool resourcePool;
    //?}

    @Inject(
            method = "render",

            //? if >= 1.21.4 {
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V"
            )
            //?} else {
            /*at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderGlobal;renderEntityOutlineFramebuffer()V"
            )
            *///?}
    )
    private void colorsaturation$applySaturation(DeltaTracker deltaTracker, boolean isTicking, CallbackInfo ci) {
        if (!ColorSaturationConfig.isEnabled()) {
            return;
        }

        //? if >= 1.21.4 {
        if (!OmniClient.get().isGameLoadFinished() || OmniClient.getWorld() == null) {
            return;
        }
        //?}

        OmniClientProfiler.withProfiler(OmniClient.get(), "colorsaturation_applier", () -> {
            //? if >= 1.21.4 {
            RenderSystem.resetTextureMatrix();
            SaturationHandler.render(this.minecraft.getMainRenderTarget(), this.resourcePool);
            //?} else {
            /*SaturationHandler.update();
            float trueTickDelta = OmniRenderTicks.get();
            SaturationHandler.render(trueTickDelta);
            *///?}
        });
    }
}
