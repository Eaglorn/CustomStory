package ru.eaglorn.cs

import javafx.application.Application
import javafx.stage.Stage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.writeFully
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

                val selectorManager = SelectorManager(Dispatchers.IO)
                val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", 9002)
                println("Connected to server")

                val sendChannel = socket.openWriteChannel(autoFlush = true)
                sendChannel.writeFully("Client connected successfully".toByteArray())

                socket.close()
            }
        }
    }
}

fun main() {
    Application.launch(ClientApplication::class.java)
}
