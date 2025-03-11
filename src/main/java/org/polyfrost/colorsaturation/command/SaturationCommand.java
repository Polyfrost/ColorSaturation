package org.polyfrost.colorsaturation.command;

import org.polyfrost.colorsaturation.ColorSaturation;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.oneconfig.utils.v1.dsl.ScreensKt;

@Command(ColorSaturation.ID)
public class SaturationCommand {
    @Command
    private void main() {
        // TODO Implement openGui
        // ColorSaturation.getConfig().openGui();
        ScreensKt.openUI(ColorSaturation.getConfig());
    }
}