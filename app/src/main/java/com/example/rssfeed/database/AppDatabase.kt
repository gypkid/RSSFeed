package com.example.rssfeed.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(User::class, RssFeedItem::class), version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun rssFeedItemDao(): RssFeedItemDao
}