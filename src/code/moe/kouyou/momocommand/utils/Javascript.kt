package moe.kouyou.momocommand.utils

import org.mozilla.javascript.engine.RhinoScriptEngineFactory
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

val rhino = RhinoScriptEngineFactory()
val mng = ScriptEngineManager()

fun getEngine(): ScriptEngine = mng.getEngineByName("nashorn") ?: rhino.scriptEngine
