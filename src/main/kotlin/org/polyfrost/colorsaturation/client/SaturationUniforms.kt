package org.polyfrost.colorsaturation.client

//? if >1.21.5 {
import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.buffers.Std140SizeCalculator
import com.mojang.blaze3d.systems.RenderSystem

object SaturationUniforms {
    private val blockSize = Std140SizeCalculator().putFloat().get()
    private val device get() = RenderSystem.getDevice()

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

    fun upload(strength: Float) {
        //? if >=26.2 {
        /*buffer.map(false, true).use { mapped ->
            mapped.data().putFloat(strength)
        }
        *///?} else {
        device.createCommandEncoder().mapBuffer(buffer, false, true).use { mapped ->
            mapped.data().putFloat(strength)
        }
        //?}
    }
}
//?}
