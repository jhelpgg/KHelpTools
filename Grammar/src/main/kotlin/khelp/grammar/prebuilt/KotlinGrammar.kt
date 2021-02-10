package khelp.grammar.prebuilt

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import khelp.grammar.Grammar
import khelp.grammar.GrammarNode
import khelp.grammar.WHITE_SPACES
import khelp.grammar.prebuilt.unicodes.unicodeClassLL
import khelp.grammar.prebuilt.unicodes.unicodeClassLM
import khelp.grammar.prebuilt.unicodes.unicodeClassLO
import khelp.grammar.prebuilt.unicodes.unicodeClassLT
import khelp.grammar.prebuilt.unicodes.unicodeClassLU
import khelp.grammar.prebuilt.unicodes.unicodeClassND
import khelp.grammar.prebuilt.unicodes.unicodeClassNL
import khelp.utilities.extensions.allCharactersExcludeThis
import khelp.utilities.extensions.allCharactersExcludeThose
import khelp.utilities.extensions.exactTimes
import khelp.utilities.extensions.plus
import khelp.utilities.extensions.regularExpression
import khelp.utilities.extensions.zeroOrMore
import khelp.utilities.extensions.zeroOrOne
import khelp.utilities.regex.ANY
import khelp.utilities.regex.END_EXPRESSION
import khelp.utilities.regex.LETTER_OR_DIGIT
import khelp.utilities.regex.RegularExpression
import khelp.utilities.regex.WHITE_SPACE
import khelp.utilities.text.interval
import org.jetbrains.annotations.TestOnly

// https://kotlinlang.org/docs/reference/grammar.html

