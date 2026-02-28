package ru.shaxowskiy.notificationservice.listeners

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.shaxowskiy.notificationservice.dto.PostEventDto
import ru.shaxowskiy.notificationservice.dto.SubscribeInfoDto
import ru.shaxowskiy.notificationservice.repository.UserRepository
import ru.shaxowskiy.notificationservice.service.TelegramBotService

@Component
class PostEventListener(
    @Autowired var userRepository: UserRepository,
    var telegramBotService: TelegramBotService
) {

    val channel = Channel<SubscribeInfoDto>()
    val coroutineScopeDefault = CoroutineScope(SupervisorJob() + Dispatchers.Default.limitedParallelism(1))
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
        //TODO rate limit to 30 messages
        coroutineScope{
            coroutineScope.launch {
                val subscribers = userRepository.findSubscribersByUsername(message.author_post)
            }
            subscribers.forEach {subscriberInfo ->
                coroutineScope.launch {
                    telegramBotService.sendNotificationMessage(
                        subscriberInfo.telegram_chat_id,
                        subscriberInfo.username
                    )
                }
            }

            println("Waiting coroutine finish")
        }

    }
}
