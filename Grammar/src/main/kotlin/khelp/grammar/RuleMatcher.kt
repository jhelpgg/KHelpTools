package khelp.grammar

class RuleMatcher(private val stringPositionReader: StringPositionReader,
                  private val ruleName: String,
                  private val rules: Rules,
                  private val ruleDefinitionElement: RuleDefinitionElement = rules.rules[ruleName]!!)
{
    val finished: Boolean get() = this.stringPositionReader.endReached

    fun find(): GrammarNode?
    {
        val ruleDefinitionElement = this.ruleDefinitionElement

        when (ruleDefinitionElement)
        {
            is RuleDefinitionRegularExpression   ->
            {
                val matcher = ruleDefinitionElement.regularExpression.matcher(this.stringPositionReader.currentText())

                if (matcher.find() && matcher.start() == 0)
                {
                    val partText = this.stringPositionReader.textRelativePart(matcher.end())
                    this.stringPositionReader.stepForward(matcher.end())
                    return GrammarNode(this.ruleName, partText)
                }
            }
            is RuleDefinitionReference           ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader, ruleDefinitionElement.ruleName, this.rules)
                return ruleMatcher.find()
            }
            is RuleDefinitionReferenceZeroOrOne  ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader, ruleDefinitionElement.ruleName, this.rules)
                val grammarNode = ruleMatcher.find()

                if (grammarNode == null && this.stringPositionReader.endReached)
                {
                    return GrammarNode(this.ruleName, "")
                }

                if (grammarNode != null)
                {
                    val node = GrammarNode(this.ruleName, grammarNode.text)
                    node.children.add(grammarNode)
                    return node
                }
            }
            is RuleDefinitionReferenceZeroOrMore ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader, ruleDefinitionElement.ruleName, this.rules)
                var grammarNode = ruleMatcher.find()

                if (grammarNode == null && this.stringPositionReader.endReached)
                {
                    return GrammarNode(this.ruleName, "")
                }

                if (grammarNode == null)
                {
                    return null
                }

                val text = StringBuilder()
                val children = ArrayList<GrammarNode>()

                do
                {
                    text.append(grammarNode!!.text)
                    children.add(grammarNode)
                    grammarNode = ruleMatcher.find()
                }
                while (grammarNode != null)

                val node = GrammarNode(this.ruleName, text.toString())
                node.children.addAll(children)
                return node
            }
            is RuleDefinitionReferenceOneOrMore  ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader, ruleDefinitionElement.ruleName, this.rules)
                var grammarNode: GrammarNode? = ruleMatcher.find() ?: return null
                val text = StringBuilder()
                val children = ArrayList<GrammarNode>()

                do
                {
                    text.append(grammarNode!!.text)
                    children.add(grammarNode)
                    grammarNode = ruleMatcher.find()
                }
                while (grammarNode != null)

                val node = GrammarNode(this.ruleName, text.toString())
                node.children.addAll(children)
                return node
            }
            is RuleDefinitionReferenceExactTimes ->
            {
                val position = this.stringPositionReader.currentIndex
                val ruleMatcher = RuleMatcher(this.stringPositionReader, ruleDefinitionElement.ruleName, this.rules)
                var grammarNode: GrammarNode? = ruleMatcher.find() ?: return null
                val text = StringBuilder()
                val children = ArrayList<GrammarNode>()
                val number = ruleDefinitionElement.numberTimes
                var currentCount = 0

                do
                {
                    text.append(grammarNode!!.text)
                    children.add(grammarNode)
                    grammarNode = ruleMatcher.find()
                    currentCount++
                }
                while (grammarNode != null && currentCount < number)

                if (currentCount < number)
                {
                    this.stringPositionReader.setPosition(position)
                    return null
                }

                val node = GrammarNode(this.ruleName, text.toString())
                node.children.addAll(children)
                return node
            }
            is RuleDefinitionReferenceAtMost     ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader, ruleDefinitionElement.ruleName, this.rules)
                var grammarNode: GrammarNode? = ruleMatcher.find() ?: return null
                val text = StringBuilder()
                val children = ArrayList<GrammarNode>()
                val number = ruleDefinitionElement.maximumTimes
                var currentCount = 0

                do
                {
                    text.append(grammarNode!!.text)
                    children.add(grammarNode)
                    grammarNode = ruleMatcher.find()
                    currentCount++
                }
                while (grammarNode != null && currentCount < number)

                val node = GrammarNode(this.ruleName, text.toString())
                node.children.addAll(children)
                return node
            }
            is RuleDefinitionReferenceAtLeast    ->
            {
                val position = this.stringPositionReader.currentIndex
                val ruleMatcher = RuleMatcher(this.stringPositionReader, ruleDefinitionElement.ruleName, this.rules)
                var grammarNode: GrammarNode? = ruleMatcher.find() ?: return null
                val text = StringBuilder()
                val children = ArrayList<GrammarNode>()
                val number = ruleDefinitionElement.minimumTimes
                var currentCount = 0

                do
                {
                    text.append(grammarNode!!.text)
                    children.add(grammarNode)
                    grammarNode = ruleMatcher.find()
                    currentCount++
                }
                while (grammarNode != null)

                if (currentCount < number)
                {
                    this.stringPositionReader.setPosition(position)
                    return null
                }

                val node = GrammarNode(this.ruleName, text.toString())
                node.children.addAll(children)
                return node
            }
            is RuleDefinitionReferenceBetween    ->
            {
                val position = this.stringPositionReader.currentIndex
                val ruleMatcher = RuleMatcher(this.stringPositionReader, ruleDefinitionElement.ruleName, this.rules)
                var grammarNode: GrammarNode? = ruleMatcher.find() ?: return null
                val text = StringBuilder()
                val children = ArrayList<GrammarNode>()
                val maximum = ruleDefinitionElement.maximumTimes
                var currentCount = 0

                do
                {
                    text.append(grammarNode!!.text)
                    children.add(grammarNode)
                    grammarNode = ruleMatcher.find()
                    currentCount++
                }
                while (grammarNode != null && currentCount < maximum)

                if (currentCount < ruleDefinitionElement.minimumTimes)
                {
                    this.stringPositionReader.setPosition(position)
                    return null
                }

                val node = GrammarNode(this.ruleName, text.toString())
                node.children.addAll(children)
                return node
            }
            is RuleDefinitionElementConcatenate  ->
            {
                val position = this.stringPositionReader.currentIndex
                val children = ArrayList<GrammarNode>()
                val text = StringBuilder()

                for ((index, ruleElement) in ruleDefinitionElement.ruleElements.withIndex())
                {
                    val matcher = RuleMatcher(this.stringPositionReader,
                                              "${this.ruleName}@$index",
                                              this.rules,
                                              ruleElement)
                    val grammarNode = matcher.find()

                    if (grammarNode == null)
                    {
                        this.stringPositionReader.setPosition(position)
                        return null
                    }

                    text.append(grammarNode.text)
                    children.add(grammarNode)
                }

                val node = GrammarNode(this.ruleName, text.toString())
                node.children.addAll(children)
                return node
            }
            is RuleDefinitionElementAlternative  ->
            {
                val position = this.stringPositionReader.currentIndex
                var lastFoundNode: GrammarNode? = null
                var lastFoundEndPosition: Int = -1

                for (ruleElement in ruleDefinitionElement.ruleElements)
                {
                    this.stringPositionReader.setPosition(position)
                    val matcher = RuleMatcher(this.stringPositionReader, "${this.ruleName}*", this.rules, ruleElement)
                    val grammarNode = matcher.find() ?: continue

                    if (lastFoundEndPosition < this.stringPositionReader.currentIndex)
                    {
                        lastFoundEndPosition = this.stringPositionReader.currentIndex
                        lastFoundNode = grammarNode
                    }
                }

                if (lastFoundNode == null)
                {
                    return null
                }

                this.stringPositionReader.setPosition(lastFoundEndPosition)
                val node = GrammarNode(this.ruleName, lastFoundNode.text)
                node.children.add(lastFoundNode)
                return node
            }
        }

        return null
    }
}
