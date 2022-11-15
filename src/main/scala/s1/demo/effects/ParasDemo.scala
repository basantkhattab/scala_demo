package s1.demo.effects

import s1.demo.Effect

import java.awt.image.BufferedImage

class ParasDemo extends Effect(500, 500, "ParasDemo"):


  override def makePic(): BufferedImage = ???

  override def tick(): Unit = ???

  override def next: Boolean = false

  override def newInstance: Effect = new ParasDemo
  
end ParasDemo

