package dev.furq.armament.gui

import dev.furq.armament.Armament
import dev.furq.armament.utils.ArmorCreator
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File

class ArmorGUI(private val plugin: Armament) {
    private val armorsConfigFile = File(plugin.dataFolder, "armors.yml")
    private val armorsConfig = YamlConfiguration.loadConfiguration(armorsConfigFile)
    private val armors = armorsConfig.getConfigurationSection("armors")?.getKeys(false)?.toList() ?: emptyList()
    private val itemsPerPage = 45
    val guiTitle = "§9Armament Armors"

    fun openGUI(player: Player, page: Int) {
        val inventory = Bukkit.createInventory(null, 54, "$guiTitle §8| §6Page ${page + 1}")
        val startIndex = page * itemsPerPage
        val endIndex = minOf(startIndex + itemsPerPage, armors.size)

        for (i in startIndex until endIndex) {
            val armorName = armors[i]
            val armorItem = ArmorCreator(plugin).createArmorPiece(armorName, "chestplate")
            if (armorItem != null) {
                inventory.addItem(armorItem)
            }
        }

        if (page > 0) {
            inventory.setItem(45, createNavigationButton("Previous Page"))
        }
        if (endIndex < armors.size) {
            inventory.setItem(53, createNavigationButton("Next Page"))
        }

        player.openInventory(inventory)
    }

    private fun createNavigationButton(name: String): ItemStack {
        return ItemStack(Material.ARROW).apply {
            itemMeta = itemMeta?.apply {
                setDisplayName(ChatColor.YELLOW.toString() + name)
            }
        }
    }

    fun getMaxPages(): Int {
        return (armors.size - 1) / itemsPerPage + 1
    }
}