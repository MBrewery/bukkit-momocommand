package moe.kouyou.momocommand.utils

import moe.kouyou.momocommand.pluginInst
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.plugin.*
import java.lang.reflect.Field

private val cmf: Field = SimplePluginManager::class.java.getDeclaredField("commandMap").also { it.isAccessible = true }
fun getCommandMap(): CommandMap {
  return cmf.get(Bukkit.getPluginManager()) as CommandMap
}

fun registerCommand(label: String, command: Command) {
  getCommandMap().register(label, pluginInst.name.lowercase(), command)
}
