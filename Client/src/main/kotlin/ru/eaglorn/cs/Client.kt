package ru.eaglorn.cs

import javafx.application.Application
import javafx.stage.Stage
import kotlinx.coroutines.runBlocking
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import kotlinx.coroutines.launch

class ClientApplication : Application() {
    override fun start(stage: Stage) {
        runBlocking {
            launch {
                val fxmlLoader =
                    FXMLLoader(ClientApplication::class.java.getResource("Application.fxml"))
                val scene = Scene(fxmlLoader.load())
                stage.title = "CustomStoryClient"
                stage.scene = scene
                stage.show()


            }
        }
    }
}

fun main() {
    Application.launch(ClientApplication::class.java)
}
