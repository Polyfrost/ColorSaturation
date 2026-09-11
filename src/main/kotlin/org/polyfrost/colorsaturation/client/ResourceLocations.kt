package org.polyfrost.colorsaturation.client

//~ if >=1.21.11 'ResourceLocation' -> 'Identifier' {
import net.minecraft.resources.Identifier

fun location(namespace: String, path: String) = Identifier.fromNamespaceAndPath(namespace, path)
//~}