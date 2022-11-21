package s1.demo.effects

import Util.Vector3D
import s1.image.ImageExtensions.*
import s1.demo.*

import scala.math.*
import java.awt.Color.*
import java.awt.{ BasicStroke, Color }
import java.awt.image.BufferedImage

val DIAMETER = 256
val RADIUS = DIAMETER / 2
val pixelRadius = 2


class ParasDemo2 extends Effect(600, 600, "ParasDemo2"):
  val xCentr = width / 2
  val yCentr = height / 2
  val lightPoint = Vector3D(RADIUS*cos(80.toRadians), RADIUS*cos(120.toRadians), RADIUS*sin(80.toRadians)).normalize
  
  var colorVector: Vector3D = Vector3D(RADIUS, 0, 0).normalize
  
  def rotateColorVector(angle: Double) =
    val xNew = colorVector.x*cos(angle.toRadians)-colorVector.z*sin(angle.toRadians)
    val zNew = colorVector.x*sin(angle.toRadians)+colorVector.z*cos(angle.toRadians)
    colorVector = Vector3D(xNew, 0, zNew).normalize
  
  def decideColor(x: Int, y: Int, z: Double): Boolean =
    val vectorFromCenterToPoint = Vector3D(x - xCentr, y - yCentr, z).normalize
    colorVector.dot(vectorFromCenterToPoint) >= 0
  
  def makeShadows(color: Color, x: Int, y: Int, z: Double): Color =
    val vectorFromCenterToPoint = Vector3D(x - xCentr, y - yCentr, z).normalize
    val distanceBetweenLightAndPoint = (vectorFromCenterToPoint - lightPoint).length // max 2
    var coef = (2 - distanceBetweenLightAndPoint)/2
    coef = min(coef*coef*2, 1)
    Color((color.getRed*coef).toInt, (color.getGreen*coef).toInt, (color.getBlue*coef).toInt)
    

  override def makePic(): BufferedImage =
    val pic = emptyImage
    val g = pic.graphics

    g.setColor(BLACK)
    g.fillRect(0, 0, 600, 600)
    for
      xCorner <- xCentr - RADIUS - pixelRadius to xCentr + RADIUS + pixelRadius by pixelRadius*4
      yCorner <- yCentr - RADIUS - pixelRadius to yCentr + RADIUS + pixelRadius by pixelRadius*4
    do
      val x = xCorner + pixelRadius
      val y = yCorner - pixelRadius
      val distanceFromCenter = math.hypot(xCentr - x, yCentr - y) - 1
      var color = BLACK

      if distanceFromCenter <= RADIUS then
        val z = math.sqrt(math.abs(RADIUS*RADIUS - (x -xCentr)*(x - xCentr) - (y - yCentr)*(y - yCentr)))
        if decideColor(x, y, z) then
          color = WHITE
        else color = RED
        
        color = makeShadows(color, x, y, z)
        
        g.setColor(color)
        g.fillOval(xCorner, yCorner, pixelRadius*2, pixelRadius*2)
    end for


    pic

  override def tick(): Unit =
    rotateColorVector(3)

  override def newInstance: Effect = new ParasDemo2

  override def next: Boolean = true
end ParasDemo2

