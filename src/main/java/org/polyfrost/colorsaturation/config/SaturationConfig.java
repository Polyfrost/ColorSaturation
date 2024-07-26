package org.polyfrost.colorsaturation.config;

import org.polyfrost.colorsaturation.ColorSaturation;
import org.polyfrost.colorsaturation.Saturation;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;

/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class SaturationConfig extends Config {

    // Temporary
    public boolean enabled = true;

    // @Info(
    //        text = "This mod will ONLY work if either Fast Render is disabled or Force Disable Fast Render is enabled.",
    //        size = 2,
    //        type = InfoType.WARNING
    //)
    private Runnable info = () -> { };

    @Switch(
            title = "Force Disable Fast Render"
    )
    public static boolean forceDisableFastRender = true;

    @Slider(
            title = "Saturation Slider",
            min = -1f, max = 5 // Minimum and maximum values for the slider.
    )
    public static float saturation = 1;

    public SaturationConfig() {
        super(ColorSaturation.MODID + ".json", "/colorsaturation.svg", ColorSaturation.NAME, Category.QOL);

        addCallback("saturation", () -> {
            if (enabled) {
                Saturation.reloadSaturation();
            }
        });
    }
}

