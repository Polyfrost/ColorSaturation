package org.polyfrost.colorsaturation.mixin.client;

//? if =1.8.9 {
/*import java.util.List;
import net.minecraft.client.render.PostChain;
import net.minecraft.client.render.PostPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PostChain.class)
public interface PostChainAccessor {
    @Accessor
    List<PostPass> getPasses();
}
*///?} else {
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameRenderer.class)
public interface PostChainAccessor {
}
//?}
