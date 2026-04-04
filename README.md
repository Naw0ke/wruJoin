# 🎀 wruJoin
**Modern & Powerful Join Message Solution**

`wruJoin` is a high-performance plugin designed for PaperMC and Folia (and their forks). \
It gives server owners complete control over player join and leave notifications with a modern, professional touch.

---

## ✨ Key Features

* 🎨 **Full Customization:** Powered by [MiniMessage](https://docs.advntr.dev/minimessage/format.html) for stunning text formatting.
* 🌈 **Modern Colors:** Native support for **HEX (RGB)** and **Gradients**.
* ⚡ **High Performance:** Optimized to ensure zero impact on your server's TPS.
* 🔄 **Instant Reload:** Update your configuration on-the-fly without restarting.
* 📺 **Join Titles:** Fully configurable title and subtitle that appears when a player joins.
* 🔊 **Join Sounds:** Fully configurable sound effects that are triggered when a player joins.
* 🕵️ **Vanish Support:** Fully compatible with **SuperVanish**. Automatically handles fake join/leave messages when administrators vanish or reappear.

---

## 🛠️ Requirements

Ensure your server environment meets the following specifications:

| Requirement         | Version                                        |
|:--------------------|:-----------------------------------------------|
| ☕ **Java**          | 21 or higher                                   |
| 🖥️ **Platform**    | Paper, Folia and their forks (`1.21 - 1.21.x`) |
| 🧩 **Dependencies** | SuperVanish (Optional)                         |

---

## ⌨️ Commands & Permissions

| Command           | Description                                | Permission      |
|:------------------|:-------------------------------------------|:----------------|
| `/wrujoin`        | Displays plugin information and help menu. | `wrujoin.admin` |
| `/wrujoin reload` | Reloads the configuration files.           | `wrujoin.admin` |

---

## 🎨 Configuration (MiniMessage)

Stop using outdated `&` color codes! `wruJoin` uses modern formatting for a more professional look.

> **Example:** Use `<#03fcb1>Welcome back, <player>!` for clean Hex colors, or try `<gradient:gold:yellow>Welcome!</gradient>` for beautiful text transitions.

📖 Check the [MiniMessage Documentation](https://docs.advntr.dev/minimessage/format.html) for more details.

---

## 📦 Installation

1.  Download the `.jar` file and place it in your server's `plugins` folder.
2.  Start your server to generate the default configuration.
3.  Edit `plugins/wruJoin/config.yml` to your liking.
4.  Run `/wrujoin reload` to apply your changes instantly.

-----

## 📊 Statistics

[![bStats](https://bstats.org/signatures/bukkit/wruJoin.svg)](https://bstats.org/plugin/bukkit/wruJoin/27905)
