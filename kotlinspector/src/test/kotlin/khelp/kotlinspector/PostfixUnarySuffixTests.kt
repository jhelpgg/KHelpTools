package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.PostFixUnarySuffix
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.AdditiveOperator
import khelp.kotlinspector.model.expression.operator.AssignmentAndOperator
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator
import khelp.kotlinspector.model.expression.operator.PostfixUnaryOperator
import khelp.kotlinspector.model.typeparameter.VarianceModifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PostfixUnarySuffixTests
{
    // *** postfixUnaryOperator ***

    @Test
    fun postfixUnaryOperator()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("++",
                                                                        KotlinGrammar.postfixUnarySuffix))
        var postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        Assertions.assertEquals(PostfixUnaryOperator.INCREMENT, postfixUnarySuffix.postfixUnaryOperator)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("--",
                                                                    KotlinGrammar.postfixUnarySuffix))
        postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        Assertions.assertEquals(PostfixUnaryOperator.DECREMENT, postfixUnarySuffix.postfixUnaryOperator)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("!!",
                                                                    KotlinGrammar.postfixUnarySuffix))
        postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        Assertions.assertEquals(PostfixUnaryOperator.ASSUME_NOT_NULL, postfixUnarySuffix.postfixUnaryOperator)
    }

    // *** typeArguments ***

    @Test
    fun oneStarTypeProjection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<*>",
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val typeArguments = assumeNotNull(postfixUnarySuffix.typeArguments)
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
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val typeArguments = assumeNotNull(postfixUnarySuffix.typeArguments)
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
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val typeArguments = assumeNotNull(postfixUnarySuffix.typeArguments)
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
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val typeArguments = assumeNotNull(postfixUnarySuffix.typeArguments)
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
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        var typeArguments = assumeNotNull(postfixUnarySuffix.typeArguments)
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

    // *** callSuffix ***

    @Test
    fun onlyValueArguments()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("(73, \"Magic number\")",
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val callSuffix = assumeNotNull(postfixUnarySuffix.callSuffix)
        val valueArguments = callSuffix.valueArguments()
        Assertions.assertEquals(2, valueArguments.size)
        val literalConstant = assumeNotNull(valueArguments[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
        val stringLiteral = assumeNotNull(valueArguments[1].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Magic number", lineStrText.text)
    }

    @Test
    fun onlyLambda()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("{ sum += it }",
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val callSuffix = assumeNotNull(postfixUnarySuffix.callSuffix)
        val annotatedLambda = assumeNotNull(callSuffix.annotatedLambda)
        val assignment = assumeNotNull(annotatedLambda.lambdaLiteral.statements()[0].assignment)
        val assignableExpression = assumeNotNull(assignment.assignableExpression)
        val prefixUnaryExpression = assumeNotNull(assignableExpression.prefixUnaryExpression)
        Assertions.assertEquals("sum", prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        Assertions.assertEquals(AssignmentAndOperator.PLUS_ASSIGN, assignment.assignmentAndOperator)
        Assertions.assertEquals("it",
                                assignment.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun typesArguments()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<Int, String> (73, \"Magic number\")",
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val callSuffix = assumeNotNull(postfixUnarySuffix.callSuffix)
        val typeArguments = assumeNotNull(callSuffix.typeArguments)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(2, typeProjections.size)
        var type = assumeNotNull(typeProjections[0].type)
        var typeReference = assumeNotNull(type.typeReference)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Int", userType.simpleUserTypes()[0].name)
        type = assumeNotNull(typeProjections[1].type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
        val valueArguments = callSuffix.valueArguments()
        Assertions.assertEquals(2, valueArguments.size)
        val literalConstant = assumeNotNull(valueArguments[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
        val stringLiteral = assumeNotNull(valueArguments[1].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Magic number", lineStrText.text)
    }

    @Test
    fun typesArgumentsAndLambda()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("<Int, String, ()->Unit> (73, \"Magic number\") { println(\"Ok\") }",
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        var callSuffix = assumeNotNull(postfixUnarySuffix.callSuffix)
        val typeArguments = assumeNotNull(callSuffix.typeArguments)
        val typeProjections = typeArguments.typeProjections()
        Assertions.assertEquals(3, typeProjections.size)
        var type = assumeNotNull(typeProjections[0].type)
        var typeReference = assumeNotNull(type.typeReference)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Int", userType.simpleUserTypes()[0].name)
        type = assumeNotNull(typeProjections[1].type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
        type = assumeNotNull(typeProjections[2].type)
        val functionType = assumeNotNull(type.functionType)
        Assertions.assertTrue(functionType.functionTypeParameters.typeOrParameters()
                                  .isEmpty())
        typeReference = assumeNotNull(functionType.type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Unit", userType.simpleUserTypes()[0].name)
        val valueArguments = callSuffix.valueArguments()
        Assertions.assertEquals(2, valueArguments.size)
        val literalConstant = assumeNotNull(valueArguments[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
        var stringLiteral = assumeNotNull(valueArguments[1].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        var lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        var lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        var lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Magic number", lineStrText.text)
        val annotatedLambda = assumeNotNull(callSuffix.annotatedLambda)
        val expression = assumeNotNull(annotatedLambda.lambdaLiteral.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("println", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Ok", lineStrText.text)
    }

    // *** indexingSuffix ***

    @Test
    fun oneIndex()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[42]",
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val indexingSuffix = assumeNotNull(postfixUnarySuffix.indexingSuffix)
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
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val indexingSuffix = assumeNotNull(postfixUnarySuffix.indexingSuffix)
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
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val indexingSuffix = assumeNotNull(postfixUnarySuffix.indexingSuffix)
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
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
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
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
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
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
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
                                                                        KotlinGrammar.postfixUnarySuffix))
        val postfixUnarySuffix = PostFixUnarySuffix()
        postfixUnarySuffix.parse(grammarNode)
        val navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
        Assertions.assertTrue(navigationSuffix.isClass)
        Assertions.assertEquals(MemberAccessOperator.METHOD_REFERENCE, navigationSuffix.memberAccessOperator)
        Assertions.assertNull(navigationSuffix.expression)
    }
}