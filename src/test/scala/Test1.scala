import org.junit.Test
import org.junit.Assert.*
import com.bynder.boss.fs.msg

class Test1:
  @Test def t1(): Unit = 
    assertEquals("I was compiled by Scala 3. :)", msg)
