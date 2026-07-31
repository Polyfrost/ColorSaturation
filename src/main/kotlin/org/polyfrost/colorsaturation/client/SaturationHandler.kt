package org.polyfrost.colorsaturation.client

//? if =1.21.1 {
/*import com.google.gson.JsonSyntaxException
import com.mojang.blaze3d.pipeline.RenderTarget
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.PostChain
import org.apache.logging.log4j.LogManager
import java.io.IOException

object SaturationHandler {
    private val logger = LogManager.getLogger(SaturationHandler::class.java)
    private val shaderLocation = location("minecraft", "shaders/post/color_saturation.json")

    private var postChain: PostChain? = null
    private var prevWidth = -1
    private var prevHeight = -1

    @JvmStatic
    val isActive: Boolean
        get() = ColorSaturationConfig.isEnabled && !ColorSaturationConfig.isIdentityStrength

    @JvmStatic
    fun updateShaderUniforms() {
        postChain?.setUniform("Saturation", ColorSaturationConfig.strength)
    }

    @JvmStatic
    fun update() {
        val minecraft = Minecraft.getInstance()
        val renderTarget = minecraft.mainRenderTarget ?: return
        getPostChain(renderTarget)
    }

    @JvmStatic
    fun render(tickDelta: Float) {
        if (!isActive) {
            return
        }

        updateShaderUniforms()
        postChain?.process(tickDelta)
    }

    private fun getPostChain(renderTarget: RenderTarget): PostChain? {
        if (postChain != null && renderTarget.viewWidth == prevWidth && renderTarget.viewHeight == prevHeight) {
            return postChain
        }

        postChain?.close()
        postChain = null

        return try {
            val minecraft = Minecraft.getInstance()
            PostChain(minecraft.textureManager, minecraft.resourceManager, renderTarget, shaderLocation).also {
                it.resize(renderTarget.viewWidth, renderTarget.viewHeight)
                postChain = it
                prevWidth = renderTarget.viewWidth
                prevHeight = renderTarget.viewHeight
                updateShaderUniforms()
            }
        } catch (e: IOException) {
            logger.error("Could not load color saturation shader", e)
            null
        } catch (e: JsonSyntaxException) {
            logger.error("Could not parse color saturation shader", e)
            null
        }
    }
}
*///?}

//? if >=1.21.4 && <=1.21.5 {
/*import com.google.gson.JsonSyntaxException
import com.mojang.blaze3d.framegraph.FrameGraphBuilder
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.resource.CrossFrameResourcePool
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.LevelTargetBundle
import net.minecraft.client.renderer.PostChain
import org.apache.logging.log4j.LogManager
import org.polyfrost.colorsaturation.ColorSaturationConstants
*///?}

//? if =1.21.5
/*import java.util.function.Consumer*/

//? if >=1.21.4 && <=1.21.5 {
/*object SaturationHandler {
    private val logger = LogManager.getLogger(SaturationHandler::class.java)
    private val shaderLocation by lazy { location(ColorSaturationConstants.ID, "color_saturation") }

    @JvmStatic
    val isActive: Boolean
        get() = ColorSaturationConfig.isEnabled && !ColorSaturationConfig.isIdentityStrength

    @JvmStatic
    fun updateShaderUniforms() {
    }

    @JvmStatic
    fun render(renderTarget: RenderTarget, resourcePool: CrossFrameResourcePool) {
        if (!isActive) {
            return
        }

        val shader = try {
            Minecraft.getInstance().shaderManager.getPostChain(shaderLocation, LevelTargetBundle.MAIN_TARGETS)
        } catch (e: JsonSyntaxException) {
            logger.error("Could not load color saturation shader", e)
            null
        }

        val builder = FrameGraphBuilder()
        val targetBundle = PostChain.TargetBundle.of(
            LevelTargetBundle.MAIN_TARGET_ID,
            builder.importExternal("main", renderTarget),
        )

        //? if =1.21.5 {
        shader?.addToFrame(
            builder,
            renderTarget.viewWidth,
            renderTarget.viewHeight,
            targetBundle,
            Consumer { renderPass: com.mojang.blaze3d.systems.RenderPass ->
                renderPass.setUniform("Saturation", ColorSaturationConfig.strength)
            }
        )
        //?} else {
        /*shader?.setUniform("Saturation", ColorSaturationConfig.strength)
        shader?.addToFrame(
            builder,
            renderTarget.viewWidth,
            renderTarget.viewHeight,
            targetBundle
        )
        *///?}

        builder.execute(resourcePool)
    }
}
*///?}

