package org.polyfrost.colorsaturation

import net.fabricmc.api.ClientModInitializer
import org.polyfrost.colorsaturation.client.ColorSaturationClient

class ColorSaturationEntrypoint : ClientModInitializer {
    override
    fun onInitializeClient() {
        ColorSaturationClient.initialize()
    }
}
