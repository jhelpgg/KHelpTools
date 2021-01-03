package khelp.grammar

class RuleMatcher(private val stringPositionReader: StringPositionReader,
                  private val ruleName: String,
                  private val rules: Rules,
                  private val ruleDefinitionElement: RuleDefinitionElement = rules.rules[ruleName]!!,
                  private val stopAtFirstAlternativeMatch: Boolean)
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
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              ruleDefinitionElement.ruleName,
                                              this.rules,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
                return ruleMatcher.find()
            }
            is RuleDefinitionReferenceZeroOrOne  ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              ruleDefinitionElement.ruleName,
                                              this.rules,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
                val grammarNode = ruleMatcher.find() ?: return GrammarNode(this.ruleName, "")
                val node = GrammarNode(this.ruleName, grammarNode.text)
                node.addChild(grammarNode)
                return node
            }
            is RuleDefinitionElementZeroOrOne    ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              "${this.ruleName}?",
                                              this.rules,
                                              ruleDefinitionElement.ruleDefinitionElement,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
                val grammarNode = ruleMatcher.find() ?: return GrammarNode(this.ruleName, "")
                val node = GrammarNode(this.ruleName, grammarNode.text)
                node.addChild(grammarNode)
                return node
            }
            is RuleDefinitionReferenceZeroOrMore ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              ruleDefinitionElement.ruleName,
                                              this.rules,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
                var grammarNode: GrammarNode? = ruleMatcher.find() ?: return GrammarNode(this.ruleName, "")
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
                node.addChildren(children)
                return node
            }
            is RuleDefinitionElementZeroOrMore   ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              "${this.ruleName}*",
                                              this.rules,
                                              ruleDefinitionElement.ruleDefinitionElement,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
                var grammarNode: GrammarNode? = ruleMatcher.find() ?: return GrammarNode(this.ruleName, "")
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
                node.addChildren(children)
                return node
            }
            is RuleDefinitionReferenceOneOrMore  ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              ruleDefinitionElement.ruleName,
                                              this.rules,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
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
                node.addChildren(children)
                return node
            }
            is RuleDefinitionElementOneOrMore    ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              "${this.ruleName}+",
                                              this.rules,
                                              ruleDefinitionElement.ruleDefinitionElement,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
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
                node.addChildren(children)
                return node
            }
            is RuleDefinitionReferenceExactTimes ->
            {
                val position = this.stringPositionReader.currentIndex
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              ruleDefinitionElement.ruleName,
                                              this.rules,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
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
                node.addChildren(children)
                return node
            }
            is RuleDefinitionElementExactTimes   ->
            {
                val position = this.stringPositionReader.currentIndex
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              "${this.ruleName}{${ruleDefinitionElement.numberTimes}}",
                                              this.rules,
                                              ruleDefinitionElement.ruleDefinitionElement,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
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
                node.addChildren(children)
                return node
            }
            is RuleDefinitionReferenceAtMost     ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              ruleDefinitionElement.ruleName,
                                              this.rules,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
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
                node.addChildren(children)
                return node
            }
            is RuleDefinitionElementAtMost       ->
            {
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              "${this.ruleName}{0,${ruleDefinitionElement.maximumTimes}}",
                                              this.rules,
                                              ruleDefinitionElement.ruleDefinitionElement,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
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
                node.addChildren(children)
                return node
            }
            is RuleDefinitionReferenceAtLeast    ->
            {
                val position = this.stringPositionReader.currentIndex
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              ruleDefinitionElement.ruleName,
                                              this.rules,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
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
                node.addChildren(children)
                return node
            }
            is RuleDefinitionElementAtLeast      ->
            {
                val position = this.stringPositionReader.currentIndex
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              "${this.ruleName}{${ruleDefinitionElement.minimumTimes},}",
                                              this.rules,
                                              ruleDefinitionElement.ruleDefinitionElement,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
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
                node.addChildren(children)
                return node
            }
            is RuleDefinitionReferenceBetween    ->
            {
                val position = this.stringPositionReader.currentIndex
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              ruleDefinitionElement.ruleName,
                                              this.rules,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
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
                node.addChildren(children)
                return node
            }
            is RuleDefinitionElementBetween      ->
            {
                val position = this.stringPositionReader.currentIndex
                val ruleMatcher = RuleMatcher(this.stringPositionReader,
                                              "${this.ruleName}{${ruleDefinitionElement.minimumTimes}, ${ruleDefinitionElement.maximumTimes}}",
                                              this.rules,
                                              ruleDefinitionElement.ruleDefinitionElement,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
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
                node.addChildren(children)
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
                                              ruleElement,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
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
                node.addChildren(children)
                return node
            }
            is RuleDefinitionElementAlternative  ->
            {
                val position = this.stringPositionReader.currentIndex
                var lastFoundNode: GrammarNode? = null
                var lastFoundEndPosition: Int = -1

                for ((index, ruleElement) in ruleDefinitionElement.ruleElements.withIndex())
                {
                    this.stringPositionReader.setPosition(position)
                    val matcher = RuleMatcher(this.stringPositionReader,
                                              "${this.ruleName}/$index",
                                              this.rules,
                                              ruleElement,
                                              stopAtFirstAlternativeMatch = stopAtFirstAlternativeMatch)
                    val grammarNode = matcher.find() ?: continue

                    if (lastFoundEndPosition < this.stringPositionReader.currentIndex)
                    {
                        lastFoundEndPosition = this.stringPositionReader.currentIndex
                        lastFoundNode = grammarNode
                    }

                    if (this.stopAtFirstAlternativeMatch)
                    {
                        break
                    }
                }

                if (lastFoundNode == null)
                {
                    return null
                }

                this.stringPositionReader.setPosition(lastFoundEndPosition)
                val node = GrammarNode(this.ruleName, lastFoundNode.text)
                node.addChild(lastFoundNode)
                return node
            }
        }

        return null
    }
}
