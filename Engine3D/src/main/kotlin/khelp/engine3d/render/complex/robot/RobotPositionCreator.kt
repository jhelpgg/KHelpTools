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
class RobotPositionCreator(neckAngleX : Float = 0f, neckAngleY : Float = 0f, neckAngleZ : Float = 0f,
                           rightShoulderAngleX : Float = 180f, rightShoulderAngleZ : Float = 0f,
                           rightElbowAngleX : Float = 0f,
                           leftShoulderAngleX : Float = 180f, leftShoulderAngleZ : Float = 0f,
                           leftElbowAngleX : Float = 0f,
                           rightAssAngleX : Float = 180f, rightAssAngleZ : Float = 0f,
                           rightKneeAngleX : Float = 0f,
                           leftAssAngleX : Float = 180f, leftAssAngleZ : Float = 0f,
                           leftKneeAngleX : Float = 0f)
{
    constructor(robotPosition : RobotPosition) :
            this(robotPosition.neckAngleX, robotPosition.neckAngleY, robotPosition.neckAngleZ,
                 robotPosition.rightShoulderAngleX, robotPosition.rightShoulderAngleZ,
                 robotPosition.rightElbowAngleX,
                 robotPosition.leftShoulderAngleX, robotPosition.leftShoulderAngleZ,
                 robotPosition.leftElbowAngleX,
                 robotPosition.rightAssAngleX, robotPosition.rightAssAngleZ,
                 robotPosition.rightKneeAngleX,
                 robotPosition.leftAssAngleX, robotPosition.leftAssAngleZ,
                 robotPosition.leftKneeAngleX)

    constructor(robot : Robot) : this(robot.robotPosition)

    var neckAngleX = neckAngleX.bounds(- 45f, 45f)
        set(value)
        {
            field = value.bounds(- 45f, 45f)
        }
    var neckAngleY = neckAngleY.bounds(- 90f, 90f)
        set(value)
        {
            field = value.bounds(- 90f, 90f)
        }
    var neckAngleZ = neckAngleZ.bounds(- 22f, 22f)
        set(value)
        {
            field = value.bounds(- 22f, 22f)
        }

    var rightShoulderAngleX = rightShoulderAngleX.modulo(0f, 360f)
        set(value)
        {
            field = value.modulo(0f, 360f)
        }
    var rightShoulderAngleZ = rightShoulderAngleZ.bounds(0f, 180f)
        set(value)
        {
            field = value.bounds(0f, 180f)
        }

    var rightElbowAngleX = rightElbowAngleX.bounds(- 150f, 0f)
        set(value)
        {
            field = value.bounds(- 150f, 0f)
        }

    var leftShoulderAngleX = leftShoulderAngleX.modulo(0f, 360f)
        set(value)
        {
            field = value.modulo(0f, 360f)
        }
    var leftShoulderAngleZ = leftShoulderAngleZ.bounds(- 180f, 0f)
        set(value)
        {
            field = value.bounds(- 180f, 0f)
        }

    var leftElbowAngleX = leftElbowAngleX.bounds(- 150f, 0f)
        set(value)
        {
            field = value.bounds(- 150f, 0f)
        }

    var rightAssAngleX = rightAssAngleX.bounds(90f, 270f)
        set(value)
        {
            field = value.bounds(90f, 270f)
        }
    var rightAssAngleZ = rightAssAngleZ.bounds(- 30f, 90f)
        set(value)
        {
            field = value.bounds(- 30f, 90f)
        }

    var rightKneeAngleX = rightKneeAngleX.bounds(0f, 150f)
        set(value)
        {
            field = value.bounds(0f, 150f)
        }

    var leftAssAngleX = leftAssAngleX.bounds(90f, 270f)
        set(value)
        {
            field = value.bounds(90f, 270f)
        }
    var leftAssAngleZ = leftAssAngleZ.bounds(- 90f, 30f)
        set(value)
        {
            field = value.bounds(- 90f, 30f)
        }

    var leftKneeAngleX = leftKneeAngleX.bounds(0f, 150f)
        set(value)
        {
            field = value.bounds(0f, 150f)
        }

    val robotPosition : RobotPosition
        get() =
            RobotPosition(this.neckAngleX, this.neckAngleY, this.neckAngleZ,
                          this.rightShoulderAngleX, this.rightShoulderAngleZ,
                          this.rightElbowAngleX,
                          this.leftShoulderAngleX, this.leftShoulderAngleZ,
                          this.leftElbowAngleX,
                          this.rightAssAngleX, this.rightAssAngleZ,
                          this.rightKneeAngleX,
                          this.leftAssAngleX, this.leftAssAngleZ,
                          this.leftKneeAngleX)

    fun setPosition(robotPosition : RobotPosition)
    {
        this.neckAngleX = robotPosition.neckAngleX
        this.neckAngleY = robotPosition.neckAngleY
        this.neckAngleZ = robotPosition.neckAngleZ

        this.rightShoulderAngleX = robotPosition.rightShoulderAngleX
        this.rightShoulderAngleZ = robotPosition.rightShoulderAngleZ

        this.rightElbowAngleX = robotPosition.rightElbowAngleX

        this.leftShoulderAngleX = robotPosition.leftShoulderAngleX
        this.leftShoulderAngleZ = robotPosition.leftShoulderAngleZ

        this.leftElbowAngleX = robotPosition.leftElbowAngleX

        this.rightAssAngleX = robotPosition.rightAssAngleX
        this.rightAssAngleZ = robotPosition.rightAssAngleZ

        this.rightKneeAngleX = robotPosition.rightKneeAngleX

        this.leftAssAngleX = robotPosition.leftAssAngleX
        this.leftAssAngleZ = robotPosition.leftAssAngleZ

        this.leftKneeAngleX = robotPosition.leftKneeAngleX
    }
}
