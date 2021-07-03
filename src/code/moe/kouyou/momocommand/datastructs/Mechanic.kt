package moe.kouyou.momocommand.datastructs

import moe.kouyou.momocommand.utils.getEngine
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.entity.*
import javax.script.Compilable

class MechanicData() {
  lateinit var name: String
  lateinit var type: String
  lateinit var text: String

  fun toMechanic(): Mechanic {
    return when (type.lowercase()) {
      "cmd", "cmdgroup" -> CmdGroupMechanic(name, text)
      "js", "javascript" -> JsMechanic(name, text)
      else -> throw IllegalArgumentException()
    }
  }
}

abstract class Mechanic(val name: String) {
  abstract fun exec(sender: CommandSender, label: String, args: Array<out String>)
}

class CmdGroupMechanic(name: String, cmd: String) : Mechanic(name) {
  val cmds = cmd.split('\n').filter(String::isNotEmpty)

  override fun exec(sender: CommandSender, label: String, args: Array<out String>) {
    cmds.forEach {
      var processed = it
      processed = processed
        .replace("\$s", sender.name)
        .replace("\$sender", sender.name)
        .replace("\$isOp", sender.isOp.toString())
      if (sender is Entity) {
        processed = processed
          .replace("\$uuid", sender.uniqueId.toString())
          .replace("\$world", sender.world.name)
          .replace("\$x", sender.location.x.toInt().toString())
          .replace("\$y", sender.location.y.toInt().toString())
          .replace("\$z", sender.location.z.toInt().toString())
          .replace("\$yaw", sender.location.yaw.toInt().toString())
          .replace("\$pitch", sender.location.pitch.toInt().toString())
        if (sender is Damageable) {
          processed = processed
            .replace("\$health", sender.health.toInt().toString())
            .replace("\$maxHealth", sender.maxHealth.toInt().toString())
          if (sender is Player) {
            processed = processed
              .replace("\$p", sender.name)
              .replace("\$displayName", sender.displayName)
              .replace("\$level", sender.level.toString())
              .replace("\$food", sender.foodLevel.toString())
          }
        }
      } else if (sender is BlockCommandSender) {
        processed = processed
          .replace("\$block", sender.block.state.type.name)
          .replace("\$world", sender.block.world.name)
          .replace("\$x", sender.block.x.toString())
          .replace("\$y", sender.block.x.toString())
          .replace("\$z", sender.block.x.toString())
      }
      Bukkit.dispatchCommand(sender, processed);
    }
  }
}

class JsMechanic(name: String, code: String) : Mechanic(name) {
  val engine = getEngine()
  val bindings = engine.createBindings()
  val script = (engine as Compilable).compile(code)

  override fun exec(sender: CommandSender, label: String, args: Array<out String>) {
    bindings.putAll(
      arrayOf(
        "\$s" to sender,
        "\$sender" to sender,
        "\$isOp" to sender.isOp,
        "\$senderType" to sender.javaClass.simpleName,
        "\$cmd" to label.lowercase(),
      )
    )
    if (sender is Entity) {
      bindings.putAll(
        arrayOf(
          "\$uuid" to sender.uniqueId.toString(),
          "\$loc" to sender.location,
          "\$world" to sender.world,
          "\$x" to sender.location.x,
          "\$y" to sender.location.y,
          "\$z" to sender.location.z,
          "\$yaw" to sender.location.yaw,
          "\$pitch" to sender.location.pitch,
        )
      )
      if (sender is Damageable) {
        bindings.putAll(
          arrayOf(
            "\$health" to sender.health,
            "\$maxHealth" to sender.maxHealth,
          )
        )
        if (sender is Player) {
          bindings.putAll(
            arrayOf(
              "\$p" to sender,
              "\$displayName" to sender.displayName,
              "\$level" to sender.level,
              "\$food" to sender.foodLevel,
            )
          )
        }
      }
    } else if (sender is BlockCommandSender) {
      bindings.putAll(
        arrayOf(
          "\$block" to sender.block,
          "\$loc" to sender.block.location,
          "\$world" to sender.block.world,
          "\$x" to sender.block.x,
          "\$y" to sender.block.y,
          "\$z" to sender.block.z,
        )
      )
    }
    script.eval(bindings)
  }
}
