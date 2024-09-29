package dev.furq.armament.utils

import dev.furq.armament.Armament
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.persistence.PersistentDataType
import java.io.File

class ArmorCreator(private val plugin: Armament) {

    private val armorsConfig = YamlConfiguration.loadConfiguration(File(plugin.dataFolder, "armors.yml"))
    private val materialGetter = MaterialGetter(plugin)

    fun createArmorPiece(armorName: String, piece: String): ItemStack? {
        val material =
            materialGetter.getArmorMaterial().find { it.name.endsWith("_${piece.uppercase()}") } ?: return null
        val armorConfig = armorsConfig.getConfigurationSection("armors.$armorName") ?: return null

        return ItemStack(material).apply {
            itemMeta = itemMeta?.apply {
                setDisplayName(
                    ChatColor.translateAlternateColorCodes(
                        '&',
                        armorConfig.getString("$piece.name") ?: return null
                    )
                )
                setCustomModelData(armorConfig.getInt("custom_model_data"))
                lore = armorConfig.getStringList("$piece.lore").map { ChatColor.translateAlternateColorCodes('&', it) }
                persistentDataContainer.set(NamespacedKey(plugin, "armor"), PersistentDataType.STRING, armorName)
                addItemFlags(ItemFlag.HIDE_ARMOR_TRIM)
            }
            setVanillaArmorTrim(this, armorName)
        }
    }

    fun createFullArmorSet(armorName: String): List<ItemStack?> {
        val pieces = listOf("helmet", "chestplate", "leggings", "boots")
        return pieces.mapNotNull { piece ->
            if (armorsConfig.contains("armors.$armorName.$piece")) {
                createArmorPiece(armorName, piece)
            } else {
                null
            }
        }
    }

    private fun setVanillaArmorTrim(itemStack: ItemStack, armorName: String) {
        if (itemStack.itemMeta !is ArmorMeta) return
        val armorMeta = itemStack.itemMeta as ArmorMeta

        val patternKey = NamespacedKey(plugin, armorName)
        val trimPattern = Registry.TRIM_PATTERN.get(patternKey) ?: return

        if (!armorMeta.hasTrim() || armorMeta.trim!!.pattern.key != patternKey) {
            armorMeta.trim = ArmorTrim(TrimMaterial.REDSTONE, trimPattern)
            armorMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM)
            itemStack.itemMeta = armorMeta
        }
    }
}