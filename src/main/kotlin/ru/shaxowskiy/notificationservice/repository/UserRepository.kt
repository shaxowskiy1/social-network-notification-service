package ru.shaxowskiy.notificationservice.repository

import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import ru.shaxowskiy.notificationservice.dto.SubscribeInfoDto

@Repository
class UserRepository(@Autowired private val dsl: DSLContext) {

    fun insert(chatId: String, username: String) { //TODO change to codegen
        dsl
            .update(table("users"))
            .set(field("telegram_chat_id"), chatId)
            .where(field("telegram_username").eq(username))
            .execute()
    }

    fun findSubscribersByUsername(authorPost: String): List<SubscribeInfoDto> {
        val author = table("users").`as`("author")
        val subscriber = table("users").`as`("subscriber")
        val subs = table("subscriptions").`as`("s")

        return dsl
            .select(field("subscriber.telegram_chat_id").`as`("telegram_chat_id"), field("author.username").`as`("username"))
            .from(author)
            .join(subs).on(field("author.id").eq(field("s.following_id")))
            .join(subscriber).on(field("s.follower_id").eq(field("subscriber.id")))
            .where(field("author.username").eq(authorPost))
            .fetchInto(SubscribeInfoDto::class.java)
    }
}
