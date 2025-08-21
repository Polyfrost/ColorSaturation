package org.polyfrost.colorsaturation.client

import dev.deftu.omnicore.client.OmniClient
import dev.deftu.omnicore.client.render.OmniRenderEnv
import dev.deftu.omnicore.common.OmniIdentifier
import net.minecraft.client.shader.ShaderGroup
import org.apache.logging.log4j.LogManager
import org.polyfrost.oneconfig.internal.mixin.Mixin_ShaderListAccessor

object SaturationHandler {
    private val LOGGER = LogManager.getLogger(SaturationHandler::class.java)

    private val LOCATION by lazy { OmniIdentifier.create(OmniIdentifier.MINECRAFT_NAMESPACE, "shaders/post/color_saturation.json") }

    private var shader: ShaderGroup? = null
    private var prevWidth = 0
    private var prevHeight = 0
    private var lastStrength = Float.NaN

    @JvmStatic
    val isActive: Boolean
        get() = OmniRenderEnv.isShaderSupported && shader != null

    @JvmStatic
    fun update() {
        if (!OmniRenderEnv.isShaderSupported) {
            return
        }

        val client = OmniClient.getInstance()
        val mainTarget = client.framebuffer ?: return
        val width = mainTarget.framebufferWidth
        val height = mainTarget.framebufferHeight
        if (width <= 0 || height <= 0) {
            return
        }

        val needsRebuild = shader == null || width != prevWidth || height != prevHeight
        if (!needsRebuild) {
            return
        }

        try {
            LOGGER.info("Invalidating saturation shader group, rebuilding with new dimensions: {}x{}", width, height)
            //#if MC >= 1.16.5
            //$$ shader?.close()
            //#else
            shader?.deleteShaderGroup()
            //#endif
        } catch (_: Throwable) {  }

        try {
            LOGGER.info("Building saturation shader group with dimensions: {}x{}", width, height)
            shader = ShaderGroup(client.textureManager, client.resourceManager, mainTarget, LOCATION)
            shader!!.createBindFramebuffers(width, height)
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
        shader.loadShaderGroup(tickDelta)
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
            val uniform = pass
                //#if MC >= 1.16.5
                //$$ .effect
                //$$ .getUniform("Saturation")
                //#else
                .shaderManager
                .getShaderUniform("Saturation")
                //#endif
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
