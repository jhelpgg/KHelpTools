package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.AssignableSuffix
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.AdditiveOperator
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator
import khelp.kotlinspector.model.typeparameter.VarianceModifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AssignableSuffixTests
{
    // *** typeArguments ***

    @Test
    fun oneStarTypeProjection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<*>",
                                                                        KotlinGrammar.assignableSuffix))
        val assignableSuffix = AssignableSuffix()
        assignableSuffix.parse(grammarNode)
        val typeArguments = assumeNotNull(assignableSuffix.typeArguments)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(1, typeProjections.size)
        val typeProjection = typeProjections[0]
        Assertions.assertTrue(typeProjection.isStar)
        Assertions.assertNull(typeProjection.type)
        Assertions.assertTrue(typeProjection.typeProjectionModifiers()
                                  .isEmpty())
    }

    @Test
    fun oneSimpleTypeProjection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<String>",
                                                                        KotlinGrammar.assignableSuffix))
        val assignableSuffix = AssignableSuffix()
        assignableSuffix.parse(grammarNode)
        val typeArguments = assumeNotNull(assignableSuffix.typeArguments)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(1, typeProjections.size)
        val typeProjection = typeProjections[0]
        Assertions.assertFalse(typeProjection.isStar)
        Assertions.assertTrue(typeProjection.typeProjectionModifiers()
                                  .isEmpty())
        val type = assumeNotNull(typeProjection.type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun inTypeProjection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<in InputStream>",
                                                                        KotlinGrammar.assignableSuffix))
        val assignableSuffix = AssignableSuffix()
        assignableSuffix.parse(grammarNode)
        val typeArguments = assumeNotNull(assignableSuffix.typeArguments)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(1, typeProjections.size)
        val typeProjection = typeProjections[0]
        Assertions.assertFalse(typeProjection.isStar)
        val typeProjectionModifiers = typeProjection.typeProjectionModifiers()
        Assertions.assertEquals(1, typeProjectionModifiers.size)
        Assertions.assertEquals(VarianceModifier.IN, typeProjectionModifiers[0].varianceModifier)
        val type = assumeNotNull(typeProjection.type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("InputStream", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun severalTypes()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<String, Int, in InputStream, out OutputStream>",
                                                                        KotlinGrammar.assignableSuffix))
        val assignableSuffix = AssignableSuffix()
        assignableSuffix.parse(grammarNode)
        val typeArguments = assumeNotNull(assignableSuffix.typeArguments)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(4, typeProjections.size)

        var typeProjection = typeProjections[0]
        Assertions.assertFalse(typeProjection.isStar)
        var typeProjectionModifiers = typeProjection.typeProjectionModifiers()
        Assertions.assertTrue(typeProjectionModifiers.isEmpty())
        var type = assumeNotNull(typeProjection.type)
        var typeReference = assumeNotNull(type.typeReference)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)

        typeProjection = typeProjections[1]
        Assertions.assertFalse(typeProjection.isStar)
        typeProjectionModifiers = typeProjection.typeProjectionModifiers()
        Assertions.assertTrue(typeProjectionModifiers.isEmpty())
        type = assumeNotNull(typeProjection.type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Int", userType.simpleUserTypes()[0].name)

        typeProjection = typeProjections[2]
        Assertions.assertFalse(typeProjection.isStar)
        typeProjectionModifiers = typeProjection.typeProjectionModifiers()
        Assertions.assertEquals(1, typeProjectionModifiers.size)
        Assertions.assertEquals(VarianceModifier.IN, typeProjectionModifiers[0].varianceModifier)
        type = assumeNotNull(typeProjection.type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("InputStream", userType.simpleUserTypes()[0].name)

        typeProjection = typeProjections[3]
        Assertions.assertFalse(typeProjection.isStar)
        typeProjectionModifiers = typeProjection.typeProjectionModifiers()
        Assertions.assertEquals(1, typeProjectionModifiers.size)
        Assertions.assertEquals(VarianceModifier.OUT, typeProjectionModifiers[0].varianceModifier)
        type = assumeNotNull(typeProjection.type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("OutputStream", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun typeWithProjection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<Pair<String, Person>>",
                                                                        KotlinGrammar.assignableSuffix))
        val assignableSuffix = AssignableSuffix()
        assignableSuffix.parse(grammarNode)
        var typeArguments = assumeNotNull(assignableSuffix.typeArguments)
        var typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(1, typeProjections.size)
        var type = assumeNotNull(typeProjections[0].type)
        var typeReference = assumeNotNull(type.typeReference)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Pair", userType.simpleUserTypes()[0].name)

        typeArguments = assumeNotNull(userType.simpleUserTypes()[0].typeArguments)
        typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(2, typeProjections.size)
        type = assumeNotNull(typeProjections[0].type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)

        type = assumeNotNull(typeProjections[1].type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Person", userType.simpleUserTypes()[0].name)
    }

    // *** indexingSuffix ***

    @Test
    fun oneIndex()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[42]",
                                                                        KotlinGrammar.assignableSuffix))
        val assignableSuffix = AssignableSuffix()
        assignableSuffix.parse(grammarNode)
        val indexingSuffix = assumeNotNull(assignableSuffix.indexingSuffix)
        val expressions = indexingSuffix.expressions()
        Assertions.assertEquals(1, expressions.size)
        val literalConstant = assumeNotNull(expressions[0].disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
    }

    @Test
    fun twoIndices()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[42, obtainIndex(\"Index\")]",
                                                                        KotlinGrammar.assignableSuffix))
        val assignableSuffix = AssignableSuffix()
        assignableSuffix.parse(grammarNode)
        val indexingSuffix = assumeNotNull(assignableSuffix.indexingSuffix)
        val expressions = indexingSuffix.expressions()
        Assertions.assertEquals(2, expressions.size)
        val literalConstant = assumeNotNull(expressions[0].disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        val postfixUnaryExpression = expressions[1].disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("obtainIndex", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Index", lineStrText.text)
    }

    @Test
    fun threeIndices()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[42, obtainIndex(\"Index\"), 600 + 66]",
                                                                        KotlinGrammar.assignableSuffix))
        val assignableSuffix = AssignableSuffix()
        assignableSuffix.parse(grammarNode)
        val indexingSuffix = assumeNotNull(assignableSuffix.indexingSuffix)
        val expressions = indexingSuffix.expressions()
        Assertions.assertEquals(3, expressions.size)
        var literalConstant = assumeNotNull(expressions[0].disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        val postfixUnaryExpression = expressions[1].disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("obtainIndex", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Index", lineStrText.text)
        val additiveExpression = expressions[2].disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0]
        literalConstant = assumeNotNull(additiveExpression.firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("600", literalConstant.value)
        val (additiveOperator, multiplicativeExpression) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[0]
        Assertions.assertEquals(AdditiveOperator.PLUS, additiveOperator)
        literalConstant = assumeNotNull(multiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("66", literalConstant.value)
    }

    // *** navigationSuffix ***

    @Test
    fun callMethod()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule(".indexOf(data)",
                                                                        KotlinGrammar.assignableSuffix))
        val assignableSuffix = AssignableSuffix()
        assignableSuffix.parse(grammarNode)
        val navigationSuffix = assumeNotNull(assignableSuffix.navigationSuffix)
        Assertions.assertFalse(navigationSuffix.isClass)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        val expression = assumeNotNull(navigationSuffix.expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("indexOf", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals("data",
                                callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun nullableCallMethod()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("?.indexOf(data)",
                                                                        KotlinGrammar.assignableSuffix))
        val assignableSuffix = AssignableSuffix()
        assignableSuffix.parse(grammarNode)
        val navigationSuffix = assumeNotNull(assignableSuffix.navigationSuffix)
        Assertions.assertFalse(navigationSuffix.isClass)
        Assertions.assertEquals(MemberAccessOperator.NULLABLE_CALL, navigationSuffix.memberAccessOperator)
        val expression = assumeNotNull(navigationSuffix.expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("indexOf", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals("data",
                                callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun referenceMethod()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("::indexOf",
                                                                        KotlinGrammar.assignableSuffix))
        val assignableSuffix = AssignableSuffix()
        assignableSuffix.parse(grammarNode)
        val navigationSuffix = assumeNotNull(assignableSuffix.navigationSuffix)
        Assertions.assertFalse(navigationSuffix.isClass)
        Assertions.assertEquals(MemberAccessOperator.METHOD_REFERENCE, navigationSuffix.memberAccessOperator)
        val expression = assumeNotNull(navigationSuffix.expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("indexOf", postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun classReference()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("::class",
                                                                        KotlinGrammar.assignableSuffix))
        val assignableSuffix = AssignableSuffix()
        assignableSuffix.parse(grammarNode)
        val navigationSuffix = assumeNotNull(assignableSuffix.navigationSuffix)
        Assertions.assertTrue(navigationSuffix.isClass)
        Assertions.assertEquals(MemberAccessOperator.METHOD_REFERENCE, navigationSuffix.memberAccessOperator)
        Assertions.assertNull(navigationSuffix.expression)
    }
}