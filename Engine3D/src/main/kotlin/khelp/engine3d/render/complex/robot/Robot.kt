package khelp.engine3d.render.complex.robot

import khelp.engine3d.animation.Animation
import khelp.engine3d.animation.Interpolation
import khelp.engine3d.animation.LinearInterpolation
import khelp.engine3d.geometry.Point3D
import khelp.engine3d.render.COLOR_ARM
import khelp.engine3d.render.COLOR_LEG
import khelp.engine3d.render.Color4f
import khelp.engine3d.render.Material
import khelp.engine3d.render.Node
import khelp.engine3d.render.ObjectClone
import khelp.engine3d.render.Texture
import khelp.engine3d.render.prebuilt.Box
import khelp.engine3d.render.prebuilt.CrossUV
import khelp.engine3d.render.prebuilt.Revolution
import khelp.engine3d.render.prebuilt.Sphere
import khelp.engine3d.resource.Textures
import kotlin.math.max

class Robot(id : String) : Node(id)
{
    /**Head object*/
    private val headObject = Sphere("${id}.head", 22, 22)

    /**Neck node. It it b the joint for move the head*/
    private val neck = Node("${id}.neck")

    /**Body object*/
    private val body = Box("${id}.body", CrossUV(5f / 22f, 17f / 22f, 0.1f, 0.5f, 0.6f))

    /**Node for attach something in the back*/
    private val backAttach = Node("${id}.backAttach")

    /**Right shoulder joint*/
    private val rightShoulder = Node("${id}.rightShoulder")

    /**Left shoulder joint*/
    private val leftShoulder = Node("${id}.leftShoulder")

    /**Right ass joint*/
    private val rightAss = Node("${id}.rightAss")

    /**Left ass joint*/
    private val leftAss = Node("${id}.leftAss")

    /**Arms/legs part base*/
    private val cylinder = Revolution("${id}.cylinder")

    /**Right before arm object*/
    private val rightBeforeArm = ObjectClone("${id}.rightBeforeArm", this.cylinder)

    /**Right after arm object*/
    private val rightAfterArm = ObjectClone("${id}.rightAfterArm", this.cylinder)

    /**Right hand where can be attach something*/
    private val rightHand = Node("${id}.rightHand")

    /**Left before arm object*/
    private val leftBeforeArm = ObjectClone("${id}.leftBeforeArm", this.cylinder)

    /**Left after arm object*/
    private val leftAfterArm = ObjectClone("${id}.leftAfterArm", this.cylinder)

    /**Left hand where can be attach something*/
    private val leftHand = Node("${id}.leftHand")

    /**Right before leg object*/
    private val rightBeforeLeg = ObjectClone("${id}.rightBeforeLeg", this.cylinder)

    /**Right after leg object*/
    private val rightAfterLeg = ObjectClone("${id}.rightAfterLeg", this.cylinder)

    /**Left before leg object*/
    private val leftBeforeLeg = ObjectClone("${id}.leftBeforeLeg", this.cylinder)

    /**Left after leg object*/
    private val leftAfterLeg = ObjectClone("${id}.leftAfterLeg", this.cylinder)

    /**Complete right arm material*/
    private val materialRightArm = Material()

    /**Complete left arm material*/
    private val materialLeftArm = Material()

    /**Complete right leg material*/
    private val materialRightLeg = Material()

    /**Complete left leg material*/
    private val materialLeftLeg = Material()

    /**Body material*/
    val materialBody = Material()
    val head = Head()
    var robotPosition : RobotPosition = RobotPosition()
        set(value)
        {
            field = value
            this.applyPosition()
        }

    var rightArmColor : Color4f
        get() = this.materialRightArm.colorDiffuse
        set(value)
        {
            this.materialRightArm.colorDiffuse = value
        }

    var leftArmColor : Color4f
        get() = this.materialLeftArm.colorDiffuse
        set(value)
        {
            this.materialLeftArm.colorDiffuse = value
        }

