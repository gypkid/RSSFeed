package com.example.rssfeed.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RssFeedItem(
        @PrimaryKey(autoGenerate = true) val rssId: Long,
        @ColumnInfo(name = "title") val title: String?,
        @ColumnInfo(name = "description") val description: String?,
        @ColumnInfo(name = "enclosures") var enclosure: String?,
        @ColumnInfo(name = "read") val read: Boolean = false,
        @ColumnInfo(name = "favorite") val favourite:Boolean = false
)