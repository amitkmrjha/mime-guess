package com.bynder.boss.fs

import akka.actor.ActorSystem
import com.bynder.boss.fs.mime.GuessMime

import java.io.File
import akka.actor.typed.scaladsl.adapter._

@main def hello(file: String): Unit = {
  implicit val system: ActorSystem = ActorSystem("QuickStart")
  try{
    val mime = new GuessMime(system.toTyped)
    println("Hello world!")
    val fileToInspect = new File(file)
    if(fileToInspect!=null && fileToInspect.exists()){
      println(s"file to inspect ${fileToInspect.getPath}")
      if(fileToInspect.isDirectory){
        mime.getMimeInFolder(fileToInspect.getAbsolutePath).map(str => println(str))
        //GuessMime.getMimeStream
        //GuessMime.getMimeByteArray
      }else println(mime.getMime(fileToInspect))

    }else println("Input file not find")
  }catch{
    case ex: Exception =>
      ex.printStackTrace()
      println(s"exception in Main ${ex.getMessage}")
  }
  println(msg)
  system.terminate()
}
  def msg = "I was compiled by Scala 3. :)"


