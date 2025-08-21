package org.polyfrost.colorsaturation.mixin.client;

import dev.deftu.omnicore.common.OmniProfiler;
import net.minecraft.client.renderer.EntityRenderer;
import org.polyfrost.colorsaturation.client.ColorSaturationConfig;
import org.polyfrost.colorsaturation.client.SaturationHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 1.21.2
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//$$ import dev.deftu.omnicore.client.OmniClient;
//$$ import net.minecraft.client.MinecraftClient;
//$$ import net.minecraft.client.util.Pool;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#else
import dev.deftu.omnicore.client.render.OmniGameRendering;
//#endif

//#if MC >= 1.21.1
//$$ import net.minecraft.client.DeltaTracker;
//#endif

@Mixin(EntityRenderer.class)
public class Mixin_EntityRenderer_SaturationHandler {
    //#if MC >= 1.21.2
    //$$ @Shadow private MinecraftClient client;
    //$$ @Shadow @Final private Pool pool;
    //#endif

    //#if MC < 1.16.5
    @Inject(method = "isShaderActive", at = @At("HEAD"), cancellable = true)
    private void colorsaturation$cancelShaderActive(CallbackInfoReturnable<Boolean> cir) {
        if (!SaturationHandler.isActive()) {
            return;
        }

        cir.setReturnValue(true);
    }
    //#endif

    @Inject(
            //#if MC >= 1.16.5
            //$$ method = "render",
            //#else
            method = "updateCameraAndRender",
            //#endif

            //#if MC >= 1.21.2
            //$$ at = @At(
            //$$     value = "INVOKE",
            //$$     target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V"
            //$$ )
            //#else
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderGlobal;renderEntityOutlineFramebuffer()V"
            )
            //#endif
    )
    private void colorsaturation$applySaturation(
            //#if MC >= 1.21.1
            //$$ DeltaTracker deltaTracker,
            //#else
            float tickDelta,
            long nanoTime,
            //#endif
            //#if MC >= 1.16.5
            //$$ boolean isTicking,
            //#endif
            CallbackInfo ci
    ) {
        if (!ColorSaturationConfig.isEnabled()) {
            return;
        }

        //#if MC >= 1.21.2
        //$$ if (!OmniClient.getInstance().isFinishedLoading() || !OmniClient.hasWorld()) {
        //$$     return;
        //$$ }
        //#endif

        OmniProfiler.withProfiler("colorsaturation_applier", () -> {
            //#if MC >= 1.21.2
            //$$ RenderSystem.resetTextureMatrix();
            //$$ SaturationHandler.render(this.client.getFramebuffer(), this.pool);
            //#else
            SaturationHandler.update();
            float trueTickDelta = OmniGameRendering.getTickDelta(true);
            SaturationHandler.render(trueTickDelta);
            //#endif
        });
    }
}