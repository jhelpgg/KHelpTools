package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.IfExpression
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.ComparisonOperator
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator
import khelp.kotlinspector.model.expression.operator.MultiplicativeOperator
import khelp.kotlinspector.model.expression.operator.PrefixUnaryOperator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IfExpressionTests
{
    @Test
    fun ifTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            if(value > first * 96L) 
            {
                this.tooMuchAlert()
            }
        """.trimIndent(), KotlinGrammar.ifExpression))
        val ifExpression = IfExpression()
        ifExpression.parse(grammarNode)
        val firstComparison = ifExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison
        Assertions.assertEquals("value",
                                firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (operator, comparison) = firstComparison.operatorGenericCallLikeComparisonCouples()[0]
        Assertions.assertEquals(ComparisonOperator.UPPER, operator)
        val firstMultiplicativeExpression = comparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression
        Assertions.assertEquals("first",
                                firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (multiplicativeOperator, asExpression) = firstMultiplicativeExpression.multiplicativeOperatorAsExpressionCouples()[0]
        Assertions.assertEquals(MultiplicativeOperator.TIMES, multiplicativeOperator)
        val literalConstant = assumeNotNull(asExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.LONG, literalConstant.literalConstantType)
        Assertions.assertEquals("96L", literalConstant.value)
        val controlStructureBody = assumeNotNull(ifExpression.controlStructureBody)
        val block = assumeNotNull(controlStructureBody.block)
        var expression = assumeNotNull(block.statements()[0].expression)
        var postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        val thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
        val navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("tooMuchAlert", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun ifElseTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            if(value > first * 96L) 
            {
                this.tooMuchAlert()
            } else {
                this.showValue(value)
            }
        """.trimIndent(), KotlinGrammar.ifExpression))
        val ifExpression = IfExpression()
        ifExpression.parse(grammarNode)
        val firstComparison = ifExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison
        Assertions.assertEquals("value",
                                firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (operator, comparison) = firstComparison.operatorGenericCallLikeComparisonCouples()[0]
        Assertions.assertEquals(ComparisonOperator.UPPER, operator)
        val firstMultiplicativeExpression = comparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression
        Assertions.assertEquals("first",
                                firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (multiplicativeOperator, asExpression) = firstMultiplicativeExpression.multiplicativeOperatorAsExpressionCouples()[0]
        Assertions.assertEquals(MultiplicativeOperator.TIMES, multiplicativeOperator)
        val literalConstant = assumeNotNull(asExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.LONG, literalConstant.literalConstantType)
        Assertions.assertEquals("96L", literalConstant.value)
        val controlStructureBody = assumeNotNull(ifExpression.controlStructureBody)
        var block = assumeNotNull(controlStructureBody.block)
        var expression = assumeNotNull(block.statements()[0].expression)
        var postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        var thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
        var navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("tooMuchAlert", postfixUnaryExpression.primaryExpression.identifier)
        var callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())

        val elseControlStructureBody = assumeNotNull(ifExpression.elseControlStructureBody)
        block = assumeNotNull(elseControlStructureBody.block)
        expression = assumeNotNull(block.statements()[0].expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
        navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("showValue", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals("value",
                                callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun ifElseIfTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            if(value > first * 96L) 
            {
                this.tooMuchAlert()
            } else if (value < -85L) {
                this.tooLowAlert()
            }
        """.trimIndent(), KotlinGrammar.ifExpression))
        var ifExpression = IfExpression()
        ifExpression.parse(grammarNode)
        var firstComparison = ifExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison
        Assertions.assertEquals("value",
                                firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (operator, comparison) = firstComparison.operatorGenericCallLikeComparisonCouples()[0]
        Assertions.assertEquals(ComparisonOperator.UPPER, operator)
        val firstMultiplicativeExpression = comparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression
        Assertions.assertEquals("first",
                                firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (multiplicativeOperator, asExpression) = firstMultiplicativeExpression.multiplicativeOperatorAsExpressionCouples()[0]
        Assertions.assertEquals(MultiplicativeOperator.TIMES, multiplicativeOperator)
        var literalConstant = assumeNotNull(asExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.LONG, literalConstant.literalConstantType)
        Assertions.assertEquals("96L", literalConstant.value)
        var controlStructureBody = assumeNotNull(ifExpression.controlStructureBody)
        var block = assumeNotNull(controlStructureBody.block)
        var expression = assumeNotNull(block.statements()[0].expression)
        var postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        var thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
        var navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("tooMuchAlert", postfixUnaryExpression.primaryExpression.identifier)
        var callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())

        val elseControlStructureBody = assumeNotNull(ifExpression.elseControlStructureBody)
        val statement = assumeNotNull(elseControlStructureBody.statement)
        expression = assumeNotNull(statement.expression)
        ifExpression = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.ifExpression)
        firstComparison = ifExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison
        Assertions.assertEquals("value",
                                firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (operator2, comparison2) = firstComparison.operatorGenericCallLikeComparisonCouples()[0]
        Assertions.assertEquals(ComparisonOperator.LOWER, operator2)
        val prefixUnaryExpression = comparison2.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression
        Assertions.assertEquals(PrefixUnaryOperator.UNARY_MINUS,
                                prefixUnaryExpression.unaryPrefixs()[0].prefixUnaryOperator)
        literalConstant = assumeNotNull(prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.LONG, literalConstant.literalConstantType)
        Assertions.assertEquals("85L", literalConstant.value)
        controlStructureBody = assumeNotNull(ifExpression.controlStructureBody)
        block = assumeNotNull(controlStructureBody.block)
        expression = assumeNotNull(block.statements()[0].expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
        navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("tooLowAlert", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }
}
