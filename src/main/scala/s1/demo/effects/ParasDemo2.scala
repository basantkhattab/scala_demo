package s1.demo.effects

import Util.Vector3D
import s1.demo.*
import s1.image.ImageExtensions.*

import java.awt.Color.*
import java.awt.image.BufferedImage
import java.awt.{ BasicStroke, Color }
import scala.math.*

val DIAMETER = 256
val RADIUS = DIAMETER / 2
val pixelRadius = 3

val angleFromX = -45.toRadians
val angleFromZ = 45.toRadians

val baseLightPoint = Vector3D(RADIUS * cos(80.toRadians), RADIUS * cos(120.toRadians), RADIUS * sin(80.toRadians))
  .normalize
val axis = Vector3D(RADIUS * cos(angleFromX) * cos(angleFromZ), RADIUS * sin(angleFromX) * cos(angleFromZ),
                    RADIUS * sin(angleFromZ)).normalize

class ParasDemo2 extends Effect(600, 600, "ParasDemo2") :
  var time = 0
  val xCentr = width / 2
  val yCentr = height / 2
  var lightPoint = Vector3D(baseLightPoint.x, baseLightPoint.y, baseLightPoint.z).normalize

  var colorVector: Vector3D = Vector3D(-1 * axis.y + axis.z, axis.x - axis.z, -1 * axis.x + axis.y).normalize

  def colorPerp = Vector3D(colorVector.y * axis.z - axis.y * colorVector.z,
                             axis.x * colorVector.z - colorVector.x * axis.z,
                             colorVector.x * axis.y - axis.x * colorVector.y).normalize

  def rotateColorVector(angle: Double) =
    val an = angle.toRadians
    val q0 = cos(an / 2)
    val q1 = sin(an / 2) * axis.x
    val q2 = sin(an / 2) * axis.y
    val q3 = sin(an / 2) * axis.z

    val xNew = colorVector.x * (q0 * q0 + q1 * q1 - q2 * q2 - q3 * q3) + colorVector
      .y * (2 * (q1 * q2 - q0 * q3)) + colorVector.z * (2 * (q1 * q3 + q0 * q2))
    val yNew = colorVector.x * (2 * (q2 * q1 + q0 * q3)) + colorVector
      .y * (q0 * q0 - q1 * q1 + q2 * q2 - q3 * q3) + colorVector.z * (2 * (q2 * q3 - q0 * q1))
    val zNew = colorVector.x * (2 * (q3 * q1 - q0 * q2)) + colorVector.y * (2 * (q3 * q2 + q0 * q1)) + colorVector
      .z * (q0 * q0 - q1 * q1 - q2 * q2 + q3 * q3)
    /* ei toimi
    val xNew = colorVector.x*(cos(an)+axis.x*axis.x*(1-cos(an))) + colorVector.y*(axis.x*axis.y*(1-cos(an))-axis.z*sin(an)) + colorVector.z*(axis.x*axis.z*(1-cos(an)) + axis.y*sin(an))
    val yNew = colorVector.x*(axis.x*axis.y*(1-cos(an))+axis.z*sin(an)) + colorVector.y*(cos(an)+axis.y*axis.y*(1-cos(an))) + colorVector.z*(axis.y*axis.z*(1-cos(an)) + axis.x*sin(an))
    val zNew = colorVector.x*(axis.x*axis.z*(1-cos(an))-axis.y*sin(an)) + colorVector.y*(axis.z*axis.y*(1-cos(an))+axis.x*sin(an)) + colorVector.z*(cos(an)+axis.z*axis.z*(1-cos(an)))
    */
    colorVector = Vector3D(xNew, yNew, zNew).normalize

  def decideColor(x: Int, y: Int, z: Double): Boolean =
    val vectorFromCenterToPoint = Vector3D(x - xCentr, y - yCentr, z).normalize
    colorPerp.dot(vectorFromCenterToPoint) * colorVector.dot(vectorFromCenterToPoint) >= 0


  def makeShadows(color: Color, x: Int, y: Int, z: Double): Color =
    val vectorFromCenterToPoint = Vector3D(x - xCentr, y - yCentr, z).normalize
    val distanceBetweenLightAndPoint = (vectorFromCenterToPoint - lightPoint).length // max 2
    var coef = (2 - distanceBetweenLightAndPoint) / 2
    coef = min(coef * coef * 2, 1)
    Color((color.getRed * coef).toInt, (color.getGreen * coef).toInt, (color.getBlue * coef).toInt)

  def makeWaves(color: Color, x: Int, y: Int, z: Double): Color =
    val axisPoint = Vector3D(axis.x*RADIUS + xCentr, axis.y*RADIUS + yCentr, axis.z*RADIUS)
    val vectorFromPointToAxis = Vector3D(x - axisPoint.x, y - axisPoint.y, z - axisPoint.z)
    val coef =
      if time - vectorFromPointToAxis.length <= 0 then 1
      else
        max((50 - (-vectorFromPointToAxis.length + time) % 50)/50, 0.3)
    Color((color.getRed * coef).toInt, (color.getGreen * coef).toInt, (color.getBlue * coef).toInt)

  override def makePic(): BufferedImage =
    val pic = emptyImage
    val g = pic.graphics
    val yOffset = (sin(time * 0.05) * 50).toInt


    lightPoint = (baseLightPoint + Vector3D(0, -1.0 * yOffset / 100, 0)).normalize

    g.setColor(BLACK)
    g.fillRect(0, 0, 600, 600)
    for
      xCorner <- xCentr - RADIUS - pixelRadius to xCentr + RADIUS + pixelRadius by pixelRadius
      yCorner <- yCentr - RADIUS - pixelRadius to yCentr + RADIUS + pixelRadius by pixelRadius
    do
      val x = xCorner + pixelRadius
      val y = yCorner - pixelRadius
      val distanceFromCenter = math.hypot(xCentr - x, yCentr - y) - 1
      var color = BLACK

      if distanceFromCenter <= RADIUS then
        val z = math.sqrt(math.abs(RADIUS * RADIUS - (x - xCentr) * (x - xCentr) - (y - yCentr) * (y - yCentr)))
        if decideColor(x, y, z) then
          color = WHITE
        else color = RED

        color = makeWaves(color, x, y, z)

        color = makeShadows(color, x, y, z)

        g.setColor(color)
        g.fillOval(xCorner, yCorner + yOffset, pixelRadius * 2, pixelRadius * 2)
    end for


    pic

  override def tick(): Unit =
    time += 1
    rotateColorVector(3)

  override def newInstance: Effect = new ParasDemo2

  override def next: Boolean = true
end ParasDemo2

