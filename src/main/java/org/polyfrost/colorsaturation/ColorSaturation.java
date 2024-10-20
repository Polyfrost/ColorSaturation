package org.polyfrost.colorsaturation;

import org.polyfrost.colorsaturation.command.SaturationCommand;
import org.polyfrost.colorsaturation.config.SaturationConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.polyfrost.oneconfig.api.commands.v1.CommandManager;
import org.polyfrost.oneconfig.api.event.v1.EventManager;

/**
 * The entrypoint of the Example Mod that initializes it.
 *
 * @see Mod
 * @see org.polyfrost.oneconfig.api.event.v1.events.InitializationEvent
 */
@Mod(modid = ColorSaturation.MODID, name = ColorSaturation.NAME, version = ColorSaturation.VERSION)
public class ColorSaturation {

    // Sets the variables from `gradle.properties`. See the `blossom` config in `build.gradle.kts`.
    public static final String MODID = "@MOD_ID@";
    public static final String NAME = "@MOD_NAME@";
    public static final String VERSION = "@MOD_VERSION@";
    public static SaturationConfig config;

    // Register the config and commands.
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new SaturationConfig();
        CommandManager.registerCommand(new SaturationCommand());
        EventManager.INSTANCE.register(new Saturation());
    }
}
