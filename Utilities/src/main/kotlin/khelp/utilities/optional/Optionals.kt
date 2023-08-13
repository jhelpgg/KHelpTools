package khelp.utilities.optional

val <T : Any> Optional<Optional<T>>.reduce : Optional<T>
    get() =
        this.ifPresentElse({ value -> value }, { Optional.empty() })
