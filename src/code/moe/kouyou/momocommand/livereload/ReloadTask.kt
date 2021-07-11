package moe.kouyou.momocommand.livereload

import moe.kouyou.momocommand.pluginInst
import moe.kouyou.momocommand.ConfigManager
import moe.kouyou.momocommand.MCommandManager
import moe.kouyou.momocommand.MechanicManager
import org.bukkit.scheduler.BukkitRunnable
import java.io.File

object ReloadTask : BukkitRunnable() {
  private val lms = hashMapOf<File, Long>()

  override fun run() {
    MechanicManager.getMechanicsFolder().listFiles { f -> f.extension == "yml" }.forEach {
      val l = it.lastModified()
      if(it !in lms) {
        lms[it] = l
      } else if(lms[it] != l) {
        lms[it] = l
        MechanicManager.loadMechanicFromFile(it)
      }
    }
    MCommandManager.getMCommandsFolder().listFiles { f -> f.extension == "yml" }.forEach {
      val l = it.lastModified()
      if(it !in lms) {
        lms[it] = l
      } else if(lms[it] != l) {
        lms[it] = l
        MCommandManager.unloadCommands()
        MCommandManager.loadCommands()
      }
    }
    val f = File(pluginInst.dataFolder, "config.yml")
    val lm = f.lastModified()
    if(f !in lms) {
      lms[f] = lm
    } else if(lms[f] != lm) {
      lms[f] = lm
      ConfigManager.loadConfig()
    }
  }
}