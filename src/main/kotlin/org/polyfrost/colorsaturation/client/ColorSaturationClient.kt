package org.polyfrost.colorsaturation.client

import dev.deftu.omnicore.api.client.commands.OmniClientCommands
import dev.deftu.omnicore.api.client.commands.command
import org.polyfrost.colorsaturation.ColorSaturationConstants
import org.polyfrost.oneconfig.utils.v1.dsl.createScreen

object ColorSaturationClient {
    fun initialize() {
        ColorSaturationConfig.preload()

        OmniClientCommands.command(ColorSaturationConstants.ID) {
            runs { ctx ->
                ctx.source.openScreen(ColorSaturationConfig.createScreen())
            }
        }.register()
    }
}
