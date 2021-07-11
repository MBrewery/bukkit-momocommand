package moe.kouyou.momocommand

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

object MainCommand: Command("momocommand", "", "", mutableListOf("mcmd")) {
  override fun execute(s: CommandSender?, l: String?, args: Array<out String>?): Boolean {
    s!!
    args!!
    if(!s.isOp) return true
    if(args.isEmpty()) {
      s.sendMessage(ConfigManager.messages("not-enough-args"))
      return true
    }
    when(args[0]) {
      "help", "h" -> {

      }
      "execute-mechanic", "em" -> {
        if(args.size < 2) {
          s.sendMessage(ConfigManager.messages("not-enough-args"))
          return true
        }
        val mec = MechanicManager.getMechanic(args[1].lowercase())
        if(mec == null) {
          s.sendMessage(ConfigManager.messages("unknown-mechanic").replace("\$mechanic", args[1]))
          return true
        }
        s.sendMessage(ConfigManager.messages("executing-mechanic").replace("\$mechanic", args[1]))
        mec.exec(s, "momocommand", args.filterIndexed { a, _ -> a > 1 })
        s.sendMessage(ConfigManager.messages("executed-successfully"))
      }
      "reload", "rl", "r" -> {
        pluginInst.reload()
        s.sendMessage(ConfigManager.messages("reloaded-successfully"))
      }
      else -> s.sendMessage(ConfigManager.messages("invalid-args"))
    }
    return true

  }
}