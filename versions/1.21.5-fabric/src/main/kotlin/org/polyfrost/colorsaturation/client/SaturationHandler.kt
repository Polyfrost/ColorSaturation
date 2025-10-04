package org.polyfrost.colorsaturation.client

import com.google.gson.JsonSyntaxException
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.identifierOrThrow
import net.minecraft.client.gl.Framebuffer
import net.minecraft.client.gl.PostEffectProcessor
import net.minecraft.client.render.DefaultFramebufferSet
import net.minecraft.client.render.FrameGraphBuilder
import net.minecraft.client.util.Pool
import org.apache.logging.log4j.LogManager
import org.polyfrost.colorsaturation.ColorSaturationConstants

object SaturationHandler {
    private val LOGGER = LogManager.getLogger(SaturationHandler::class.java)

    private val LOCATION by lazy { identifierOrThrow(ColorSaturationConstants.ID, "color_saturation") }

    @JvmStatic
    val isActive: Boolean
        get() = ColorSaturationConfig.isEnabled

    @JvmStatic
    fun render(renderTarget: Framebuffer, resourcePool: Pool) {
        if (!isActive) {
            return
        }

        val shader = try {
            client.shaderLoader.loadPostEffect(LOCATION, DefaultFramebufferSet.MAIN_ONLY)
        } catch (e: JsonSyntaxException) {
            LOGGER.error("Could not load motion blur: ", e)
            null
        }

        val builder = FrameGraphBuilder()
        shader?.render(
            builder,
            renderTarget.viewportWidth, renderTarget.viewportHeight,
            PostEffectProcessor.FramebufferSet.singleton(
                PostEffectProcessor.MAIN,
                builder.createObjectNode("main", renderTarget)
            )
        ) { renderPass ->
            renderPass.setUniform("Saturation", ColorSaturationConfig.strength)
        }

        builder.run(resourcePool)
    }
}
