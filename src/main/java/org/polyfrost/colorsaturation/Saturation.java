package org.polyfrost.colorsaturation;

import cc.polyfrost.oneconfig.events.event.RenderEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.libs.universal.UResolution;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import org.polyfrost.colorsaturation.config.SaturationConfig;
import org.polyfrost.colorsaturation.mixin.ShaderGroupAccessor;

import java.io.IOException;
import java.util.List;

public class Saturation {
    private static boolean lastEnabled = false;

    private static final ResourceLocation phosphorBlur = new ResourceLocation("minecraft:shaders/post/color_convolve.json");

    @Subscribe
    private void onRenderTick(RenderEvent event) {
        if (event.stage != Stage.END) {
            return;
        }

        // Only update the shader if one is active
        if (!isShaderActive() || lastEnabled != ColorSaturation.config.enabled) {
            lastEnabled = ColorSaturation.config.enabled;
            reloadShader();
        }
    }

    public static void reloadShader() {
        if (UMinecraft.getWorld() == null) {
            return;
        }

        if (!isShaderActive() && ColorSaturation.config.enabled) {
            try {
                final ShaderGroup saturationShader = new ShaderGroup(UMinecraft.getMinecraft().getTextureManager(), UMinecraft.getMinecraft().getResourceManager(), UMinecraft.getMinecraft().getFramebuffer(), phosphorBlur);
                saturationShader.createBindFramebuffers(UResolution.getWindowWidth(), UResolution.getWindowHeight());
                ((EntityRendererHook) UMinecraft.getMinecraft().entityRenderer).colorSaturation$setSaturationShader(saturationShader);
                reloadSaturation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (isShaderActive() && !ColorSaturation.config.enabled) {
            final EntityRendererHook entityRenderer = (EntityRendererHook) UMinecraft.getMinecraft().entityRenderer;
            if (entityRenderer.colorSaturation$getSaturationShader() != null) {
                entityRenderer.colorSaturation$getSaturationShader().deleteShaderGroup();
            }

            entityRenderer.colorSaturation$setSaturationShader(null);
        }
    }

    public static void reloadSaturation() {
        try {
            final List<Shader> listShaders = ((ShaderGroupAccessor) ((EntityRendererHook) UMinecraft.getMinecraft().entityRenderer).colorSaturation$getSaturationShader()).getListShaders();

            if (listShaders == null) {
                return;
            }

            for (Shader shader : listShaders) {
                ShaderUniform su = shader.getShaderManager().getShaderUniform("Saturation");

                if (su == null) {
                    continue;
                }

                su.set(SaturationConfig.saturation);
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean isShaderActive() {
        return ((EntityRendererHook) UMinecraft.getMinecraft().entityRenderer).colorSaturation$getSaturationShader() != null
                //#if MC<=11202
                && net.minecraft.client.renderer.OpenGlHelper.shadersSupported
                //#endif
                ;
    }
}
