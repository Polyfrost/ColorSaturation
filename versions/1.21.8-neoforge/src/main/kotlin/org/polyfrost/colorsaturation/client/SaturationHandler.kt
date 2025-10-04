package org.polyfrost.colorsaturation.client

import com.mojang.blaze3d.framegraph.FrameGraphBuilder
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.platform.DepthTestFunction
import com.mojang.blaze3d.resource.CrossFrameResourcePool
import com.mojang.blaze3d.shaders.UniformType
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.identifierOrThrow
import org.polyfrost.colorsaturation.ColorSaturationConstants
import java.util.OptionalInt
import kotlin.use

object SaturationHandler {
    private val pipeline by lazy {
        RenderPipeline.builder()
            .withLocation(identifierOrThrow(ColorSaturationConstants.ID, "saturation_pipeline"))
            .withVertexShader("core/blit_screen")
            .withFragmentShader(identifierOrThrow(ColorSaturationConstants.ID, "post/color_saturation"))
            .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
            .withDepthWrite(false)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .withColorWrite(true, true)
            .withUniform("SaturationConfig", UniformType.UNIFORM_BUFFER)
            .withSampler("DiffuseSampler")
            .withSampler("PrevSampler")
            .build()
    }

    @JvmStatic
    val isActive: Boolean
        get() = ColorSaturationConfig.isEnabled

    @JvmStatic
    fun render(renderTarget: RenderTarget, resourcePool: CrossFrameResourcePool) {
        if (!isActive) {
            return
        }

        InternalTargetTracker.updateSize(renderTarget.viewWidth, renderTarget.viewHeight)
        SaturationUniforms.upload(ColorSaturationConfig.strength)

        val tempTarget = InternalTargetTracker.target ?: return
        val builder = FrameGraphBuilder()
        val tempNode = builder.importExternal("saturation_temp", tempTarget)
        builder.addPass("ColorSaturation/Saturation").apply {
            readsAndWrites(tempNode)

            executes {
                val autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS)
                val indexBuffer = autoStorageIndexBuffer.getBuffer(6)
                val vertexBuffer = RenderSystem.getQuadVertexBuffer()

                RenderSystem.getDevice().createCommandEncoder().createRenderPass(
                    { "PolyBlur/Phosphor" },
                    tempTarget.colorTextureView,
                    OptionalInt.empty()
                ).use { renderPass ->
                    renderPass.setPipeline(pipeline)
                    renderPass.setVertexBuffer(0, vertexBuffer)
                    renderPass.setIndexBuffer(indexBuffer, autoStorageIndexBuffer.type())

                    renderPass.bindSampler("DiffuseSampler", renderTarget.colorTextureView)

                    renderPass.setUniform("SaturationConfig", SaturationUniforms.buffer)
                    renderPass.drawIndexed(0, 0, 6, 1)
                }
            }
        }

        builder.execute(resourcePool)
        blit(tempTarget, renderTarget)
    }

    fun blit(srcTarget: RenderTarget, dstTarget: RenderTarget) {
        RenderSystem.assertOnRenderThread()

        val autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS)
        val indexBuffer = autoStorageIndexBuffer.getBuffer(6)
        val vertexBuffer = RenderSystem.getQuadVertexBuffer()

        RenderSystem.getDevice().createCommandEncoder().createRenderPass(
            { "Blit Handler" },
            dstTarget.colorTextureView,
            OptionalInt.empty()
        ).use { renderPass ->
            renderPass.setPipeline(pipeline)
            renderPass.setVertexBuffer(0, vertexBuffer)
            renderPass.setIndexBuffer(indexBuffer, autoStorageIndexBuffer.type())
            renderPass.bindSampler("InSampler", srcTarget.colorTextureView)
            renderPass.drawIndexed(0, 0, 6, 1)
        }
    }
}
