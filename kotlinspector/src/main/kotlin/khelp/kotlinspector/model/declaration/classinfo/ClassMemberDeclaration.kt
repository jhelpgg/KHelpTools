package khelp.kotlinspector.model.declaration.classinfo

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.declaration.Declaration
import khelp.kotlinspector.parseDeclarationInformation

class ClassMemberDeclaration
{
    var comments = ""
        private set
    var declaration: Declaration? = null
        private set
    var companionObject: CompanionObject? = null
        private set
    var anonymousInitializer: AnonymousInitializer? = null
        private set
    var secondaryConstructor: SecondaryConstructor? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.comments = grammarNode[0].text
        val classMemberDeclarationNature = grammarNode[2][0]
        this.declaration = null
        this.companionObject = null
        this.anonymousInitializer = null
        this.secondaryConstructor = null

        when (classMemberDeclarationNature.rule)
        {
            KotlinGrammar.declaration          ->
                this.declaration = parseDeclarationInformation(classMemberDeclarationNature[2][0])
            KotlinGrammar.companionObject      ->
            {
                this.companionObject = CompanionObject()
                this.companionObject?.parse(classMemberDeclarationNature)
            }
            KotlinGrammar.anonymousInitializer ->
            {
                this.anonymousInitializer = AnonymousInitializer()
                this.anonymousInitializer?.parse(classMemberDeclarationNature)
            }
            KotlinGrammar.secondaryConstructor ->
            {
                this.secondaryConstructor = SecondaryConstructor()
                this.secondaryConstructor?.parse(classMemberDeclarationNature)
            }
        }
    }
}