    var rightLegColor : Color4f
        get() = this.materialRightLeg.colorDiffuse
        set(value)
        {
            this.materialRightLeg.colorDiffuse = value
        }

    var leftLegColor : Color4f
        get() = this.materialLeftLeg.colorDiffuse
        set(value)
        {
            this.materialLeftLeg.colorDiffuse = value
        }

    init
    {
        this.materialRightArm.colorDiffuse = COLOR_ARM
        this.materialLeftArm.colorDiffuse = COLOR_ARM
        this.materialRightLeg.colorDiffuse = COLOR_LEG
        this.materialLeftLeg.colorDiffuse = COLOR_LEG

        val joint = Sphere("${id}.joint", 7, 7)
        this.cylinder.path {
            move(0.3f, 2f)
            line(0.3f, 0f)
        }

        //Head
        this.headObject.material.textureDiffuse = this.head.texture
        this.headObject.mesh { movePoint(headObject.northPole, Point3D(0f, - 0.25f, 0f), 0.87f, 1) }
        this.headObject.x = 0f
        this.headObject.y = 0.75f
        this.headObject.z = 0f

        this.neck.addChild(this.headObject)
        this.neck.limitAngleX(- 45f, 45f)
        this.neck.limitAngleY(- 90f, 90f)
        this.neck.limitAngleZ(- 22f, 22f)
        this.neck.x = 0f
        this.neck.y = 2.5f
        this.neck.z = 0f
        this.addChild(this.neck)

        //Body
        this.body.scaleX = 2.4f
        this.body.scaleY = 4f
        this.body.scaleZ = 1f
        this.body.material = this.materialBody
        this.addChild(this.body)
        this.materialBody.textureDiffuse = Texture(Textures.BODY_COSTUME.image)

        this.backAttach.x = 0f
        this.backAttach.y = 2f
        this.backAttach.z = - 0.6f
        this.addChild(this.backAttach)

        //Right arm
        val joint1 = ObjectClone("${id}.joint1", joint)
        joint1.scaleX = 0.3f
        joint1.scaleY = 0.3f
        joint1.scaleZ = 0.3f
        this.rightShoulder.addChild(joint1)
        this.rightShoulder.addChild(this.rightBeforeArm)
        this.rightShoulder.x = - 1.5f
        this.rightShoulder.y = 1.7f
        this.rightShoulder.z = 0f
        this.rightShoulder.angleX = 180f
        this.rightShoulder.limitAngleY(0f, 0f)
        this.rightShoulder.limitAngleZ(0f, 180f)
        this.addChild(this.rightShoulder)

        val joint2 = ObjectClone("${id}.joint2", joint)
        joint2.scaleX = 0.3f
        joint2.scaleY = 0.3f
        joint2.scaleZ = 0.3f
        joint2.x = 0f
        joint2.y = 2f
        joint2.z = 0f
        this.rightBeforeArm.addChild(joint2)
        this.rightAfterArm.x = 0f
        this.rightAfterArm.y = 2f
        this.rightAfterArm.z = 0f
        this.rightAfterArm.limitAngleX(- 150f, 0f)
        this.rightAfterArm.limitAngleY(0f, 0f)
        this.rightAfterArm.limitAngleZ(0f, 0f)
        this.rightBeforeArm.addChild(this.rightAfterArm)

        val joint3 = ObjectClone("${id}.joint3", joint)
        joint3.scaleX = 0.3f
        joint3.scaleY = 0.3f
        joint3.scaleZ = 0.3f
        joint3.x = 0f
        joint3.y = 2f
        joint3.z = 0f
        this.rightAfterArm.addChild(joint3)

        this.rightHand.x = 0f
        this.rightHand.y = 2.1f
        this.rightHand.z = 0f
        this.rightAfterArm.addChild(this.rightHand)

        this.rightShoulder.applyMaterialHierarchically(this.materialRightArm)

        //Left arm
        val joint4 = ObjectClone("${id}.joint4", joint)
        joint4.scaleX = 0.3f
        joint4.scaleY = 0.3f
        joint4.scaleZ = 0.3f
        this.leftShoulder.addChild(joint4)
        this.leftShoulder.addChild(this.leftBeforeArm)
        this.leftShoulder.x = 1.5f
        this.leftShoulder.y = 1.7f
        this.leftShoulder.z = 0f
        this.leftShoulder.angleX = 180f
        this.leftShoulder.limitAngleY(0f, 0f)
        this.leftShoulder.limitAngleZ(- 180f, 0f)
        this.addChild(this.leftShoulder)

        val joint5 = ObjectClone("${id}.joint5", joint)
        joint5.scaleX = 0.3f
        joint5.scaleY = 0.3f
        joint5.scaleZ = 0.3f
        joint5.x = 0f
        joint5.y = 2f
        joint5.z = 0f
        this.leftBeforeArm.addChild(joint5)
        this.leftAfterArm.x = 0f
        this.leftAfterArm.y = 2f
        this.leftAfterArm.z = 0f
        this.leftAfterArm.limitAngleX(- 150f, 0f)
        this.leftAfterArm.limitAngleY(0f, 0f)
        this.leftAfterArm.limitAngleZ(0f, 0f)
        this.leftBeforeArm.addChild(this.leftAfterArm)

        val joint6 = ObjectClone("${id}.joint6", joint)
        joint6.scaleX = 0.3f
        joint6.scaleY = 0.3f
        joint6.scaleZ = 0.3f
        joint6.x = 0f
        joint6.y = 2f
        joint6.z = 0f
        this.leftAfterArm.addChild(joint6)

        this.leftHand.x = 0f
        this.leftHand.y = 2.1f
        this.leftHand.z = 0f
        this.leftAfterArm.addChild(this.leftHand)

        this.leftShoulder.applyMaterialHierarchically(this.materialLeftArm)

        // Right leg
        val joint7 = ObjectClone("${id}.joint7", joint)
        joint7.scaleX = 0.3f
        joint7.scaleY = 0.3f
        joint7.scaleZ = 0.3f
        this.rightAss.addChild(joint7)
        this.rightAss.addChild(this.rightBeforeLeg)
        this.rightAss.x = - 0.6f
        this.rightAss.y = - 2.2f
        this.rightAss.z = 0f
        this.rightAss.angleX = 180f
        this.rightAss.limitAngleX(90f, 270f)
        this.rightAss.limitAngleY(0f, 0f)
        this.rightAss.limitAngleZ(- 30f, 90f)
        this.addChild(this.rightAss)

        val joint8 = ObjectClone("${id}.joint8", joint)
        joint8.scaleX = 0.3f
        joint8.scaleY = 0.3f
        joint8.scaleZ = 0.3f
        joint8.x = 0f
        joint8.y = 2f
        joint8.z = 0f
        this.rightBeforeLeg.addChild(joint8)
        this.rightAfterLeg.x = 0f
        this.rightAfterLeg.y = 2f
        this.rightAfterLeg.z = 0f
        this.rightAfterLeg.limitAngleX(0f, 150f)
        this.rightAfterLeg.limitAngleY(0f, 0f)
        this.rightAfterLeg.limitAngleZ(0f, 0f)
        this.rightBeforeLeg.addChild(this.rightAfterLeg)

        val joint9 = ObjectClone("${id}.joint9", joint)
        joint9.scaleX = 0.3f
        joint9.scaleY = 0.3f
        joint9.scaleZ = 0.3f
        joint9.x = 0f
        joint9.y = 2f
        joint9.z = 0f
        this.rightAfterLeg.addChild(joint9)

        this.rightAss.applyMaterialHierarchically(this.materialRightLeg)

        // Left leg
        val joint10 = ObjectClone("${id}.joint10", joint)
        joint10.scaleX = 0.3f
        joint10.scaleY = 0.3f
        joint10.scaleZ = 0.3f
        this.leftAss.addChild(joint10)
        this.leftAss.addChild(this.leftBeforeLeg)
        this.leftAss.x = 0.6f
        this.leftAss.y = - 2.2f
        this.leftAss.z = 0f
        this.leftAss.angleX = 180f
        this.leftAss.limitAngleX(90f, 270f)
        this.leftAss.limitAngleY(0f, 0f)
        this.leftAss.limitAngleZ(- 90f, 30f)
        this.addChild(this.leftAss)

        val joint11 = ObjectClone("${id}.joint11", joint)
        joint11.scaleX = 0.3f
        joint11.scaleY = 0.3f
        joint11.scaleZ = 0.3f
        joint11.x = 0f
        joint11.y = 2f
        joint11.z = 0f
        this.leftBeforeLeg.addChild(joint11)
        this.leftAfterLeg.x = 0f
        this.leftAfterLeg.y = 2f
        this.leftAfterLeg.z = 0f
        this.leftAfterLeg.limitAngleX(0f, 150f)
        this.leftAfterLeg.limitAngleY(0f, 0f)
        this.leftAfterLeg.limitAngleZ(0f, 0f)
        this.leftBeforeLeg.addChild(this.leftAfterLeg)

        val joint12 = ObjectClone("${id}.joint12", joint)
        joint12.scaleX = 0.3f
        joint12.scaleY = 0.3f
        joint12.scaleZ = 0.3f
        joint12.x = 0f
        joint12.y = 2f
        joint12.z = 0f
        this.leftAfterLeg.addChild(joint12)

        this.leftAss.applyMaterialHierarchically(this.materialLeftLeg)
        this.applyPosition()
    }

