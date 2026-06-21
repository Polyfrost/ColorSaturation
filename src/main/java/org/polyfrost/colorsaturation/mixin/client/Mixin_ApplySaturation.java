package org.polyfrost.colorsaturation.mixin.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.polyfrost.colorsaturation.client.ColorSaturationConfig;
import org.polyfrost.colorsaturation.client.SaturationHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.2 {
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Final;
//?}
//? if >=26.2
/*import com.mojang.blaze3d.pipeline.RenderTarget;*/

@Mixin(GameRenderer.class)
public class Mixin_ApplySaturation {
    @Shadow private Minecraft minecraft;
    //? if >=1.21.2
    @Shadow @Final private CrossFrameResourcePool resourcePool;
    //? if >=26.2
    /*@Shadow @Final private RenderTarget mainRenderTarget;*/

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V"
            )
    )
    private void colorsaturation$applySaturation(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (!ColorSaturationConfig.isEnabled || !this.minecraft.isGameLoadFinished() || this.minecraft.level == null) {
            return;
        }

        //? if >=1.21.2 {
        //? if <1.21.11
        RenderSystem.resetTextureMatrix();
        //? if >=26.2 {
        /*SaturationHandler.render(this.mainRenderTarget, this.resourcePool);
        *///?} else {
        SaturationHandler.render(this.minecraft.getMainRenderTarget(), this.resourcePool);
        //?}
        //?} else {
        /*SaturationHandler.update();
        SaturationHandler.render(deltaTracker.getGameTimeDeltaPartialTick(false));
        *///?}
    }
}
