package khelp.engine3d.render.complex.robot

import khelp.utilities.extensions.bounds
import khelp.utilities.extensions.modulo

/**
 * Describe [Robot] joints position
 * @param neckAngleX Neck angle around X axis in `[-45, 45]`
 * @param neckAngleY Neck angle around Y axis in `[-90, 90]`
 * @param neckAngleZ Neck angle around Z axis in `[-22, 22]`
 * @param rightShoulderAngleX Right shoulder angle around X axis
 * @param rightShoulderAngleZ Right shoulder angle around Z axis in `[0, 180]`
 * @param rightElbowAngleX Right elbow angle around X axis in `[-150, 0]`
 * @param leftShoulderAngleX Left shoulder angle around X axis
 * @param leftShoulderAngleZ Left shoulder angle around Z axis in `[-180, 0]`
 * @param leftElbowAngleX Left elbow angle around X axis in `[-150, 0]`
 * @param rightAssAngleX Right ass angle around X axis in `[90, 270]`
 * @param rightAssAngleZ Right ass angle around Z axis in `[-30, 90]`
 * @param rightKneeAngleX Right knee angle around X axis in `[0, 150]`
 * @param leftAssAngleX Left ass angle around X axis in `[90, 270]`
 * @param leftAssAngleZ Left ass angle around Z axis in `[-90, 30]`
 * @param leftKneeAngleX Left knee angle around X axis in `[0, 150]`
 */
