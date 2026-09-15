package org.polyfrost.colorsaturation.client

//? if >1.21.5 {
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.ByteBufferBuilder
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.renderpearl.api.buffers.GpuBuffer
import com.mojang.renderpearl.api.vertex.VertexFormat
//? if >=26.2
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology

object FullscreenTriangle {
    const val VERTEX_COUNT = 3

    val vertexBuffer: GpuBuffer by lazy {
        val storage = ByteBufferBuilder(DefaultVertexFormat.POSITION.vertexSize * VERTEX_COUNT)
        val builder = BufferBuilder(
            storage,
            //? if >=26.2
            PrimitiveTopology.TRIANGLES,
            //? if <26.2
            //VertexFormat.Mode.TRIANGLES,
            DefaultVertexFormat.POSITION
        )

        builder.addVertex(-1f, -1f, 0f)
        builder.addVertex(3f, -1f, 0f)
        builder.addVertex(-1f, 3f, 0f)

        builder.buildOrThrow().use { mesh ->
            RenderSystem.getDevice().createBuffer({ "ColorSaturation fullscreen triangle" }, GpuBuffer.USAGE_VERTEX, mesh.vertexBuffer())
        }
    }
}
//?}
