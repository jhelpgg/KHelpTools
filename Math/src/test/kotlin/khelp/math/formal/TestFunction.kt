package khelp.math.formal

import khelp.utilities.log.debug
import khelp.utilities.log.todo
import khelp.utilities.math.EPSILON
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestFunction
{
    @Test
    fun testParser()
    {
        var function = "3".toFunction()
        Assertions.assertTrue(function is Constant)
        Assertions.assertEquals(3.0, (function as Constant).real, EPSILON)

        function = "X+Y".toFunction()
        Assertions.assertTrue(function is Addition)
        function as Addition
        Assertions.assertEquals(Variable("X"), function.parameter1)
        Assertions.assertEquals(Variable("Y"), function.parameter2)

        function = "zorro".toFunction()
        Assertions.assertTrue(function is Variable)
        function as Variable
        Assertions.assertEquals("zorro", function.name)

        function = "cos(t+PI/2)".toFunction()
        Assertions.assertTrue(function is Cosinus)
        function as Cosinus
        function = function.parameter
        Assertions.assertTrue(function is Addition)
        function as Addition
        Assertions.assertTrue(function.parameter1 is Variable)
        Assertions.assertEquals("t", (function.parameter1 as Variable).name)
        function = function.parameter2
        Assertions.assertTrue(function is Division)
        function as Division
        Assertions.assertTrue(function.parameter1 is Variable)
        Assertions.assertEquals("PI", (function.parameter1 as Variable).name)
        Assertions.assertTrue(function.parameter2 is Constant)
        Assertions.assertEquals(2.0, (function.parameter2 as Constant).real, EPSILON)

        function = "   \t    \n cos    \n    \r (   t  + \n\tPI  \t/\n2\n    )    ".toFunction()
        Assertions.assertTrue(function is Cosinus)
        function as Cosinus
        function = function.parameter
        Assertions.assertTrue(function is Addition)
        function as Addition
        Assertions.assertTrue(function.parameter1 is Variable)
        Assertions.assertEquals("t", (function.parameter1 as Variable).name)
        function = function.parameter2
        Assertions.assertTrue(function is Division)
        function as Division
        Assertions.assertTrue(function.parameter1 is Variable)
        Assertions.assertEquals("PI", (function.parameter1 as Variable).name)
        Assertions.assertTrue(function.parameter2 is Constant)
        Assertions.assertEquals(2.0, (function.parameter2 as Constant).real, EPSILON)

        //...
        todo("Write more tests")
    }

    private fun assertSimplify(expected: String, complex: String)
    {
        Assertions.assertEquals(expected.toFunction(), complex.toFunction()(System.out))
    }

    @Test
    fun testSimplify()
    {
        this.assertSimplify("y", "x+y-x")
        this.assertSimplify("0", "x+y+z+a+b+c+d+e+f-x-y-z-a-b-c-d-e-f")
        this.assertSimplify("2 * (x-a)", "x-a + x-a")
        this.assertSimplify("1", "cos(vis)*cos(vis)+sin(vis)*sin(vis)")
        this.assertSimplify(Math.PI.toString(), "PI")
        this.assertSimplify("y +(z +(6 * x))", "2*x+y+2*x+z+2*x")
        //        this.assertSimplify("6*x+y+z", "3*x+y+2*x+z+x")

        //...
        todo("Write more tests")
    }

    @Test
    fun fret()
    {
        val x = Variable("x")
        val y = Variable("y")
        val f = x + 3
        val f2 = (f * (f + y)) + x
        debug(f2)
        debug(f2.simplifyMaximum(System.out))
        val f3 = f2.replace(x, 42.0)
        debug(f3)
        debug(f3.simplifyMaximum(System.out))
        val f4 = f2.replace(y, 73.0)
        debug(f4)
        debug(f4.simplifyMaximum(System.out))
        val f5 = f3.replace(y, 73.0)
        debug(f5)
        debug(f5.simplifyMaximum(System.out))
    }
}