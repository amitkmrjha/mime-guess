package com.bynder.boss.fs.mime


import java.io.File
import java.net.URL
import com.bynder.boss.fs.util.jna.LibMagicIdentifier
import scala.io.Source

object GuessMime {
  //System.setProperty("jna.debug_load", "true");
  System.setProperty("jna.library.path", "/usr/local/lib");
  val lmi = new LibMagicIdentifier()
  //val magicFiles = new File("/Users/amit.kumar/ws/misc/mime-guess/src/main/resources/magic-files")

  case class Chunk(length: Int, bytes: Array[Byte])
  //val magicFilePath = getClass.getResource("/magic-files").getPath
 // val magicFilePath = "./magic-files"


  def  getMime(filToInspect: File): Unit = {
    val magicFiles = getClass.getResource("/magic-files")
    println(s"magicFiles path ${magicFiles}")
    val magicFolder = new File(magicFiles.getPath)
    if (magicFolder.exists && magicFolder.isDirectory){
      //lmi.setMagicFileDir(new File(magicFiles.getPath))
    }


    //val file = new File("test_docs/test_powerpoint.pptx")
    val file = new File("test_docs/test_powerpoint.pptx")
    val mimetype = lmi.identify(filToInspect)
    if(mimetype!=null) println(s"Content-type is: :  "+mimetype)
    else println("Unknown content-type")
  }

  def  getMimeStream(filToInspect: File): Unit = {
    val lmi = new LibMagicIdentifier()
    val magicFiles = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("magic-files"))
    val firstFile =  magicFiles.getLines().mkString
   println(firstFile)
    magicFiles.close()

    /*println(s"magicFiles path ${magicFiles}")
    val magicFolder = new File(magicFiles.getPath)
    if (magicFolder.exists && magicFolder.isDirectory)
      lmi.setMagicFileDir(new File(magicFiles.getPath))

    val file = new File("test_docs/test_powerpoint.pptx")
    println(s"file "+file.getName)
    val mimetype = lmi.identify(file)
    if(mimetype!=null) println(s"Content-type is: :  "+mimetype)
    else println("Unknown content-type")*/
  }

  def  getMimeByteArray(filToInspect: File): Unit = {
    /*val chunkUtil = new FileUtilChunk()
    //val file = new File("test_docs/testmy.png")
    val file = new File("test_docs/test.au")
    println(s"file "+file.getName)
    val info = chunkUtil.contentInfoFromByteArray(file)

    if(info == null) println("Unknown content-type")
    else println("Content-type is: " + info.getName())*/
  }
}

