package khelp.utilities.collections.dsl

import java.util.TreeMap
import khelp.utilities.collections.tree.BinaryTree
import khelp.utilities.comparators.ComparableNaturalOrderComparator

fun <K : Any, V : Any> HashMap(filler : MapFiller<K, V>.() -> Unit) : HashMap<K, V>
{
    val map = HashMap<K, V>()
    val mapFiller = MapFiller<K, V>(map)
    mapFiller.filler()
    return map
}

fun <K : Any, V : Any> TreeMap(comparator : Comparator<K>, filler : MapFiller<K, V>.() -> Unit) : TreeMap<K, V>
{
    val map = TreeMap<K, V>(comparator)
    val mapFiller = MapFiller<K, V>(map)
    mapFiller.filler()
    return map
}

fun <KC : Comparable<KC>, V : Any> TreeMap(filler : MapFiller<KC, V>.() -> Unit) : TreeMap<KC, V> =
    TreeMap(ComparableNaturalOrderComparator<KC>(), filler)

fun <K : Any, V : Any> BinaryTree(comparator : Comparator<K>, filler : MapFiller<K, V>.() -> Unit) : BinaryTree<K, V>
{
    val map = BinaryTree<K, V>(comparator)
    val mapFiller = MapFiller<K, V>(map)
    mapFiller.filler()
    return map
}

fun <KC : Comparable<KC>, V : Any> BinaryTree(filler : MapFiller<KC, V>.() -> Unit) : BinaryTree<KC, V> =
    BinaryTree(ComparableNaturalOrderComparator<KC>(), filler)
