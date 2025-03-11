package org.polyfrost.colorsaturation;

import dev.deftu.omnicore.client.OmniClient;
import dev.deftu.omnicore.client.render.OmniResolution;
import dev.deftu.omnicore.client.render.OmniTextureManager;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import org.polyfrost.colorsaturation.config.SaturationConfig;
import org.polyfrost.colorsaturation.mixin.ShaderGroupAccessor;
import org.polyfrost.oneconfig.api.event.v1.events.RenderEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

import java.io.IOException;
import java.util.List;

public class Saturation {
    private static boolean lastEnabled = false;

    private static final ResourceLocation phosphorBlur = new ResourceLocation("minecraft:shaders/post/color_convolve.json");

    @Subscribe
    private void onRenderTick(RenderEvent.Post event) {
        // Only update the shader if one is active
        if (!isShaderActive() || lastEnabled != ColorSaturation.getConfig().enabled) {
            lastEnabled = ColorSaturation.getConfig().enabled;
            reloadShader();
        }
    }

    public static void reloadShader() {
        if (!OmniClient.getHasWorld()) {
            return;
        }

        if (!isShaderActive() && ColorSaturation.getConfig().enabled) {
            try {
                final ShaderGroup saturationShader = new ShaderGroup(OmniTextureManager.get(), OmniClient.getInstance().getResourceManager(), OmniClient.getInstance().getFramebuffer(), phosphorBlur);
                saturationShader.createBindFramebuffers(OmniResolution.getViewportWidth(), OmniResolution.getViewportHeight());
                ((EntityRendererHook) OmniClient.getInstance().entityRenderer).colorSaturation$setSaturationShader(saturationShader);
                reloadSaturation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (isShaderActive() && !ColorSaturation.getConfig().enabled) {
            final EntityRendererHook entityRenderer = (EntityRendererHook) OmniClient.getInstance().entityRenderer;
            if (entityRenderer.colorSaturation$getSaturationShader() != null) {
                entityRenderer.colorSaturation$getSaturationShader().deleteShaderGroup();
            }

            entityRenderer.colorSaturation$setSaturationShader(null);
        }
    }

    public static void reloadSaturation() {
        try {
            final List<Shader> listShaders = ((ShaderGroupAccessor) ((EntityRendererHook) OmniClient.getInstance().entityRenderer).colorSaturation$getSaturationShader()).getListShaders();

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
        return ((EntityRendererHook) OmniClient.getInstance().entityRenderer).colorSaturation$getSaturationShader() != null
                //#if MC<=11202
                && net.minecraft.client.renderer.OpenGlHelper.shadersSupported
                //#endif
                ;
    }
}
