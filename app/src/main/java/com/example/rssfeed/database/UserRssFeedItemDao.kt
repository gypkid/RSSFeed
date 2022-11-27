package com.example.rssfeed.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserRssFeedItemDao {
    @Transaction
    @Query("SELECT * FROM User")
    fun getUsersWithRssFeedItems(): List<UserRssFeedItem>

    @Transaction
    @Query("SELECT * FROM User WHERE userid == :userIds")
    fun getUserWithRssFeedItems(userIds: Long): List<UserRssFeedItem>
}