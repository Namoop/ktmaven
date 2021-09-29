package tutorial

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.input.UserAction
import javafx.geometry.Point2D
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration


class KotlinGameApp : GameApplication() {

    enum class Type {
        BULLET, TARGET
    }
    private var position = 0.0
    lateinit var sprite:Entity
    val spriteSize = 50.0;

    override fun initSettings(settings: GameSettings) {
        with(settings) {
            width = 720
            height = 640
            title = "Kotlin Game - Target Practice"
        }
    }

    override fun initInput() {
        onKeyDown(KeyCode.SPACE, "Shoot") {
            shoot()
        }
        UserAction moveLeft = new UserAction("moveLeft")
        onKeyDown(KeyCode.A, "moveleft") {
            position -= 10
        }
        onKeyDown(KeyCode.D, "moveright") {
            position += 10
        }

    }

    override fun initGame() {
        position = getAppWidth() / 2.0
        sprite = entityBuilder()
            .type(Type.SPRITE)
            .at(position-(spriteSize/2), getAppHeight().toDouble()-35.0)
            .viewWithBBox(Rectangle(spriteSize, 35.0, Color.GRAY))
            .buildAndAttach()

        run({
            spawnTarget()
        }, Duration.seconds(1.0))
    }

    override fun initPhysics() {
        onCollisionBegin(Type.BULLET, Type.TARGET) { bullet, target ->
            bullet.removeFromWorld()
            target.removeFromWorld()
        }
    }
//coords of spaceship: position, getAppHeight().toDouble()
    private fun shoot() {
        entityBuilder()
            .type(Type.BULLET)
            .at(position, getAppHeight().toDouble())
            .viewWithBBox(Rectangle(10.0, 5.0, Color.BLUE))
            .collidable()
            .with(ProjectileComponent(Point2D(0.0, -1.0), 550.0))
            .buildAndAttach()
    }

    private fun spawnTarget() {
        entityBuilder()
            .type(Type.TARGET)
            .at(0.0, getAppHeight() / 2.0)
            .viewWithBBox(Rectangle(40.0, 40.0, Color.RED))
            .collidable()
            .with(ProjectileComponent(Point2D(1.0, 0.0), 100.0))
            .buildAndAttach()
    }
}

fun main() {
    GameApplication.launch(KotlinGameApp::class.java, emptyArray())
}
