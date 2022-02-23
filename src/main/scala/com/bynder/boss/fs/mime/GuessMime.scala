package com.bynder.boss.fs.mime


import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.{FileIO, Sink}

import java.nio.file.Paths
import java.nio.file.Files
import java.io.{ByteArrayInputStream, File, FileInputStream, InputStream}
import java.net.{URL, URLConnection}
import com.bynder.boss.fs.util.jna.LibMagicIdentifier
import akka.util.ByteString

import scala.collection.AbstractIterator
import scala.io.Source

class  GuessMime(system: ActorSystem[?]) {
  //System.setProperty("jna.debug_load", "true");
  System.setProperty("jna.library.path", "/usr/local/lib");
  val chunkSize = 1  * 1024 // 1MB chunk size

  val lmi = new LibMagicIdentifier()

  case class Chunk(length: Int, bytes: Array[Byte])

   def  getMime(filToInspect: File): String = {
    val magicFiles = getClass.getResource("/magic-files")
    println(s"magicFiles path ${magicFiles}")
    val magicFolder = new File(magicFiles.getPath)
    if (magicFolder.exists && magicFolder.isDirectory){
      magicFolder.listFiles().map(f => println(f.getAbsolutePath))
      lmi.setMagicFileDir(new File(magicFiles.getPath))
    }
     //val mimetype = lmi.identify(filToInspect)
     val chunk = getChunk(filToInspect)
     val mimetype = lmi.identify(java.nio.ByteBuffer.wrap(chunk))
    if(mimetype!=null) s" ${filToInspect.getPath} :  "+mimetype
    else s" ${filToInspect.getPath} :  "+"Unknown content-type"
  }

  def  getMimeInFolder(dirPath: String): Seq[String] = {
    val magicFiles = getClass.getResource("/magic-files")
    println(s"magicFiles path ${magicFiles}")
    val magicFolder = new File(magicFiles.getPath)
    if (magicFolder.exists && magicFolder.isDirectory){
      magicFolder.listFiles().map(f => println(f.getAbsolutePath))
      lmi.setMagicFileDir(new File(magicFiles.getPath))
    }else println("unable to read magicFiles path magicFolder.exists "
      +magicFolder.exists+" magicFolder.isDirectory "+magicFolder.isDirectory)
    val dirFile = new File(dirPath)
    if(dirFile.isDirectory){
      recursiveListFiles(dirFile).map{ file =>
        //val mimetype = lmi.identify(file)
        val chunk = getChunk(file)
        val mimetype = lmi.identify(java.nio.ByteBuffer.wrap(chunk))
        val extraGuess = URLConnection.guessContentTypeFromName(file.getName())
        if(mimetype!=null) s" ${file.getPath} :  "+mimetype+" "+extraGuess
        else s" ${file.getPath} :  "+"Unknown content-type"
      }
    }else Seq.empty
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

  private def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these.filter(f => !f.isDirectory && f.length()>0 ) ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  private def getChunk(file: File): Array[Byte] = {
    val fileStream = new FileInputStream(file)
    val chunkFile = splitFile(fileStream,getChunkSize(file))
      val byteArray = LazyList.continually(chunkFile.read).takeWhile(_ != -1).map(_.toByte).toArray
      chunkFile.close()
      fileStream.close()
      byteArray

    /*val out = filIterator.dropWhile(_.available>0).next()
    val byteArray = LazyList.continually(out.read).takeWhile(_ != -1).map(_.toByte).toArray
    out.close()
    fileStream.close()
    byteArray*/
  }

  private def splitFile( input: InputStream,chunkSize: Int): InputStream =  {
      val buffer = new Array[Byte](chunkSize)
      val bytes: Int = input.read(buffer)
      new ByteArrayInputStream(buffer, 0, bytes max 0)
  }

  private def getChunkSize(file: File) : Int = {
    file.length() match {
      case x if x > 5000 => 5000
      case _ => file.length().toInt
    }
  }


}

