package khelp.utilities.collections.tree

internal data class BinaryTreeEntry<K:Any,V:Any>(override val key:K, override var value:V) : MutableMap.MutableEntry<K,V>
{
    override fun setValue(newValue : V) : V
    {
        val oldValue = this.value
        this.value = newValue
        return oldValue
    }
}