package khelp.neural

fun main()
{
    val network = Network(2, 3, 1)
    val source = Vector(2)
    source.vector[0] = 1.0
    source.vector[1] = 1.0
    println("---source---\n$source")
    val predict = network.predict(source)
    println("---predict---\n$predict")
    var expected = Vector(1)
    expected.vector[0] = 1.0
    println("---expected---\n$expected")
    val distance = network.train(source, expected)
    println("distance = \n$distance")
}

