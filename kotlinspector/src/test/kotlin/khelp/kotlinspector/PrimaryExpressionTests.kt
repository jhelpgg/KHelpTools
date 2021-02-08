package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.ValOrVar
import khelp.kotlinspector.model.declaration.FunctionDeclaration
import khelp.kotlinspector.model.declaration.PropertyDeclaration
import khelp.kotlinspector.model.delegation.UserType
import khelp.kotlinspector.model.expression.JumpExpressionType
import khelp.kotlinspector.model.expression.PrimaryExpression
import khelp.kotlinspector.model.expression.literal.LiteralConstantType
import khelp.kotlinspector.model.expression.operator.AdditiveOperator
import khelp.kotlinspector.model.expression.operator.AssignmentAndOperator
import khelp.kotlinspector.model.expression.operator.ComparisonOperator
import khelp.kotlinspector.model.expression.operator.IsOperator
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator
import khelp.kotlinspector.model.expression.operator.MultiplicativeOperator
import khelp.kotlinspector.model.expression.operator.PrefixUnaryOperator
import khelp.kotlinspector.model.modifier.MemberModifier
import khelp.kotlinspector.model.modifier.MemberModifierType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PrimaryExpressionTests
{
    // *** parenthesizedExpression ***

    @Test
    fun parenthesizedExpression()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("(operation(73, true))",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val parenthesizedExpression = assumeNotNull(primaryExpression.parenthesizedExpression)
        val postfixUnaryExpression = parenthesizedExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("operation", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        var literalConstant = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
        literalConstant = assumeNotNull(callSuffix.valueArguments()[1].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BOOLEAN, literalConstant.literalConstantType)
        Assertions.assertEquals("true", literalConstant.value)
    }

    // *** stringLiteral ***

    @Test
    fun emptyTextSimpleLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertTrue(lineStringContentOrExpressions.isEmpty())
    }

    @Test
    fun emptyTextMultiLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\"\"\"",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        val multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertTrue(multilineStringContentOrExpressions.isEmpty())
    }

    @Test
    fun simpleTextSimpleLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"Hello world ! \t Hello folks!\"",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertEquals(1, lineStringContentOrExpressions.size)
        val lineStringContent = assumeNotNull(lineStringContentOrExpressions[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("Hello world ! \t Hello folks!", lineStrText.text)
    }

    @Test
    fun simpleTextMultiLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\nHello world !\n \t Hello folks! \t \n\"\"\"",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        val multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(1, multilineStringContentOrExpressions.size)
        val multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        val multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("\nHello world !\n \t Hello folks! \t \n", multiLineStrText.text)
    }

    @Test
    fun stringReferenceSimpleLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\$value\"",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertEquals(1, lineStringContentOrExpressions.size)
        val lineStringContent = assumeNotNull(lineStringContentOrExpressions[0].lineStringContent)
        val lineStrRef = assumeNotNull(lineStringContent.lineStrRef)
        Assertions.assertEquals("value", lineStrRef.reference)
    }

    @Test
    fun onlyQuotesMultiLine()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\"\"\"\"",
                                                                        KotlinGrammar.primaryExpression))
        var primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        var stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        var multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        var multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(1, multilineStringContentOrExpressions.size)
        var multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\"\"\"\"\"",
                                                                    KotlinGrammar.primaryExpression))
        primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(2, multilineStringContentOrExpressions.size)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[1].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\"\"\"\"\"\"",
                                                                    KotlinGrammar.primaryExpression))
        primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(3, multilineStringContentOrExpressions.size)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[1].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[2].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
    }

    @Test
    fun stringReferenceMultiLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\$value\"\"\"",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        val multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(1, multilineStringContentOrExpressions.size)
        val multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        val multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("value", multiLineStrRef.reference)
    }

    @Test
    fun expressionSimpleLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\${computeResult()}\"",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertEquals(1, lineStringContentOrExpressions.size)
        val lineStringExpression = assumeNotNull(lineStringContentOrExpressions[0].lineStringExpression)
        val postfixUnaryExpression = lineStringExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeResult", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun expressionMultiLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"\${computeResult()}\"\"\"",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        val multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(1, multilineStringContentOrExpressions.size)
        val multiLineStringExpression = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringExpression)
        val postfixUnaryExpression = multiLineStringExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeResult", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun complexSimpleLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"The result is : \${computeResult()}\tParameters used : \$first,\$second, \$third\"",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContentOrExpressions = lineStringLiteral.lineStringContentOrExpressions()
        Assertions.assertEquals(8, lineStringContentOrExpressions.size)
        var lineStringContent = assumeNotNull(lineStringContentOrExpressions[0].lineStringContent)
        var lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("The result is : ", lineStrText.text)
        val lineStringExpression = assumeNotNull(lineStringContentOrExpressions[1].lineStringExpression)
        val postfixUnaryExpression = lineStringExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeResult", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
        lineStringContent = assumeNotNull(lineStringContentOrExpressions[2].lineStringContent)
        lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("\tParameters used : ", lineStrText.text)
        lineStringContent = assumeNotNull(lineStringContentOrExpressions[3].lineStringContent)
        var lineStrRef = assumeNotNull(lineStringContent.lineStrRef)
        Assertions.assertEquals("first", lineStrRef.reference)
        lineStringContent = assumeNotNull(lineStringContentOrExpressions[4].lineStringContent)
        lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals(",", lineStrText.text)
        lineStringContent = assumeNotNull(lineStringContentOrExpressions[5].lineStringContent)
        lineStrRef = assumeNotNull(lineStringContent.lineStrRef)
        Assertions.assertEquals("second", lineStrRef.reference)
        lineStringContent = assumeNotNull(lineStringContentOrExpressions[6].lineStringContent)
        lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals(", ", lineStrText.text)
        lineStringContent = assumeNotNull(lineStringContentOrExpressions[7].lineStringContent)
        lineStrRef = assumeNotNull(lineStringContent.lineStrRef)
        Assertions.assertEquals("third", lineStrRef.reference)
    }

    @Test
    fun complexMultiLine()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("\"\"\"The result is : \"\${computeResult()}\"\nParameters used : \$first,\$second, \$third\nDouble double quote \"\" should work\"\"\"",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val stringLiteral = assumeNotNull(primaryExpression.stringLiteral)
        val multiLineStringLiteral = assumeNotNull(stringLiteral.multiLineStringLiteral)
        val multilineStringContentOrExpressions = multiLineStringLiteral.multilineStringContentOrExpressions()
        Assertions.assertEquals(14, multilineStringContentOrExpressions.size)
        var multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[0].multiLineStringContent)
        var multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("The result is : ", multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[1].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        val multiLineStringExpression = assumeNotNull(multilineStringContentOrExpressions[2].multiLineStringExpression)
        val postfixUnaryExpression = multiLineStringExpression.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeResult", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[3].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[4].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("\nParameters used : ", multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[5].multiLineStringContent)
        var multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("first", multiLineStrRef.reference)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[6].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals(",", multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[7].multiLineStringContent)
        multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("second", multiLineStrRef.reference)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[8].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals(", ", multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[9].multiLineStringContent)
        multiLineStrRef = assumeNotNull(multiLineStringContent.multiLineStrRef)
        Assertions.assertEquals("third", multiLineStrRef.reference)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[10].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals("\nDouble double quote ", multiLineStrText.text)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[11].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[12].multiLineStringContent)
        Assertions.assertTrue(multiLineStringContent.isQuote)
        multiLineStringContent = assumeNotNull(multilineStringContentOrExpressions[13].multiLineStringContent)
        multiLineStrText = assumeNotNull(multiLineStringContent.multiLineStrText)
        Assertions.assertEquals(" should work", multiLineStrText.text)
    }

    // *** callableReference ***

    @Test
    fun typedIdentifierReference()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("CallableReferenceTests::identifierReference",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val callableReference = assumeNotNull(primaryExpression.callableReference)

        Assertions.assertFalse(callableReference.isClasReference)

        val receiverType = assumeNotNull(callableReference.receiverType)
        val typeReference = assumeNotNull(receiverType.typeReference)
        Assertions.assertFalse(typeReference.isDynamic)
        val userType = assumeNotNull(typeReference.userType)
        val simpleUserTypes = userType.simpleUserTypes()
        Assertions.assertEquals(1, simpleUserTypes.size)
        val simpleUserType = simpleUserTypes[0]
        Assertions.assertNull(simpleUserType.typeArguments)
        Assertions.assertEquals("CallableReferenceTests", simpleUserType.name)

        Assertions.assertEquals("identifierReference", callableReference.simpleIdentifier)
    }

    @Test
    fun identifierReference()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("::identifierReference",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val callableReference = assumeNotNull(primaryExpression.callableReference)

        Assertions.assertFalse(callableReference.isClasReference)

        Assertions.assertNull(callableReference.receiverType)

        Assertions.assertEquals("identifierReference", callableReference.simpleIdentifier)
    }

    @Test
    fun classReference()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("CallableReferenceTests::class",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val callableReference = assumeNotNull(primaryExpression.callableReference)

        Assertions.assertTrue(callableReference.isClasReference)

        val receiverType = assumeNotNull(callableReference.receiverType)
        val typeReference = assumeNotNull(receiverType.typeReference)
        Assertions.assertFalse(typeReference.isDynamic)
        val userType = assumeNotNull(typeReference.userType)
        val simpleUserTypes = userType.simpleUserTypes()
        Assertions.assertEquals(1, simpleUserTypes.size)
        val simpleUserType = simpleUserTypes[0]
        Assertions.assertNull(simpleUserType.typeArguments)
        Assertions.assertEquals("CallableReferenceTests", simpleUserType.name)

        Assertions.assertEquals("", callableReference.simpleIdentifier)
    }

    // *** functionLiteral ***

    @Test
    fun lambdaNoParameter()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            { sum += it }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val functionLiteral = assumeNotNull(primaryExpression.functionLiteral)
        val lambdaLiteral = assumeNotNull(functionLiteral.lambdaLiteral)
        Assertions.assertTrue(lambdaLiteral.lambdaParameters()
                                  .isEmpty())
        val assignment = assumeNotNull(lambdaLiteral.statements()[0].assignment)
        Assertions.assertEquals(AssignmentAndOperator.PLUS_ASSIGN, assignment.assignmentAndOperator)
        val assignableExpression = assumeNotNull(assignment.assignableExpression)
        val prefixUnaryExpression = assumeNotNull(assignableExpression.prefixUnaryExpression)
        Assertions.assertEquals("sum", prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        Assertions.assertEquals("it",
                                assignment.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun lambdaOneParameter()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            { value : Int -> value + 42 }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val functionLiteral = assumeNotNull(primaryExpression.functionLiteral)
        val lambdaLiteral = assumeNotNull(functionLiteral.lambdaLiteral)
        val variableDeclaration = lambdaLiteral.lambdaParameters()[0].variableDeclarations()[0]
        Assertions.assertEquals("value", variableDeclaration.name)
        val type = assumeNotNull(variableDeclaration.type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Int", userType.simpleUserTypes()[0].name)
        val expression = assumeNotNull(lambdaLiteral.statements()[0].expression)
        val additiveExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0]
        Assertions.assertEquals("value",
                                additiveExpression.firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (additiveOperator, multiplicativeExpression) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[0]
        Assertions.assertEquals(AdditiveOperator.PLUS, additiveOperator)
        val literalConstant = assumeNotNull(multiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
    }

    @Test
    fun lambdaTwoParameters()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
           { first, second ->
                first * second
           }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val functionLiteral = assumeNotNull(primaryExpression.functionLiteral)
        val lambdaLiteral = assumeNotNull(functionLiteral.lambdaLiteral)
        Assertions.assertEquals("first", lambdaLiteral.lambdaParameters()[0].variableDeclarations()[0].name)
        Assertions.assertEquals("second", lambdaLiteral.lambdaParameters()[1].variableDeclarations()[0].name)
        val expression = assumeNotNull(lambdaLiteral.statements()[0].expression)
        val firstMultiplicativeExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression
        Assertions.assertEquals("first",
                                firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (multiplicativeOperator, asExpression) = firstMultiplicativeExpression.multiplicativeOperatorAsExpressionCouples()[0]
        Assertions.assertEquals(MultiplicativeOperator.TIMES, multiplicativeOperator)
        Assertions.assertEquals("second",
                                asExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun anonymousFunctionSimple()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            fun  ()  
            {
                doSomething()
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val functionLiteral = assumeNotNull(primaryExpression.functionLiteral)
        val anonymousFunction = assumeNotNull(functionLiteral.anonymousFunction)
        val functionBody = assumeNotNull(anonymousFunction.functionBody)
        val block = assumeNotNull(functionBody.block)
        val expression = assumeNotNull(block.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("doSomething", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun anonymousFunctionTyped()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            fun String.(value, text:String) {
                  doSomething(text+" : "+value)
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val functionLiteral = assumeNotNull(primaryExpression.functionLiteral)
        val anonymousFunction = assumeNotNull(functionLiteral.anonymousFunction)

        var typeReference = assumeNotNull(anonymousFunction.parametersTypes()[0].typeReference)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)

        var parameterWithOptionalType = anonymousFunction.parametersWithOptionalType()[0]
        Assertions.assertEquals("value", parameterWithOptionalType.name)
        Assertions.assertNull(parameterWithOptionalType.type)

        parameterWithOptionalType = anonymousFunction.parametersWithOptionalType()[1]
        Assertions.assertEquals("text", parameterWithOptionalType.name)
        val type = assumeNotNull(parameterWithOptionalType.type)
        typeReference = assumeNotNull(type.typeReference)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)

        val functionBody = assumeNotNull(anonymousFunction.functionBody)
        val block = assumeNotNull(functionBody.block)
        val expression = assumeNotNull(block.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("doSomething", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val additiveExpression = callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0]
        Assertions.assertEquals("text",
                                additiveExpression.firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (additiveOperator0, multiplicativeExpression0) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[0]
        Assertions.assertEquals(AdditiveOperator.PLUS, additiveOperator0)
        val stringLiteral = assumeNotNull(multiplicativeExpression0.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals(" : ", lineStrText.text)
        val (additiveOperator1, multiplicativeExpression1) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[1]
        Assertions.assertEquals(AdditiveOperator.PLUS, additiveOperator1)
        Assertions.assertEquals("value",
                                multiplicativeExpression1.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
    }

    @Test
    fun anonymousFunctionDelegated()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            fun (value) : Int {
                  doSomething(value)
                  return 73
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val functionLiteral = assumeNotNull(primaryExpression.functionLiteral)
        val anonymousFunction = assumeNotNull(functionLiteral.anonymousFunction)

        val parameterWithOptionalType = anonymousFunction.parametersWithOptionalType()[0]
        Assertions.assertEquals("value", parameterWithOptionalType.name)
        Assertions.assertNull(parameterWithOptionalType.type)

        val returnType = assumeNotNull(anonymousFunction.returnType)
        val typeReference = assumeNotNull(returnType.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Int", userType.simpleUserTypes()[0].name)

        val functionBody = assumeNotNull(anonymousFunction.functionBody)
        val block = assumeNotNull(functionBody.block)
        var expression = assumeNotNull(block.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("doSomething", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals("value",
                                callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        expression = assumeNotNull(block.statements()[1].expression)
        val jumpExpression = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.jumpExpression)
        Assertions.assertEquals(JumpExpressionType.RETURN, jumpExpression.jumpExpressionType)
        expression = assumeNotNull(jumpExpression.expression)
        val literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
    }

    // *** objectLiteral ***

    @Test
    fun delegatedObject()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            object : Runnable {
                override fun run() 
                {
                    doSomething()
                }
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val objectLiteral = assumeNotNull(primaryExpression.objectLiteral)
        val userType: UserType = assumeIs(objectLiteral.delegationSpecifiers()[0].delegationSpecifier)
        Assertions.assertEquals("Runnable", userType.simpleUserTypes()[0].name)
        val functionDeclaration: FunctionDeclaration = assumeIs(objectLiteral.classBody.classMemberDeclarations()[0].declaration)
        val memberModifier: MemberModifier = assumeIs(functionDeclaration.modifiers()[0])
        Assertions.assertEquals(MemberModifierType.OVERRIDE, memberModifier.memberModifierType)
        Assertions.assertEquals("run", functionDeclaration.name)
        Assertions.assertTrue(functionDeclaration.functionValueParameters()
                                  .isEmpty())
        val functionBody = assumeNotNull(functionDeclaration.functionBody)
        val block = assumeNotNull(functionBody.block)
        val expression = assumeNotNull(block.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("doSomething",
                                postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun notDelegatedObject()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            object {
                fun method() 
                {
                    doSomething()
                }
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val objectLiteral = assumeNotNull(primaryExpression.objectLiteral)
        Assertions.assertTrue(objectLiteral.delegationSpecifiers()
                                  .isEmpty())
        val functionDeclaration: FunctionDeclaration = assumeIs(objectLiteral.classBody.classMemberDeclarations()[0].declaration)
        Assertions.assertTrue(functionDeclaration.modifiers()
                                  .isEmpty())
        Assertions.assertEquals("method", functionDeclaration.name)
        Assertions.assertTrue(functionDeclaration.functionValueParameters()
                                  .isEmpty())
        val functionBody = assumeNotNull(functionDeclaration.functionBody)
        val block = assumeNotNull(functionBody.block)
        val expression = assumeNotNull(block.statements()[0].expression)
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("doSomething",
                                postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    // *** collectionLiteral ***

    @Test
    fun emptyCollection()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[]",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val collectionLiteral = assumeNotNull(primaryExpression.collectionLiteral)
        Assertions.assertTrue(collectionLiteral.expressions()
                                  .isEmpty())
    }

    @Test
    fun oneConstant()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[42]",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val collectionLiteral = assumeNotNull(primaryExpression.collectionLiteral)
        Assertions.assertEquals(1, collectionLiteral.expressions().size)
        val expression = collectionLiteral.expressions()[0]
        val literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
    }

    @Test
    fun oneOperation()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[ 42 - 73 ]",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val collectionLiteral = assumeNotNull(primaryExpression.collectionLiteral)
        Assertions.assertEquals(1, collectionLiteral.expressions().size)
        val expression = collectionLiteral.expressions()[0]
        val additiveExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0]
        var literalConstant = assumeNotNull(additiveExpression.firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        val (additiveOperator, multiplicativeExpression) = additiveExpression.additiveOperatorMultiplicativeExpressionCouples()[0]
        Assertions.assertEquals(AdditiveOperator.MINUS, additiveOperator)
        literalConstant = assumeNotNull(multiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
    }

    @Test
    fun oneCall()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[ computeKey() ]",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val collectionLiteral = assumeNotNull(primaryExpression.collectionLiteral)
        Assertions.assertEquals(1, collectionLiteral.expressions().size)
        val expression = collectionLiteral.expressions()[0]
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeKey", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun twoArguments()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[ \"key\", 666 ]",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val collectionLiteral = assumeNotNull(primaryExpression.collectionLiteral)
        Assertions.assertEquals(2, collectionLiteral.expressions().size)

        var expression = collectionLiteral.expressions()[0]
        val stringLiteral = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("key", lineStrText.text)

        expression = collectionLiteral.expressions()[1]
        val literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("666", literalConstant.value)
    }

    @Test
    fun threeArguments()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("[ \"key\", 666, computeKey() ]",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val collectionLiteral = assumeNotNull(primaryExpression.collectionLiteral)
        Assertions.assertEquals(3, collectionLiteral.expressions().size)

        var expression = collectionLiteral.expressions()[0]
        val stringLiteral = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("key", lineStrText.text)

        expression = collectionLiteral.expressions()[1]
        val literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("666", literalConstant.value)

        expression = collectionLiteral.expressions()[2]
        val postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("computeKey", postfixUnaryExpression.primaryExpression.identifier)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    // *** thisExpression ***

    @Test
    fun thisOnly()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("this",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val thisExpression = assumeNotNull(primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
    }

    @Test
    fun thisAt()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("this@method",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val thisExpression = assumeNotNull(primaryExpression.thisExpression)
        Assertions.assertEquals("method", thisExpression.atIdentifier)
    }

    // *** superExpression ***

    @Test
    fun superOnly()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("super",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val superExpression = assumeNotNull(primaryExpression.superExpression)
        Assertions.assertTrue(superExpression.simpleIdentifier.isEmpty())
        Assertions.assertNull(superExpression.type)
    }

    @Test
    fun superType()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("super<String>",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val superExpression = assumeNotNull(primaryExpression.superExpression)
        Assertions.assertTrue(superExpression.simpleIdentifier.isEmpty())
        val type = assumeNotNull(superExpression.type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun superTypeAt()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("super<String>@method",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val superExpression = assumeNotNull(primaryExpression.superExpression)
        Assertions.assertEquals("method", superExpression.simpleIdentifier)
        val type = assumeNotNull(superExpression.type)
        val typeReference = assumeNotNull(type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("String", userType.simpleUserTypes()[0].name)
    }

    @Test
    fun superAt()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("super@method",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val superExpression = assumeNotNull(primaryExpression.superExpression)
        Assertions.assertEquals("method", superExpression.simpleIdentifier)
        Assertions.assertNull(superExpression.type)
    }

    // *** ifExpression ***

    @Test
    fun ifTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            if(value > first * 96L) 
            {
                this.tooMuchAlert()
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val ifExpression = assumeNotNull(primaryExpression.ifExpression)
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
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val ifExpression = assumeNotNull(primaryExpression.ifExpression)
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
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        var ifExpression = assumeNotNull(primaryExpression.ifExpression)
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

    // *** whenExpression ***

    @Test
    fun withSubjectTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            when(value) {
                is Number -> 0b1000_0001
                42        -> 0b0001_1000
                else      -> 0b1010_0101
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val whenExpression = assumeNotNull(primaryExpression.whenExpression)
        val whenSubject = assumeNotNull(whenExpression.whenSubject)
        Assertions.assertEquals("value",
                                whenSubject.expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)

        var whenEntry = whenExpression.whenEntries()[0]
        val typeTest = assumeNotNull(whenEntry.whenConditions()[0].typeTest)
        Assertions.assertEquals(IsOperator.IS, typeTest.isOperator)
        val typeReference = assumeNotNull(typeTest.type.typeReference)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Number", userType.simpleUserTypes()[0].name)
        var statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        var expression = assumeNotNull(statement.expression)
        var literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1000_0001", literalConstant.value)

        whenEntry = whenExpression.whenEntries()[1]
        expression = assumeNotNull(whenEntry.whenConditions()[0].expression)
        literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        expression = assumeNotNull(statement.expression)
        literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b0001_1000", literalConstant.value)

        whenEntry = whenExpression.whenEntries()[2]
        Assertions.assertTrue(whenEntry.whenConditions()
                                  .isEmpty())
        statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        expression = assumeNotNull(statement.expression)
        literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1010_0101", literalConstant.value)
    }

    @Test
    fun withoutSubjectTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            when {
                value > 73 -> 0b1000_0001
                value < 42 -> 0b0001_1000
                else       -> 0b1010_0101
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val whenExpression = assumeNotNull(primaryExpression.whenExpression)
        Assertions.assertNull(whenExpression.whenSubject)

        var whenEntry = whenExpression.whenEntries()[0]
        var expression = assumeNotNull(whenEntry.whenConditions()[0].expression)
        var equality = expression.disjunction.conjunctions()[0].equalities()[0]
        Assertions.assertEquals("value",
                                equality.firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (operator, comparison) = equality.firstComparison.operatorGenericCallLikeComparisonCouples()[0]
        Assertions.assertEquals(ComparisonOperator.UPPER, operator)
        var literalConstant = assumeNotNull(comparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)
        var statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        expression = assumeNotNull(statement.expression)
        literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1000_0001", literalConstant.value)

        whenEntry = whenExpression.whenEntries()[1]
        expression = assumeNotNull(whenEntry.whenConditions()[0].expression)
        equality = expression.disjunction.conjunctions()[0].equalities()[0]
        Assertions.assertEquals("value",
                                equality.firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.identifier)
        val (operator2, comparison2) = equality.firstComparison.operatorGenericCallLikeComparisonCouples()[0]
        Assertions.assertEquals(ComparisonOperator.LOWER, operator2)
        literalConstant = assumeNotNull(comparison2.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("42", literalConstant.value)
        statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        expression = assumeNotNull(statement.expression)
        literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b0001_1000", literalConstant.value)

        whenEntry = whenExpression.whenEntries()[2]
        Assertions.assertTrue(whenEntry.whenConditions()
                                  .isEmpty())
        statement = assumeNotNull(whenEntry.controlStructureBody.statement)
        expression = assumeNotNull(statement.expression)
        literalConstant = assumeNotNull(expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1010_0101", literalConstant.value)
    }

    // *** tryExpression ***

    @Test
    fun tryFinally()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            try
            {
                // ...
                var line = bufferedReader.line()
                // ...
            }
            finally {
                bufferedReader?.close()
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val tryExpression = assumeNotNull(primaryExpression.tryExpression)

        // ****************
        // *** Try part ***
        // ****************

        var statements = tryExpression.block.statements()
        Assertions.assertEquals(3, statements.size)
        Assertions.assertEquals("// ...\n", statements[0].comments)

        var statement = statements[1]
        val propertyDeclaration: PropertyDeclaration = assumeIs(statement.declaration)
        Assertions.assertEquals(ValOrVar.VAR, propertyDeclaration.valOrVar)
        val variableDeclarations = propertyDeclaration.variableDeclarations()
        Assertions.assertEquals(1, variableDeclarations.size)
        Assertions.assertEquals("line", variableDeclarations[0].name)
        var expression = assumeNotNull(propertyDeclaration.value)
        var postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("bufferedReader", postfixUnaryExpression.primaryExpression.identifier)
        var navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("line", postfixUnaryExpression.primaryExpression.identifier)
        var callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())

        Assertions.assertEquals("// ...\n", statements[2].comments)

        // ******************
        // *** Catch part ***
        // ******************

        val catches = tryExpression.catchBlocks()
        Assertions.assertTrue(catches.isEmpty())

        // ********************
        // *** Finally part ***
        // ********************

        val finallyBlock = assumeNotNull(tryExpression.finallyBlock)
        statements = finallyBlock.block.statements()
        Assertions.assertEquals(1, statements.size)
        statement = statements[0]
        Assertions.assertTrue(statement.annotation.isEmpty())
        Assertions.assertTrue(statement.label.isEmpty())
        Assertions.assertNull(statement.assignment)
        Assertions.assertNull(statement.declaration)
        Assertions.assertNull(statement.loopStatement)
        expression = assumeNotNull(statement.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("bufferedReader", postfixUnaryExpression.primaryExpression.identifier)
        Assertions.assertEquals(1, postfixUnaryExpression.postfixUnarySuffixs().size)
        val postfixUnarySuffix = postfixUnaryExpression.postfixUnarySuffixs()[0]
        navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.NULLABLE_CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("close", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun tryCatch()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            try
            {
                // ...
                var line = bufferedReader.line()
                // ...
            }
            catch(ioException : IOException) {
                this.alertReadingIssue(ioException)
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val tryExpression = assumeNotNull(primaryExpression.tryExpression)

        // ****************
        // *** Try part ***
        // ****************

        var statements = tryExpression.block.statements()
        Assertions.assertEquals(3, statements.size)
        Assertions.assertEquals("// ...\n", statements[0].comments)

        var statement = statements[1]
        val propertyDeclaration: PropertyDeclaration = assumeIs(statement.declaration)
        Assertions.assertEquals(ValOrVar.VAR, propertyDeclaration.valOrVar)
        val variableDeclarations = propertyDeclaration.variableDeclarations()
        Assertions.assertEquals(1, variableDeclarations.size)
        Assertions.assertEquals("line", variableDeclarations[0].name)
        var expression = assumeNotNull(propertyDeclaration.value)
        var postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("bufferedReader", postfixUnaryExpression.primaryExpression.identifier)
        var navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("line", postfixUnaryExpression.primaryExpression.identifier)
        var callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())

        Assertions.assertEquals("// ...\n", statements[2].comments)

        // ******************
        // *** Catch part ***
        // ******************

        val catches = tryExpression.catchBlocks()
        Assertions.assertEquals(1, catches.size)
        Assertions.assertEquals("ioException", catches[0].simpleIdentifier)
        val typeReference = assumeNotNull(catches[0].type.typeReference)
        Assertions.assertFalse(typeReference.isDynamic)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("IOException", userType.simpleUserTypes()[0].name)
        statements = catches[0].block.statements()
        Assertions.assertEquals(1, statements.size)
        statement = statements[0]
        Assertions.assertTrue(statement.annotation.isEmpty())
        Assertions.assertTrue(statement.label.isEmpty())
        Assertions.assertNull(statement.assignment)
        Assertions.assertNull(statement.declaration)
        Assertions.assertNull(statement.loopStatement)
        expression = assumeNotNull(statement.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        val thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
        Assertions.assertEquals(1, postfixUnaryExpression.postfixUnarySuffixs().size)
        val postfixUnarySuffix = postfixUnaryExpression.postfixUnarySuffixs()[0]
        navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("alertReadingIssue", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals(1, callSuffix.valueArguments().size)
        expression = callSuffix.valueArguments()[0].expression
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("ioException", postfixUnaryExpression.primaryExpression.identifier)

        // ********************
        // *** Finally part ***
        // ********************

        Assertions.assertNull(tryExpression.finallyBlock)
    }

    @Test
    fun tryCatchFinally()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            try
            {
                // ...
                var line = bufferedReader.line()
                // ...
            }
            catch(ioException : IOException) {
                this.alertReadingIssue(ioException)
            }
            finally {
                bufferedReader?.close()
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val tryExpression = assumeNotNull(primaryExpression.tryExpression)

        // ****************
        // *** Try part ***
        // ****************

        var statements = tryExpression.block.statements()
        Assertions.assertEquals(3, statements.size)
        Assertions.assertEquals("// ...\n", statements[0].comments)

        var statement = statements[1]
        val propertyDeclaration: PropertyDeclaration = assumeIs(statement.declaration)
        Assertions.assertEquals(ValOrVar.VAR, propertyDeclaration.valOrVar)
        val variableDeclarations = propertyDeclaration.variableDeclarations()
        Assertions.assertEquals(1, variableDeclarations.size)
        Assertions.assertEquals("line", variableDeclarations[0].name)
        var expression = assumeNotNull(propertyDeclaration.value)
        var postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("bufferedReader", postfixUnaryExpression.primaryExpression.identifier)
        var navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("line", postfixUnaryExpression.primaryExpression.identifier)
        var callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())

        Assertions.assertEquals("// ...\n", statements[2].comments)

        // ******************
        // *** Catch part ***
        // ******************

        val catches = tryExpression.catchBlocks()
        Assertions.assertEquals(1, catches.size)
        Assertions.assertEquals("ioException", catches[0].simpleIdentifier)
        val typeReference = assumeNotNull(catches[0].type.typeReference)
        Assertions.assertFalse(typeReference.isDynamic)
        val userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("IOException", userType.simpleUserTypes()[0].name)
        statements = catches[0].block.statements()
        Assertions.assertEquals(1, statements.size)
        statement = statements[0]
        Assertions.assertTrue(statement.annotation.isEmpty())
        Assertions.assertTrue(statement.label.isEmpty())
        Assertions.assertNull(statement.assignment)
        Assertions.assertNull(statement.declaration)
        Assertions.assertNull(statement.loopStatement)
        expression = assumeNotNull(statement.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        val thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
        Assertions.assertEquals(1, postfixUnaryExpression.postfixUnarySuffixs().size)
        var postfixUnarySuffix = postfixUnaryExpression.postfixUnarySuffixs()[0]
        navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("alertReadingIssue", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals(1, callSuffix.valueArguments().size)
        expression = callSuffix.valueArguments()[0].expression
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("ioException", postfixUnaryExpression.primaryExpression.identifier)

        // ********************
        // *** Finally part ***
        // ********************

        val finallyBlock = assumeNotNull(tryExpression.finallyBlock)
        statements = finallyBlock.block.statements()
        Assertions.assertEquals(1, statements.size)
        statement = statements[0]
        Assertions.assertTrue(statement.annotation.isEmpty())
        Assertions.assertTrue(statement.label.isEmpty())
        Assertions.assertNull(statement.assignment)
        Assertions.assertNull(statement.declaration)
        Assertions.assertNull(statement.loopStatement)
        expression = assumeNotNull(statement.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("bufferedReader", postfixUnaryExpression.primaryExpression.identifier)
        Assertions.assertEquals(1, postfixUnaryExpression.postfixUnarySuffixs().size)
        postfixUnarySuffix = postfixUnaryExpression.postfixUnarySuffixs()[0]
        navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.NULLABLE_CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("close", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    @Test
    fun tryCatchCatch()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            try
            {
                // ...
                var number = bufferedReader.line().toInt()
                // ...
            }
            catch(ioException : IOException) {
                this.alertReadingIssue(ioException)
            }
            catch(exception : Exception) {
                this.alertInvalidDataIssue(exception)
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val tryExpression = assumeNotNull(primaryExpression.tryExpression)

        // ****************
        // *** Try part ***
        // ****************

        var statements = tryExpression.block.statements()
        Assertions.assertEquals(3, statements.size)
        Assertions.assertEquals("// ...\n", statements[0].comments)

        var statement = statements[1]
        val propertyDeclaration: PropertyDeclaration = assumeIs(statement.declaration)
        Assertions.assertEquals(ValOrVar.VAR, propertyDeclaration.valOrVar)
        val variableDeclarations = propertyDeclaration.variableDeclarations()
        Assertions.assertEquals(1, variableDeclarations.size)
        Assertions.assertEquals("number", variableDeclarations[0].name)
        var expression = assumeNotNull(propertyDeclaration.value)
        var postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("bufferedReader", postfixUnaryExpression.primaryExpression.identifier)
        var navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("line", postfixUnaryExpression.primaryExpression.identifier)
        var callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
        navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[1].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("toInt", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())

        Assertions.assertEquals("// ...\n", statements[2].comments)

        // ******************
        // *** Catch part ***
        // ******************

        val catches = tryExpression.catchBlocks()
        Assertions.assertEquals(2, catches.size)

        // First catch
        Assertions.assertEquals("ioException", catches[0].simpleIdentifier)
        var typeReference = assumeNotNull(catches[0].type.typeReference)
        Assertions.assertFalse(typeReference.isDynamic)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("IOException", userType.simpleUserTypes()[0].name)
        statements = catches[0].block.statements()
        Assertions.assertEquals(1, statements.size)
        statement = statements[0]
        Assertions.assertTrue(statement.annotation.isEmpty())
        Assertions.assertTrue(statement.label.isEmpty())
        Assertions.assertNull(statement.assignment)
        Assertions.assertNull(statement.declaration)
        Assertions.assertNull(statement.loopStatement)
        expression = assumeNotNull(statement.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        var thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
        Assertions.assertEquals(1, postfixUnaryExpression.postfixUnarySuffixs().size)
        var postfixUnarySuffix = postfixUnaryExpression.postfixUnarySuffixs()[0]
        navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("alertReadingIssue", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals(1, callSuffix.valueArguments().size)
        expression = callSuffix.valueArguments()[0].expression
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("ioException", postfixUnaryExpression.primaryExpression.identifier)

        // second catch
        Assertions.assertEquals("exception", catches[1].simpleIdentifier)
        typeReference = assumeNotNull(catches[1].type.typeReference)
        Assertions.assertFalse(typeReference.isDynamic)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Exception", userType.simpleUserTypes()[0].name)
        statements = catches[1].block.statements()
        Assertions.assertEquals(1, statements.size)
        statement = statements[0]
        Assertions.assertTrue(statement.annotation.isEmpty())
        Assertions.assertTrue(statement.label.isEmpty())
        Assertions.assertNull(statement.assignment)
        Assertions.assertNull(statement.declaration)
        Assertions.assertNull(statement.loopStatement)
        expression = assumeNotNull(statement.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
        Assertions.assertEquals(1, postfixUnaryExpression.postfixUnarySuffixs().size)
        postfixUnarySuffix = postfixUnaryExpression.postfixUnarySuffixs()[0]
        navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("alertInvalidDataIssue", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals(1, callSuffix.valueArguments().size)
        expression = callSuffix.valueArguments()[0].expression
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("exception", postfixUnaryExpression.primaryExpression.identifier)

        // ********************
        // *** Finally part ***
        // ********************

        Assertions.assertNull(tryExpression.finallyBlock)
    }

    @Test
    fun tryCatchCatchFinally()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("""
            try
            {
                // ...
                var number = bufferedReader.line().toInt()
                // ...
            }
            catch(ioException : IOException) {
                this.alertReadingIssue(ioException)
            }
            catch(exception : Exception) {
                this.alertInvalidDataIssue(exception)
            }
            finally {
                bufferedReader?.close()
            }
        """.trimIndent(),
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val tryExpression = assumeNotNull(primaryExpression.tryExpression)

        // ****************
        // *** Try part ***
        // ****************

        var statements = tryExpression.block.statements()
        Assertions.assertEquals(3, statements.size)
        Assertions.assertEquals("// ...\n", statements[0].comments)

        var statement = statements[1]
        val propertyDeclaration: PropertyDeclaration = assumeIs(statement.declaration)
        Assertions.assertEquals(ValOrVar.VAR, propertyDeclaration.valOrVar)
        val variableDeclarations = propertyDeclaration.variableDeclarations()
        Assertions.assertEquals(1, variableDeclarations.size)
        Assertions.assertEquals("number", variableDeclarations[0].name)
        var expression = assumeNotNull(propertyDeclaration.value)
        var postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("bufferedReader", postfixUnaryExpression.primaryExpression.identifier)
        var navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("line", postfixUnaryExpression.primaryExpression.identifier)
        var callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
        navigationSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[1].navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("toInt", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())

        Assertions.assertEquals("// ...\n", statements[2].comments)

        // ******************
        // *** Catch part ***
        // ******************

        val catches = tryExpression.catchBlocks()
        Assertions.assertEquals(2, catches.size)

        // First catch
        Assertions.assertEquals("ioException", catches[0].simpleIdentifier)
        var typeReference = assumeNotNull(catches[0].type.typeReference)
        Assertions.assertFalse(typeReference.isDynamic)
        var userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("IOException", userType.simpleUserTypes()[0].name)
        statements = catches[0].block.statements()
        Assertions.assertEquals(1, statements.size)
        statement = statements[0]
        Assertions.assertTrue(statement.annotation.isEmpty())
        Assertions.assertTrue(statement.label.isEmpty())
        Assertions.assertNull(statement.assignment)
        Assertions.assertNull(statement.declaration)
        Assertions.assertNull(statement.loopStatement)
        expression = assumeNotNull(statement.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        var thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
        Assertions.assertEquals(1, postfixUnaryExpression.postfixUnarySuffixs().size)
        var postfixUnarySuffix = postfixUnaryExpression.postfixUnarySuffixs()[0]
        navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("alertReadingIssue", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals(1, callSuffix.valueArguments().size)
        expression = callSuffix.valueArguments()[0].expression
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("ioException", postfixUnaryExpression.primaryExpression.identifier)

        // second catch
        Assertions.assertEquals("exception", catches[1].simpleIdentifier)
        typeReference = assumeNotNull(catches[1].type.typeReference)
        Assertions.assertFalse(typeReference.isDynamic)
        userType = assumeNotNull(typeReference.userType)
        Assertions.assertEquals("Exception", userType.simpleUserTypes()[0].name)
        statements = catches[1].block.statements()
        Assertions.assertEquals(1, statements.size)
        statement = statements[0]
        Assertions.assertTrue(statement.annotation.isEmpty())
        Assertions.assertTrue(statement.label.isEmpty())
        Assertions.assertNull(statement.assignment)
        Assertions.assertNull(statement.declaration)
        Assertions.assertNull(statement.loopStatement)
        expression = assumeNotNull(statement.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        thisExpression = assumeNotNull(postfixUnaryExpression.primaryExpression.thisExpression)
        Assertions.assertTrue(thisExpression.atIdentifier.isEmpty())
        Assertions.assertEquals(1, postfixUnaryExpression.postfixUnarySuffixs().size)
        postfixUnarySuffix = postfixUnaryExpression.postfixUnarySuffixs()[0]
        navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("alertInvalidDataIssue", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertEquals(1, callSuffix.valueArguments().size)
        expression = callSuffix.valueArguments()[0].expression
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("exception", postfixUnaryExpression.primaryExpression.identifier)

        // ********************
        // *** Finally part ***
        // ********************

        val finallyBlock = assumeNotNull(tryExpression.finallyBlock)
        statements = finallyBlock.block.statements()
        Assertions.assertEquals(1, statements.size)
        statement = statements[0]
        Assertions.assertTrue(statement.annotation.isEmpty())
        Assertions.assertTrue(statement.label.isEmpty())
        Assertions.assertNull(statement.assignment)
        Assertions.assertNull(statement.declaration)
        Assertions.assertNull(statement.loopStatement)
        expression = assumeNotNull(statement.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("bufferedReader", postfixUnaryExpression.primaryExpression.identifier)
        Assertions.assertEquals(1, postfixUnaryExpression.postfixUnarySuffixs().size)
        postfixUnarySuffix = postfixUnaryExpression.postfixUnarySuffixs()[0]
        navigationSuffix = assumeNotNull(postfixUnarySuffix.navigationSuffix)
        Assertions.assertEquals(MemberAccessOperator.NULLABLE_CALL, navigationSuffix.memberAccessOperator)
        expression = assumeNotNull(navigationSuffix.expression)
        postfixUnaryExpression = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        Assertions.assertEquals("close", postfixUnaryExpression.primaryExpression.identifier)
        callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        Assertions.assertTrue(callSuffix.valueArguments()
                                  .isEmpty())
    }

    // *** jumpExpression ***

    @Test
    fun throwTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("throw IllegalArgumentException(\"message\")",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val jumpExpression = assumeNotNull(primaryExpression.jumpExpression)
        Assertions.assertEquals(JumpExpressionType.THROW, jumpExpression.jumpExpressionType)
        Assertions.assertTrue(jumpExpression.atName.isEmpty())
        val expression = assumeNotNull(jumpExpression.expression)
        val firstGenericCallLikeComparison = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison
        val postfixUnaryExpression = firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        val name = postfixUnaryExpression.primaryExpression.identifier
        Assertions.assertEquals("IllegalArgumentException", name)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("message", lineStrText.text)
    }

    @Test
    fun returnTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("return String(\"message\")",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val jumpExpression = assumeNotNull(primaryExpression.jumpExpression)
        Assertions.assertEquals(JumpExpressionType.RETURN, jumpExpression.jumpExpressionType)
        Assertions.assertTrue(jumpExpression.atName.isEmpty())
        val expression = assumeNotNull(jumpExpression.expression)
        val firstGenericCallLikeComparison = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison
        val postfixUnaryExpression = firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        val name = postfixUnaryExpression.primaryExpression.identifier
        Assertions.assertEquals("String", name)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("message", lineStrText.text)
    }

    @Test
    fun returnAtTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("return@returnAtTest String(\"message\")",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val jumpExpression = assumeNotNull(primaryExpression.jumpExpression)
        Assertions.assertEquals(JumpExpressionType.RETURN, jumpExpression.jumpExpressionType)
        Assertions.assertEquals("returnAtTest", jumpExpression.atName)
        val expression = assumeNotNull(jumpExpression.expression)
        val firstGenericCallLikeComparison = expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison
        val postfixUnaryExpression = firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression
        val name = postfixUnaryExpression.primaryExpression.identifier
        Assertions.assertEquals("String", name)
        val callSuffix = assumeNotNull(postfixUnaryExpression.postfixUnarySuffixs()[0].callSuffix)
        val stringLiteral = assumeNotNull(callSuffix.valueArguments()[0].expression.disjunction.conjunctions()[0].equalities()[0].firstComparison.firstGenericCallLikeComparison.infixOperation.elvisExpression.infixFunctionCalls()[0].firstRangeExpression.additiveExpressions()[0].firstMultiplicativeExpression.firstAsExpression.prefixUnaryExpression.postfixUnaryExpression.primaryExpression.stringLiteral)
        val lineStringLiteral = assumeNotNull(stringLiteral.lineStringLiteral)
        val lineStringContent = assumeNotNull(lineStringLiteral.lineStringContentOrExpressions()[0].lineStringContent)
        val lineStrText = assumeNotNull(lineStringContent.lineStrText)
        Assertions.assertEquals("message", lineStrText.text)
    }

    @Test
    fun continueTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("continue",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val jumpExpression = assumeNotNull(primaryExpression.jumpExpression)
        Assertions.assertEquals(JumpExpressionType.CONTINUE, jumpExpression.jumpExpressionType)
        Assertions.assertTrue(jumpExpression.atName.isEmpty())
        Assertions.assertNull(jumpExpression.expression)
    }

    @Test
    fun continueAtTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("continue@somewhere",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val jumpExpression = assumeNotNull(primaryExpression.jumpExpression)
        Assertions.assertEquals(JumpExpressionType.CONTINUE, jumpExpression.jumpExpressionType)
        Assertions.assertEquals("somewhere", jumpExpression.atName)
        Assertions.assertNull(jumpExpression.expression)
    }

    @Test
    fun breakTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("break",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val jumpExpression = assumeNotNull(primaryExpression.jumpExpression)
        Assertions.assertEquals(JumpExpressionType.BREAK, jumpExpression.jumpExpressionType)
        Assertions.assertTrue(jumpExpression.atName.isEmpty())
        Assertions.assertNull(jumpExpression.expression)
    }

    @Test
    fun breakAtTest()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("break@somewhere",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val jumpExpression = assumeNotNull(primaryExpression.jumpExpression)
        Assertions.assertEquals(JumpExpressionType.BREAK, jumpExpression.jumpExpressionType)
        Assertions.assertEquals("somewhere", jumpExpression.atName)
        Assertions.assertNull(jumpExpression.expression)
    }

    // *** literalConstant ***

    @Test
    fun booleanLiteral()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("true",
                                                                        KotlinGrammar.primaryExpression))
        var primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        var literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BOOLEAN, literalConstant.literalConstantType)
        Assertions.assertEquals("true", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("false",
                                                                    KotlinGrammar.primaryExpression))
        primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BOOLEAN, literalConstant.literalConstantType)
        Assertions.assertEquals("false", literalConstant.value)
    }

    @Test
    fun unsignedLiteral()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("42UL",
                                                                        KotlinGrammar.primaryExpression))
        var primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        var literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.UNSIGNED, literalConstant.literalConstantType)
        Assertions.assertEquals("42UL", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0xCAFEu",
                                                                    KotlinGrammar.primaryExpression))
        primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.UNSIGNED, literalConstant.literalConstantType)
        Assertions.assertEquals("0xCAFEu", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0b1001ul",
                                                                    KotlinGrammar.primaryExpression))
        primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.UNSIGNED, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1001ul", literalConstant.value)

        Assertions.assertNull(KotlinGrammar.parseSpecificRule("73uv",
                                                              KotlinGrammar.literalConstant))

        Assertions.assertNull(KotlinGrammar.parseSpecificRule("666ule",
                                                              KotlinGrammar.literalConstant))
    }

    @Test
    fun longLiteral()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("42L",
                                                                        KotlinGrammar.primaryExpression))
        var primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        var literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.LONG, literalConstant.literalConstantType)
        Assertions.assertEquals("42L", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0xCAFEl",
                                                                    KotlinGrammar.primaryExpression))
        primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.LONG, literalConstant.literalConstantType)
        Assertions.assertEquals("0xCAFEl", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0b1001L",
                                                                    KotlinGrammar.primaryExpression))
        primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.LONG, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1001L", literalConstant.value)

        Assertions.assertNull(KotlinGrammar.parseSpecificRule("73LA",
                                                              KotlinGrammar.literalConstant))
    }

    @Test
    fun hexLiteral()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0xFacE09",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.HEXADECIMAL, literalConstant.literalConstantType)
        Assertions.assertEquals("0xFacE09", literalConstant.value)
    }

    @Test
    fun binLiteral()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0b1001",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.BINARY, literalConstant.literalConstantType)
        Assertions.assertEquals("0b1001", literalConstant.value)
    }

    @Test
    fun characterLiteral()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("'O'",
                                                                        KotlinGrammar.primaryExpression))
        var primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        var literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.CHARACTER, literalConstant.literalConstantType)
        Assertions.assertEquals("'O'", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("'\\n'",
                                                                    KotlinGrammar.primaryExpression))
        primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.CHARACTER, literalConstant.literalConstantType)
        Assertions.assertEquals("'\\n'", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("'\\u1AB2'",
                                                                    KotlinGrammar.primaryExpression))
        primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.CHARACTER, literalConstant.literalConstantType)
        Assertions.assertEquals("'\\u1AB2'", literalConstant.value)
    }

    @Test
    fun realLiteral()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("0.123456789",
                                                                        KotlinGrammar.primaryExpression))
        var primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        var literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.REAL, literalConstant.literalConstantType)
        Assertions.assertEquals("0.123456789", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("1e-9",
                                                                    KotlinGrammar.primaryExpression))
        primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.REAL, literalConstant.literalConstantType)
        Assertions.assertEquals("1e-9", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("-73.42",
                                                                    KotlinGrammar.primaryExpression))
        primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.REAL, literalConstant.literalConstantType)
        Assertions.assertEquals("-73.42", literalConstant.value)
    }

    @Test
    fun nullLiteral()
    {
        val grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("null",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        val literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.NULL, literalConstant.literalConstantType)
        Assertions.assertEquals("null", literalConstant.value)
    }

    @Test
    fun integerLiteral()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("73",
                                                                        KotlinGrammar.primaryExpression))
        var primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        var literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("73", literalConstant.value)

        grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("-42",
                                                                    KotlinGrammar.primaryExpression))
        primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        literalConstant = assumeNotNull(primaryExpression.literalConstant)
        Assertions.assertEquals(LiteralConstantType.INTEGER, literalConstant.literalConstantType)
        Assertions.assertEquals("-42", literalConstant.value)
    }

    // *** simpleIdentifier ***

    @Test
    fun simpleIdentifier()
    {
        var grammarNode = assumeNotNull(KotlinGrammar.parseSpecificRule("turtule",
                                                                        KotlinGrammar.primaryExpression))
        val primaryExpression = PrimaryExpression()
        primaryExpression.parse(grammarNode)
        Assertions.assertEquals("turtule", primaryExpression.identifier)
    }
}