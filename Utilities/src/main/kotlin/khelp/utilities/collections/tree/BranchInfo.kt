package khelp.utilities.collections.tree

internal fun BranchInfo(minimum : Int,  maximum : Int) : BranchInfo<Unit> =
    BranchInfo<Unit>(minimum, maximum, Unit)

internal data class BranchInfo<I:Any>( val minimum : Int, val maximum : Int, val information : I)