object KotlinGrammar
{
    const val kotlinFile = "kotlinFile"
    const val shebangLine = "shebangLine"
    const val fileAnnotation = "fileAnnotation"
    const val packageHeader = "packageHeader"
    const val importList = "importList"
    const val topLevelObject = "topLevelObject"
    const val unescapedAnnotation = "unescapedAnnotation"
    const val declaration = "declaration"
    const val identifier = "identifier"
    const val importHeader = "importHeader"
    const val importAlias = "importAlias"
    const val simpleIdentifier = "simpleIdentifier"
    const val typeAlias = "typeAlias"
    const val classDeclaration = "classDeclaration"
    const val primaryConstructor = "primaryConstructor"
    const val classBody = "classBody"
    const val classParameters = "classParameters"
    const val classParameter = "classParameter"
    const val delegationSpecifiers = "delegationSpecifiers"
    const val delegationSpecifier = "delegationSpecifier"
    const val constructorInvocation = "constructorInvocation"
    const val annotatedDelegationSpecifier = "annotatedDelegationSpecifier"
    const val explicitDelegation = "explicitDelegation"
    const val typeParameters = "typeParameters"
    const val typeParameter = "typeParameter"
    const val typeConstraints = "typeConstraints"
    const val typeConstraint = "typeConstraint"
    const val classMemberDeclarations = "classMemberDeclarations"
    const val classMemberDeclaration = "classMemberDeclaration"
    const val anonymousInitializer = "anonymousInitializer"
    const val companionObject = "companionObject"
    const val functionValueParameters = "functionValueParameters"
    const val functionValueParameter = "functionValueParameter"
    const val functionDeclaration = "functionDeclaration"
    const val functionBody = "functionBody"
    const val variableDeclaration = "variableDeclaration"
    const val multiVariableDeclaration = "multiVariableDeclaration"
    const val propertyDeclaration = "propertyDeclaration"
    const val propertyDelegate = "propertyDelegate"
    const val getter = "getter"
    const val setter = "setter"
    const val parametersWithOptionalType = "parametersWithOptionalType"
    const val parameterWithOptionalType = "parameterWithOptionalType"
    const val parameter = "parameter"
    const val objectDeclaration = "objectDeclaration"
    const val secondaryConstructor = "secondaryConstructor"
    const val constructorDelegationCall = "constructorDelegationCall"
    const val enumClassBody = "enumClassBody"
    const val enumEntries = "enumEntries"
    const val enumEntry = "enumEntry"
    const val type = "type"
    const val typeReference = "typeReference"
    const val nullableType = "nullableType"
    const val quest = "quest"
    const val userType = "userType"
    const val simpleUserType = "simpleUserType"
    const val typeProjection = "typeProjection"
    const val typeProjectionModifiers = "typeProjectionModifiers"
    const val typeProjectionModifier = "typeProjectionModifier"
    const val functionType = "functionType"
    const val functionTypeParameters = "functionTypeParameters"
    const val parenthesizedType = "parenthesizedType"
    const val parenthesizedUserType = "parenthesizedUserType"
    const val statements = "statements"
    const val statement = "statement"
    const val label = "label"
    const val controlStructureBody = "controlStructureBody"
    const val block = "block"
    const val loopStatement = "loopStatement"
    const val forStatement = "forStatement"
    const val whileStatement = "whileStatement"
    const val doWhileStatement = "doWhileStatement"
    const val assignment = "assignment"
    const val expression = "expression"
    const val disjunction = "disjunction"
    const val conjunction = "conjunction"
    const val equality = "equality"
    const val comparison = "comparison"
    const val genericCallLikeComparison = "genericCallLikeComparison"
    const val infixOperation = "infixOperation"
    const val elvisExpression = "elvisExpression"
    const val infixFunctionCall = "infixFunctionCall"
    const val rangeExpression = "rangeExpression"
    const val additiveExpression = "additiveExpression"
    const val multiplicativeExpression = "multiplicativeExpression"
    const val asExpression = "asExpression"
    const val prefixUnaryExpression = "prefixUnaryExpression"
    const val unaryPrefix = "unaryPrefix"
    const val postfixUnaryExpression = "postfixUnaryExpression"
    const val postfixUnarySuffix = "postfixUnarySuffix"
    const val directlyAssignableExpression = "directlyAssignableExpression"
    const val parenthesizedDirectlyAssignableExpression = "parenthesizedDirectlyAssignableExpression"
    const val assignableExpression = "assignableExpression"
    const val parenthesizedAssignableExpression = "parenthesizedAssignableExpression"
    const val assignableSuffix = "assignableSuffix"
    const val indexingSuffix = "indexingSuffix"
    const val navigationSuffix = "navigationSuffix"
    const val callSuffix = "callSuffix"
    const val annotatedLambda = "annotatedLambda"
    const val typeArguments = "typeArguments"
    const val valueArguments = "valueArguments"
    const val valueArgument = "valueArgument"
    const val primaryExpression = "primaryExpression"
    const val parenthesizedExpression = "parenthesizedExpression"
    const val collectionLiteral = "collectionLiteral"
    const val literalConstant = "literalConstant"
    const val stringLiteral = "stringLiteral"
    const val lineStringLiteral = "lineStringLiteral"
    const val multiLineStringLiteral = "multiLineStringLiteral"
    const val lineStringContent = "lineStringContent"
    const val lineStringExpression = "lineStringExpression"
    const val multiLineStringContent = "multiLineStringContent"
    const val multiLineStringExpression = "multiLineStringExpression"
    const val lambdaLiteral = "lambdaLiteral"
    const val lambdaParameters = "lambdaParameters"
    const val lambdaParameter = "lambdaParameter"
    const val anonymousFunction = "anonymousFunction"
    const val functionLiteral = "functionLiteral"
    const val objectLiteral = "objectLiteral"
    const val thisExpression = "thisExpression"
    const val superExpression = "superExpression"
    const val ifExpression = "ifExpression"
    const val whenSubject = "whenSubject"
    const val whenExpression = "whenExpression"
    const val whenEntry = "whenEntry"
    const val whenCondition = "whenCondition"
    const val rangeTest = "rangeTest"
    const val typeTest = "typeTest"
    const val tryExpression = "tryExpression"
    const val catchBlock = "catchBlock"
    const val finallyBlock = "finallyBlock"
    const val jumpExpression = "jumpExpression"
    const val callableReference = "callableReference"
    const val assignmentAndOperator = "assignmentAndOperator"
    const val equalityOperator = "equalityOperator"
    const val comparisonOperator = "comparisonOperator"
    const val inOperator = "inOperator"
    const val isOperator = "isOperator"
    const val additiveOperator = "additiveOperator"
    const val multiplicativeOperator = "multiplicativeOperator"
    const val asOperator = "asOperator"
    const val prefixUnaryOperator = "prefixUnaryOperator"
    const val postfixUnaryOperator = "postfixUnaryOperator"
    const val memberAccessOperator = "memberAccessOperator"
    const val modifiers = "modifiers"
    const val modifier = "modifier"
    const val typeModifiers = "typeModifiers"
    const val typeModifier = "typeModifier"
    const val classModifier = "classModifier"
    const val memberModifier = "memberModifier"
    const val visibilityModifier = "visibilityModifier"
    const val varianceModifier = "varianceModifier"
    const val typeParameterModifiers = "typeParameterModifiers"
    const val typeParameterModifier = "typeParameterModifier"
    const val functionModifier = "functionModifier"
    const val propertyModifier = "propertyModifier"
    const val inheritanceModifier = "inheritanceModifier"
    const val parameterModifier = "parameterModifier"
    const val reificationModifier = "reificationModifier"
    const val platformModifier = "platformModifier"
    const val annotation = "annotation"
    const val singleAnnotation = "singleAnnotation"
    const val multiAnnotation = "multiAnnotation"
    const val annotationUseSiteTarget = "annotationUseSiteTarget"
    const val receiverType = "receiverType"
    const val parameterModifiers = "parameterModifiers"
    const val DelimitedComment = "DelimitedComment"
    const val LineComment = "LineComment"
    const val WS = "WS"
    const val Hidden = "Hidden"
    const val RESERVED = "RESERVED"
    const val DOUBLE_ARROW = "DOUBLE_ARROW"
    const val DOUBLE_SEMICOLON = "DOUBLE_SEMICOLON"
    const val HASH = "HASH"
    const val SINGLE_QUOTE = "SINGLE_QUOTE"
    const val RETURN_AT = "RETURN_AT"
    const val CONTINUE_AT = "CONTINUE_AT"
    const val BREAK_AT = "BREAK_AT"
    const val THIS_AT = "THIS_AT"
    const val SUPER_AT = "SUPER_AT"
    const val TYPEOF = "TYPEOF"
    const val DecDigit = "DecDigit"
    const val DecDigitNoZero = "DecDigitNoZero"
    const val DecDigitOrSeparator = "DecDigitOrSeparator"
    const val DecDigits = "DecDigits"
    const val DoubleExponent = "DoubleExponent"
    const val RealLiteral = "RealLiteral"
    const val FloatLiteral = "FloatLiteral"
    const val DoubleLiteral = "DoubleLiteral"
    const val IntegerLiteral = "IntegerLiteral"
    const val HexDigit = "HexDigit"
    const val HexDigitOrSeparator = "HexDigitOrSeparator"
    const val HexLiteral = "HexLiteral"
    const val BinDigit = "BinDigit"
    const val BinDigitOrSeparator = "BinDigitOrSeparator"
    const val BinLiteral = "BinLiteral"
    const val UnsignedLiteral = "UnsignedLiteral"
    const val LongLiteral = "LongLiteral"
    const val BooleanLiteral = "BooleanLiteral"
    const val CharacterLiteral = "CharacterLiteral"
    const val UnicodeDigit = "UnicodeDigit"
    const val Identifier = "Identifier"
    const val IdentifierOrSoftKey = "IdentifierOrSoftKey"
    const val FieldIdentifier = "FieldIdentifier"
    const val UniCharacterLiteral = "UniCharacterLiteral"
    const val EscapedIdentifier = "EscapedIdentifier"
    const val EscapeSeq = "EscapeSeq"
    const val LineStrRef = "LineStrRef"
    const val LineStrText = "LineStrText"
    const val LineStrEscapedChar = "LineStrEscapedChar"
    const val TRIPLE_QUOTE_CLOSE = "TRIPLE_QUOTE_CLOSE"
    const val MultiLineStrRef = "MultiLineStrRef"
    const val MultiLineStrText = "MultiLineStrText"
    const val ErrorCharacter = "ErrorCharacter"
    const val UNICODE_CLASS_LL = "UNICODE_CLASS_LL"
    const val UNICODE_CLASS_LM = "UNICODE_CLASS_LM"
    const val UNICODE_CLASS_LO = "UNICODE_CLASS_LO"
    const val UNICODE_CLASS_LT = "UNICODE_CLASS_LT"
    const val UNICODE_CLASS_LU = "UNICODE_CLASS_LU"
    const val UNICODE_CLASS_ND = "UNICODE_CLASS_ND"
    const val UNICODE_CLASS_NL = "UNICODE_CLASS_NL"

