package s1.demo.effects

import s1.image.ImageExtensions.*
import s1.demo.*

import scala.math.*
import java.awt.Color.*
import java.awt.{ BasicStroke, Color }
import java.awt.image.BufferedImage

//Ax+By+C=0
//  =>
//A(x-x0)+B(y-y0)=0
var a = 1.0
var b = 1.0

def rotateLine(t: Double) =
  val temp = a
  a = a*math.cos(t) + b*math.sin(t)
  b = b*math.cos(t) - temp*math.sin(t)

class ParasDemo extends Effect(600, 600, "ParasDemo"):

  def positionOnLineHorizontal(x: Int, y: Int) = a*(x-xCentr)+b*(y-yCentr)
  def positionOnLineVertical(x: Int, y: Int) = -1*b*(x - xCentr) + a*(y-yCentr)



  var time = 0
  val baseRADIUS = 10

  val xCentr = width / 2
  val yCentr = height / 2


  var colorPointx = xCentr
  var colorPointY = yCentr




  override def makePic(): BufferedImage =
    val pic = emptyImage
    val g = pic.graphics

    val xOffset = (math.sin(time*0.05)*100).toInt
    val yOffset = (math.cos(time*0.07)*100).toInt


    g.setColor(BLACK)
    g.fillRect(0, 0, width, height)

    g.setColor(WHITE)

    for
      x <- 0 - xOffset to 600 - xOffset by 5
      y <- 0 - yOffset to 600 - yOffset by 5
    do
      val distanceFromCenter = math.hypot(xCentr - x, yCentr - y)
      val radius = max((((-1*distanceFromCenter*0.1 + time*0.3)) % baseRADIUS).toInt, 0)

      val grayCoef = radius*1.0/baseRADIUS
      val grayIndex = max((255*grayCoef*grayCoef*grayCoef).toInt, 100)
      var color = WHITE

      val realX = x - radius / 2
      val realY = y - radius / 2

      if positionOnLineHorizontal(realX, realY) <= 0 then
        if positionOnLineVertical(realX, realY) <= 0 then
          color = Color(grayIndex, grayIndex, grayIndex)
        else
          color = Color(0, grayIndex, 0)
      else
        if positionOnLineVertical(realX, realY) <= 0 then
          color = Color(grayIndex, 0, 0)
        else
          color = Color(0, 0, grayIndex)

      g.setColor(color)
      g.fillOval(x - radius / 2 + xOffset, y - radius / 2 + yOffset, radius, radius)

    //g.setPaintMode()
    pic

  override def tick(): Unit =
    time += 1
    rotateLine(0.05)

  override def next: Boolean = false

  override def newInstance: Effect = new ParasDemo

end ParasDemo

