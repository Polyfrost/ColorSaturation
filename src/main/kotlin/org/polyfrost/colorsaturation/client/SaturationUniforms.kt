package org.polyfrost.colorsaturation.client

//? if >1.21.5 {
import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.buffers.Std140SizeCalculator
import com.mojang.blaze3d.systems.RenderSystem

object SaturationUniforms {
    private val blockSize = Std140SizeCalculator()
        .putFloat()
        .putFloat()
        .putFloat()
        .putFloat()
        .get()
    private val device get() = RenderSystem.getDevice()

    private var uploadedSaturation = Float.NaN
    private var uploadedContrast = Float.NaN
    private var uploadedBrightness = Float.NaN
    private var uploadedHue = Float.NaN

    val buffer: GpuBuffer by lazy {
        device.createBuffer(
            { "ColorSaturation_UBO" },
            GpuBuffer.USAGE_UNIFORM or GpuBuffer.USAGE_MAP_WRITE,
            //? if >=1.21.11
            /*blockSize.toLong()*/
            //? if <1.21.11
            blockSize
        )
    }

    fun upload(strength: Float, contrast: Float, brightness: Float, hue: Float) {
        if (strength == uploadedSaturation &&
            contrast == uploadedContrast &&
            brightness == uploadedBrightness &&
            hue == uploadedHue
        ) {
            return
        }

        //? if >=26.2 {
        /*buffer.map(false, true).use { mapped ->
            mapped.data()
                .putFloat(strength)
                .putFloat(contrast)
                .putFloat(brightness)
                .putFloat(hue)
        }
        *///?} else {
        device.createCommandEncoder().mapBuffer(buffer, false, true).use { mapped ->
            mapped.data()
                .putFloat(strength)
                .putFloat(contrast)
                .putFloat(brightness)
                .putFloat(hue)
        }
        //?}

        uploadedSaturation = strength
        uploadedContrast = contrast
        uploadedBrightness = brightness
        uploadedHue = hue
    }
}
//?}
