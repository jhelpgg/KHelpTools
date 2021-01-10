package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar

class JumpExpression
{
    var jumpExpressionType = JumpExpressionType.NONE
        private set
    var atName = ""
        private set
    var expression: Expression? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.atName = ""
        this.expression = null
        val node = grammarNode[0]

        when
        {
            node.numberChildren > 1 && node[0].text == "throw" ->
            {
                this.jumpExpressionType = JumpExpressionType.THROW
                this.expression = Expression()
                this.expression?.parse(node[2])
            }
            node.numberChildren > 1                            ->
            {
                this.jumpExpressionType = JumpExpressionType.RETURN
                var child = node[0]

                if (child.rule == KotlinGrammar.RETURN_AT)
                {
                    this.atName = child[2].text
                }

                child = node[2]

                if (child.numberChildren > 0)
                {
                    this.expression = Expression()
                    this.expression?.parse(child[0])
                }
            }
            node.text == "continue"                            -> this.jumpExpressionType = JumpExpressionType.CONTINUE
            node.rule == KotlinGrammar.CONTINUE_AT             ->
            {
                this.jumpExpressionType = JumpExpressionType.CONTINUE
                this.atName = node[2].text
            }
            node.text == "break"                               -> this.jumpExpressionType = JumpExpressionType.BREAK
            node.rule == KotlinGrammar.BREAK_AT                ->
            {
                this.jumpExpressionType = JumpExpressionType.BREAK
                this.atName = node[2].text
            }
        }
    }
}