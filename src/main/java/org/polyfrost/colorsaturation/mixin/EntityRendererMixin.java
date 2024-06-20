package org.polyfrost.colorsaturation.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import org.polyfrost.colorsaturation.EntityRendererHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin implements EntityRendererHook {
    @Shadow
    private ShaderGroup theShaderGroup;
    @Unique
    private ShaderGroup colorSaturation$saturationShader;

    @Inject(method = "isShaderActive", at = @At("HEAD"), cancellable = true)
    private void onIsShaderActive(CallbackInfoReturnable<Boolean> cir) {
        if (colorSaturation$saturationShader != null && OpenGlHelper.shadersSupported) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getShaderGroup", at = @At("HEAD"), cancellable = true)
    private void onGetShaderGroup(CallbackInfoReturnable<ShaderGroup> cir) {
        if (colorSaturation$saturationShader != null && OpenGlHelper.shadersSupported && this.theShaderGroup == null) {
            cir.setReturnValue(colorSaturation$saturationShader);
        }
    }

    @Inject(method = "updateShaderGroupSize", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;createBindEntityOutlineFbs(II)V"))
    private void updatePhosphor(int width, int height, CallbackInfo ci) {
        if (colorSaturation$saturationShader != null) {
            colorSaturation$saturationShader.createBindFramebuffers(width, height);
        }
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;renderEntityOutlineFramebuffer()V", shift = At.Shift.AFTER))
    private void renderPhosphor(float partialTicks, long nanoTime, CallbackInfo ci) {
        if (this.colorSaturation$saturationShader != null) {
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            this.colorSaturation$saturationShader.loadShaderGroup(partialTicks);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public ShaderGroup colorSaturation$getSaturationShader() {
        return colorSaturation$saturationShader;
    }

    @Override
    public void colorSaturation$setSaturationShader(ShaderGroup saturationShader) {
        this.colorSaturation$saturationShader = saturationShader;
    }
}