    fun neckRotate(neckAngleX : Float, neckAngleY : Float, neckAngleZ : Float)
    {
        this.robotPosition = this.robotPosition.neckRotate(neckAngleX, neckAngleY, neckAngleZ)
    }

    fun addNeckRotate(neckAngleX : Float, neckAngleY : Float, neckAngleZ : Float)
    {
        this.robotPosition = this.robotPosition.addNeckRotate(neckAngleX, neckAngleY, neckAngleZ)
    }

    fun rightShoulder(rightShoulderAngleX : Float, rightShoulderAngleZ : Float)
    {
        this.robotPosition = this.robotPosition.rightShoulder(rightShoulderAngleX, rightShoulderAngleZ)
    }

    fun addRightShoulder(rightShoulderAngleX : Float, rightShoulderAngleZ : Float)
    {
        this.robotPosition = this.robotPosition.addRightShoulder(rightShoulderAngleX, rightShoulderAngleZ)
    }

    fun rightElbow(rightElbowAngleX : Float)
    {
        this.robotPosition = this.robotPosition.rightElbow(rightElbowAngleX)
    }

    fun addRightElbow(rightElbowAngleX : Float)
    {
        this.robotPosition = this.robotPosition.addRightElbow(rightElbowAngleX)
    }

    fun leftShoulder(leftShoulderAngleX : Float, leftShoulderAngleZ : Float)
    {
        this.robotPosition = this.robotPosition.leftShoulder(leftShoulderAngleX, leftShoulderAngleZ)
    }

