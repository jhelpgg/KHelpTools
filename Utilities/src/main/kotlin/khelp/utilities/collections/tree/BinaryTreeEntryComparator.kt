package khelp.utilities.collections.tree

internal class BinaryTreeEntryComparator<K : Any, V : Any>(private val comparator : Comparator<K>) : Comparator<BinaryTreeEntry<K, V>>
{
    override fun compare(first : BinaryTreeEntry<K, V>, second : BinaryTreeEntry<K, V>) : Int =
        this.comparator.compare(first.key, second.key)
}