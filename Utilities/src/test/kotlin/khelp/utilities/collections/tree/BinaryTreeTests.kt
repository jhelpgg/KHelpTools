package khelp.utilities.collections.tree

import khelp.utilities.collections.dsl.BinaryTree
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BinaryTreeTests
{
    @Test
    fun visitLowDeepTest()
    {
        val binaryTree =
            BinaryTree<Int, String> {
                0 IS "Zero"
                1 IS "One"
                2 IS "Two"
                3 IS "Three"
                4 IS "Four"
                5 IS "Five"
                6 IS "Six"
            }

        val collector = ArrayList<MutableMap.MutableEntry<Int, String>>()
        binaryTree.visitLowDeep { entry ->
            collector.add(entry)
            true
        }
        Assertions.assertEquals(7, collector.size)
        Assertions.assertEquals(3, collector[0].key)
        Assertions.assertEquals("Three", collector[0].value)
        Assertions.assertEquals(1, collector[1].key)
        Assertions.assertEquals("One", collector[1].value)
        Assertions.assertEquals(0, collector[2].key)
        Assertions.assertEquals("Zero", collector[2].value)
        Assertions.assertEquals(2, collector[3].key)
        Assertions.assertEquals("Two", collector[3].value)
        Assertions.assertEquals(5, collector[4].key)
        Assertions.assertEquals("Five", collector[4].value)
        Assertions.assertEquals(4, collector[5].key)
        Assertions.assertEquals("Four", collector[5].value)
        Assertions.assertEquals(6, collector[6].key)
        Assertions.assertEquals("Six", collector[6].value)
    }
    @Test
    fun visitLowDeepestTest()
    {
        val binaryTree =
            BinaryTree<Int, String> {
                0 IS "Zero"
                1 IS "One"
                2 IS "Two"
                3 IS "Three"
                4 IS "Four"
                5 IS "Five"
                6 IS "Six"
            }

        val collector = ArrayList<MutableMap.MutableEntry<Int, String>>()
        binaryTree.visitLowDeepest { entry ->
            collector.add(entry)
            true
        }
        Assertions.assertEquals(7, collector.size)
        Assertions.assertEquals(0, collector[0].key)
        Assertions.assertEquals("Zero", collector[0].value)
        Assertions.assertEquals(2, collector[1].key)
        Assertions.assertEquals("Two", collector[1].value)
        Assertions.assertEquals(1, collector[2].key)
        Assertions.assertEquals("One", collector[2].value)
        Assertions.assertEquals(4, collector[3].key)
        Assertions.assertEquals("Four", collector[3].value)
        Assertions.assertEquals(6, collector[4].key)
        Assertions.assertEquals("Six", collector[4].value)
        Assertions.assertEquals(5, collector[5].key)
        Assertions.assertEquals("Five", collector[5].value)
        Assertions.assertEquals(3, collector[6].key)
        Assertions.assertEquals("Three", collector[6].value)
    }
    @Test
    fun visitHighDeepTest()
    {
        val binaryTree =
            BinaryTree<Int, String> {
                0 IS "Zero"
                1 IS "One"
                2 IS "Two"
                3 IS "Three"
                4 IS "Four"
                5 IS "Five"
                6 IS "Six"
            }

        val collector = ArrayList<MutableMap.MutableEntry<Int, String>>()
        binaryTree.visitHighDeep { entry ->
            collector.add(entry)
            true
        }
        Assertions.assertEquals(7, collector.size)
        Assertions.assertEquals(3, collector[0].key)
        Assertions.assertEquals("Three", collector[0].value)
        Assertions.assertEquals(5, collector[1].key)
        Assertions.assertEquals("Five", collector[1].value)
        Assertions.assertEquals(6, collector[2].key)
        Assertions.assertEquals("Six", collector[2].value)
        Assertions.assertEquals(4, collector[3].key)
        Assertions.assertEquals("Four", collector[3].value)
        Assertions.assertEquals(1, collector[4].key)
        Assertions.assertEquals("One", collector[4].value)
        Assertions.assertEquals(2, collector[5].key)
        Assertions.assertEquals("Two", collector[5].value)
        Assertions.assertEquals(0, collector[6].key)
        Assertions.assertEquals("Zero", collector[6].value)
    }
    @Test
    fun visitHighDeepestTest()
    {
        val binaryTree =
            BinaryTree<Int, String> {
                0 IS "Zero"
                1 IS "One"
                2 IS "Two"
                3 IS "Three"
                4 IS "Four"
                5 IS "Five"
                6 IS "Six"
            }

        val collector = ArrayList<MutableMap.MutableEntry<Int, String>>()
        binaryTree.visitHighDeepest { entry ->
            collector.add(entry)
            true
        }
        Assertions.assertEquals(7, collector.size)
        Assertions.assertEquals(6, collector[0].key)
        Assertions.assertEquals("Six", collector[0].value)
        Assertions.assertEquals(4, collector[1].key)
        Assertions.assertEquals("Four", collector[1].value)
        Assertions.assertEquals(5, collector[2].key)
        Assertions.assertEquals("Five", collector[2].value)
        Assertions.assertEquals(2, collector[3].key)
        Assertions.assertEquals("Two", collector[3].value)
        Assertions.assertEquals(0, collector[4].key)
        Assertions.assertEquals("Zero", collector[4].value)
        Assertions.assertEquals(1, collector[5].key)
        Assertions.assertEquals("One", collector[5].value)
        Assertions.assertEquals(3, collector[6].key)
        Assertions.assertEquals("Three", collector[6].value)
    }
    @Test
    fun visitLowFirstTest()
    {
        val binaryTree =
            BinaryTree<Int, String> {
                0 IS "Zero"
                1 IS "One"
                2 IS "Two"
                3 IS "Three"
                4 IS "Four"
                5 IS "Five"
                6 IS "Six"
            }

        val collector = ArrayList<MutableMap.MutableEntry<Int, String>>()
        binaryTree.visitLowFirst { entry ->
            collector.add(entry)
            true
        }
        Assertions.assertEquals(7, collector.size)
        Assertions.assertEquals(3, collector[0].key)
        Assertions.assertEquals("Three", collector[0].value)
        Assertions.assertEquals(1, collector[1].key)
        Assertions.assertEquals("One", collector[1].value)
        Assertions.assertEquals(5, collector[2].key)
        Assertions.assertEquals("Five", collector[2].value)
        Assertions.assertEquals(0, collector[3].key)
        Assertions.assertEquals("Zero", collector[3].value)
        Assertions.assertEquals(2, collector[4].key)
        Assertions.assertEquals("Two", collector[4].value)
        Assertions.assertEquals(4, collector[5].key)
        Assertions.assertEquals("Four", collector[5].value)
        Assertions.assertEquals(6, collector[6].key)
        Assertions.assertEquals("Six", collector[6].value)
    }
    @Test
    fun visitHighFirstTest()
    {
        val binaryTree =
            BinaryTree<Int, String> {
                0 IS "Zero"
                1 IS "One"
                2 IS "Two"
                3 IS "Three"
                4 IS "Four"
                5 IS "Five"
                6 IS "Six"
            }

        val collector = ArrayList<MutableMap.MutableEntry<Int, String>>()
        binaryTree.visitHighFirst { entry ->
            collector.add(entry)
            true
        }
        Assertions.assertEquals(7, collector.size)
        Assertions.assertEquals(3, collector[0].key)
        Assertions.assertEquals("Three", collector[0].value)
        Assertions.assertEquals(5, collector[1].key)
        Assertions.assertEquals("Five", collector[1].value)
        Assertions.assertEquals(1, collector[2].key)
        Assertions.assertEquals("One", collector[2].value)
        Assertions.assertEquals(6, collector[3].key)
        Assertions.assertEquals("Six", collector[3].value)
        Assertions.assertEquals(4, collector[4].key)
        Assertions.assertEquals("Four", collector[4].value)
        Assertions.assertEquals(2, collector[5].key)
        Assertions.assertEquals("Two", collector[5].value)
        Assertions.assertEquals(0, collector[6].key)
        Assertions.assertEquals("Zero", collector[6].value)
    }
}