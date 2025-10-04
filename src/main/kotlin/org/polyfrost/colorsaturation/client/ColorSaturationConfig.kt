package org.polyfrost.colorsaturation.client

import org.polyfrost.colorsaturation.ColorSaturationConstants
import org.polyfrost.oneconfig.api.config.v1.KtConfig

object ColorSaturationConfig : KtConfig(
    id = "${ColorSaturationConstants.ID}.json",
    title = ColorSaturationConstants.NAME,
    category = Category.QOL,
    icon = "/assets/colorsaturation/colorsaturation_dark.svg"
) {
    @JvmStatic var isEnabled by switch(def = true, name = "Enabled")
    @JvmStatic var forceDisableFastRender by switch(def = true, name = "Force Disable Fast Render", description = "Forces OptiFine's Fast Render option to be disabled.")

    var strength by slider(min = -1f, max = 5f, def = 1f, name = "Saturation Strength")

    init {
        //#if MC < 1.21.2
        addCallback("strength") {
            if (isEnabled) {
                SaturationHandler.updateShaderUniforms()
            }
        }
        //#endif
    }
}
