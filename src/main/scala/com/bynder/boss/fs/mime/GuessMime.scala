package com.bynder.boss.fs.mime

import com.bynder.boss.fs.util.FileUtilChunk
import com.j256.simplemagic.ContentInfoUtil

import java.io.File
import java.net.URL

object GuessMime {
  case class Chunk(length: Int, bytes: Array[Byte])
  //val magicFilePath = getClass.getResource("/magic-files").getPath
 // val magicFilePath = "./magic-files"


  def  getMime: Unit = {
    val util = new ContentInfoUtil()
    val info = util.findMatch("test_docs/test.au")
    if(info == null) println("Unknown content-type")
    else println("Content-type is: " + info.getName())
  }

  def  getMimeStream: Unit = {
    val chunkUtil = new FileUtilChunk()
    //val file = new File("test_docs/testmy.png")
    val file = new File("test_docs/test.au")
    println(s"file "+file.getName)
    val info = chunkUtil.contentInfoFromStreamWrapper(file)

    if(info == null) println("Unknown content-type")
    else println("Content-type is: " + info.getName())
  }

  def  getMimeByteArray: Unit = {
    val chunkUtil = new FileUtilChunk()
    //val file = new File("test_docs/testmy.png")
    val file = new File("test_docs/test.au")
    println(s"file "+file.getName)
    val info = chunkUtil.contentInfoFromByteArray(file)

    if(info == null) println("Unknown content-type")
    else println("Content-type is: " + info.getName())
  }
}

