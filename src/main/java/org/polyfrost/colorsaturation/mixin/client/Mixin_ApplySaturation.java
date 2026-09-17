package org.polyfrost.colorsaturation.mixin.client;

//? if >1.8.9
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
import com.mojang.blaze3d.pipeline.RenderTarget;

@Mixin(GameRenderer.class)
public class Mixin_ApplySaturation {
    @Shadow private Minecraft minecraft;
    //? if >=1.21.2
    @Shadow @Final private CrossFrameResourcePool resourcePool;
    //? if >=26.2
    @Shadow @Final private RenderTarget mainRenderTarget;

    //? if >1.8.9 {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    //? if >=26.3 {
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;blitEntityOutline()V"
                    //?} else {
                    /*target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V"
                    *///?}
            )
    )
    //?} else {
    /*@Inject(
            method = "render(FJ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/world/WorldRenderer;renderEntityOutlines()V",
                    shift = At.Shift.AFTER
            )
    )
    *///?}
    //? if >=26.3 {
    private void colorsaturation$applySaturation(CallbackInfo ci) {
    //?} elif >1.8.9 {
    /*private void colorsaturation$applySaturation(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
    *///?} else {
    /*private void colorsaturation$applySaturation(float tickDelta, long startTime, CallbackInfo ci) {
    *///?}
        //? if >1.8.9 {
        if (!ColorSaturationConfig.isEnabled || !this.minecraft.isGameLoadFinished() || this.minecraft.level == null) {
        //?} else
        //if (!ColorSaturationConfig.isEnabled) {
            SaturationHandler.free();
            return;
        }

        //? if >=1.21.2 {
        //? if <1.21.11
        //RenderSystem.resetTextureMatrix();
        //? if >=26.2 {
        SaturationHandler.render(this.mainRenderTarget, this.resourcePool);
        //?} else {
        /*SaturationHandler.render(this.minecraft.getMainRenderTarget(), this.resourcePool);
        *///?}
        //?} elif >1.8.9 {
        /*SaturationHandler.update();
        SaturationHandler.render(deltaTracker.getGameTimeDeltaPartialTick(false));
        *///?} else {
        /*SaturationHandler.update();
        SaturationHandler.render(tickDelta);
        *///?}
    }
}
