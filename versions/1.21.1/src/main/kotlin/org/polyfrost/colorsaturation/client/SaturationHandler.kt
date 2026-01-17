package org.polyfrost.colorsaturation.client

import dev.deftu.omnicore.api.DEFAULT_NAMESPACE
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.render.GlCapabilities
import dev.deftu.omnicore.api.locationOrThrow
import net.minecraft.client.renderer.PostChain
import org.apache.logging.log4j.LogManager
import org.polyfrost.oneconfig.internal.mixin.Mixin_ShaderListAccessor

object SaturationHandler {
    private val LOGGER = LogManager.getLogger(SaturationHandler::class.java)

    private val LOCATION by lazy { locationOrThrow(DEFAULT_NAMESPACE, "shaders/post/color_saturation.json") }

    private var shader: PostChain? = null
    private var prevWidth = 0
    private var prevHeight = 0
    private var lastStrength = Float.NaN

    @JvmStatic
    val isActive: Boolean
        get() = GlCapabilities.isShaderSupported && shader != null

    @JvmStatic
    fun update() {
        if (!GlCapabilities.isShaderSupported) {
            return
        }

        val mainTarget = client.mainRenderTarget ?: return
        val width = mainTarget.width
        val height = mainTarget.height
        if (width <= 0 || height <= 0) {
            return
        }

        val needsRebuild = shader == null || width != prevWidth || height != prevHeight
        if (!needsRebuild) {
            return
        }

        try {
            LOGGER.info("Invalidating saturation shader group, rebuilding with new dimensions: {}x{}", width, height)
            shader?.close()
        } catch (_: Throwable) {  }

        try {
            LOGGER.info("Building saturation shader group with dimensions: {}x{}", width, height)
            shader = PostChain(client.textureManager, client.resourceManager, mainTarget, LOCATION)
            shader!!.resize(width, height)
            prevWidth = width
            prevHeight = height
            lastStrength = Float.NaN
        } catch (e: Exception) {
            LOGGER.error("Could not load color convolve shader: ", e)
            shader = null
        }
    }

    @JvmStatic
    fun render(tickDelta: Float) {
        if (!isActive) {
            return
        }

        val shader = shader ?: return
        updateShaderUniforms()
        shader.process(tickDelta)
    }

    fun updateShaderUniforms() {
        if (shader == null) {
            return
        }

        val strength = ColorSaturationConfig.strength
        if (strength == lastStrength) {
            return
        }

        val shader = shader ?: return
        val shaders = (shader as? Mixin_ShaderListAccessor)?.listShaders
        if (shaders == null) {
            LOGGER.debug("Shader list not accessible; skipping saturation strength update")
            return
        }

        for (pass in shaders) {
            val uniform = pass.effect.getUniform("Saturation")

            if (uniform == null) {
                LOGGER.debug("Uniform 'Saturation' missing on pass {}", pass)
                continue
            }

            LOGGER.info("Updating strength uniform to {}", strength)
            uniform.set(strength)
        }

        lastStrength = strength
    }
}
