package khelp.uno.game.play

import khelp.engine3d.animation.Animation
import khelp.engine3d.animation.AnimationAtomicTasks
import khelp.engine3d.animation.AnimationList
import khelp.engine3d.animation.AnimationManager
import khelp.engine3d.animation.AnimationNodePosition
import khelp.engine3d.animation.AnimationTask
import khelp.engine3d.animation.DecelerationInterpolation
import khelp.engine3d.extensions.position
import khelp.engine3d.render.Material
import khelp.engine3d.render.Node
import khelp.engine3d.render.Window3D
import khelp.engine3d.render.prebuilt.Plane
import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.thread.observable.Observer
import khelp.thread.parallel
import khelp.ui.events.MouseState
import khelp.uno.game.GameAction
import khelp.uno.game.GameScreen
import khelp.uno.model.Card
import khelp.uno.model.CardChangeColor
import khelp.uno.model.CardColor
import khelp.uno.model.CardMore4
import khelp.uno.model.CardPlay
import khelp.uno.model.CardStack
import khelp.uno.model.CardValue
import khelp.uno.ui.Uno
import khelp.uno.ui.UnoTextures
import khelp.utilities.math.random

class GamePlayScreen : GameScreen
{
    companion object
    {
        private const val DISTRIBUTE_GIVE_DURATION = 512L
        private const val DISTRIBUTE_RETURN_DURATION = GamePlayScreen.DISTRIBUTE_GIVE_DURATION / 2L
    }

    private val player = Player(true)
    private val computerLeft = Player(false)
    private val computerFront = Player(false)
    private val computerRight = Player(false)
    private var turn = Turn.PLAYER
    private var takeStack = CardStack()
    private val playStack = CardStack()
    private var trigonometricWay = true
    private var promiseEndGame = Promise<GameAction>()
    private var promiseDistribute = Promise<Unit>()
    private var lastPlayerCard : Card? = null
    private lateinit var window3D : Window3D
    private lateinit var mouseObserver : Observer<MouseState>

    private val materialWay : Material by lazy {
        val material = Material()
        material.settingAsFor2D()
        material.textureDiffuse = UnoTextures.trigonometricWayTexture
        material
    }

    private val planeWay : Plane by lazy {
        val plane = Plane("way")
        plane.material = this.materialWay
        plane.materialForSelection = this.materialWay
        plane.z = - 20f
        plane.scaleX = 20f
        plane.scaleY = 20f
        plane.scaleZ = 1f
        plane
    }

    private val nodeTakeStack : Node by lazy {
        val node = Node("takeStack")
        node.x = - 1f
        node.y = 0f
        node.z = - 5f
        node
    }

    private val nodePlayStack : Node by lazy {
        val node = Node("playStack")
        node.x = 1f
        node.y = 0f
        node.z = - 5f
        node
    }

    private val nodePlayer : Node by lazy {
        val node = Node("player")
        node.x = 0f
        node.y = - 3f
        node.z = - 5f
        node
    }

    private val planePlayerBlocked : Plane by lazy {
        val plane = Plane("playerBlocked")
        plane.scaleX = 2f
        plane.scaleY = 2f
        plane.scaleZ = 1f
        plane.material.settingAsFor2D()
        plane.material.textureDiffuse = UnoTextures.forbiddenTexture
        plane.materialForSelection = plane.material
        plane
    }

    private val nodeComputerLeft : Node by lazy {
        val node = Node("computerLeft")
        node.x = - 6f
        node.y = 0f
        node.z = - 5f
        node
    }

    private val planeComputerLeftBlocked : Plane by lazy {
        val plane = Plane("computerLeftBlocked")
        plane.scaleX = 2f
        plane.scaleY = 2f
        plane.scaleZ = 1f
        plane.material.settingAsFor2D()
        plane.material.textureDiffuse = UnoTextures.forbiddenTexture
        plane.materialForSelection = plane.material
        plane
    }

    private val nodeComputerFront : Node by lazy {
        val node = Node("computerFront")
        node.x = 0f
        node.y = 3f
        node.z = - 5f
        node
    }

