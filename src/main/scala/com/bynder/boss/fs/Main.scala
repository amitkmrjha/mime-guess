package com.bynder.boss.fs

import com.bynder.boss.fs.mime.GuessMime

@main def hello: Unit =
  println("Hello world!")
  GuessMime.getMime
  //GuessMime.getMimeStream
  //GuessMime.getMimeByteArray
  println(msg)

def msg = "I was compiled by Scala 3. :)"