class RobotPosition(neckAngleX : Float = 0f, neckAngleY : Float = 0f, neckAngleZ : Float = 0f,
                    rightShoulderAngleX : Float = 180f, rightShoulderAngleZ : Float = 0f,
                    rightElbowAngleX : Float = 0f,
                    leftShoulderAngleX : Float = 180f, leftShoulderAngleZ : Float = 0f,
                    leftElbowAngleX : Float = 0f,
                    rightAssAngleX : Float = 180f, rightAssAngleZ : Float = 0f,
                    rightKneeAngleX : Float = 0f,
                    leftAssAngleX : Float = 180f, leftAssAngleZ : Float = 0f,
                    leftKneeAngleX : Float = 0f)
{
    val neckAngleX = neckAngleX.bounds(- 45f, 45f)
    val neckAngleY = neckAngleY.bounds(- 90f, 90f)
    val neckAngleZ = neckAngleZ.bounds(- 22f, 22f)

    val rightShoulderAngleX = rightShoulderAngleX.modulo(0f, 360f)
    val rightShoulderAngleZ = rightShoulderAngleZ.bounds(0f, 180f)

    val rightElbowAngleX = rightElbowAngleX.bounds(- 150f, 0f)

    val leftShoulderAngleX = leftShoulderAngleX.modulo(0f, 360f)
    val leftShoulderAngleZ = leftShoulderAngleZ.bounds(- 180f, 0f)

    val leftElbowAngleX = leftElbowAngleX.bounds(- 150f, 0f)

    val rightAssAngleX = rightAssAngleX.bounds(90f, 270f)
    val rightAssAngleZ = rightAssAngleZ.bounds(- 30f, 90f)

    val rightKneeAngleX = rightKneeAngleX.bounds(0f, 150f)

    val leftAssAngleX = leftAssAngleX.bounds(90f, 270f)
    val leftAssAngleZ = leftAssAngleZ.bounds(- 90f, 30f)

    val leftKneeAngleX = leftKneeAngleX.bounds(0f, 150f)

    fun neckRotate(neckAngleX : Float, neckAngleY : Float, neckAngleZ : Float) : RobotPosition =
        RobotPosition(neckAngleX, neckAngleY, neckAngleZ,
                      this.rightShoulderAngleX, this.rightShoulderAngleZ,
                      this.rightElbowAngleX,
                      this.leftShoulderAngleX, this.leftShoulderAngleZ,
                      this.leftElbowAngleX,
                      this.rightAssAngleX, this.rightAssAngleZ,
                      this.rightKneeAngleX,
                      this.leftAssAngleX, this.leftAssAngleZ,
                      this.leftKneeAngleX)

    fun addNeckRotate(neckAngleX : Float, neckAngleY : Float, neckAngleZ : Float) : RobotPosition =
        this.neckRotate(this.neckAngleX + neckAngleX,
                        this.neckAngleY + neckAngleY,
                        this.neckAngleZ + neckAngleZ)

    fun rightShoulder(rightShoulderAngleX : Float, rightShoulderAngleZ : Float) : RobotPosition =
        RobotPosition(this.neckAngleX, this.neckAngleY, this.neckAngleZ,
                      rightShoulderAngleX, rightShoulderAngleZ,
                      this.rightElbowAngleX,
                      this.leftShoulderAngleX, this.leftShoulderAngleZ,
                      this.leftElbowAngleX,
                      this.rightAssAngleX, this.rightAssAngleZ,
                      this.rightKneeAngleX,
                      this.leftAssAngleX, this.leftAssAngleZ,
                      this.leftKneeAngleX)

    fun addRightShoulder(rightShoulderAngleX : Float, rightShoulderAngleZ : Float) : RobotPosition =
        this.rightShoulder(this.rightShoulderAngleX + rightShoulderAngleX,
                           this.rightShoulderAngleZ + rightShoulderAngleZ)

    fun rightElbow(rightElbowAngleX : Float) : RobotPosition =
        RobotPosition(this.neckAngleX, this.neckAngleY, this.neckAngleZ,
                      this.rightShoulderAngleX, this.rightShoulderAngleZ,
                      rightElbowAngleX,
                      this.leftShoulderAngleX, this.leftShoulderAngleZ,
                      this.leftElbowAngleX,
                      this.rightAssAngleX, this.rightAssAngleZ,
                      this.rightKneeAngleX,
                      this.leftAssAngleX, this.leftAssAngleZ,
                      this.leftKneeAngleX)

    fun addRightElbow(rightElbowAngleX : Float) : RobotPosition =
        this.rightElbow(this.rightElbowAngleX + rightElbowAngleX)

    fun leftShoulder(leftShoulderAngleX : Float, leftShoulderAngleZ : Float) : RobotPosition =
        RobotPosition(this.neckAngleX, this.neckAngleY, this.neckAngleZ,
                      this.rightShoulderAngleX, this.rightShoulderAngleZ,
                      this.rightElbowAngleX,
                      leftShoulderAngleX, leftShoulderAngleZ,
                      this.leftElbowAngleX,
                      this.rightAssAngleX, this.rightAssAngleZ,
                      this.rightKneeAngleX,
                      this.leftAssAngleX, this.leftAssAngleZ,
                      this.leftKneeAngleX)

    fun addLeftShoulder(leftShoulderAngleX : Float, leftShoulderAngleZ : Float) : RobotPosition =
        this.leftShoulder(this.leftShoulderAngleX + leftShoulderAngleX,
                          this.leftShoulderAngleZ + leftShoulderAngleZ)


    fun leftElbow(leftElbowAngleX : Float) : RobotPosition =
        RobotPosition(this.neckAngleX, this.neckAngleY, this.neckAngleZ,
                      this.rightShoulderAngleX, this.rightShoulderAngleZ,
                      this.rightElbowAngleX,
                      this.leftShoulderAngleX, this.leftShoulderAngleZ,
                      leftElbowAngleX,
                      this.rightAssAngleX, this.rightAssAngleZ,
                      this.rightKneeAngleX,
                      this.leftAssAngleX, this.leftAssAngleZ,
                      this.leftKneeAngleX)

    fun addLeftElbow(leftElbowAngleX : Float) : RobotPosition =
        this.leftElbow(this.leftElbowAngleX + leftElbowAngleX)

    fun rightAss(rightAssAngleX : Float, rightAssAngleZ : Float) : RobotPosition =
        RobotPosition(this.neckAngleX, this.neckAngleY, this.neckAngleZ,
                      this.rightShoulderAngleX, this.rightShoulderAngleZ,
                      this.rightElbowAngleX,
                      this.leftShoulderAngleX, this.leftShoulderAngleZ,
                      this.leftElbowAngleX,
                      rightAssAngleX, rightAssAngleZ,
                      this.rightKneeAngleX,
                      this.leftAssAngleX, this.leftAssAngleZ,
                      this.leftKneeAngleX)

    fun addRightAss(rightAssAngleX : Float, rightAssAngleZ : Float) : RobotPosition =
        this.rightAss(this.rightAssAngleX + rightAssAngleX,
                      this.rightAssAngleZ + rightAssAngleZ)

    fun rightKnee(rightKneeAngleX : Float) : RobotPosition =
        RobotPosition(this.neckAngleX, this.neckAngleY, this.neckAngleZ,
                      this.rightShoulderAngleX, this.rightShoulderAngleZ,
                      this.rightElbowAngleX,
                      this.leftShoulderAngleX, this.leftShoulderAngleZ,
                      this.leftElbowAngleX,
                      this.rightAssAngleX, this.rightAssAngleZ,
                      rightKneeAngleX,
                      this.leftAssAngleX, this.leftAssAngleZ,
                      this.leftKneeAngleX)

    fun addRightKnee(rightKneeAngleX : Float) : RobotPosition =
        this.rightKnee(this.rightKneeAngleX + rightKneeAngleX)

    fun leftAss(leftAssAngleX : Float, leftAssAngleZ : Float) : RobotPosition =
        RobotPosition(this.neckAngleX, this.neckAngleY, this.neckAngleZ,
                      this.rightShoulderAngleX, this.rightShoulderAngleZ,
                      this.rightElbowAngleX,
                      this.leftShoulderAngleX, this.leftShoulderAngleZ,
                      this.leftElbowAngleX,
                      this.rightAssAngleX, this.rightAssAngleZ,
                      this.rightKneeAngleX,
                      leftAssAngleX, leftAssAngleZ,
                      this.leftKneeAngleX)

    fun addLeftAss(leftAssAngleX : Float, leftAssAngleZ : Float) : RobotPosition =
        this.leftAss(this.leftAssAngleX + leftAssAngleX,
                     this.leftAssAngleZ + leftAssAngleZ)

    fun leftKnee(leftKneeAngleX : Float) : RobotPosition =
        RobotPosition(this.neckAngleX, this.neckAngleY, this.neckAngleZ,
                      this.rightShoulderAngleX, this.rightShoulderAngleZ,
                      this.rightElbowAngleX,
                      this.leftShoulderAngleX, this.leftShoulderAngleZ,
                      this.leftElbowAngleX,
                      this.rightAssAngleX, this.rightAssAngleZ,
                      this.rightKneeAngleX,
                      this.leftAssAngleX, this.leftAssAngleZ,
                      leftKneeAngleX)

    fun addLeftKnee(leftKneeAngleX : Float) : RobotPosition =
        this.leftKnee(this.leftKneeAngleX + leftKneeAngleX)
}
