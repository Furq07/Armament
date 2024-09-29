package dev.furq.armament.utils

import dev.furq.armament.Armament
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.persistence.PersistentDataType
import java.io.File

class InventoryUpdater(private val plugin: Armament) {

    private val armorsConfigFile = File(plugin.dataFolder, "armors.yml")
    private val armorsConfig = YamlConfiguration.loadConfiguration(armorsConfigFile)

    fun updatePlayerInventory(inventory: PlayerInventory) {
        inventory.contents.filterNotNull().forEachIndexed { index, item ->
            if (isCustomArmor(item)) {
                updateItemIfNeeded(item, inventory, index)
            }
        }
    }

    fun updateItemIfNeeded(item: ItemStack?, inventory: PlayerInventory, index: Int) {
        val itemMeta = item?.itemMeta ?: return
        val pdc = itemMeta.persistentDataContainer
        val armorID = pdc.get(NamespacedKey(plugin, "armor"), PersistentDataType.STRING) ?: return
        val armorConfig = armorsConfig.getConfigurationSection("armors.$armorID") ?: return
        val piece = when {
            item.type.name.endsWith("_HELMET") -> "helmet"
            item.type.name.endsWith("_CHESTPLATE") -> "chestplate"
            item.type.name.endsWith("_LEGGINGS") -> "leggings"
            item.type.name.endsWith("_BOOTS") -> "boots"
            else -> return
        }
        val customModelData = armorConfig.getInt("custom_model_data")
        val displayName = armorConfig.getString("$piece.name")?.let { ChatColor.translateAlternateColorCodes('&', it) }
        val lore = armorConfig.getStringList("$piece.lore").map { ChatColor.translateAlternateColorCodes('&', it) }

        var updateNeeded = false

        val materialGetter = MaterialGetter(plugin)
        if (item.type !in materialGetter.getArmorMaterial()) {
            val material =
                materialGetter.getArmorMaterial().firstOrNull { it.name.contains(item.type.name.split("_").last()) }
            if (material != null) {
                item.type = material
                updateNeeded = true
            }
        }

        if (displayName != itemMeta.displayName) {
            itemMeta.setDisplayName(displayName)
            updateNeeded = true
        }

        if (customModelData != itemMeta.customModelData) {
            itemMeta.setCustomModelData(customModelData)
            updateNeeded = true
        }

        if (lore != itemMeta.lore) {
            itemMeta.lore = lore
            updateNeeded = true
        }

        if (updateNeeded) {
            item.itemMeta = itemMeta
            inventory.setItem(index, item)
        }
    }

    private fun isCustomArmor(item: ItemStack): Boolean {
        val pdc = item.itemMeta?.persistentDataContainer
        return pdc?.has(NamespacedKey(plugin, "armor"), PersistentDataType.STRING) == true
    }
}