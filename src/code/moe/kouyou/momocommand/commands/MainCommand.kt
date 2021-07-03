package moe.kouyou.momocommand

class MainCommand: CommandExecutor {
  override onCommand(s: CommandSender?, l: String?, c: Command?, args: Array<out String>?): Boolean {
    if(!s.isOp) return true
    if(args.size == 0) {
      s.sendMessage(ConfigManager.messages("not-enough-args"))
      return true
    }
    when(args[0]) {
      "execute-mechanic", "em" -> {
        if(args.size <= 2) {
          s.sendMessage(ConfigManager.messages("not-enough-args"))
          return true
        }
        MechanicManager.getMechanic(args[1]).execute(s, "momocommand", args.filterWithIndex { a,b -> a >= 1 }.toTypedArray())
        s.sendMessage(ConfigManager.messages("executing-mechanic").replace("\$mechanic", args[1]))
      }
      else -> s.sendMessage(ConfigManager.messages("invalid-args"))
    }
  }
}