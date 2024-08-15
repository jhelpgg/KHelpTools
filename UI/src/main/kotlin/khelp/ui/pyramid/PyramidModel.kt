package khelp.ui.pyramid

import java.util.Stack
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import javax.swing.tree.TreeModel
import javax.swing.tree.TreePath
import khelp.thread.future.FutureResult
import khelp.thread.parallel
import khelp.utilities.collections.tree.Tree
import khelp.utilities.math.random

@Suppress("UNCHECKED_CAST")
class PyramidModel : TreeModel
{
    companion object
    {
         val distribute = arrayOf(500, 200, 100, 50, 20, 10, 5, 2, 1)
    }

    private val pyramid = Tree<Int>(0)
    private val listeners = ArrayList<TreeModelListener>()

    override fun getRoot() : Any =
        this.pyramid

    override fun getChild(parent : Any, index : Int) : Any =
        (parent as Tree<Int>)[index]

    override fun getChildCount(parent : Any) : Int =
        (parent as Tree<Int>).numberBranches

    override fun isLeaf(node : Any) : Boolean =
        (node as Tree<Int>).leaf

    override fun valueForPathChanged(path : TreePath, newValue : Any)
    {
        val branch = path.lastPathComponent as Tree<Int>
        branch.value = newValue as Int
        this.fireTreeModelEvent(TreeModelEvent(this, path.parentPath,
                                               if (branch.root) null else intArrayOf(branch.parent!!.indexOf(branch)),
                                               if (branch.root) null else arrayOf(branch)),
                                TreeModelListener::treeNodesChanged)
    }

    override fun getIndexOfChild(parent : Any, child : Any) : Int =
        (parent as Tree<Int>).indexOf(child as Tree<Int>)

    fun addRandomChild() : FutureResult<Unit> =
        parallel {
            var branch = this.randomBranch()
            val child = branch.addBranch(-1000)
            var path = this.treePath(branch)
            this.fireTreeModelEvent(TreeModelEvent(this, path, intArrayOf(branch.indexOf(child)), arrayOf(child)),
                                    TreeModelListener::treeNodesInserted)
            var sum = 1000
            var distributeIndex = 0

            while (branch.root.not() && distributeIndex < PyramidModel.distribute.size)
            {
                Thread.sleep(512)
                path = path.parentPath
                branch.value += PyramidModel.distribute[distributeIndex]
                sum -= PyramidModel.distribute[distributeIndex]

                this.fireTreeModelEvent(TreeModelEvent(this, path,
                                                       intArrayOf(branch.parent!!.indexOf(branch)),
                                                       arrayOf(branch)),
                                        TreeModelListener::treeNodesChanged)

                branch = branch.parent!!
                distributeIndex++
            }

            Thread.sleep(512)
            this.pyramid.value += sum
            this.fireTreeModelEvent(TreeModelEvent(this, TreePath(this.pyramid),
                                                   null,
                                                   null),
                                    TreeModelListener::treeNodesChanged)
        }

    override fun addTreeModelListener(listener : TreeModelListener)
    {
        synchronized(this.listeners)
        {
            this.listeners.add(listener)
        }
    }

    override fun removeTreeModelListener(listener : TreeModelListener)
    {
        synchronized(this.listeners)
        {
            this.listeners.remove(listener)
        }
    }

    private fun fireTreeModelEvent(treeModelEvent : TreeModelEvent,
                                   fireAction : TreeModelListener.(TreeModelEvent) -> Unit)
    {
        synchronized(this.listeners)
        {
            for (listener in this.listeners)
            {
                listener.fireAction(treeModelEvent)
            }
        }
    }

    private fun randomBranch() : Tree<Int>
    {
        var branch = this.pyramid

        while (true)
        {
            if (branch.leaf)
            {
                return branch
            }

            val index = random(0, branch.numberBranches)

            if (index == branch.numberBranches)
            {
                return branch
            }

            branch = branch[index]
        }
    }

    private fun treePath(branch : Tree<Int>) : TreePath
    {
        val stack = Stack<Tree<Int>>()
        stack.push(branch)
        var child = branch

        while (child.root.not())
        {
            child = child.parent!!
            stack.push(child)
        }

        val list = ArrayList<Tree<Int>>()

        while (stack.isNotEmpty())
        {
            list.add(stack.pop())
        }

        return TreePath(list.toTypedArray())
    }
}
