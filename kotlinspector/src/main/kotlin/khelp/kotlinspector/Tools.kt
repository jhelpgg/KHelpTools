package khelp.kotlinspector

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.declaration.ClassDeclaration
import khelp.kotlinspector.model.declaration.Declaration
import khelp.kotlinspector.model.declaration.FunctionDeclaration
import khelp.kotlinspector.model.declaration.FunctionValueParameter
import khelp.kotlinspector.model.declaration.ObjectDeclaration
import khelp.kotlinspector.model.declaration.ParameterWithOptionalType
import khelp.kotlinspector.model.declaration.PropertyDeclaration
import khelp.kotlinspector.model.declaration.TypeAliasDeclaration
import khelp.kotlinspector.model.declaration.VariableDeclaration
import khelp.kotlinspector.model.declaration.classinfo.ClassMemberDeclaration
import khelp.kotlinspector.model.delegation.AnnotatedDelegationSpecifier
import khelp.kotlinspector.model.expression.ValueArgument
import khelp.kotlinspector.model.modifier.ClassModifier
import khelp.kotlinspector.model.modifier.FunctionModifier
import khelp.kotlinspector.model.modifier.InheritanceModifier
import khelp.kotlinspector.model.modifier.MemberModifier
import khelp.kotlinspector.model.modifier.Modifier
import khelp.kotlinspector.model.modifier.ParameterModifier
import khelp.kotlinspector.model.modifier.PlatformModifier
import khelp.kotlinspector.model.modifier.PropertyModifier
import khelp.kotlinspector.model.modifier.VisibilityModifier
import khelp.kotlinspector.model.statement.Statement
import khelp.kotlinspector.model.type.TypeConstraint
import khelp.kotlinspector.model.type.TypeModifier
import khelp.kotlinspector.model.typeparameter.TypeParameter
import khelp.kotlinspector.model.typeparameter.TypeParameterModifier

private const val modifiersList = "${KotlinGrammar.modifiers}/1"

internal fun fillModifiersAndReturnAnnotation(grammarNodeParent: GrammarNode,
                                              modifierList: MutableList<Modifier>): String
{
    val annotation = grammarNodeParent.searchByFirstHeap(KotlinGrammar.annotation)?.text ?: ""
    modifierList.clear()
    val modifierListNode = grammarNodeParent.searchByFirstHeap(modifiersList) ?: return annotation

    for (modifierNode in modifierListNode)
    {
        modifierList.add(parseModifierInformation(modifierNode))
    }

    return annotation
}

internal fun parseModifierInformation(modifierNode: GrammarNode): Modifier
{
    var node = modifierNode.searchByFirstHeap(KotlinGrammar.classModifier)

    if (node != null)
    {
        val modifier = ClassModifier()
        modifier.parse(node)
        return modifier
    }

    node = modifierNode.searchByFirstHeap(KotlinGrammar.memberModifier)

    if (node != null)
    {
        val modifier = MemberModifier()
        modifier.parse(node)
        return modifier
    }

    node = modifierNode.searchByFirstHeap(KotlinGrammar.visibilityModifier)

    if (node != null)
    {
        val modifier = VisibilityModifier()
        modifier.parse(node)
        return modifier
    }

    node = modifierNode.searchByFirstHeap(KotlinGrammar.functionModifier)

    if (node != null)
    {
        val modifier = FunctionModifier()
        modifier.parse(node)
        return modifier
    }

    node = modifierNode.searchByFirstHeap(KotlinGrammar.propertyModifier)

    if (node != null)
    {
        val modifier = PropertyModifier()
        modifier.parse(node)
        return modifier
    }

    node = modifierNode.searchByFirstHeap(KotlinGrammar.inheritanceModifier)

    if (node != null)
    {
        val modifier = InheritanceModifier()
        modifier.parse(node)
        return modifier
    }

    node = modifierNode.searchByFirstHeap(KotlinGrammar.parameterModifier)

    if (node != null)
    {
        val modifier = ParameterModifier()
        modifier.parse(node)
        return modifier
    }

    node = modifierNode.searchByFirstHeap(KotlinGrammar.platformModifier)

    if (node != null)
    {
        val modifier = PlatformModifier()
        modifier.parse(node)
        return modifier
    }

    throw IllegalArgumentException("No modifier type !!!")
}

internal fun parseDeclarationInformation(declarationNode: GrammarNode): Declaration
{
    val declaration =
        when (declarationNode.rule)
        {
            KotlinGrammar.classDeclaration    -> ClassDeclaration()
            KotlinGrammar.objectDeclaration   -> ObjectDeclaration()
            KotlinGrammar.functionDeclaration -> FunctionDeclaration()
            KotlinGrammar.propertyDeclaration -> PropertyDeclaration()
            KotlinGrammar.typeAlias           -> TypeAliasDeclaration()
            else                              -> throw IllegalArgumentException("No declaration defined")

        }

    declaration.parse(declarationNode)
    return declaration
}

internal fun fillTypeParameters(grammarNodeParent: GrammarNode, typeParameterList: MutableList<TypeParameter>)
{
    typeParameterList.clear()
    val typeParameterListNode = grammarNodeParent.searchByFirstHeap(KotlinGrammar.typeParameters) ?: return
    typeParameterListNode.forEachDeep(KotlinGrammar.typeParameter) { typeParameterNode ->
        val typeParameter = TypeParameter()
        typeParameter.parse(typeParameterNode)
        typeParameterList.add(typeParameter)
    }
}