    private val planeComputerFrontBlocked : Plane by lazy {
        val plane = Plane("computerFrontBlocked")
        plane.scaleX = 2f
        plane.scaleY = 2f
        plane.scaleZ = 1f
        plane.material.settingAsFor2D()
        plane.material.textureDiffuse = UnoTextures.forbiddenTexture
        plane.materialForSelection = plane.material
        plane
    }

    private val nodeComputerRight : Node by lazy {
        val node = Node("computerRight")
        node.x = 6f
        node.y = 0f
        node.z = - 5f
        node
    }

    private val planeComputerRightBlocked : Plane by lazy {
        val plane = Plane("computerRightBlocked")
        plane.scaleX = 2f
        plane.scaleY = 2f
        plane.scaleZ = 1f
        plane.material.settingAsFor2D()
        plane.material.textureDiffuse = UnoTextures.forbiddenTexture
        plane.materialForSelection = plane.material
        plane
    }

    override fun play(window3D : Window3D) : FutureResult<GameAction>
    {
        this.window3D = window3D
        this.initialize(window3D)
        this.updateStacks()
        this.distribute(window3D)
            .and { this.playTurn(this.turnInfo(this.turn)) }
        return this.promiseEndGame.futureResult
    }

    private fun initialize(window3D : Window3D)
    {
        this.mouseObserver =
            window3D
                .mouseManager
                .mouseStateObservable
                .observedBy(TaskContext.INDEPENDENT, this::mouseState)
        this.player.initialize()
        this.computerLeft.initialize()
        this.computerFront.initialize()
        this.computerRight.initialize()
        this.takeStack = CardStack.fullStack()
        this.playStack.clear()
        this.trigonometricWay = random()
        this.turn = random<Turn>()
        this.updateWay()

        val scene = window3D.scene
        scene.root.addChild(this.planeWay)
        scene.root.addChild(this.nodeTakeStack)
        scene.root.addChild(this.nodePlayStack)
        scene.root.addChild(this.nodePlayer)
        scene.root.addChild(this.nodeComputerLeft)
        scene.root.addChild(this.nodeComputerFront)
        scene.root.addChild(this.nodeComputerRight)

        window3D.nodePickedFlow.then(TaskContext.INDEPENDENT, this::nodeSelected)
    }

    private fun distribute(window3D : Window3D) : FutureResult<Unit>
    {
        this.promiseDistribute = Promise<Unit>()

        val animationList = AnimationList()

        for (time in 0 until 4 * 7)
        {
            val card = this.takeStack.removeAt(0)

            when (this.turn)
            {
                Turn.PLAYER         ->
                    animationList.addAnimation(
                        this.createAnimationDistribute(card, this.player, this.nodePlayer, window3D,
                                                       this::updatePlayerStack))
                Turn.COMPUTER_LEFT  ->
                    animationList.addAnimation(
                        this.createAnimationDistribute(card, this.computerLeft, this.nodeComputerLeft, window3D,
                                                       this::updateComputerLeftStack))
                Turn.COMPUTER_FRONT ->
                    animationList.addAnimation(
                        this.createAnimationDistribute(card, this.computerFront, this.nodeComputerFront, window3D,
                                                       this::updateComputerFrontStack))
                Turn.COMPUTER_RIGHT ->
                    animationList.addAnimation(
                        this.createAnimationDistribute(card, this.computerRight, this.nodeComputerRight, window3D,
                                                       this::updateComputerRightStack))
            }

            this.turn = this.turn.next(this.trigonometricWay)
        }

        var card = this.takeStack.removeAt(0)

        while (card.points > 9)
        {
            this.takeStack.add(card)
            card = this.takeStack.removeAt(0)
        }

        animationList.addAnimation(this.createAnimationReturnCard(card, window3D))
        animationList.addAnimation(AnimationTask { this.promiseDistribute.result(Unit) })

        AnimationManager.addAnimation("Distribute", animationList)
        AnimationManager.play("Distribute")

        return this.promiseDistribute.futureResult
    }

