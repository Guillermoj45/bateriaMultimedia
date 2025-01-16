package com.example.bateria_elementos_multimedia.Actividad3

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bateria_elementos_multimedia.R

class Actividad3 : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var selectedAudioUri: Uri? = null
    private var selectedVideoUri: Uri? = null
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actividad3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        videoView = findViewById(R.id.videoView)

        val btnSelectAudio = findViewById<Button>(R.id.btnSelectAudio)
        val btnSelectVideo = findViewById<Button>(R.id.btnSelectVideo)
        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val btnStop = findViewById<Button>(R.id.btnStop)

        btnSelectAudio.setOnClickListener {
            seleccionar("audio/*", REQUEST_CODE_AUDIO)
        }

        btnSelectVideo.setOnClickListener {
            seleccionar("video/*", REQUEST_CODE_VIDEO)
        }

        btnPlay.setOnClickListener {
            reproducir()
        }

        btnStop.setOnClickListener {
            parar()
        }
    }

    private fun seleccionar (type: String, requestCode: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            setType(type)
        }
        startActivityForResult(intent, requestCode)
    }

    private fun reproducir() {
        parar()

        when {
            selectedAudioUri != null -> {
                mediaPlayer = MediaPlayer.create(this, selectedAudioUri).apply { start() }
                videoView.visibility = android.view.View.GONE
            }
            selectedVideoUri != null -> {
                videoView.visibility = android.view.View.VISIBLE
                videoView.setVideoURI(selectedVideoUri)
                videoView.start()
            }
        }
    }

    private fun parar() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null

        if (videoView.isPlaying) {
            videoView.stopPlayback()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val uri = data?.data
            when (requestCode) {
                REQUEST_CODE_AUDIO -> {
                    selectedAudioUri = uri
                    selectedVideoUri = null
                }
                REQUEST_CODE_VIDEO -> {
                    selectedVideoUri = uri
                    selectedAudioUri = null
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_AUDIO = 1
        private const val REQUEST_CODE_VIDEO = 2
    }
}
