package org.polyfrost.colorsaturation.client

import org.polyfrost.colorsaturation.ColorSaturationConstants
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
import kotlin.math.abs

object ColorSaturationConfig : Config(
    "${ColorSaturationConstants.ID}.json",
    "/assets/colorsaturation/colorsaturation_dark.svg",
    ColorSaturationConstants.NAME,
    Category.QOL,
) {
    @JvmField
    @Switch(title = "Enabled")
    var isEnabled = false

    @JvmField
    @Slider(title = "Saturation Strength", min = -1f, max = 5f, step = 0.05f)
    var strength = 1f

    @JvmField
    @Slider(title = "Contrast", min = 0f, max = 2f, step = 0.05f)
    var contrast = 1f

    @JvmField
    @Slider(title = "Brightness", min = 0f, max = 2f, step = 0.05f)
    var brightness = 1f

    @JvmField
    @Slider(title = "Hue Shift", min = -180f, max = 180f, step = 1f)
    var hue = 0f

    @JvmStatic
    val isIdentity: Boolean
        get() = abs(strength - 1f) < 1e-4f &&
            abs(contrast - 1f) < 1e-4f &&
            abs(brightness - 1f) < 1e-4f &&
            abs(hue) < 1e-4f

    init {
        for (key in arrayOf("strength", "contrast", "brightness", "hue")) {
            addCallback(key) {
                SaturationHandler.updateShaderUniforms()
            }
        }
        save()
    }
}
