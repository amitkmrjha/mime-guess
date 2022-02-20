package com.bynder.boss.fs.mime


import java.io.File
import java.net.URL
import com.bynder.boss.fs.util.jna.LibMagicIdentifier

object GuessMime {
  //System.setProperty("jna.debug_load", "true");
  System.setProperty("jna.library.path", "/usr/local/lib");


  case class Chunk(length: Int, bytes: Array[Byte])
  //val magicFilePath = getClass.getResource("/magic-files").getPath
 // val magicFilePath = "./magic-files"


  def  getMime: Unit = {
    val lmi = new LibMagicIdentifier()
    val file = new File("test_docs/test_powerpoint.pptx")
    println(s"file "+file.getName)
    val mimetype = lmi.identify(file)
    if(mimetype!=null) println(s"Content-type is: :  "+mimetype)
    else println("Unknown content-type")
  }

  def  getMimeStream: Unit = {
    /*val chunkUtil = new FileUtilChunk()
    //val file = new File("test_docs/testmy.png")
    val file = new File("test_docs/test.au")
    println(s"file "+file.getName)
    val info = chunkUtil.contentInfoFromStreamWrapper(file)

    if(info == null) println("Unknown content-type")
    else println("Content-type is: " + info.getName())*/
  }

  def  getMimeByteArray: Unit = {
    /*val chunkUtil = new FileUtilChunk()
    //val file = new File("test_docs/testmy.png")
    val file = new File("test_docs/test.au")
    println(s"file "+file.getName)
    val info = chunkUtil.contentInfoFromByteArray(file)

    if(info == null) println("Unknown content-type")
    else println("Content-type is: " + info.getName())*/
  }
}

