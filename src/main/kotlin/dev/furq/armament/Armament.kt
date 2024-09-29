package dev.furq.armament

import dev.furq.armament.commands.ArmamentCommand
import dev.furq.armament.listeners.ArmorTrimListener
import dev.furq.armament.listeners.InventoryUpdateListener
import dev.furq.armament.listeners.GUIListener
import dev.furq.armament.utils.DatapackGenerator
import dev.furq.armament.utils.ResourcePackGenerator
import dev.furq.armament.utils.TabCompleter
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Armament : JavaPlugin() {
    private lateinit var inventoryUpdateListener: InventoryUpdateListener
    private lateinit var messagesConfig: YamlConfiguration

    override fun onEnable() {
        inventoryUpdateListener = InventoryUpdateListener(this)
        server.pluginManager.registerEvents(InventoryUpdateListener(this), this)
        server.pluginManager.registerEvents(ArmorTrimListener(this), this)
        server.pluginManager.registerEvents(GUIListener(this), this)

        saveDefaultConfig()
        reloadConfig()

        val messagesConfigFile = File(dataFolder, "messages.yml")
        if (!messagesConfigFile.exists()) saveResource("messages.yml", false)
        messagesConfig = YamlConfiguration.loadConfiguration(messagesConfigFile)

        val armorsConfigFile = File(dataFolder, "armors.yml")
        if (!armorsConfigFile.exists()) saveResource("armors.yml", false)
        val armorsConfig = YamlConfiguration.loadConfiguration(armorsConfigFile)

        val sourceFolder = File(dataFolder, "source_files")
        if (!sourceFolder.exists()) sourceFolder.mkdirs()
        val targetFolder = File(dataFolder, "resource_pack")
        if (!targetFolder.exists()) targetFolder.mkdirs()

        DatapackGenerator(this).generateDatapack()
        ResourcePackGenerator(this).generateResourcePack(armorsConfig, sourceFolder, targetFolder)

        getCommand("armament")?.setExecutor(ArmamentCommand(this))
        getCommand("armament")?.tabCompleter = TabCompleter(this)

        logger.info("Thank you for using my plugin - Furq")
    }

    override fun onDisable() {
        logger.info("Armament plugin has been disabled!")
    }

    fun getMessage(key: String): String {
        return messagesConfig.getString(key, "Message not found")!!
    }
}