    private fun createAnimationDistribute(card : Card, player : Player, destination : Node, window3D : Window3D,
                                          update : () -> Unit) : Animation
    {
        val node = card.node
        node.position(this.nodeTakeStack.position)
        node.angleY = 180f

        val angleY = if (player.human) 0f else 180f
        val destinationPosition = destination.position.copy()
        val animationList = AnimationList()
        val animation = AnimationNodePosition(node)
        animation.add(GamePlayScreen.DISTRIBUTE_GIVE_DURATION, DecelerationInterpolation(2.0)) {
            this.x = destinationPosition.x
            this.y = destinationPosition.y
            this.z = destinationPosition.z
            this.angleY = angleY
        }

        animationList.addAnimation(AnimationAtomicTasks(
            { window3D.scene.root.addChild(node) },
            { this.updateTakeStack() }
        ))
        animationList.addAnimation(animation)
        animationList.addAnimation(AnimationAtomicTasks(
            { window3D.scene.root.removeChild(node) },
            { player.stack.add(card) },
            { update() }
        ))

        return animationList
    }

    private fun playCard(card : Card, player : Player, source : Node, window3D : Window3D,
                         update : () -> Unit) : FutureResult<Unit>
    {
        val promise = Promise<Unit>()
        player.stack.remove(card)

        when (card)
        {
            is CardMore4       ->
                if (player.human)
                {
                    Uno.selectColorForMore4()
                        .and { cardToPlay ->
                            this.playCardAnimation(cardToPlay, player, source, window3D, update, promise)
                        }
                }
                else
                {
                    val color = this.computerChooseAColor(player)
                    this.playCardAnimation(CardPlay(color, CardValue.MORE_4), player, source, window3D, update, promise)
                }
            is CardChangeColor ->
                if (player.human)
                {
                    Uno.selectColorForChangeColor()
                        .and { cardToPlay ->
                            this.playCardAnimation(cardToPlay, player, source, window3D, update, promise)
                        }
                }
                else
                {
                    val color = this.computerChooseAColor(player)
                    this.playCardAnimation(CardPlay(color, CardValue.COLOR), player, source, window3D, update, promise)
                }
            else               ->
                this.playCardAnimation(card, player, source, window3D, update, promise)
        }


        return promise.futureResult
    }

    private fun computerChooseAColor(player : Player) : CardColor
    {
        var blue = 0
        var green = 0
        var red = 0
        var yellow = 0

        for (card in player.stack)
        {
            if (card is CardPlay)
            {
                when (card.color)
                {
                    CardColor.BLUE   -> blue ++
                    CardColor.GREEN  -> green ++
                    CardColor.RED    -> red ++
                    CardColor.YELLOW -> yellow ++
                }
            }
        }

        return when
        {
            blue >= green && blue >= red && blue >= yellow -> CardColor.BLUE
            green >= red && green >= yellow                -> CardColor.GREEN
            red >= yellow                                  -> CardColor.RED
            else                                           -> CardColor.YELLOW
        }
    }

    private fun playCardAnimation(card : Card, player : Player, source : Node, window3D : Window3D,
                                  update : () -> Unit, promise : Promise<Unit>)
    {
        val node = card.node

        val animationList = AnimationList()
        val animation = AnimationNodePosition(node)
        animation.add(GamePlayScreen.DISTRIBUTE_GIVE_DURATION, DecelerationInterpolation(2.0)) {
            this.x = this@GamePlayScreen.nodePlayStack.x
            this.y = this@GamePlayScreen.nodePlayStack.y
            this.z = this@GamePlayScreen.nodePlayStack.z
            this.angleY = 0f
        }

        animationList.addAnimation(AnimationAtomicTasks(
            { update() },
            { node.position(source.position) },
            { node.angleY = if (player.human) 0f else 180f },
            { window3D.scene.root.addChild(node) }
        ))
        animationList.addAnimation(animation)
        animationList.addAnimation(AnimationAtomicTasks(
            { window3D.scene.root.removeChild(node) },
            { this.playStack.add(card) },
            { this.updatePlayStack() },
            { promise.result(Unit) }
        ))

        AnimationManager.addAnimation("playCard", animationList)
        AnimationManager.play("playCard")
    }

