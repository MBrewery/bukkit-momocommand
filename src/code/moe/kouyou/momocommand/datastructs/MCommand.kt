package moe.kouyou.momocommand.datastructs

import moe.kouyou.momocommand.ConfigManager
import moe.kouyou.momocommand.MechanicManager
import org.bukkit.command.*
import moe.kouyou.momocommand.utils.registerCommand

class MCommandData() {
  lateinit var name: String
  var aliases: MutableList<String>? = null
  var permission: String? = null
  lateinit var mechanics: MutableList<String>
  fun toMCommand(): MCommand {
    return MCommand(name, permission, aliases?:mutableListOf(), mechanics.map { MechanicManager.mechanics[it.lowercase()]!! })
  }
}

class MCommand(val cmdLabel: String, val perm: String?, val alias: MutableList<String>, val mechanics: List<Mechanic>)
  : Command("momocommand-implemented", "", "", alias) {
  override fun execute(sender: CommandSender?, label: String?, args: Array<out String>?): Boolean {
    sender!!
    if(!sender.isOp) {
      perm?.also {
        if(!sender.hasPermission(perm)) {
          sender.sendMessage(ConfigManager.messages("no-permission").replace("\$perm", perm))
          return@execute true
        }
      }
    }
    mechanics.forEach { it.exec(sender, label!!, args!!.filter { it.isNotBlank() }) }
    return true
  }
}

fun MCommand.register() {
  registerCommand(cmdLabel, this)
}
