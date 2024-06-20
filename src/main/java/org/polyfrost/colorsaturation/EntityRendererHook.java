package org.polyfrost.colorsaturation;

import net.minecraft.client.shader.ShaderGroup;

public interface EntityRendererHook {
    ShaderGroup colorSaturation$getSaturationShader();
    void colorSaturation$setSaturationShader(ShaderGroup saturationShader);
}