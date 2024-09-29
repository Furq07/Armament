package dev.furq.armament.listeners

import dev.furq.armament.Armament
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.persistence.PersistentDataType

class ArmorTrimListener(private val plugin: Armament) : Listener {

    @EventHandler
    fun onPrepareSmithing(event: PrepareSmithingEvent) {

        val inventory = event.inventory
        val armorItem = inventory.getItem(1) ?: return
        val armorMeta = armorItem.itemMeta ?: return

        val key = NamespacedKey(plugin, "armor")
        if (armorMeta.persistentDataContainer.has(key, PersistentDataType.STRING)) {
            event.result = null
        }
    }
}