    val Letter = unicodeClassLL + unicodeClassLM + unicodeClassLO + unicodeClassLT + unicodeClassLU + unicodeClassNL
    val valOrVar: RegularExpression get() = "va".regularExpression + charArrayOf('l', 'r') + WHITE_SPACE
    private val grammar = Grammar(automaticWhiteSpaces = true)

    init
    {
        this.grammar.rules {
            kotlinFile IS { rule = shebangLine.zeroOrOne() * fileAnnotation.zeroOrMore() * Hidden.zeroOrMore() * packageHeader * importList * topLevelObject.zeroOrMore() }
            shebangLine IS { rule = +("#!".regularExpression + ANY.zeroOrMore() + '\n') }
            fileAnnotation IS { rule = '@'.regularExpression * "file".regularExpression * ':'.regularExpression * (('['.regularExpression * unescapedAnnotation.oneOrMore() * ']'.regularExpression) I unescapedAnnotation) }
            packageHeader IS { rule = "package".regularExpression * identifier * WHITE_SPACES }
            importList IS { rule = importHeader.zeroOrMore() }
            importHeader IS { rule = Hidden.zeroOrMore() * "import".regularExpression * identifier * ((".*".regularExpression) I importAlias).zeroOrMore() }
            importAlias IS { rule = "as".regularExpression * simpleIdentifier }
            topLevelObject IS { rule = declaration * WHITE_SPACES }
            typeAlias IS { rule = modifiers.zeroOrOne() * "typealias".regularExpression * simpleIdentifier * typeParameters.zeroOrOne() * '='.regularExpression * type }
            declaration IS { rule = Hidden.zeroOrMore() * (classDeclaration I objectDeclaration I functionDeclaration I propertyDeclaration I typeAlias) }

            /// Classes

            classDeclaration IS {
                rule = modifiers.zeroOrOne() * ("class".regularExpression I
                        ("fun".regularExpression.zeroOrOne() * "interface".regularExpression)) *
                       simpleIdentifier * typeParameters.zeroOrOne() * primaryConstructor.zeroOrOne() *
                       (':'.regularExpression * delegationSpecifiers).zeroOrOne() *
                       typeConstraints.zeroOrOne() * (classBody I enumClassBody).zeroOrOne()
            }
            primaryConstructor IS { rule = (modifiers.zeroOrOne() * "constructor".regularExpression).zeroOrOne() * classParameters }
            classBody IS { rule = '{'.regularExpression * classMemberDeclarations * '}'.regularExpression }
            classParameters IS {
                rule = '('.regularExpression *
                       (classParameter * (','.regularExpression * classParameter).zeroOrMore() * ','.zeroOrOne()).zeroOrOne() *
                       ')'.regularExpression
            }
            classParameter IS { rule = modifiers.zeroOrOne() * valOrVar.zeroOrOne() * simpleIdentifier * ':'.regularExpression * type * ('='.regularExpression * expression).zeroOrOne() }
            delegationSpecifiers IS { rule = annotatedDelegationSpecifier * (','.regularExpression * annotatedDelegationSpecifier).zeroOrMore() }
            delegationSpecifier IS { rule = constructorInvocation I explicitDelegation I userType I functionType }
            constructorInvocation IS { rule = userType * valueArguments }
            annotatedDelegationSpecifier IS { rule = annotation.zeroOrMore() * delegationSpecifier }
            explicitDelegation IS { rule = (userType I functionType) * "by".regularExpression * expression }
            typeParameters IS { rule = '<'.regularExpression * typeParameter * (','.regularExpression * typeParameter).zeroOrMore() * ','.zeroOrOne() * '>'.regularExpression }
            typeParameter IS { rule = typeParameterModifiers.zeroOrOne() * simpleIdentifier * (':'.regularExpression * type).zeroOrOne() }
            typeConstraints IS { rule = "where".regularExpression * typeConstraint * (','.regularExpression * typeConstraint).zeroOrMore() }
            typeConstraint IS { rule = annotation.zeroOrMore() * simpleIdentifier * ':'.regularExpression * type }

            // Class members

            classMemberDeclarations IS { rule = (classMemberDeclaration * WHITE_SPACES).zeroOrMore() }
            classMemberDeclaration IS { rule = Hidden.zeroOrMore() * (declaration I companionObject I anonymousInitializer I secondaryConstructor) }
            anonymousInitializer IS { rule = "init".regularExpression * block }
            companionObject IS {
                rule = modifiers.zeroOrOne() * "companion".regularExpression * "object".regularExpression * simpleIdentifier.zeroOrOne() *
                       (':'.regularExpression * delegationSpecifiers).zeroOrOne() *
                       classBody.zeroOrOne()
            }
            functionValueParameters IS { rule = '('.regularExpression * (functionValueParameter * (','.regularExpression * functionValueParameter).zeroOrMore() * ','.zeroOrOne()).zeroOrOne() * ')'.regularExpression }
            functionValueParameter IS { rule = parameterModifiers.zeroOrOne() * parameter * ('='.regularExpression * expression).zeroOrOne() }
            functionDeclaration IS {
                rule = modifiers.zeroOrOne() * "fun".regularExpression * typeParameters.zeroOrOne() *
                       (receiverType * '.'.regularExpression).zeroOrOne() *
                       simpleIdentifier * functionValueParameters *
                       (':'.regularExpression * type).zeroOrOne() * typeConstraints.zeroOrOne() *
                       functionBody.zeroOrOne()
            }
            functionBody IS { rule = block I ('='.regularExpression * expression) }
            variableDeclaration IS { rule = annotation.zeroOrMore() * simpleIdentifier * (':'.regularExpression * type).zeroOrOne() }
            multiVariableDeclaration IS { rule = '('.regularExpression * variableDeclaration * (','.regularExpression * variableDeclaration).zeroOrMore() * ','.zeroOrOne() * ')'.regularExpression }
            propertyDeclaration IS {
                rule = modifiers.zeroOrOne() * valOrVar * typeParameters.zeroOrOne() *
                       (receiverType * '.'.regularExpression).zeroOrOne() *
                       (multiVariableDeclaration I variableDeclaration) *
                       typeConstraints.zeroOrOne() *
                       (('='.regularExpression * expression) I propertyDelegate).zeroOrOne() * ';'.zeroOrOne() *
                       ((getter.zeroOrOne() * (WHITE_SPACES * setter).zeroOrOne()) I (setter.zeroOrOne() * (WHITE_SPACES * getter).zeroOrOne()))
            }
            propertyDelegate IS { rule = "by".regularExpression * expression }
            getter IS {
                rule = (modifiers.zeroOrOne() * "get".regularExpression) I
                        (modifiers.zeroOrOne() * "get".regularExpression * '('.regularExpression * ')'.regularExpression *
                         (':'.regularExpression * type).zeroOrOne() *
                         functionBody)
            }
            setter IS {
                rule = (modifiers.zeroOrOne() * "set".regularExpression) I
                        (modifiers.zeroOrOne() * "set".regularExpression * '('.regularExpression * parameterWithOptionalType * ','.zeroOrOne() * ')'.regularExpression *
                         (':'.regularExpression * type).zeroOrOne() *
                         functionBody)
            }
            parametersWithOptionalType IS { rule = '('.regularExpression * (parameterWithOptionalType * (','.regularExpression * parameterWithOptionalType).zeroOrMore() * ','.zeroOrOne()).zeroOrOne() * ')'.regularExpression }
            parameterWithOptionalType IS { rule = parameterModifiers.zeroOrOne() * simpleIdentifier * (':'.regularExpression * type).zeroOrOne() }
            parameter IS { rule = simpleIdentifier * ':'.regularExpression * type }
            objectDeclaration IS { rule = modifiers.zeroOrOne() * "object".regularExpression * simpleIdentifier * (':'.regularExpression * delegationSpecifiers).zeroOrOne() * classBody.zeroOrOne() }
            secondaryConstructor IS {
                rule = modifiers.zeroOrOne() * "constructor".regularExpression * functionValueParameters *
                       (':'.regularExpression * constructorDelegationCall).zeroOrOne() * block.zeroOrOne()
            }
            constructorDelegationCall IS { rule = ("this".regularExpression OR "super") * valueArguments }

            // Enum classes

            enumClassBody IS { rule = '{'.regularExpression * enumEntries.zeroOrOne() * (';'.regularExpression * classMemberDeclarations).zeroOrOne() * '}'.regularExpression }
            enumEntries IS { rule = enumEntry * (','.regularExpression * enumEntry).zeroOrMore() * ','.regularExpression.zeroOrOne() }
            enumEntry IS { rule = modifiers.zeroOrOne() * simpleIdentifier * valueArguments.zeroOrOne() * classBody.zeroOrOne() }

            // Types

            type IS { rule = typeModifiers.zeroOrOne() * (parenthesizedType I nullableType I typeReference I functionType) }
            typeReference IS { rule = userType I "dynamic".regularExpression }
            nullableType IS { rule = (typeReference I parenthesizedType) * '?'.regularExpression }
            userType IS { rule = simpleUserType * ('.'.regularExpression * simpleUserType).zeroOrMore() }
            simpleUserType IS { rule = simpleIdentifier * typeArguments.zeroOrOne() }
            typeProjection IS { rule = (typeProjectionModifiers.zeroOrOne() * type) I '*'.regularExpression }
            typeProjectionModifiers IS { rule = typeProjectionModifier.oneOrMore() }
            typeProjectionModifier IS { rule = varianceModifier I annotation }
            functionType IS { rule = (receiverType * '.'.regularExpression).zeroOrOne() * functionTypeParameters * "->".regularExpression * type }
            functionTypeParameters IS { rule = '('.regularExpression * (parameter I type).zeroOrOne() * (','.regularExpression * (parameter I type)).zeroOrMore() * ','.zeroOrOne() * ')'.regularExpression }
            parenthesizedType IS { rule = '('.regularExpression * type * ')'.regularExpression }
            receiverType IS { rule = typeModifiers.zeroOrOne() * (parenthesizedType I nullableType I typeReference) }
            parenthesizedUserType IS {
                rule = ('('.regularExpression * userType * ')'.regularExpression) I
                        ('('.regularExpression * parenthesizedUserType * ')'.regularExpression)
            }

            // Statements

            statements IS { rule = (statement * (WHITE_SPACES * statement).zeroOrMore()).zeroOrOne() * WHITE_SPACES }
            statement IS { rule = (label I annotation).zeroOrMore() * (Hidden I declaration I assignment I loopStatement I expression) }
            label IS { rule = simpleIdentifier * '@'.regularExpression }
            controlStructureBody IS { rule = block I statement }
            block IS { rule = '{'.regularExpression * statements * '}'.regularExpression }
            loopStatement IS { rule = forStatement I whileStatement I doWhileStatement }
            forStatement IS {
                rule = "for".regularExpression *
                       '('.regularExpression * annotation.zeroOrMore() * (variableDeclaration I multiVariableDeclaration) * "in".regularExpression * expression * ')'.regularExpression *
                       controlStructureBody.zeroOrOne()
            }
            whileStatement IS {
                rule = ("while".regularExpression * '('.regularExpression * expression * ')'.regularExpression * controlStructureBody) I
                        ("while".regularExpression * '('.regularExpression * expression * ')'.regularExpression * ';'.regularExpression)
            }
            doWhileStatement IS { rule = "do".regularExpression * controlStructureBody.zeroOrOne() * "while".regularExpression * '('.regularExpression * expression * ')'.regularExpression }
            assignment IS {
                rule = (directlyAssignableExpression * '='.regularExpression * expression) I
                        (assignableExpression * assignmentAndOperator * expression)
            }

            // Expressions

            expression IS { rule = +disjunction }
            disjunction IS { rule = conjunction * ("||".regularExpression * conjunction).zeroOrMore() }
            conjunction IS { rule = equality * ("&&".regularExpression * equality).zeroOrMore() }
            equality IS { rule = comparison * (equalityOperator * comparison).zeroOrMore() }
            comparison IS { rule = genericCallLikeComparison * (comparisonOperator * genericCallLikeComparison).zeroOrMore() }
            genericCallLikeComparison IS { rule = infixOperation * callSuffix.zeroOrMore() }
            infixOperation IS { rule = elvisExpression * ((inOperator * elvisExpression) I (isOperator * type)).zeroOrMore() }
            elvisExpression IS { rule = infixFunctionCall SPACE (WHITE_SPACES * "?:".regularExpression * infixFunctionCall).zeroOrMore() }
            infixFunctionCall IS { rule = rangeExpression SPACE (simpleIdentifier * rangeExpression).zeroOrMore() }
            rangeExpression IS { rule = additiveExpression SPACE ("..".regularExpression * additiveExpression).zeroOrMore() }
            additiveExpression IS { rule = multiplicativeExpression SPACE (additiveOperator * multiplicativeExpression).zeroOrMore() }
            multiplicativeExpression IS { rule = asExpression SPACE (multiplicativeOperator * asExpression).zeroOrMore() }
            asExpression IS { rule = prefixUnaryExpression SPACE (asOperator * type).zeroOrMore() }
            prefixUnaryExpression IS { rule = unaryPrefix.zeroOrMore() * postfixUnaryExpression }
            unaryPrefix IS { rule = annotation I label I prefixUnaryOperator }
            postfixUnaryExpression IS { rule = (primaryExpression * postfixUnarySuffix.oneOrMore()) I primaryExpression }
            postfixUnarySuffix IS { rule = callSuffix I postfixUnaryOperator I typeArguments I indexingSuffix I navigationSuffix }
            directlyAssignableExpression IS { rule = parenthesizedDirectlyAssignableExpression I postfixUnaryExpression }
            parenthesizedDirectlyAssignableExpression IS { rule = '('.regularExpression * directlyAssignableExpression * ')'.regularExpression }
            assignableExpression IS { rule = parenthesizedAssignableExpression I prefixUnaryExpression }
            parenthesizedAssignableExpression IS { rule = '('.regularExpression * assignableExpression * ')'.regularExpression }
            assignableSuffix IS {
                rule = typeArguments I
                        indexingSuffix I
                        navigationSuffix
            }
            indexingSuffix IS { rule = '['.regularExpression * expression * (','.regularExpression * expression).zeroOrMore() * ','.zeroOrOne() * ']'.regularExpression }
            navigationSuffix IS { rule = memberAccessOperator * ("class".regularExpression I expression) }
            callSuffix IS {
                rule = (typeArguments.zeroOrOne() * valueArguments.zeroOrOne() * annotatedLambda) I
                        (typeArguments.zeroOrOne() * valueArguments)
            }
            annotatedLambda IS { rule = annotation.zeroOrMore() * label.zeroOrOne() * lambdaLiteral }
            typeArguments IS { rule = '<'.regularExpression * typeProjection * (','.regularExpression * typeProjection).zeroOrMore() * ','.zeroOrOne() * '>'.regularExpression }
            valueArguments IS {
                rule = ('('.regularExpression * ')'.regularExpression) I
                        ('('.regularExpression * valueArgument * (','.regularExpression * valueArgument).zeroOrMore() * ','.zeroOrOne() * ')'.regularExpression)
            }
            valueArgument IS { rule = annotation.zeroOrOne() * (simpleIdentifier * '='.regularExpression).zeroOrOne() * '*'.zeroOrOne() * expression }
            primaryExpression IS {
                rule = parenthesizedExpression I
                        stringLiteral I
                        callableReference I
                        functionLiteral I
                        objectLiteral I
                        collectionLiteral I
                        thisExpression I
                        superExpression I
                        ifExpression I
                        whenExpression I
                        tryExpression I
                        jumpExpression I
                        literalConstant I
                        simpleIdentifier
            }
            parenthesizedExpression IS { rule = '('.regularExpression * expression * ')'.regularExpression }
            collectionLiteral IS {
                rule = ('['.regularExpression * expression * (','.regularExpression * expression).zeroOrMore() * ','.zeroOrOne() * ']'.regularExpression) I
                        ('['.regularExpression * ']'.regularExpression)
            }
            literalConstant IS {
                rule = BooleanLiteral I
                        UnsignedLiteral I
                        LongLiteral I
                        HexLiteral I
                        BinLiteral I
                        CharacterLiteral I
                        RealLiteral I
                        "null".regularExpression I
                        IntegerLiteral
            }
            stringLiteral IS { rule = multiLineStringLiteral I lineStringLiteral }
            lineStringLiteral IS {
                automaticWhiteSpace = false
                rule = '"'.regularExpression * (lineStringContent I lineStringExpression).zeroOrMore() * '"'.regularExpression
                automaticWhiteSpace = true
            }

            multiLineStringLiteral IS {
                automaticWhiteSpace = false
                rule = "\"\"\"".regularExpression * (multiLineStringContent I multiLineStringExpression).zeroOrMore() * TRIPLE_QUOTE_CLOSE
                automaticWhiteSpace = true
            }
            lineStringContent IS { rule = LineStrText I LineStrEscapedChar I LineStrRef }
            lineStringExpression IS { rule = "\${".regularExpression * expression * '}'.regularExpression }
            multiLineStringContent IS { rule = MultiLineStrText I '"'.regularExpression.notFollowBy("\"\"".regularExpression + ('"'.allCharactersExcludeThis OR END_EXPRESSION)) I MultiLineStrRef }
            multiLineStringExpression IS { rule = "\${".regularExpression * expression * '}'.regularExpression }
            lambdaLiteral IS {
                rule = ('{'.regularExpression * statements * '}'.regularExpression) I
                        ('{'.regularExpression * lambdaParameters.zeroOrOne() * "->".regularExpression * statements * '}'.regularExpression)
            }
            lambdaParameters IS { rule = lambdaParameter * (','.regularExpression * lambdaParameter).zeroOrMore() * ','.zeroOrOne() }
            lambdaParameter IS { rule = (variableDeclaration I multiVariableDeclaration) * (':'.regularExpression * type).zeroOrOne() }
            anonymousFunction IS {
                rule = "fun".regularExpression * (type * '.'.regularExpression).zeroOrMore() * parametersWithOptionalType *
                       (':'.regularExpression * type).zeroOrOne() * typeConstraints.zeroOrOne() *
                       functionBody.zeroOrOne()
            }
            functionLiteral IS { rule = lambdaLiteral I anonymousFunction }
            objectLiteral IS {
                rule = ("object".regularExpression * ':'.regularExpression * delegationSpecifiers * classBody) I
                        ("object".regularExpression * classBody)
            }
            thisExpression IS { rule = THIS_AT I "this".regularExpression }
            superExpression IS {
                rule = "super".regularExpression * ('<'.regularExpression * type * '>'.regularExpression).zeroOrOne() * ('@'.regularExpression * simpleIdentifier).zeroOrOne()
            }
            ifExpression IS {
                rule = ("if".regularExpression * '('.regularExpression * expression * ')'.regularExpression *
                        controlStructureBody.zeroOrOne() * ';'.zeroOrOne() * "else".regularExpression * (controlStructureBody I ';'.regularExpression)) I
                        ("if".regularExpression * '('.regularExpression * expression * ')'.regularExpression *
                         (controlStructureBody I ';'.regularExpression).zeroOrMore())
            }
            whenSubject IS { rule = '('.regularExpression * (annotation.zeroOrMore() * ("val".regularExpression + WHITE_SPACE) * variableDeclaration * '='.regularExpression).zeroOrOne() * expression * ')'.regularExpression }
            whenExpression IS { rule = "when".regularExpression * whenSubject.zeroOrOne() * '{'.regularExpression * whenEntry.zeroOrMore() * '}'.regularExpression }
            whenEntry IS {
                rule = ("else".regularExpression * "->".regularExpression * controlStructureBody * WHITE_SPACES) I
                        (whenCondition * (','.regularExpression * whenCondition).zeroOrMore() * ','.zeroOrOne() * "->".regularExpression * controlStructureBody * WHITE_SPACES)
            }
            whenCondition IS { rule = rangeTest I typeTest I expression }
            rangeTest IS { rule = inOperator * expression }
            typeTest IS { rule = isOperator * type }
            tryExpression IS { rule = "try".regularExpression * block * ((catchBlock.oneOrMore() * finallyBlock.zeroOrOne()) I finallyBlock) }
            catchBlock IS { rule = "catch".regularExpression * '('.regularExpression * annotation.zeroOrMore() * simpleIdentifier * ':'.regularExpression * type * ','.zeroOrOne() * ')'.regularExpression * block * WHITE_SPACES }
            finallyBlock IS { rule = "finally".regularExpression * block }
            jumpExpression IS {
                rule = ("throw".regularExpression * expression) I
                        ((RETURN_AT I "return".regularExpression) * expression.zeroOrOne()) I
                        CONTINUE_AT I
                        "continue".regularExpression I
                        BREAK_AT I
                        "break".regularExpression
            }
            callableReference IS { rule = receiverType.zeroOrOne() * "::".regularExpression * ("class".regularExpression I simpleIdentifier) }
            assignmentAndOperator IS { rule = +(charArrayOf('+', '-', '*', '/', '%').regularExpression + '=') }
            equalityOperator IS { rule = +(charArrayOf('!', '=').regularExpression + '=' + '='.zeroOrOne()) }
            comparisonOperator IS { rule = +(charArrayOf('<', '>').regularExpression + '='.zeroOrOne()) }
            inOperator IS { rule = +('!'.zeroOrOne() + "in".regularExpression) }
            isOperator IS { rule = +('!'.zeroOrOne() + "is".regularExpression) }
            additiveOperator IS { rule = +charArrayOf('+', '-').regularExpression.notFollowBy('>'.regularExpression) }
            multiplicativeOperator IS { rule = +charArrayOf('*', '/', '%').regularExpression }
            asOperator IS { rule = +("as".regularExpression + '?'.zeroOrOne()) }
            prefixUnaryOperator IS { rule = +("++".regularExpression OR "--".regularExpression OR '!'.regularExpression OR "+".regularExpression OR "-".regularExpression) }
            postfixUnaryOperator IS { rule = +("++".regularExpression OR "--".regularExpression OR "!!".regularExpression) }
            memberAccessOperator IS { rule = +('.'.regularExpression OR "?.".regularExpression OR "::".regularExpression) }

            // Modifiers

            modifiers IS { rule = annotation I modifier.oneOrMore() }
            parameterModifiers IS { rule = annotation I parameterModifier.oneOrMore() }
            modifier IS { rule = classModifier I memberModifier I visibilityModifier I functionModifier I propertyModifier I inheritanceModifier I parameterModifier I platformModifier }
            typeModifiers IS { rule = typeModifier.oneOrMore() }
            typeModifier IS { rule = annotation I ("suspend".regularExpression * WHITE_SPACES) }
            classModifier IS { rule = +("enum".regularExpression OR "sealed" OR "annotation" OR "data" OR "inner") * WHITE_SPACES }
            memberModifier IS { rule = +("override".regularExpression OR "lateinit") * WHITE_SPACES }
            varianceModifier IS { rule = +("in".regularExpression OR "out") * WHITE_SPACES }
            typeParameterModifiers IS { rule = typeParameterModifier.oneOrMore() }
            typeParameterModifier IS { rule = reificationModifier I varianceModifier I annotation }
            functionModifier IS { rule = +("tailrec".regularExpression OR "operator" OR "infix" OR "inline" OR "external" OR "suspend") * WHITE_SPACES }
            propertyModifier IS { rule = +"const".regularExpression * WHITE_SPACES }
            inheritanceModifier IS { rule = +("abstract".regularExpression OR "final" OR "open") * WHITE_SPACES }
            parameterModifier IS { rule = +("vararg".regularExpression OR "noinline" OR "crossinline") * WHITE_SPACES }
            reificationModifier IS { rule = +"reified".regularExpression * WHITE_SPACES }
            platformModifier IS { rule = +("expect".regularExpression OR "actual") * WHITE_SPACES }
            visibilityModifier IS { rule = +("public".regularExpression OR "private" OR "internal" OR "protected") * WHITE_SPACES }

            // Annotations

            annotation IS { rule = singleAnnotation I multiAnnotation }
            singleAnnotation IS {
                rule = (annotationUseSiteTarget * unescapedAnnotation) I
                        ('@'.regularExpression * unescapedAnnotation)
            }
            multiAnnotation IS {
                rule = (annotationUseSiteTarget * '['.regularExpression * unescapedAnnotation.oneOrMore() * ']'.regularExpression) I
                        ('@'.regularExpression * '['.regularExpression * unescapedAnnotation.oneOrMore() * ']'.regularExpression)
            }
            annotationUseSiteTarget IS {
                rule = '@'.regularExpression *
                       ("field".regularExpression OR "property" OR "get" OR "set" OR "receiver" OR "param" OR "setparam" OR "delegate") * ':'.regularExpression
            }
            unescapedAnnotation IS { rule = constructorInvocation I userType }

            // Identifiers

            simpleIdentifier IS {
                rule = Identifier I ("abstract".regularExpression OR "annotation" OR "by" OR "catch" OR "companion" OR
                        "constructor" OR "crossinline" OR "data" OR "dynamic" OR "enum" OR "external" OR "final" OR
                        "finally" OR "get" OR "import" OR "infix" OR "init" OR "inline" OR "inner" OR "internal" OR
                        "lateinit" OR "noinline" OR "open" OR "operator" OR "out" OR "override" OR "private" OR
                        "protected" OR "public" OR "reified" OR "sealed" OR "tailrec" OR "set" OR "vararg" OR "where" OR
                        "field" OR "property" OR "receiver" OR "param" OR "setparam" OR "delegate" OR "file" OR
                        "expect" OR "actual" OR "const" OR "suspend")
            }
            identifier IS { rule = simpleIdentifier * ('.'.regularExpression * simpleIdentifier).zeroOrMore() }

            // Lexical grammar

            // LOOK strange !!!
            DelimitedComment IS { rule = +("/*".regularExpression + ('*'.allCharactersExcludeThis OR ('*' + '/'.allCharactersExcludeThis)).zeroOrMore() + "*/") }
            LineComment IS { rule = +("//".regularExpression + ANY.zeroOrMore() + '\n') }
            WS IS { rule = +WHITE_SPACE.oneOrMore() }
            Hidden IS { rule = DelimitedComment I LineComment I WS }

            // Separators and operations

            RESERVED IS { rule = +"...".regularExpression }
            DOUBLE_ARROW IS { rule = +"=>".regularExpression }
            DOUBLE_SEMICOLON IS { rule = +";;".regularExpression }
            HASH IS { rule = +'#'.regularExpression }
            SINGLE_QUOTE IS { rule = +'\''.regularExpression }

            // Keywords

            RETURN_AT IS { rule = "return@".regularExpression * Identifier }
            CONTINUE_AT IS { rule = "continue@".regularExpression * Identifier }
            BREAK_AT IS { rule = "break@".regularExpression * Identifier }
            THIS_AT IS { rule = "this@".regularExpression * Identifier }
            SUPER_AT IS { rule = "super@".regularExpression * Identifier }
            TYPEOF IS { rule = +"typeof".regularExpression }

            // Literals

            DecDigit IS { rule = +interval('0', '9').regularExpression }
            DecDigitNoZero IS { rule = +interval('1', '9').regularExpression }
            DecDigitOrSeparator IS { rule = +(interval('0', '9') + '_').regularExpression }
            DecDigits IS { rule = (DecDigit * DecDigitOrSeparator.zeroOrMore()) I DecDigit }
            DoubleExponent IS {
                rule = (charArrayOf('e', 'E').regularExpression +
                        charArrayOf('+', '-').zeroOrOne()) * DecDigits
            }
            RealLiteral IS { rule = FloatLiteral I DoubleLiteral }
            FloatLiteral IS {
                rule = (DoubleLiteral * charArrayOf('f', 'F').regularExpression) I
                        (DecDigits * charArrayOf('f', 'F').regularExpression)
            }
            DoubleLiteral IS {
                rule = ('-'.zeroOrOne() * DecDigits.zeroOrOne() * '.'.regularExpression * DecDigits * DoubleExponent.zeroOrOne()) I
                        ('-'.zeroOrOne() * DecDigits * DoubleExponent)
            }
            IntegerLiteral IS { rule = ('-'.zeroOrOne() * DecDigitNoZero * DecDigitOrSeparator.zeroOrMore()) I ('-'.zeroOrOne() * DecDigit) }
            HexDigit IS { rule = +(interval('0', '9') + interval('a', 'f') + interval('A', 'F')).regularExpression }
            HexDigitOrSeparator IS { rule = HexDigit I '_'.regularExpression }
            HexLiteral IS {
                rule = (('0'.regularExpression + charArrayOf('x',
                                                             'X')) * HexDigit * HexDigitOrSeparator.zeroOrMore()) I
                        (('0'.regularExpression + charArrayOf('x', 'X')) * HexDigit)
            }
            BinDigit IS { rule = +charArrayOf('0', '1').regularExpression }
            BinDigitOrSeparator IS { rule = BinDigit I '_'.regularExpression }
            BinLiteral IS {
                rule = (('0'.regularExpression + charArrayOf('b',
                                                             'B')) * BinDigit * BinDigitOrSeparator.zeroOrMore()) I
                        (('0'.regularExpression + charArrayOf('b', 'B')) * BinDigit)
            }
            UnsignedLiteral IS {
                rule = (HexLiteral I BinLiteral I IntegerLiteral) *
                       (charArrayOf('u', 'U').regularExpression
                            .followBy(charArrayOf('l', 'L', ' ', '\n', '\t').regularExpression OR END_EXPRESSION) +
                        charArrayOf('l', 'L').zeroOrOne()
                            .notFollowBy(LETTER_OR_DIGIT))
            }
            LongLiteral IS {
                rule = (HexLiteral I BinLiteral I IntegerLiteral) *
                       charArrayOf('l', 'L').regularExpression
                           .notFollowBy(LETTER_OR_DIGIT)
            }
            BooleanLiteral IS { rule = +("true".regularExpression OR "false") }
            CharacterLiteral IS {
                rule = '\''.regularExpression * (EscapeSeq I
                        charArrayOf('\n', '\r', '\\', '\'').allCharactersExcludeThose) *
                       '\''.regularExpression
            }

            // Identifiers

            UnicodeDigit IS { rule = +UNICODE_CLASS_ND }
            Identifier IS {
                rule = +(((Letter + '_').regularExpression + (Letter + '_' + unicodeClassND).zeroOrMore()) OR
                        ('`'.regularExpression + charArrayOf('\n', '\r', '\'').allCharactersExcludeThose + '`'))
            }

            // Kotlin/JVM (any declaration publicity)
            // ~ ( [\r\n] | '`' | '.' | ';' | ':' | '\' | '/' | '[' | ']' | '<' | '>' )

            IdentifierOrSoftKey IS {
                rule = Identifier I ("abstract".regularExpression OR "annotation" OR "by" OR "catch" OR "companion" OR
                        "constructor" OR "crossinline" OR "data" OR "dynamic" OR "enum" OR "external" OR "final" OR
                        "finally" OR "import" OR "infix" OR "init" OR "inline" OR "inner" OR "internal" OR "lateinit" OR
                        "noinline" OR "open" OR "operator" OR "out" OR "override" OR "private" OR "protected" OR
                        "public" OR "reified" OR "sealed" OR "tailrec" OR "vararg" OR "where" OR "get" OR "set" OR
                        "field" OR "property" OR "receiver" OR "param" OR "setparam" OR "delegate" OR "file" OR
                        "expect" OR "actual" OR "const" OR "suspend")
            }
            FieldIdentifier IS {
                automaticWhiteSpace = false
                rule = '$'.regularExpression * IdentifierOrSoftKey
                automaticWhiteSpace = true
            }
            UniCharacterLiteral IS {
                rule = +("\\u".regularExpression +
                         (interval('0', '9') +
                          interval('a', 'f') +
                          interval('A', 'F')).exactTimes(4))
            }
            EscapedIdentifier IS {
                rule = +('\\'.regularExpression +
                         charArrayOf('t', 'b', 'r', 'n', '\'', '"', '\\', '$'))
            }
            EscapeSeq IS { rule = UniCharacterLiteral I EscapedIdentifier }

            // Strings

            LineStrRef IS { rule = +FieldIdentifier }

            // See String templates

            LineStrText IS {
                rule = +("\\$".regularExpression OR
                        charArrayOf('\\', '"', '$').allCharactersExcludeThose).oneOrMore()
            }
            LineStrEscapedChar IS { rule = EscapedIdentifier I UniCharacterLiteral }
            TRIPLE_QUOTE_CLOSE IS { rule = +('"'.zeroOrOne() + "\"\"\"") }
            MultiLineStrRef IS { rule = +FieldIdentifier }
            MultiLineStrText IS {
                rule = +("\\$".regularExpression OR charArrayOf('"',
                                                                '$').allCharactersExcludeThose).oneOrMore()
            }
            ErrorCharacter IS { rule = +ANY }

            /*
             * Kotlin lexical grammar in ANTLR4 notation (Unicode classes)
             *
             * Taken from http://www.antlr3.org/grammar/1345144569663/AntlrUnicode.txt
             */

            UNICODE_CLASS_LL IS { rule = +unicodeClassLL.regularExpression }
            UNICODE_CLASS_LM IS { rule = +unicodeClassLM.regularExpression }
            UNICODE_CLASS_LO IS { rule = +unicodeClassLO.regularExpression }
            UNICODE_CLASS_LT IS { rule = +unicodeClassLT.regularExpression }
            UNICODE_CLASS_LU IS { rule = +unicodeClassLU.regularExpression }
            UNICODE_CLASS_ND IS { rule = +unicodeClassND.regularExpression }
            UNICODE_CLASS_NL IS { rule = +unicodeClassNL.regularExpression }
        }
    }

    fun parse(inputStream: InputStream): GrammarNode?
    {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line = bufferedReader.readLine()

        while (line != null)
        {
            stringBuilder.append(line)
            stringBuilder.append('\n')
            line = bufferedReader.readLine()
        }

        bufferedReader.close()
        return this.parse(stringBuilder.toString())
    }

    fun parse(text: String): GrammarNode? =
        this.grammar.parse(text)

    @TestOnly
    fun parseSpecificRule(text: String, ruleName: String): GrammarNode? =
        this.grammar.parseSpecificRule(text, ruleName)
}
