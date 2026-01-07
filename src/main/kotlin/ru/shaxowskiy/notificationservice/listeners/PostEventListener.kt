package ru.shaxowskiy.notificationservice.listeners

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import ru.shaxowskiy.notificationservice.dto.PostEventDto
import ru.shaxowskiy.notificationservice.repository.UserRepository
import ru.shaxowskiy.notificationservice.service.TelegramBotService
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Component
class PostEventListener(
    @Autowired var userRepository: UserRepository,
    var telegramBotService: TelegramBotService
) {

    val workerPool: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    @KafkaListener(topics = ["PUBLISHPOSTEVENT"], groupId = "notification-group")
    fun listen(message: PostEventDto) {
        val task: Runnable = Runnable {
            handleMessage(message) //FIXME ack successfully get kafka event (mb user inner support using VT)
        }
        workerPool.submit(task)
    }

    private fun handleMessage(message: PostEventDto) {
        val findSubscribersByUsername = userRepository.findSubscribersByUsername(message.author_post)

        findSubscribersByUsername.forEach { subscriberInfo ->
            telegramBotService.sendNotificationMessage(
                subscriberInfo.telegram_chat_id,
                subscriberInfo.username
            )
        }
    }
}
