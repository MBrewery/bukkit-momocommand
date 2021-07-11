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
        s.sendMessage("§aMomoCommand §9>> §a正在显示帮助")
        s.sendMessage("/momocommand 或 /mcmd - 插件主要命令")
        s.sendMessage("/mcmd rl 或 /mcmd reload - 重载插件")
        s.sendMessage("/mcmd em <机制名称> [参数] - 直接执行机制")
        s.sendMessage("/mcmd h 或 /mcmd help - 显示插件帮助")
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