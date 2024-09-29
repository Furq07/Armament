package dev.furq.armament.commands

import dev.furq.armament.Armament
import dev.furq.armament.utils.ArmorCreator
import dev.furq.armament.utils.DatapackGenerator
import dev.furq.armament.utils.ResourcePackGenerator
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import dev.furq.armament.gui.ArmorGUI

class ArmamentCommand(private val plugin: Armament) : CommandExecutor {

    private var armorsConfig = YamlConfiguration.loadConfiguration(File(plugin.dataFolder, "armors.yml"))
    private val prefix = plugin.getMessage("prefix")
    private val armorCreator = ArmorCreator(plugin)

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.label.equals("armament", ignoreCase = true) && args.isNotEmpty()) {
            when (args[0].lowercase()) {
                "reload" -> handleReload(sender)
                "give" -> handleGive(sender, args)
                "giveset" -> handleGiveSet(sender, args)
                "gui" -> handleGUI(sender)
                else -> sender.sendMessage("$prefix ${plugin.getMessage("command-unknown")}")
            }
        }
        return true
    }

    private fun handleReload(sender: CommandSender) {
        plugin.reloadConfig()
        armorsConfig = YamlConfiguration.loadConfiguration(File(plugin.dataFolder, "armors.yml"))
        val sourceFolder = File(plugin.dataFolder, "source_files")
        val targetFolder = File(plugin.dataFolder, "resource_pack")
        if (!sourceFolder.exists()) sourceFolder.mkdirs()
        if (!targetFolder.exists()) targetFolder.mkdirs()
        DatapackGenerator(plugin).generateDatapack()
        ResourcePackGenerator(plugin).generateResourcePack(armorsConfig, sourceFolder, targetFolder)
        sender.sendMessage("$prefix §7Reloaded Armament successfully!")
    }

    private fun handleGive(sender: CommandSender, args: Array<out String>) {
        if (args.size < 3) {
            sender.sendMessage("$prefix §7Usage: /armament give <armorName> <armorPiece> [player]")
            return
        }
        val armorName = args[1]
        val armorPiece = args[2]
        val targetPlayer = if (args.size >= 4) Bukkit.getPlayer(args[3]) else sender as? Player

        if (armorName !in armorsConfig.getConfigurationSection("armors")?.getKeys(false).orEmpty()) {
            sender.sendMessage("$prefix ${plugin.getMessage("armor-not-found")}")
            return
        }

        val armorItem = armorCreator.createArmorPiece(armorName, armorPiece) ?: return

        if (targetPlayer != null) {
            targetPlayer.inventory.addItem(armorItem)
            sender.sendMessage("$prefix ${plugin.getMessage("armor-given").replace("{player}", targetPlayer.name).replace("{armorName}", armorName)}")
            if (sender != targetPlayer) {
                targetPlayer.sendMessage("$prefix ${plugin.getMessage("armor-received").replace("{armorName}", armorName)}")
            }
        } else {
            sender.sendMessage("$prefix ${plugin.getMessage("player-not-found")}")
        }
    }

    private fun handleGiveSet(sender: CommandSender, args: Array<out String>) {
        if (args.size < 2) {
            sender.sendMessage("$prefix §7Usage: /armament giveset <armorName> [player]")
            return
        }
        val armorName = args[1]
        val armors = armorsConfig.getConfigurationSection("armors")?.getKeys(false)
            ?: return sender.sendMessage("$prefix ${plugin.getMessage("armors-not-found")}")

        if (armorName !in armors) {
            sender.sendMessage("$prefix ${plugin.getMessage("armor-not-found")}")
            return
        }
        val armorItems = armorCreator.createFullArmorSet(armorName)

        val targetPlayer: Player? = when {
            args.size >= 3 -> Bukkit.getPlayer(args[2])
            sender is Player -> sender
            else -> null
        }

        if (targetPlayer != null) {
            armorItems.forEach {
                targetPlayer.inventory.addItem(it)
            }
            sender.sendMessage(
                "$prefix ${
                    plugin.getMessage("armorset-given").replace("{player}", targetPlayer.name)
                        .replace("{armorName}", armorName)
                }"
            )
            targetPlayer.sendMessage(
                "$prefix ${
                    plugin.getMessage("armorset-received").replace("{armorName}", armorName)
                }"
            )
        } else {
            sender.sendMessage("$prefix ${plugin.getMessage("player-not-found")}")
        }
    }

    private fun handleGUI(sender: CommandSender) {
        if (sender !is Player) {
            sender.sendMessage("$prefix ${plugin.getMessage("player-only-command")}")
            return
        }
        ArmorGUI(plugin).openGUI(sender, 0)
    }
}