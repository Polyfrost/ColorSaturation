package org.polyfrost.colorsaturation;

//#if MC >= 11700
//#if MC >= 12105
//#else
//$$ import net.minecraft.client.renderer.PostChain;
//#endif
//#else
import net.minecraft.client.shader.ShaderGroup;
//#endif

public interface EntityRendererHook {
    //#if MC >= 11700
    //#if MC >= 12105
    //#else
    //$$ PostChain colorSaturation$getSaturationShader();
    //$$ void colorSaturation$setSaturationShader(PostChain saturationShader);
    //#endif
    //#else
    ShaderGroup colorSaturation$getSaturationShader();
    void colorSaturation$setSaturationShader(ShaderGroup saturationShader);
    //#endif
}