package khelp.utilities.weak

class Reference(val reference : String)
{
    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (other == null || other !is Reference)
        {
            return false
        }

        return this.reference == other.reference
    }

    override fun hashCode() : Int = this.reference.hashCode()

    override fun toString() : String = this.reference
}