    fun addLeftShoulder(leftShoulderAngleX : Float, leftShoulderAngleZ : Float)
    {
        this.robotPosition = this.robotPosition.addLeftShoulder(leftShoulderAngleX, leftShoulderAngleZ)
    }

    fun leftElbow(leftElbowAngleX : Float)
    {
        this.robotPosition = this.robotPosition.leftElbow(leftElbowAngleX)
    }

    fun addLeftElbow(leftElbowAngleX : Float)
    {
        this.robotPosition = this.robotPosition.addLeftElbow(leftElbowAngleX)
    }

    fun rightAss(rightAssAngleX : Float, rightAssAngleZ : Float)
    {
        this.robotPosition = this.robotPosition.rightAss(rightAssAngleX, rightAssAngleZ)
    }

    fun addRightAss(rightAssAngleX : Float, rightAssAngleZ : Float)
    {
        this.robotPosition = this.robotPosition.addRightAss(rightAssAngleX, rightAssAngleZ)
    }

    fun rightKnee(rightKneeAngleX : Float)
    {
        this.robotPosition = this.robotPosition.rightKnee(rightKneeAngleX)
    }

    fun addRightKnee(rightKneeAngleX : Float)
    {
        this.robotPosition = this.robotPosition.addRightKnee(rightKneeAngleX)
    }

