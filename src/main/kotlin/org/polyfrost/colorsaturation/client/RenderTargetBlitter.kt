package org.polyfrost.colorsaturation.client

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
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import org.polyfrost.colorsaturation.ColorSaturationConstants
//? if >=26.1
/*import java.util.Optional*/
//? if <26.2
import java.util.OptionalInt

object RenderTargetBlitter {
    private val pipeline = RenderPipeline.builder()
        .withLocation(location(ColorSaturationConstants.ID, "blit_pipeline"))
        .withVertexShader(location(ColorSaturationConstants.ID, "core/fullscreen_quad"))
        .withFragmentShader("core/blit_screen")
        //? if >=26.2 {
        /*.withVertexBinding(0, DefaultVertexFormat.POSITION)
        .withPrimitiveTopology(PrimitiveTopology.QUADS)
        .withDepthStencilState(Optional.empty())
        .withColorTargetState(ColorTargetState.DEFAULT)
        .withBindGroupLayout(
            BindGroupLayout.builder()
                .withSampler("InSampler")
                .build()
        )
        *///?}
        //? if <26.2 {
        .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
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
        .withSampler("InSampler")
        //?}
        .build()

    fun blit(srcTarget: RenderTarget, dstTarget: RenderTarget) {
        RenderSystem.assertOnRenderThread()

        //? if >=26.2 {
        /*val autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS)*/
        //?}
        //? if <26.2 {
        val autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS)
        //?}
        val indexBuffer = autoStorageIndexBuffer.getBuffer(6)
        val vertexBuffer = FullscreenQuad.vertexBuffer

        RenderSystem.getDevice().createCommandEncoder().createRenderPass(
            { "ColorSaturation/Blit" },
            dstTarget.getColorTextureView()!!,
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
            renderPass.setIndexBuffer(indexBuffer, autoStorageIndexBuffer.type())
            //? if >=1.21.11 {
            /*renderPass.bindTexture("InSampler", srcTarget.getColorTextureView()!!, SaturationSampler.linearClamp)*/
            //?}
            //? if <1.21.11 {
            renderPass.bindSampler("InSampler", srcTarget.getColorTextureView()!!)
            //?}
            //? if >=26.2 {
            /*renderPass.drawIndexed(6, 1, 0, 0, 0)*/
            //?}
            //? if <26.2 {
            renderPass.drawIndexed(0, 0, 6, 1)
            //?}
        }
    }
}
//?}
