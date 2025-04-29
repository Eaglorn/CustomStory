import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ChatRooms {
    private val rooms = HashMap<String, MutableList<SendChannel<ByteArray>>>()
    private val mutex = Mutex()

    suspend fun joinRoom(roomKey: String, channel: SendChannel<ByteArray>) {
        mutex.withLock {
            rooms.computeIfAbsent(roomKey) { mutableListOf() }.add(channel)
        }
    }

    suspend fun leaveRoom(roomKey: String, channel: SendChannel<ByteArray>) {
        mutex.withLock {
            rooms[roomKey]?.remove(channel)
            if (rooms[roomKey]?.isEmpty() == true) {
                rooms.remove(roomKey)
            }
        }
    }

    suspend fun sendMessageToRoom(roomKey: String, message: ByteArray) {
        mutex.withLock {
            rooms[roomKey]?.forEach { channel ->
                try {
                    channel.writeFully(message)
                } catch (e: Throwable) {
                    println("Error sending message: ${e.message}")
                }
            }
        }
    }
}