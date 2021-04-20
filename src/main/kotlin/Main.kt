
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import kotlin.random.Random


fun main() {
    var c: Connection? = null
    var stmt: Statement? = null
    try {
        Class.forName("org.postgresql.Driver")
        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres")
        println("Opened database successfully")
        c.autoCommit = false;
        val random = Random(42)
        stmt = c.createStatement()
        for (i in 1..1_000_000) {
            val sql = """INSERT INTO books_no_shard VALUES 
            |($i, ${i%2 +1}, '${getRandomString(5)}', '${getRandomString(7)}', '${(1500 until 2000).random()}' )
            |""".trimMargin()
            stmt.executeUpdate(sql)
            println("row: $i")
        }

        stmt.close()
        c.commit()
        c.close()
    } catch (e: Exception) {
        System.err.println(e.javaClass.name + ": " + e.message)
        System.exit(0)
    }
}

fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}