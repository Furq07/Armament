# 🧥 **Armament**

Adds the ability to create configurable custom armors using resource packs.

---

## 🎧 **Key Features**

| Feature | Description |
| --- | --- |
| **🛡️ Custom Armor Sets** | Easily add your own armor sets to Minecraft. |
| **⚡ Performance Optimization** | Built to ensure smooth server performance. |
| **💡 User-Friendly** | Simple commands for reloading configuration and giving armor to players. |
| **⚙ Configurable** | Manage your custom armors via a straightforward `armors.yml` file. |
| **🎨 Resource Pack Generation** | Automatically generate a resource pack from your configuration. |
| **🔃 Cross-Version Compatibility** | Supports Minecraft versions from `1.20` to the latest. |
| **🖼️ GUI Interface** | Browse and select custom armor sets through an intuitive GUI. |

---

## 💻 **Available Commands**

| Command | Description |
| --- | --- |
| **`/armament reload`** | Reload the plugin's configuration without a server restart. |
| **`/armament give <armor_name> <piece> [player]`** | Grant a custom armor piece to a player. |
| **`/armament giveset <armor_name> [player]`** | Grant a full custom armor set to a player. |
| **`/armament gui`** | Open the GUI to browse and select custom armor sets. |

---

## 🔒 **Permissions**

| Permission | Description |
| --- | --- |
| **`armament.admin`** | Grants access to all admin commands. |

---

## 📩 **Installation Steps**

1. **[Download Armament](https://modrinth.com/plugin/armament):** Get the latest version from Modrinth.
2. **Install:** 
   - Place the `.jar` file into your server's `plugins` directory.
   - Example:
     ```bash
     /plugins/Armament-x.x.x.jar
     ```
3. **Activate:**
   - Restart your server to load the plugin.
4. **Customize:** 
   - Edit the `armors.yml` file located in `plugins/Armament/armors.yml` to add your custom armor sets.
   - Example:
   ```yaml
   armors:
     epic:
       custom_model_data: 1
       chestplate:
         name: "&6Epic Chestplate"
         lore:
           - "&7Custom Epic Lore!"
       leggings:
         name: "&6Epic Leggings"
         lore:
           - "&7Custom Epic Lore!"
       boots:
         name: "&6Epic Boots"
         lore:
           - "&7Custom Epic Lore!"
   ```
5. **Add Resources:**
   - Place your custom armor textures in the `source_files` directory.
    - Armor layers will be placed in `source_files/layer_files`.
    - Armor display items will be placed in `source_files/item_files`.
6. **Load Resource Pack:**
   - Upload the resource pack to [mc-packs.net](https://mc-packs.net).
   - Update the `server.properties` file:
     | Property | Value |
     | --- | --- |
     | `resource-pack` | *URL to resource pack* |
     | `resource-pack-sha1` | *SHA1 hash of the resource pack* |
     | `require-resource-pack` | `true` |
   - **Note:** If you're using an existing resource pack, merge it with the Armament resource pack.

---

## 📞 **Support**

For assistance, visit the [GitHub Repository](https://github.com/furq07/armament/issues) or join our [Discord Server](https://discord.gg/7ugrBEKza4).

---

## 📜 **License**

Armament is released under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).

---

## 🤝 **Partner**

<p align="center"> <a href="https://billing.revivenode.com/aff.php?aff=517"> <img src="https://versions.revivenode.com/resources/banner_wide_one.gif" alt="Partner GIF"> </a> </p> <p align="center"> Use code <b>FURQ</b> for 15% off your order! </p>
