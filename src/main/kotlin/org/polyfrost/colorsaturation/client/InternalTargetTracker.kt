package org.polyfrost.colorsaturation.client

//? if >1.21.5 {
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.resource.RenderTargetDescriptor
//? if >=26.2
/*import com.mojang.blaze3d.GpuFormat*/
//? if >=26.2
/*import org.joml.Vector4f*/

object InternalTargetTracker {
    private var framebufferFactory: RenderTargetDescriptor? = null
    var target: RenderTarget? = null
        private set

    private var prevWidth = -1
    private var prevHeight = -1
    //? if >=26.2
    /*private var prevFormat: GpuFormat? = null*/

    fun updateSize(renderTarget: RenderTarget) {
        val width = renderTarget.width
        val height = renderTarget.height

        //? if >=26.2 {
        /*val format = renderTarget.getColorTexture()!!.getFormat()
        if (width == prevWidth && height == prevHeight && format == prevFormat && target != null) {
            return
        }

        framebufferFactory = createTargetDescriptor(width, height, format)
        prevFormat = format
        *///?}
        //? if <26.2 {
        if (width == prevWidth && height == prevHeight && target != null) {
            return
        }

        if (framebufferFactory?.width != width || framebufferFactory?.height != height) {
            framebufferFactory = createTargetDescriptor(width, height)
        }
        //?}

        free()
        target = framebufferFactory?.allocate()
        prevWidth = width
        prevHeight = height
    }

    fun free() {
        val allocated = target ?: return
        framebufferFactory?.free(allocated)
        target = null
        prevWidth = -1
        prevHeight = -1
    }
}

//? if >=26.2 {
/*fun createTargetDescriptor(width: Int, height: Int, format: GpuFormat): RenderTargetDescriptor =
    RenderTargetDescriptor(width, height, false, Vector4f(0f, 0f, 0f, 0f), format)
*///?}
//? if <26.2 {
fun createTargetDescriptor(width: Int, height: Int): RenderTargetDescriptor =
    RenderTargetDescriptor(width, height, false, 0)
//?}
//?}
