package ru.shaxowskiy.notificationservice.listeners

import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import ru.shaxowskiy.notificationservice.dto.PostEventDto
import ru.shaxowskiy.notificationservice.repository.UserRepository
import ru.shaxowskiy.notificationservice.service.TelegramBotService

@Component
class PostEventListener(
    @Autowired var userRepository: UserRepository,
    var telegramBotService: TelegramBotService
) {

    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    @KafkaListener(topics = ["PUBLISHPOSTEVENT"], groupId = "notification-group")
    fun listen(message: PostEventDto) {
        runBlocking {
            coroutineScope.launch {
                try {
                    handleMessage(message)
                } catch (e: Exception){
                    // TODO LOG AND ERROR
                }
            }
        }
    }

    private suspend fun handleMessage(message: PostEventDto) {
        val subscribers = userRepository.findSubscribersByUsername(message.author_post)
        //rate limit to 30 messages
        coroutineScope{
            subscribers.forEach {subscriberInfo ->
                coroutineScope.launch {
                    telegramBotService.sendNotificationMessage(
                        subscriberInfo.telegram_chat_id,
                        subscriberInfo.username
                    )
                }
            }
        }

    }
}