internal fun fillTypeParameterModifiers(grammarNodeParent: GrammarNode,
                                        typeParameterModifierList: MutableList<TypeParameterModifier>)
{
    typeParameterModifierList.clear()
    val typeParameterModifierListNode = grammarNodeParent.searchByFirstHeap(KotlinGrammar.typeParameterModifiers)
                                        ?: return

    for (typeParameterModifierNode in typeParameterModifierListNode)
    {
        val typeParameterModifier = TypeParameterModifier()
        typeParameterModifier.parse(typeParameterModifierNode)
        typeParameterModifierList.add(typeParameterModifier)
    }
}

fun fillDelegationSpecifiers(grammarNodeParent: GrammarNode,
                             delegationSpecifiersList: MutableList<AnnotatedDelegationSpecifier>)
{
    delegationSpecifiersList.clear()
    val delegationSpecifiersListNode = grammarNodeParent.searchByFirstHeap(KotlinGrammar.delegationSpecifiers) ?: return
    delegationSpecifiersListNode.forEachDeep(KotlinGrammar.annotatedDelegationSpecifier) { annotatedDelegationSpecifierNode ->
        val annotatedDelegationSpecifier = AnnotatedDelegationSpecifier()
        annotatedDelegationSpecifier.parse(annotatedDelegationSpecifierNode)
        delegationSpecifiersList.add(annotatedDelegationSpecifier)
    }
}

fun fillTypeConstraints(grammarNodeParent: GrammarNode, typeConstraints: MutableList<TypeConstraint>)
{
    typeConstraints.clear()
    val typeConstraintsNode = grammarNodeParent.searchByFirstHeap(KotlinGrammar.typeConstraints) ?: return
    typeConstraintsNode.forEachDeep(KotlinGrammar.typeConstraint) { typeConstraintNode ->
        val typeConstraint = TypeConstraint()
        typeConstraint.parse(typeConstraintNode)
        typeConstraints.add(typeConstraint)
    }
}

fun fillClassMemberDeclarations(grammarNodeParent: GrammarNode,
                                classMemberDeclarations: MutableList<ClassMemberDeclaration>)
{
    classMemberDeclarations.clear()
    val classMemberDeclarationsNode = grammarNodeParent.searchByFirstHeap(KotlinGrammar.classMemberDeclarations)
                                      ?: return
    classMemberDeclarationsNode.forEachDeep(KotlinGrammar.classMemberDeclaration) { classMemberDeclarationNode ->
        val classMemberDeclaration = ClassMemberDeclaration()
        classMemberDeclaration.parse(classMemberDeclarationNode)
        classMemberDeclarations.add(classMemberDeclaration)
    }
}

fun fillVariableDeclarations(grammarNodeParent: GrammarNode,
                             variableDeclarations: MutableList<VariableDeclaration>)
{
    variableDeclarations.clear()
    grammarNodeParent.forEachDeep(KotlinGrammar.variableDeclaration) { variableDeclarationNode ->
        val variableDeclaration = VariableDeclaration()
        variableDeclaration.parse(variableDeclarationNode)
        variableDeclarations.add(variableDeclaration)
    }
}

fun fillValueArguments(grammarNodeParent: GrammarNode,
                       valueArguments: MutableList<ValueArgument>)
{
    valueArguments.clear()
    grammarNodeParent.forEachDeep(KotlinGrammar.valueArgument) { valueArgumentNode ->
        val valueArgument = ValueArgument()
        valueArgument.parse(valueArgumentNode)
        valueArguments.add(valueArgument)
    }
}

fun fillFunctionValueParameters(grammarNodeParent: GrammarNode,
                                functionValueParameters: MutableList<FunctionValueParameter>)
{
    functionValueParameters.clear()
    grammarNodeParent.forEachDeep(KotlinGrammar.functionValueParameter) { functionValueParameterNode ->
        val functionValueParameter = FunctionValueParameter()
        functionValueParameter.parse(functionValueParameterNode)
        functionValueParameters.add(functionValueParameter)
    }
}

fun fillParameterWithOptionalTypes(grammarNodeParent: GrammarNode,
                                   parameterWithOptionalTypes: MutableList<ParameterWithOptionalType>)
{
    parameterWithOptionalTypes.clear()
    grammarNodeParent.forEachDeep(KotlinGrammar.parameterWithOptionalType) { parameterWithOptionalTypeNode ->
        val parameterWithOptionalType = ParameterWithOptionalType()
        parameterWithOptionalType.parse(parameterWithOptionalTypeNode)
        parameterWithOptionalTypes.add(parameterWithOptionalType)
    }
}

fun fillTypeModifiers(grammarNodeParent: GrammarNode,
                      typeModifiers: MutableList<TypeModifier>)
{
    typeModifiers.clear()
    grammarNodeParent.forEachDeep(KotlinGrammar.typeModifier) { typeModifierNode ->
        val typeModifier = TypeModifier()
        typeModifier.parse(typeModifierNode)
        typeModifiers.add(typeModifier)
    }
}

fun fillStatements(grammarNodeParent: GrammarNode,
                   statements: MutableList<Statement>)
{
    statements.clear()
    grammarNodeParent.forEachDeep(KotlinGrammar.statement) { statementNode ->
        val statement = Statement()
        statement.parse(statementNode)
        statements.add(statement)
    }
}
