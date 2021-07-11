package moe.kouyou.momocommand

import moe.kouyou.momocommand.datastructs.*
import moe.kouyou.momocommand.utils.ListConstructor
import moe.kouyou.momocommand.utils.getCommandMap
import org.bukkit.command.Command
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.SimplePluginManager
import org.yaml.snakeyaml.TypeDescription
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor
import org.yaml.snakeyaml.representer.Representer
import java.io.File
import java.lang.reflect.Field

object ConfigManager {
  class Config {
    var livereload: Boolean = false
    var livereloadDelay: Int = 10
    lateinit var messages: MutableMap<String, String>
  }

  lateinit var config: Config

  fun messages(key: String): String = config.messages[key]!!

  fun loadConfig() {
    val f = File(pluginInst.dataFolder, "config.yml")
    if(!f.exists()) pluginInst.saveResource("config.yml", true)
    val br = f.bufferedReader()
    val cfg = br.readText()
    br.close()
    val con = CustomClassLoaderConstructor(Config::class.java, pluginInst.javaClass.classLoader)
    config = Yaml(con).load(cfg) as Config
  }
}

object MechanicManager {
  val mechanics = hashMapOf<String, Mechanic>()

  fun getMechanic(name: String) = mechanics[name]

  fun getMechanicsFolder(): File {
    val fol = File(pluginInst.dataFolder, "mechanics")
    if(!fol.exists()) {
      fol.mkdirs()
      pluginInst.saveResource("mechanics/example.yml", true)
    }
    return fol
  }

  fun loadMechanics() {
    val fol = getMechanicsFolder()
    val yaml = Yaml(ListConstructor(MechanicData::class.java))
    fol.listFiles()!!.map {
      val br = it.bufferedReader(Charsets.UTF_8)
      yaml.load<List<MechanicData>>(br.readText()).also { br.close() }
    }.flatten().map(MechanicData::toMechanic).forEach { mechanics[it.name.lowercase()] = it }
  }

  fun loadMechanicFromFile(f: File){
    val br = f.bufferedReader()
    loadMechanicFromString(br.readText())
    br.close()
  }

  fun loadMechanicFromString(s: String){
    Yaml(ListConstructor(MechanicData::class.java))
      .load<List<MechanicData>>(s).map(MechanicData::toMechanic)
      .forEach { mechanics[it.name.lowercase()] = it }
  }
}

object MCommandManager {
  val mcmds = hashMapOf<String, MCommand>()

  fun getMCommand(name: String) = mcmds[name]

  fun getMCommandsFolder(): File {
    val fol = File(pluginInst.dataFolder, "commands")
    if(!fol.exists()) {
      fol.mkdirs()
      pluginInst.saveResource("commands/example.yml", true)
    }
    return fol
  }

  fun loadCommands() {
    val fol = getMCommandsFolder()
    val yaml = Yaml(ListConstructor(MCommandData::class.java))
    fol.listFiles()!!.map {
      val br = it.bufferedReader(Charsets.UTF_8)
      yaml.load<List<MCommandData>>(br.readText()).also { br.close() }
    }.flatten().map(MCommandData::toMCommand).forEach {
      mcmds[it.cmdLabel.lowercase()] = it
      it.register()
    }
  }

  private val cmf: Field = SimpleCommandMap::class.java.getDeclaredField("knownCommands").also { it.isAccessible = true }
  fun unloadCommands() {
    cmf.set(getCommandMap(), (cmf.get(getCommandMap()) as HashMap<*, *>).filterValues { c -> c !is MCommand })
  }
}
