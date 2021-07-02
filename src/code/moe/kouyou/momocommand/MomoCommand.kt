package moe.kouyou.momocommand

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

lateinit var pluginInst: MomoCommand

class MomoCommand : JavaPlugin() {

  override fun onEnable() {
    pluginInst = this
    if(!dataFolder.exists()) dataFolder.mkdirs()
    ConfigManager.loadConfig()
    MechanicManager.loadMechanics()
    MCommandManager.loadCommands()
    Bukkit.getPluginCommand("momocommand").setExecutor(this)
  }

  override fun onDisable() {

  }

}
