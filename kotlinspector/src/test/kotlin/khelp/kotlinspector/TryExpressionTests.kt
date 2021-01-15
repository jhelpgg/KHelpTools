package khelp.kotlinspector

import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.ValOrVar
import khelp.kotlinspector.model.declaration.PropertyDeclaration
import khelp.kotlinspector.model.expression.operator.MemberAccessOperator
import khelp.kotlinspector.model.expression.trycacth.TryExpression
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TryExpressionTests
{
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
        """.trimIndent(), KotlinGrammar.tryExpression))
        val tryExpression = TryExpression()
        tryExpression.parse(grammarNode)

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
        """.trimIndent(), KotlinGrammar.tryExpression))
        val tryExpression = TryExpression()
        tryExpression.parse(grammarNode)

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
        """.trimIndent(), KotlinGrammar.tryExpression))
        val tryExpression = TryExpression()
        tryExpression.parse(grammarNode)

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
        """.trimIndent(), KotlinGrammar.tryExpression))
        val tryExpression = TryExpression()
        tryExpression.parse(grammarNode)

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
        """.trimIndent(), KotlinGrammar.tryExpression))
        val tryExpression = TryExpression()
        tryExpression.parse(grammarNode)

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
}
