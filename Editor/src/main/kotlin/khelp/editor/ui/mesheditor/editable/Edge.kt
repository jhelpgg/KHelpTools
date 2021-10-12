package khelp.editor.ui.mesheditor.editable

data class Edge(var startIndex : Int, var endIndex : Int) {
    override fun toString() : String = "[${this.startIndex};${this.endIndex}]"
}
