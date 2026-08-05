package org.polyfrost.colorsaturation.client

//~ if >=1.21.11 'ResourceLocation' -> 'Identifier' {
import net.minecraft.resources.ResourceLocation

fun location(namespace: String, path: String) = ResourceLocation.fromNamespaceAndPath(namespace, path)
//~}