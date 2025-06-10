package org.polyfrost.colorsaturation.mixin;

//#if MC >= 11700
//#if MC >= 12105
//$$ import net.minecraft.client.renderer.GameRenderer;
//#else
//$$ import net.minecraft.client.renderer.GameRenderer;
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//$$ import net.minecraft.client.renderer.PostChain;
//#endif
//#else
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
//#endif

import org.polyfrost.colorsaturation.EntityRendererHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11700
//$$ @Mixin(GameRenderer.class)
//#else
@Mixin(EntityRenderer.class)
//#endif
public class EntityRendererMixin implements EntityRendererHook {
    //#if MC >= 11700
    //#if MC >= 12105
    //#else
    //$$ @Shadow
    //$$ private PostChain postEffect;
    //$$ @Unique
    //$$ private PostChain colorSaturation$saturationShader;
    //#endif
    //#else
    @Shadow
    private ShaderGroup theShaderGroup;
    @Unique
    private ShaderGroup colorSaturation$saturationShader;
    //#endif

    //#if MC >= 11700
    //#if MC >= 12105
    //#else
    //$$ @Inject(method = "checkEntityPostEffect", at = @At("HEAD"), cancellable = true)
    //$$ private void onIsShaderActive(CallbackInfoReturnable<Boolean> cir) {
    //$$     if (colorSaturation$saturationShader != null) {
    //$$         cir.setReturnValue(true);
    //$$     }
    //$$ }
    //#endif
    //#else
    @Inject(method = "isShaderActive", at = @At("HEAD"), cancellable = true)
    private void onIsShaderActive(CallbackInfoReturnable<Boolean> cir) {
        if (colorSaturation$saturationShader != null && OpenGlHelper.shadersSupported) {
            cir.setReturnValue(true);
        }
    }
    //#endif

    //#if MC >= 11700
    //#if MC >= 12105
    //#else
    //$$ @Inject(method = "getPostEffect", at = @At("HEAD"), cancellable = true)
    //$$ private void onGetShaderGroup(CallbackInfoReturnable<PostChain> cir) {
    //$$     if (colorSaturation$saturationShader != null && this.postEffect == null) {
    //$$         cir.setReturnValue(colorSaturation$saturationShader);
    //$$     }
    //$$ }
    //#endif
    //#else
    @Inject(method = "getShaderGroup", at = @At("HEAD"), cancellable = true)
    private void onGetShaderGroup(CallbackInfoReturnable<ShaderGroup> cir) {
        if (colorSaturation$saturationShader != null && OpenGlHelper.shadersSupported && this.theShaderGroup == null) {
            cir.setReturnValue(colorSaturation$saturationShader);
        }
    }
    //#endif

    //#if MC >= 11700
    //#if MC >= 12105
    //#else
    //$$ @Inject(method = "resize", at = @At("TAIL"))
    //$$ private void updatePhosphor(int width, int height, CallbackInfo ci) {
    //$$     if (colorSaturation$saturationShader != null) {
    //$$         colorSaturation$saturationShader.resize(width, height);
    //$$     }
    //$$ }
    //#endif
    //#else
    @Inject(method = "updateShaderGroupSize", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;createBindEntityOutlineFbs(II)V"))
    private void updatePhosphor(int width, int height, CallbackInfo ci) {
        if (colorSaturation$saturationShader != null) {
            colorSaturation$saturationShader.createBindFramebuffers(width, height);
        }
    }
    //#endif

    //#if MC >= 11700
    //#if MC >= 12105
    //#else
    //$$ @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lcom/mojang/math/Matrix4f;)V", shift = At.Shift.AFTER))
    //$$ private void renderPhosphor(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {
    //$$     if (this.colorSaturation$saturationShader != null) {
    //$$         this.colorSaturation$saturationShader.process(partialTicks);
    //$$     }
    //$$ }
    //#endif
    //#else
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
    //#endif

    //#if MC >= 11700
    //#if MC >= 12105
    //#else
    //$$ @Override
    //$$ public PostChain colorSaturation$getSaturationShader() {
    //$$     return colorSaturation$saturationShader;
    //$$     }
    //$$
    //$$ @Override
    //$$ public void colorSaturation$setSaturationShader(PostChain saturationShader) {
    //$$     this.colorSaturation$saturationShader = saturationShader;
    //$$ }
    //#endif
    //#else
    @Override
    public ShaderGroup colorSaturation$getSaturationShader() {
        return colorSaturation$saturationShader;
    }

    @Override
    public void colorSaturation$setSaturationShader(ShaderGroup saturationShader) {
        this.colorSaturation$saturationShader = saturationShader;
    }
    //#endif
}