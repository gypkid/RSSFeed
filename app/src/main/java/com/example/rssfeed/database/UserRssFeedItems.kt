package com.example.rssfeed.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(primaryKeys = ["userId", "rssId"])
data class UserRssFeedItem(
    val userId: Long,
    val rssId: Long
)

data class UserWithRssFeedItems(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "rssId",
        associateBy = Junction(UserRssFeedItem::class)
    )
    val rssFeedItems: List<RssFeedItem>
)
