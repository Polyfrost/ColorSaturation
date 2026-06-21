package org.polyfrost.colorsaturation.client

import org.polyfrost.colorsaturation.ColorSaturationConstants
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch

object ColorSaturationConfig : Config(
    "${ColorSaturationConstants.ID}.json",
    "/assets/colorsaturation/colorsaturation_dark.svg",
    ColorSaturationConstants.NAME,
    Category.QOL,
) {
    @JvmField
    @Switch(title = "Enabled")
    var isEnabled = true

    @JvmField
    @Slider(title = "Saturation Strength", min = -1f, max = 5f, step = 0.05f)
    var strength = 1f

    init {
        addCallback("strength") {
            SaturationHandler.updateShaderUniforms()
        }
        save()
    }
}
