package com.example.memes

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.media.audiofx.AutomaticGainControl.create
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.cronet.CronetHttpStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File
import java.net.URI.create
import java.util.*
import java.util.Calendar.getInstance
import java.util.concurrent.TimeUnit
import kotlin.Int as Int1

class MainActivity : AppCompatActivity() {
    var currentimageurl: String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }
   private fun loadMeme(){
       // Instantiate the RequestQueue.
       val progressBar: ProgressBar=findViewById(R.id.progressBar)
       progressBar.visibility=View.VISIBLE
       currentimageurl= "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
       val JsonobjectRequest = JsonObjectRequest(
       Request.Method.GET,currentimageurl,null,
           { response ->
                   currentimageurl= response.getString("url")
               val imageView: ImageView=findViewById(R.id.imageView)
               Glide.with(this).load(currentimageurl).listener(object: RequestListener<Drawable> {

                   override fun onLoadFailed(
                       e: GlideException?,
                       model: Any?,
                       target: Target<Drawable>?,
                       isFirstResource: Boolean
                   ): Boolean {
                       progressBar.visibility=View.GONE
                       return false
                   }

                   override fun onResourceReady(
                       resource: Drawable?,
                       model: Any?,
                       target: Target<Drawable>?,
                       dataSource: DataSource?,
                       isFirstResource: Boolean
                   ): Boolean{
                       progressBar.visibility=View.GONE
                       return false
                   }
               }).into(imageView)
               },
           {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()
           }
       )
       MySingleton.getInstance(this).addToRequestQueue(JsonobjectRequest)

   }

    fun nextMeme(view: View) {
       loadMeme()
    }
    fun shareMeme(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Dekh Na RE Baba Kitna Mast Maal Bheja Hai $currentimageurl")
        val chooser =Intent.createChooser(intent,"Share This Meme Using...")
        startActivity(chooser)
    }
   fun download(view: View){
       val request: DownloadManager.Request = DownloadManager.Request(
           Uri.parse(currentimageurl))
           .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
       val dm: DownloadManager=getSystemService(DOWNLOAD_SERVICE)as DownloadManager
     var mydownlodid= dm.enqueue(request)
       var br= object:BroadcastReceiver() {
           override fun onReceive(p0: Context?, p1: Intent?) {
               var id =p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1)
               if(id==mydownlodid){
                   Toast.makeText(applicationContext, "Downloaded", Toast.LENGTH_SHORT).show()
               }
           }
       }
       registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
   }
    fun heart(view: View) {}
}