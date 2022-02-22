package com.bynder.boss.fs

import com.bynder.boss.fs.mime.GuessMime
import java.io.File


@main def hello(file: String): Unit =
  println("Hello world!")
  val fileToInspect = new File(file)
  if(fileToInspect!=null && fileToInspect.exists()){
    println(s"file to inspect ${fileToInspect.getPath}")
    if(fileToInspect.isDirectory){
      GuessMime.getMimeInFolder(fileToInspect.getAbsolutePath).map(str => println(str))
    }else println(GuessMime.getMime(fileToInspect))
    //GuessMime.getMimeStream
    //GuessMime.getMimeByteArray
  }else println("Input file not find")
  println(msg)

def msg = "I was compiled by Scala 3. :)"
