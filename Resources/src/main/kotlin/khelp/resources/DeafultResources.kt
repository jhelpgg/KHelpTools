package khelp.resources

import khelp.io.ClassSource

val defaultResources : Resources by lazy { Resources(ClassSource(Resources::class.java)) }

val defaultTexts : ResourcesText by lazy { defaultResources.resourcesText("defaultTexts") }

const val YES = "yes"

const val NO = "no"

const val CANCEL = "cancel"

const val SAVE = "save"

const val SAVE_AS = "saveAs"

const val LOAD = "load"

const val OTHER = "other"
