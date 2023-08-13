package khelp.engine3d.gui.model

import khelp.engine3d.gui.model.tree.FoldBranch
import khelp.utilities.collections.tree.Tree

class GUITreeModel<V:Any>(root:V)
{
    private val tree = Tree<FoldBranch<V>>(FoldBranch(root, false))

    
}