//? if >1.21.5 {
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.pipeline.RenderTarget
//? if >=26.2
/*import com.mojang.blaze3d.PrimitiveTopology*/
//? if >=26.1
/*import com.mojang.blaze3d.pipeline.ColorTargetState*/
//? if >=26.2
/*import com.mojang.blaze3d.pipeline.BindGroupLayout*/
//? if >=26.1
/*import com.mojang.blaze3d.pipeline.DepthStencilState*/
//? if >=26.1
/*import com.mojang.blaze3d.platform.CompareOp*/
//? if <26.1
import com.mojang.blaze3d.platform.DepthTestFunction
import com.mojang.blaze3d.resource.CrossFrameResourcePool
import com.mojang.blaze3d.shaders.UniformType
import com.mojang.blaze3d.systems.RenderSystem
//? if >=1.21.11
/*import com.mojang.blaze3d.textures.FilterMode*/
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import org.polyfrost.colorsaturation.ColorSaturationConstants
//? if >=26.1
/*import java.util.Optional*/
//? if <26.2
import java.util.OptionalInt

object SaturationHandler {
    private val pipeline by lazy {
        RenderPipeline.builder()
            .withLocation(location(ColorSaturationConstants.ID, "saturation_pipeline"))
            .withVertexShader(location(ColorSaturationConstants.ID, "core/fullscreen_triangle"))
            .withFragmentShader(location(ColorSaturationConstants.ID, "post/color_saturation"))
            //? if >=26.2 {
            /*.withVertexBinding(0, DefaultVertexFormat.POSITION)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
            .withDepthStencilState(Optional.empty())
            .withColorTargetState(ColorTargetState.DEFAULT)
            .withBindGroupLayout(
                BindGroupLayout.builder()
                    .withSampler("DiffuseSampler")
                    .withUniform("SaturationConfig", UniformType.UNIFORM_BUFFER)
                    .build()
            )
            *///?}
            //? if <26.2 {
            .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.TRIANGLES)
            //?}
            //? if >=26.1 && <26.2 {
            /*.withDepthStencilState(Optional.empty())
            .withColorTargetState(ColorTargetState.DEFAULT)
            *///?}
            //? if <26.1 {
            .withDepthWrite(false)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .withColorWrite(true, true)
            //?}
            //? if <26.2 {
            .withUniform("SaturationConfig", UniformType.UNIFORM_BUFFER)
            .withSampler("DiffuseSampler")
            //?}
            .build()
    }

    @JvmStatic
    val isActive: Boolean
        get() = ColorSaturationConfig.isEnabled && !ColorSaturationConfig.isIdentityStrength

    @JvmStatic
    fun updateShaderUniforms() {
    }

    @JvmStatic
    fun render(renderTarget: RenderTarget, resourcePool: CrossFrameResourcePool) {
        if (!isActive) {
            InternalTargetTracker.free()
            return
        }

        InternalTargetTracker.updateSize(renderTarget)
        SaturationUniforms.upload(ColorSaturationConfig.strength)

        val tempTarget = InternalTargetTracker.target ?: return
        val commandEncoder = RenderSystem.getDevice().createCommandEncoder()

        commandEncoder.copyTextureToTexture(
            renderTarget.getColorTexture()!!,
            tempTarget.getColorTexture()!!,
            0, 0, 0, 0, 0, renderTarget.width, renderTarget.height,
        )

        val vertexBuffer = FullscreenTriangle.vertexBuffer

        commandEncoder.createRenderPass(
            { "ColorSaturation/Saturation" },
            renderTarget.getColorTextureView()!!,
            //? if >=26.2 {
            /*Optional.empty()*/
            //?}
            //? if <26.2 {
            OptionalInt.empty()
            //?}
        ).use { renderPass ->
            renderPass.setPipeline(pipeline)
            //? if >=26.2 {
            /*renderPass.setVertexBuffer(0, vertexBuffer.slice())*/
            //?}
            //? if <26.2 {
            renderPass.setVertexBuffer(0, vertexBuffer)
            //?}
            //? if >=1.21.11 {
            /*renderPass.bindTexture(
                "DiffuseSampler",
                tempTarget.getColorTextureView()!!,
                RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST)
            )*/
            //?}
            //? if <1.21.11 {
            renderPass.bindSampler("DiffuseSampler", tempTarget.getColorTextureView()!!)
            //?}
            renderPass.setUniform("SaturationConfig", SaturationUniforms.buffer)
            //? if >=26.2 {
            /*renderPass.draw(FullscreenTriangle.VERTEX_COUNT, 1, 0, 0)*/
            //?}
            //? if <26.2 {
            renderPass.draw(0, FullscreenTriangle.VERTEX_COUNT)
            //?}
        }
    }
}
//?}
