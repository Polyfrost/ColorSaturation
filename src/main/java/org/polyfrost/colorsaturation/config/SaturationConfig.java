package org.polyfrost.colorsaturation.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import org.polyfrost.colorsaturation.ColorSaturation;
import org.polyfrost.colorsaturation.Saturation;

/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class SaturationConfig extends Config {

    @Info(
            text = "This mod will ONLY work if either Fast Render is disabled or Force Disable Fast Render is enabled.",
            size = 2,
            type = InfoType.WARNING
    )
    private boolean agajsjg = false;

    @Switch(
            name = "Force Disable Fast Render"
    )
    public static boolean forceDisableFastRender = true;

    @Slider(
            name = "Example Slider",
            min = -1f, max = 5 // Minimum and maximum values for the slider.
    )
    public static float saturation = 1;

    public SaturationConfig() {
        super(new Mod(ColorSaturation.NAME, ModType.UTIL_QOL), ColorSaturation.MODID + ".json");
        initialize();

        addListener("saturation", () -> {
            if (enabled) {
                Saturation.reloadSaturation();
            }
        });
    }
}

