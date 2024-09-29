package dev.furq.armament.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.furq.armament.Armament
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class DatapackGenerator(private val plugin: Armament) {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    fun generateDatapack() {
        val worldFolder = Bukkit.getWorlds()[0].worldFolder
        val datapackFolder = File(worldFolder, "datapacks/armament")
        datapackFolder.mkdirs()

        val armorsConfig = YamlConfiguration.loadConfiguration(File(plugin.dataFolder, "armors.yml"))
        cleanupDatapack(datapackFolder, armorsConfig)

        val packMcmeta = mapOf(
            "pack" to mapOf(
                "pack_format" to 15,
                "description" to "Armament Custom Armors Datapack"
            )
        )
        File(datapackFolder, "pack.mcmeta").writeText(gson.toJson(packMcmeta))
        val trimPatternsFolder = File(datapackFolder, "data/armament/trim_pattern")
        trimPatternsFolder.mkdirs()

        armorsConfig.getConfigurationSection("armors")?.getKeys(false)?.forEach { armorName ->
            val trimPatternData = mapOf(
                "description" to mapOf("translate" to "trim_pattern.armament.$armorName"),
                "asset_id" to "armament:$armorName",
                "template_item" to "minecraft:debug_stick"
            )
            File(trimPatternsFolder, "$armorName.json").writeText(gson.toJson(trimPatternData))
        }

        val armorMaterial = MaterialGetter(plugin).getArmorString()
        val mcTrimPatternsFolder = File(datapackFolder, "data/minecraft/trim_pattern")
        mcTrimPatternsFolder.mkdirs()
        val materialTrimPatternData = mapOf(
            "description" to mapOf("translate" to "trim_pattern.minecraft.${armorMaterial.lowercase()}"),
            "asset_id" to "minecraft:${armorMaterial.lowercase()}",
            "template_item" to "minecraft:debug_stick"
        )
        File(mcTrimPatternsFolder, "${armorMaterial.lowercase()}.json").writeText(gson.toJson(materialTrimPatternData))

        plugin.logger.info("Datapack generated successfully!")
    }

    private fun cleanupDatapack(datapackFolder: File, armorsConfig: YamlConfiguration) {
        val currentArmors = armorsConfig.getConfigurationSection("armors")?.getKeys(false) ?: emptySet()
        val currentMaterial = MaterialGetter(plugin).getArmorString().lowercase()

        val trimPatternsFolder = File(datapackFolder, "data/armament/trim_pattern")
        if (trimPatternsFolder.exists()) {
            trimPatternsFolder.listFiles()?.forEach { file ->
                val armorName = file.nameWithoutExtension
                if (armorName !in currentArmors) {
                    file.delete()
                }
            }
        }

        val mcTrimPatternsFolder = File(datapackFolder, "data/minecraft/trim_pattern")
        if (mcTrimPatternsFolder.exists()) {
            mcTrimPatternsFolder.listFiles()?.forEach { file ->
                if (file.nameWithoutExtension != currentMaterial) {
                    file.delete()
                }
            }
        }

        val materialFile = File(mcTrimPatternsFolder, "$currentMaterial.json")
        if (!materialFile.exists()) {
            mcTrimPatternsFolder.listFiles()?.forEach { file ->
                file.delete()
            }
        }
    }
}
