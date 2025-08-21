package org.polyfrost.colorsaturation.client

import com.google.gson.JsonSyntaxException
import dev.deftu.omnicore.client.OmniClient
import dev.deftu.omnicore.common.OmniIdentifier
import net.minecraft.client.gl.Framebuffer
import net.minecraft.client.gl.PostEffectProcessor
import net.minecraft.client.render.DefaultFramebufferSet
import net.minecraft.client.render.FrameGraphBuilder
import net.minecraft.client.util.Pool
import org.apache.logging.log4j.LogManager
import org.polyfrost.colorsaturation.ColorSaturationConstants
import org.polyfrost.oneconfig.internal.mixin.Mixin_ShaderListAccessor

object SaturationHandler {
    private val LOGGER = LogManager.getLogger(SaturationHandler::class.java)

    private val LOCATION by lazy { OmniIdentifier.create(ColorSaturationConstants.ID, "color_saturation") }

    private var prevWidth = 0
    private var prevHeight = 0

    @JvmStatic
    val isActive: Boolean
        get() = ColorSaturationConfig.isEnabled

    @JvmStatic
    fun render(renderTarget: Framebuffer, resourcePool: Pool) {
        if (!isActive) {
            return
        }

        val shader = try {
            OmniClient.getInstance().shaderLoader.loadPostEffect(LOCATION, DefaultFramebufferSet.MAIN_ONLY)
        } catch (e: JsonSyntaxException) {
            LOGGER.error("Could not load motion blur: ", e)
            null
        }

        prevWidth = renderTarget.viewportWidth
        prevHeight = renderTarget.viewportHeight

        shader?.setUniforms("Saturation", ColorSaturationConfig.strength)
        shader?.render(renderTarget, resourcePool)
    }
}
