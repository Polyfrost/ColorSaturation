package org.polyfrost.colorsaturation.client

import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.buffers.Std140SizeCalculator
import com.mojang.blaze3d.systems.RenderSystem
import kotlin.use

object SaturationUniforms {
    private val BLOCK_SIZE = Std140SizeCalculator().putFloat().get()
    private val device get() = RenderSystem.getDevice()

    val buffer: GpuBuffer by lazy {
        device.createBuffer({ "ColorSaturation_UBO" }, GpuBuffer.USAGE_UNIFORM or GpuBuffer.USAGE_MAP_WRITE, BLOCK_SIZE)
    }

    fun upload(strength: Float) {
        device.createCommandEncoder().mapBuffer(buffer, false, true).use { mapped ->
            mapped.data().putFloat(strength)
        }
    }
}
