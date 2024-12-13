package org.polyfrost.colorsaturation

//#if FORGE
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#else
//$$ import net.fabricmc.api.ClientModInitializer
//#endif

import org.polyfrost.colorsaturation.command.SaturationCommand
import org.polyfrost.colorsaturation.config.SaturationConfig
import org.polyfrost.oneconfig.api.commands.v1.CommandManager
import org.polyfrost.oneconfig.api.event.v1.EventManager

//#if FORGE
@Mod(modid = ColorSaturation.ID, version = ColorSaturation.VERSION, name = ColorSaturation.NAME, modLanguageAdapter = "org.polyfrost.oneconfig.utils.v1.forge.KotlinLanguageAdapter")
//#endif
object ColorSaturation
    //#if FABRIC
    //$$ : ClientModInitializer
    //#endif
{

    const val ID = "@MOD_ID@"
    const val NAME = "@MOD_NAME@"
    const val VERSION = "@MOD_VERSION@"

    @JvmStatic
    lateinit var config: SaturationConfig
        private set

    fun initialize() {
        config = SaturationConfig()
        CommandManager.registerCommand(SaturationCommand())
        EventManager.INSTANCE.register(Saturation())
    }

    //#if FORGE
    @Mod.EventHandler
    fun onInit(e: FMLInitializationEvent) {
        initialize()
    }
    //#else
    //$$ override fun onInitializeClient() {
    //$$     initialize()
    //$$ }
    //#endif

}
