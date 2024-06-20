package org.polyfrost.colorsaturation.command;

import org.polyfrost.colorsaturation.ColorSaturation;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value = ColorSaturation.MODID, description = "Access the " + ColorSaturation.NAME + " GUI.")
public class SaturationCommand {
    @Main
    private void handle() {
        ColorSaturation.config.openGui();
    }
}