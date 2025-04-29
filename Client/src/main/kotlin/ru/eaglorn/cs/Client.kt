package ru.eaglorn.cs

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.readFully
import io.ktor.utils.io.readInt
import io.ktor.utils.io.writeFully
import javafx.application.Application
import javafx.stage.Stage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javafx.fxml.FXMLLoader
import javafx.scene.Scene

class Client : Application() {
    override fun start(stage: Stage) {
        runBlocking {
            launch {
                try {
                    val fxmlLoader =
                        FXMLLoader(Client::class.java.getResource("Application.fxml"))
                    val scene = Scene(fxmlLoader.load())
                    stage.title = "CustomStoryClient"
                    stage.scene = scene
                    stage.show()
                } catch (e: Exception) {
                    println(e.message)
                }
                connectToServer()
            }
        }
    }

    private suspend fun connectToServer() {
        val selectorManager = SelectorManager(Dispatchers.IO)
        val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", 9002)

        val receiveChannel = socket.openReadChannel()
        val sendChannel = socket.openWriteChannel(autoFlush = true)

        try {
            val size = receiveChannel.readInt()
            val compressedData = ByteArray(size)
            receiveChannel.readFully(compressedData)

            val decompressedData = ZstdHelper.decompress(compressedData)
            val welcomeMessage = Message.ChatMessage.parseFrom(decompressedData)
            println("Server: ${welcomeMessage.message}")

            val chatMessage = Message.ChatMessage.newBuilder()
                .setName("Client")
                .setMessage("Hello, Server!")
                .build()

            val compressedMessage = ZstdHelper.compress(chatMessage.toByteArray())
            sendChannel.writeFully(compressedMessage.size.toByteArray())
            sendChannel.writeFully(compressedMessage)

            while (true) {
                val messageSize = receiveChannel.readInt()
                val messageData = ByteArray(messageSize)
                receiveChannel.readFully(messageData)

                val decompressedMessage = ZstdHelper.decompress(messageData)
                val serverMessage = Message.ChatMessage.parseFrom(decompressedMessage)
                println("Server: ${serverMessage.name}: ${serverMessage.message}")
            }
        } catch (e: Throwable) {
            println("Connection error: ${e.message}")
            socket.close()
        }
    }

    private fun Int.toByteArray(): ByteArray {
        return byteArrayOf(
            (this shr 24).toByte(),
            (this shr 16).toByte(),
            (this shr 8).toByte(),
            this.toByte()
        )
    }
}

fun main() {
    Application.launch(Client::class.java)
}