    fun leftAss(leftAssAngleX : Float, leftAssAngleZ : Float)
    {
        this.robotPosition = this.robotPosition.leftAss(leftAssAngleX, leftAssAngleZ)
    }

    fun addLeftAss(leftAssAngleX : Float, leftAssAngleZ : Float)
    {
        this.robotPosition = this.robotPosition.addLeftAss(leftAssAngleX, leftAssAngleZ)
    }

    fun leftKnee(leftKneeAngleX : Float)
    {
        this.robotPosition = this.robotPosition.leftKnee(leftKneeAngleX)
    }

    fun addLeftKnee(leftKneeAngleX : Float)
    {
        this.robotPosition = this.robotPosition.addLeftKnee(leftKneeAngleX)
    }


    /**
     * Remove any object in the right hand
     */
    fun freeRightHand() = this.rightHand.removeAllChildren()

    /**
     * Put something in the right hand
     * @param node Main node of the thing to carry
     */
    fun putOnRightHand(node : Node)
    {
        this.freeRightHand()
        this.rightHand.addChild(node)
    }

    /**
     * Remove any object in the left hand
     */
    fun freeLeftHand()
    {
        this.leftHand.removeAllChildren()
    }

    /**
     * Put something in the left hand
     * @param node Main node of the thing to carry
     */
    fun putOnLeftHand(node : Node)
    {
        this.freeLeftHand()
        this.leftHand.addChild(node)
    }

    /**
     * Remove any object attach on the back
     */
    fun freeBack()
    {
        this.backAttach.removeAllChildren()
    }

    /**
     * Attach something in the back
     * @param node Main node of the thing to attach
     */
    fun putOnBack(node : Node)
    {
        this.freeBack()
        this.backAttach.addChild(node)
    }


    /**
     * Create animation that make the robot to return at it start position
     * @param numberMilliseconds Number milliseconds to return to start position
     * @param interpolation Interpolation to use
     * @return Created animation
     */
    fun startPosition(numberMilliseconds : Long = 32L, interpolation : Interpolation = LinearInterpolation) : Animation
    {
        val animation = RobotAnimation(this)
        animation.add(max(32L, numberMilliseconds), interpolation) { setPosition(RobotPosition()) }
        return animation
    }

    /**
     * Create animation that make robot to walk
     * @param numberMillisecondsPerStep Number milliseconds to make a step
     * @param numberStep Number step to make
     * @return Created animation
     */
    fun walk(numberMillisecondsPerStep : Long = 64L, numberStep : Int = 1) : Animation
    {
        val time = max(64L, numberMillisecondsPerStep);
        val semiTime = time shr 1
        val stepMax = max(1, numberStep)
        val animation = RobotAnimation(this)
        val robotPosition1 = RobotPosition(rightShoulderAngleX = 144f, leftShoulderAngleX = 216f,
                                           rightAssAngleX = 216f, leftAssAngleX = 144f)
        val robotPosition2 = RobotPosition(rightShoulderAngleX = 216f, leftShoulderAngleX = 144f,
                                           rightAssAngleX = 144f, leftAssAngleX = 216f)
        var keyTime = semiTime
        animation.add(keyTime) { setPosition(robotPosition1) }
        var step = 1

        while (step < stepMax)
        {
            keyTime += time
            animation.add(keyTime) { setPosition(if ((step and 1) == 0) robotPosition1 else robotPosition2) }
            step ++
        }

        keyTime += semiTime
        animation.add(keyTime) { setPosition(RobotPosition()) }
        return animation
    }

