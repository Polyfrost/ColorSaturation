package org.polyfrost.colorsaturation.client

//? if >1.8.9 {
//~ if >=1.21.11 'ResourceLocation' -> 'Identifier' {
import net.minecraft.resources.Identifier

fun location(namespace: String, path: String) = Identifier.fromNamespaceAndPath(namespace, path)
//~}
//?} else {
/*import net.minecraft.resource.Identifier

fun location(namespace: String, path: String) = Identifier(namespace, path)
*///?}