    private fun createAnimationReturnCard(card : Card, window3D : Window3D) : Animation
    {
        val node = card.node
        node.position(this.nodeTakeStack.position)
        node.angleY = 180f

        val destinationPosition = this.nodePlayStack.position.copy()
        val animationList = AnimationList()
        val animation = AnimationNodePosition(node)
        animation.add(GamePlayScreen.DISTRIBUTE_RETURN_DURATION, DecelerationInterpolation(2.0)) {
            this.x = destinationPosition.x
            this.y = destinationPosition.y
            this.z = destinationPosition.z
            this.angleY = 0f
        }

        animationList.addAnimation(AnimationAtomicTasks(
            { window3D.scene.root.addChild(node) },
            { this.updateTakeStack() }
        ))
        animationList.addAnimation(animation)
        animationList.addAnimation(AnimationAtomicTasks(
            { window3D.scene.root.removeChild(node) },
            { this.playStack.add(card) },
            { this.updatePlayStack() }
        ))

        return animationList
    }

    private fun updateWay()
    {
        if (this.trigonometricWay)
        {
            this.materialWay.textureDiffuse = UnoTextures.trigonometricWayTexture
        }
        else
        {
            this.materialWay.textureDiffuse = UnoTextures.clockWayTexture
        }
    }

    private fun updateStacks()
    {
        this.updateTakeStack()
        this.updatePlayStack()
        this.updatePlayerStack()
        this.updateComputerLeftStack()
        this.updateComputerFrontStack()
        this.updateComputerRightStack()
    }

    private fun updateTakeStack()
    {
        this.nodeTakeStack.removeAllChildren()

        if (this.takeStack.notEmpty)
        {
            val cardNode = this.takeStack[0].node
            cardNode.x = 0f
            cardNode.y = 0f
            cardNode.z = 0f
            cardNode.angleY = - 180f
            this.nodeTakeStack.addChild(cardNode)
        }
    }

    private fun updatePlayStack()
    {
        this.nodePlayStack.removeAllChildren()

        if (this.playStack.notEmpty)
        {
            val cardNode = this.playStack[this.playStack.size - 1].node
            cardNode.x = 0f
            cardNode.y = 0f
            cardNode.z = 0f
            cardNode.angleY = 0f
            this.nodePlayStack.addChild(cardNode)
        }
    }

    private fun updatePlayerStack()
    {
        this.nodePlayer.removeAllChildren()
        val size = this.player.stack.size
        var space = 0f
        var startX = 0f

        if (size < 6)
        {
            space = 2f
            startX = 1f - size.toFloat()
        }
        else
        {
            space = 10f / (size.toFloat() - 1f)
            startX = - 5f
        }

        var x = startX

        for ((index, card) in this.player.stack.withIndex())
        {
            val node = card.node
            node.x = x
            node.y = 0f
            node.z = (index.toFloat() - size.toFloat()) / 100f
            this.nodePlayer.addChild(node)
            x += space
        }

        if (! this.player.canPlay)
        {
            this.nodePlayer.addChild(this.planePlayerBlocked)
        }
    }

    private fun updateComputerLeftStack()
    {
        this.nodeComputerLeft.removeAllChildren()
        val size = this.computerLeft.stack.size
        var space = 0f
        var startY = 0f

        if (size < 6)
        {
            space = 1f
            startY = 1f - size.toFloat() / 2f
        }
        else
        {
            space = 5f / (size.toFloat() - 1f)
            startY = - 2.5f
        }

        var y = startY

        for ((index, card) in this.computerLeft.stack.withIndex())
        {
            val node = card.node
            node.angleY = - 180f
            node.x = 0f
            node.y = y
            node.z = (index.toFloat() - size.toFloat()) / 100f
            this.nodeComputerLeft.addChild(node)
            y += space
        }

        if (! this.computerLeft.canPlay)
        {
            this.nodeComputerLeft.addChild(this.planeComputerLeftBlocked)
        }
    }

