package com.bynder.boss.fs.mime


import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.{FileIO, Sink}

import java.nio.file.{Files, Path, Paths, StandardCopyOption}
import java.io.{ByteArrayInputStream, File, FileInputStream, InputStream}
import java.net.{URL, URLConnection, URLDecoder}
import com.bynder.boss.fs.util.jna.LibMagicIdentifier

import java.nio.charset.StandardCharsets
import java.util
import java.util.jar.{JarEntry, JarFile}
import scala.collection.AbstractIterator
import scala.io.{BufferedSource, Source}
import scala.jdk.CollectionConverters.*

class  GuessMime(system: ActorSystem[?]) {
  //System.setProperty("jna.debug_load", "true");
  System.setProperty("jna.library.path", "/usr/local/lib");
  val chunkSize = 1  * 1024 // 1MB chunk size

  val lmi = new LibMagicIdentifier()

  case class Chunk(length: Int, bytes: Array[Byte])

   def  getMime(filToInspect: File): String = {
    val magicFiles: URL = getClass.getResource("/magic-files")
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
    //val magicFiles = getClass.getResource("/magic-files")
    getMagicFileFromJar("magic-files").map(magicFiles =>
      lmi.setMagicFileDir(new File(magicFiles.getPath))
    )
    val dirFile = new File(dirPath)
    if(dirFile.isDirectory){
      recursiveListFiles(dirFile).map{ file =>
        //val mimetype = lmi.identify(file)
        val chunk = getChunk(file)
        val mimetype = lmi.identify(java.nio.ByteBuffer.wrap(chunk))
        if(mimetype!=null) s" ${file.getPath} :  "+mimetype+" "
        else s" ${file.getPath} :  "+"Unknown content-type"
      }
    }else Seq.empty
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

  private def getMagicFileFromJar(magicFolder:String):Option[File] = {
    val magicFilesUrl: URL = getClass.getResource("/"+ magicFolder)
    /*val magicFilesUrl = new URL("jar:file:/Users/amit.kumar/ws/misc/mime-guess/target/universal" +
      "/mime-guess-0.1.0-SNAPSHOT/lib/mime-guess.mime-guess-0.1.0-SNAPSHOT.jar!/magic-files")*/
    magicFilesUrl.getProtocol match {
      case "file" => Option(new File(magicFilesUrl.toURI()))
      case "jar"  =>
        val jarPath = magicFilesUrl.getPath().substring(5, magicFilesUrl.getPath().indexOf("!"))
        val jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))
        val jarEntry = jar.entries().asIterator().asScala.toSeq.map(_.getName)
          .filter(f => f.startsWith(magicFolder)  && !f.endsWith("/"))
        val tmpDir: Path = Files.createTempDirectory(magicFolder)
        jarEntry.foreach{ entry =>
          val magicStream = Source.
            fromInputStream(getClass.getClassLoader.getResourceAsStream(entry))
          val s = magicStream.mkString.getBytes(StandardCharsets.UTF_8)
          val tempFilePath = Paths.get(tmpDir.toFile.getPath+"/"+entry.split("/")(1))
          Files.deleteIfExists(tempFilePath)
          val p: Path = Files.createFile( tempFilePath);
          Files.write(p,s)
          magicStream.close()
        }
        Option(tmpDir.toFile)
      case _ => None
    }
  }
}

