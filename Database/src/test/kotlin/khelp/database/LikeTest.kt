package khelp.database

import khelp.database.type.DataType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LikeTest
{
    @Test
    fun startLike()
    {
        val database = Database.database("Test", "Test", "unitTest/likeTest")
        val table = database.table("TEST") {
            "Name" AS DataType.STRING
        }

        table.insertList {
            add { "Name" IS "Toto" }
            add { "Name" IS "Toby" }
            add { "Name" IS "Value" }
            add { "Name" IS "Term" }
            add { "Name" IS "Totally" }
        }

        val result = table.select {
            + "Name"
            where { condition = "Name" LIKE "To%" }
        }

        Assertions.assertTrue(result.hasNext)
        result.next { Assertions.assertEquals("Toto", getString(1)) }
        Assertions.assertTrue(result.hasNext)
        result.next { Assertions.assertEquals("Toby", getString(1)) }
        Assertions.assertTrue(result.hasNext)
        result.next { Assertions.assertEquals("Totally", getString(1)) }
        Assertions.assertFalse(result.hasNext)
        result.close()

        database.dropTable(table)
        database.close()
    }
}