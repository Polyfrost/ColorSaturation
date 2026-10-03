package org.polyfrost.colorsaturation.mixin.client;

import net.minecraft.client.renderer.PostChain;
import org.spongepowered.asm.mixin.Mixin;

//? if =1.8.9 {
/*import java.util.List;
import net.minecraft.client.render.PostPass;
import org.spongepowered.asm.mixin.gen.Accessor;
*///?}

// Only 1.8.9 lacks PostChain#setUniform; the empty mixin elsewhere keeps the mixin config shared
@Mixin(PostChain.class)
public interface PostChainAccessor {
    //? if =1.8.9 {
    /*@Accessor
    List<PostPass> getPasses();
    *///?}
}