    private fun updateComputerRightStack()
    {
        this.nodeComputerRight.removeAllChildren()
        val size = this.computerRight.stack.size
        var space = 0f
        var startY = 0f

        if (size < 6)
        {
            space = 1f
            startY = 1f - size.toFloat() / 2f
        }
        else
        {
            space = 5f / (size.toFloat() - 1f)
            startY = - 2.5f
        }

        var y = startY

        for ((index, card) in this.computerRight.stack.withIndex())
        {
            val node = card.node
            node.angleY = - 180f
            node.x = 0f
            node.y = y
            node.z = (index.toFloat() - size.toFloat()) / 100f
            this.nodeComputerRight.addChild(node)
            y += space
        }

        if (! this.computerRight.canPlay)
        {
            this.nodeComputerRight.addChild(this.planeComputerRightBlocked)
        }
    }

    private fun updateComputerFrontStack()
    {
        this.nodeComputerFront.removeAllChildren()
        val size = this.computerFront.stack.size
        var space = 0f
        var startX = 0f

        if (size < 6)
        {
            space = 2f
            startX = 1f - size.toFloat()
        }
        else
        {
            space = 10f / (size.toFloat() - 1f)
            startX = - 5f
        }

        var x = startX

        for ((index, card) in this.computerFront.stack.withIndex())
        {
            val node = card.node
            node.x = x
            node.y = 0f
            node.z = (index.toFloat() - size.toFloat()) / 100f
            node.angleY = 180f
            this.nodeComputerFront.addChild(node)
            x += space
        }

        if (! this.computerFront.canPlay)
        {
            this.nodeComputerFront.addChild(this.planeComputerFrontBlocked)
        }
    }

    private fun nodeSelected(node : Node?)
    {
        if (node == null)
        {
            this.lastPlayerCard = null
            return
        }

        this.lastPlayerCard = this.player.stack.firstOrNull { card -> card.id == node.id } ?: return
        //  debug("it is a player card : ${lastPlayerCard?.name()}")
    }

    private fun mouseState(mouseState : MouseState)
    {
        val card = this.lastPlayerCard ?: return

        if (this.turn != Turn.PLAYER || ! this.player.canPlay || ! mouseState.clicked)
        {
            return
        }

        when (card)
        {
            is CardMore4, is CardChangeColor ->
                this.playCard(card, this.player, this.nodePlayer, this.window3D, this::updatePlayerStack)
                    .and { this.cardPlayed(card) }

            is CardPlay                      ->
            {
                val cardReference = this.playStack[this.playStack.size - 1] as CardPlay

                if (card.color == cardReference.color || card.value == cardReference.value)
                {
                    this.playCard(card, this.player, this.nodePlayer, this.window3D, this::updatePlayerStack)
                        .and { this.cardPlayed(card) }
                }
            }

            else                             -> Unit
        }
    }

    private fun cardPlayed(card : Card)
    {
        when (card)
        {
            is CardMore4 -> this.giveCardToNext(4)
                .and { this.nextTurn() }
            is CardPlay  ->
                when (card.value)
                {
                    CardValue.MORE_4 ->
                        this.giveCardToNext(4)
                            .and { this.nextTurn() }

                    CardValue.MORE_2 ->
                        this.giveCardToNext(2)
                            .and { this.nextTurn() }

                    CardValue.RETURN ->
                    {
                        this.trigonometricWay = ! this.trigonometricWay
                        this.updateWay()
                        this.nextTurn()
                    }

                    CardValue.SKIP   ->
                    {
                        this.blockNext()
                        this.nextTurn()
                    }

                    else             -> this.nextTurn()
                }

            else         -> this.nextTurn()
        }
    }

    private fun turnInfo(turn : Turn) : TurnInfo =
        when (turn)
        {
            Turn.PLAYER         -> TurnInfo(this.player, this.nodePlayer, this::updatePlayerStack)
            Turn.COMPUTER_LEFT  -> TurnInfo(this.computerLeft, this.nodeComputerLeft, this::updateComputerLeftStack)
            Turn.COMPUTER_FRONT -> TurnInfo(this.computerFront, this.nodeComputerFront, this::updateComputerFrontStack)
            Turn.COMPUTER_RIGHT -> TurnInfo(this.computerRight, this.nodeComputerRight, this::updateComputerRightStack)
        }

