package com.example.rssfeed.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RssFeedItemDao {
    @Query("SELECT * FROM rssfeeditem")
    fun getAll(): List<RssFeedItem>

    @Query("SELECT * FROM rssfeeditem WHERE rssId IN (:Id)")
    fun loadAllByIds(Id: IntArray): List<RssFeedItem>

    @Query("SELECT * FROM rssfeeditem WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): RssFeedItem

    @Insert
    fun insertAll(vararg rssfeeditem: RssFeedItem)

    @Delete
    fun delete(rssfeeditem: RssFeedItem)
}