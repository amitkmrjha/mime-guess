package com.bynder.boss.fs

import com.bynder.boss.fs.mime.GuessMime
import java.io.File


@main def hello(file: String): Unit =
  println("Hello world!")
  val fileToInspect = new File(file)
  if(fileToInspect!=null && fileToInspect.exists()){
    println(s"file to inspect ${fileToInspect.getPath}")
    GuessMime.getMime(fileToInspect)
    //GuessMime.getMimeStream
    //GuessMime.getMimeByteArray
  }


  println(msg)

def msg = "I was compiled by Scala 3. :)"