    private fun computeNextAlive() : Turn
    {
        var turn = this.turn.next(this.trigonometricWay)
        var turnInfo = turnInfo(turn)

        while (turnInfo.player.coins <= 0)
        {
            turn = turn.next(this.trigonometricWay)
            turnInfo = turnInfo(turn)
        }

        return turn
    }

    private fun nextTurn()
    {
        this.turn = this.computeNextAlive()
        var turnInfo = turnInfo(this.turn)

        while (turnInfo.player.blocked)
        {
            turnInfo.player.blocked = false
            this.updateStacks()
            this.turn = this.computeNextAlive()
            turnInfo = turnInfo(this.turn)
        }

        this.playTurn(turnInfo)
    }

    private fun playTurn(turnInfo : TurnInfo)
    {
        val possibles = ArrayList<Card>()
        val cardReference = this.playStack[this.playStack.size - 1] as CardPlay

        for (card in turnInfo.player.stack)
        {
            if (card is CardPlay)
            {
                if (card.color == cardReference.color || card.value == cardReference.value)
                {
                    possibles.add(card)
                }
            }
            else
            {
                possibles.add(card)
            }
        }

        if (possibles.isNotEmpty())
        {
            if (! turnInfo.player.human)
            {
                this.computerChooseCard(possibles, turnInfo)
            }

            return
        }

        this.playTakeOneCard(turnInfo)
            .and { this.nextTurn() }
    }

    private fun playTakeOneCard(turnInfo : TurnInfo) : FutureResult<Unit>
    {
        val promise = Promise<Unit>()
        parallel {
            this.fillTakeStackIfEmpty()
            val card = this.takeStack.removeAt(0)
            val animationList = AnimationList()
            animationList.addAnimation(AnimationAtomicTasks({ this.updateTakeStack() }))
            animationList.addAnimation(
                this.createAnimationDistribute(card, turnInfo.player, turnInfo.node, this.window3D, turnInfo.update))
            animationList.addAnimation(AnimationTask { promise.result(Unit) })
            AnimationManager.addAnimation("TakeOneCard", animationList)
            AnimationManager.play("TakeOneCard")
        }
        return promise.futureResult
    }

    private fun fillTakeStackIfEmpty()
    {
        if (this.takeStack.empty)
        {
            for (card in this.playStack)
            {
                if (card is CardPlay)
                {
                    when (card.value)
                    {
                        CardValue.MORE_4 -> this.takeStack.add(CardMore4())
                        CardValue.COLOR  -> this.takeStack.add(CardChangeColor())
                        else             -> this.takeStack.add(card)
                    }
                }
                else
                {
                    this.takeStack.add(card)
                }
            }

            this.playStack.clear()
            this.takeStack.shuffle()
            this.updateStacks()
        }
    }

    private fun giveCardToNext(number : Int) : FutureResult<Unit>
    {
        val nextTurnInfo = this.turnInfo(this.computeNextAlive())

        var futureResult = this.playTakeOneCard(nextTurnInfo)

        for (time in 1 until number)
        {
            futureResult = futureResult.andUnwrap { this.playTakeOneCard(nextTurnInfo) }
        }

        return futureResult.and {
            nextTurnInfo.player.blocked = true
            this.updateStacks()
        }
    }

    private fun blockNext()
    {
        val nextTurnInfo = this.turnInfo(this.computeNextAlive())
        nextTurnInfo.player.blocked = true
        this.updateStacks()
    }

    private fun computerChooseCard(possibles : List<Card>, turnInfo : TurnInfo)
    {
        // TODO better algorithm
        val card = possibles.random()
        this.playCard(card, turnInfo.player, turnInfo.node, this.window3D, turnInfo.update)
            .and { this.cardPlayed(card) }
    }
}
