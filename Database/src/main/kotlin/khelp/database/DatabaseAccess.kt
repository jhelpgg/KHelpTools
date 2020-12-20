package khelp.database

import java.sql.Connection
import java.sql.DriverManager
import khelp.security.rsa.RSAKeyPair
import org.hsqldb.lib.FileUtil
import org.hsqldb.lib.SecureFileUtil

internal object DatabaseAccess
{
    private val secureFileUtil: SecureFileUtil by lazy { SecureFileUtil() }

    fun createConnection(path: String, rsaKeyPair: RSAKeyPair): Connection
    {
        this.secureFileUtil.associate(path, rsaKeyPair)

        try
        {
            for (field in FileUtil::class.java.declaredFields)
            {
                if (field.type == FileUtil::class.java)
                {
                    field.isAccessible = true
                    field.set(null, secureFileUtil)
                    break
                }
            }
        }
        catch (exception: Exception)
        {
            exception.printStackTrace()
        }

        Class.forName("org.hsqldb.jdbcDriver")
            .getConstructor()
            .newInstance()
        return DriverManager.getConnection("jdbc:hsqldb:file:$path", "SA", "")
    }
}