    /**
     * Create animation that make robot to run
     * @param numberMillisecondsPerStep Number milliseconds to make a step
     * @param numberStep Number step to make
     * @return Created animation
     */
    fun run(numberMillisecondsPerStep : Long = 32L, numberStep : Int = 1) : Animation
    {
        val frame = max(32L, numberMillisecondsPerStep);
        val semiFrame = frame shr 1
        val portion = 5
        val part = frame / portion
        val left = frame - part
        val angle = (36 * portion) / (portion + 1)
        val stepMax = max(1, numberStep)
        val animation = RobotAnimation(this)
        val robotPosition1 = RobotPosition(rightShoulderAngleX = 180f - angle, leftShoulderAngleX = 180f + angle,
                                           rightElbowAngleX = - 90f, leftElbowAngleX = - 90f,
                                           rightAssAngleX = 180f + angle, leftAssAngleX = 90f,
                                           rightKneeAngleX = 0f, leftKneeAngleX = 90f)
        val robotPosition2 = RobotPosition(rightShoulderAngleX = 144f, leftShoulderAngleX = 216f,
                                           rightElbowAngleX = - 90f, leftElbowAngleX = - 90f,
                                           rightAssAngleX = 216f, leftAssAngleX = 144f)
        val robotPosition3 = RobotPosition(rightShoulderAngleX = 180f + angle, leftShoulderAngleX = 180f - angle,
                                           rightElbowAngleX = - 90f, leftElbowAngleX = - 90f,
                                           rightAssAngleX = 90f, leftAssAngleX = 180f + angle,
                                           rightKneeAngleX = 90f, leftKneeAngleX = 0f)
        val robotPosition4 = RobotPosition(rightShoulderAngleX = 216f, leftShoulderAngleX = 144f,
                                           rightElbowAngleX = - 90f, leftElbowAngleX = - 90f,
                                           rightAssAngleX = 144f, leftAssAngleX = 216f)
        var key = semiFrame
        animation.add(key) { setPosition(robotPosition1) }
        var step = 1

        while (step < stepMax)
        {
            key += left
            animation.add(key) { setPosition(if ((step and 1) == 0) robotPosition1 else robotPosition3) }
            key += part
            animation.add(key) { setPosition(if ((step and 1) == 0) robotPosition2 else robotPosition4) }
            step ++
        }

        key += semiFrame
        animation.add(key) { setPosition(RobotPosition()) }
        return animation
    }

    private fun applyPosition()
    {
        this.neck.angleX = this.robotPosition.neckAngleX
        this.neck.angleY = this.robotPosition.neckAngleY
        this.neck.angleZ = this.robotPosition.neckAngleZ

        this.rightShoulder.angleX = this.robotPosition.rightShoulderAngleX
        this.rightShoulder.angleZ = this.robotPosition.rightShoulderAngleZ

        this.rightAfterArm.angleX = this.robotPosition.rightElbowAngleX

        this.leftShoulder.angleX = this.robotPosition.leftShoulderAngleX
        this.leftShoulder.angleZ = this.robotPosition.leftShoulderAngleZ

        this.leftAfterArm.angleX = this.robotPosition.leftElbowAngleX

        this.rightAss.angleX = this.robotPosition.rightAssAngleX
        this.rightAss.angleZ = this.robotPosition.rightAssAngleZ

        this.rightAfterLeg.angleX = this.robotPosition.rightKneeAngleX

        this.leftAss.angleX = this.robotPosition.leftAssAngleX
        this.leftAss.angleZ = this.robotPosition.leftAssAngleZ

        this.leftAfterLeg.angleX = this.robotPosition.leftKneeAngleX
    }
}
