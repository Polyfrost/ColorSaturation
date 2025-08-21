package org.polyfrost.colorsaturation.client

import dev.deftu.omnicore.client.OmniClientCommands
import dev.deftu.omnicore.client.OmniClientCommands.command
import dev.deftu.omnicore.client.OmniClientCommands.does
import dev.deftu.omnicore.client.OmniClientCommands.register
import org.polyfrost.colorsaturation.ColorSaturationConstants
import org.polyfrost.oneconfig.utils.v1.dsl.openUI

object ColorSaturationClient {
    fun initialize() {
        ColorSaturationConfig.preload()

        OmniClientCommands.command(ColorSaturationConstants.ID) {
            does {
                ColorSaturationConfig.openUI()
                1
            }
        }.register()
    }
}
