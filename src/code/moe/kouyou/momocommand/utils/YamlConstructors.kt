package moe.kouyou.momocommand.utils

import org.yaml.snakeyaml.*
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.nodes.*

class ListConstructor<T>(val clazz: Class<T>) : Constructor(MutableList::class.java) {
  val itemType = TypeDescription(clazz)

  init {
    this.rootTag = Tag("root");
    this.addTypeDescription(itemType);
  }

  override fun constructObject(node: Node): Any? {
    if (rootTag == node.tag && node is SequenceNode) {
      return node.value.map {
        it.type = clazz
        super.constructObject(it)
      }
    }
    return super.constructObject(node)
  }
}
