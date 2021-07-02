package moe.kouyou.momocommand.utils

import javax.script.*

val globalManager = ScriptEngineManager()

fun getEngine() = globalManager.getEngineByName("nashorn")
