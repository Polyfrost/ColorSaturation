package org.polyfrost.colorsaturation.command;

import org.polyfrost.colorsaturation.ColorSaturation;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;

@Command(value = ColorSaturation.MODID, description = "Access the " + ColorSaturation.NAME + " GUI.")
public class SaturationCommand {
    @Command
    private void handle() {
        // TODO Implement openGui
        // ColorSaturation.config.openGui();
    }
}