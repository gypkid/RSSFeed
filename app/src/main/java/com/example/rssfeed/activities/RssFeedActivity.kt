package com.example.rssfeed.activities

import android.Manifest
import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rssfeed.adapters.RssFeedRecyclerViewAdapter
import com.example.rssfeed.database.RssFeedItem
import com.example.rssfeed.utils.RSSFeed
import com.example.rssfeed.databinding.ActivityRssFeedBinding
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_rss_feed.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.URL
import kotlin.coroutines.EmptyCoroutineContext

class RssFeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRssFeedBinding

    val rssFeedUtility : RSSFeed = RSSFeed()
    val rssNewsUrlPoland = "https://www.polsatnews.pl/rss/polska.xml"
    val rssNewsUrlWorld = "https://www.polsatnews.pl/rss/swiat.xml"
    var rssURL = rssNewsUrlPoland
    var result = listOf<RssFeedItem>()
    var rssadapter = RssFeedRecyclerViewAdapter(result)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRssFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.rvRSSFeed.apply {
            layoutManager = LinearLayoutManager(this@RssFeedActivity)
            adapter = rssadapter
        }

    }

    override fun onResume() {
        super.onResume()
        getRssFeed(rssadapter, rssURL)
    }

    override fun onStop() {
        super.onStop()
        Firebase.auth.signOut()
    }

    fun getRssFeed(rssadapter: RssFeedRecyclerViewAdapter, rssURL: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try{
                result = getFeeds(rssURL)
                withContext(Dispatchers.Main){
                    rssadapter.update(result)
                }
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun getFeeds(rssURL: String): List<RssFeedItem>{
        var list = rssFeedUtility.loadXmlFromNetwork(rssURL)!!
        return list
    }

}