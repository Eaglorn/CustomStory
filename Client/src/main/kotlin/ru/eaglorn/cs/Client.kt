package ru.eaglorn.cs

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Client : Application() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun start(stage: Stage) {
        val fxmlLoader =
            FXMLLoader(Client::class.java.getResource("Application.fxml"))
        val scene = Scene(fxmlLoader.load())
        stage.title = "CustomStoryClient"
        stage.scene = scene
        stage.show()

        GlobalScope.launch(Dispatchers.IO) {
            connectToServer()
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

