package moe.kouyou.momocommand

import moe.kouyou.momocommand.livereload.ReloadTask
import moe.kouyou.momocommand.utils.registerCommand
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin

lateinit var pluginInst: MomoCommand

class MomoCommand : JavaPlugin() {

  override fun onEnable() {
    Metrics(this, 12023)
    pluginInst = this
    registerCommands()
    reload()
    ReloadTask.runTaskTimerAsynchronously(this, 0,
      (ConfigManager.config.livereloadDelay * 20).toLong())
  }

  override fun onDisable() {
    MCommandManager.unloadCommands()
  }

  fun reload() {
    if(!dataFolder.exists()) dataFolder.mkdirs()
    ConfigManager.loadConfig()
    MechanicManager.loadMechanics()
    MCommandManager.unloadCommands()
    MCommandManager.loadCommands()
  }

  private fun registerCommands() {
    registerCommand("momocommand", MainCommand)
  }

}
