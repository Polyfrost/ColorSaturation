package org.polyfrost.colorsaturation.client

import com.google.gson.JsonSyntaxException
import com.mojang.blaze3d.framegraph.FrameGraphBuilder
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.resource.CrossFrameResourcePool
import dev.deftu.omnicore.client.OmniClient
import dev.deftu.omnicore.common.OmniIdentifier
import net.minecraft.client.renderer.LevelTargetBundle
import net.minecraft.client.renderer.PostChain
import org.apache.logging.log4j.LogManager
import org.polyfrost.colorsaturation.ColorSaturationConstants

object SaturationHandler {
    private val LOGGER = LogManager.getLogger(SaturationHandler::class.java)

    private val LOCATION by lazy { OmniIdentifier.create(ColorSaturationConstants.ID, "color_saturation") }

    private var prevWidth = 0
    private var prevHeight = 0

    @JvmStatic
    val isActive: Boolean
        get() = ColorSaturationConfig.isEnabled

    @JvmStatic
    fun render(renderTarget: RenderTarget, resourcePool: CrossFrameResourcePool) {
        if (!isActive) {
            return
        }

        val shader = try {
            OmniClient.getInstance().shaderManager.getPostChain(LOCATION, LevelTargetBundle.MAIN_TARGETS)
        } catch (e: JsonSyntaxException) {
            LOGGER.error("Could not load motion blur: ", e)
            null
        }

        prevWidth = renderTarget.viewWidth
        prevHeight = renderTarget.viewHeight

        val builder = FrameGraphBuilder()
        shader?.addToFrame(
            builder,
            renderTarget.viewWidth, renderTarget.viewHeight,
            PostChain.TargetBundle.of(
                PostChain.MAIN_TARGET_ID,
                builder.importExternal("main", renderTarget)
            )
        ) { renderPass ->
            renderPass.setUniform("Saturation", ColorSaturationConfig.strength)
        }

        builder.execute(resourcePool)
    }
}
