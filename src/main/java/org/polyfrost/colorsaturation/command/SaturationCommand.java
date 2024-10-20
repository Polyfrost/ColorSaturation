package org.polyfrost.colorsaturation.command;

import org.polyfrost.colorsaturation.ColorSaturation;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.utils.v1.dsl.ScreensKt;

@Command(value = ColorSaturation.MODID, description = "Access the " + ColorSaturation.NAME + " GUI.")
public class SaturationCommand {
    @Command
    private void main() {
        // TODO Implement openGui
        // ColorSaturation.config.openGui();
        ScreensKt.openUI(ColorSaturation.config);
    }
}