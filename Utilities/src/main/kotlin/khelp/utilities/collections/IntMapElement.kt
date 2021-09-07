package khelp.utilities.collections

internal class IntMapElement<T>(val key : Int, var value : T? = null) : Comparable<IntMapElement<T>>
{
    override fun hashCode() : Int = this.key

    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other                              -> true
            null == other || other !is IntMapElement<*> -> false
            else                                        -> this.key == other.key
        }

    override operator fun compareTo(other : IntMapElement<T>) : Int = this.key - other.key

    override fun toString() : String = "${this.key} => ${this.value}"
}
