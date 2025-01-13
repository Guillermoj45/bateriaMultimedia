package com.example.bateria_elementos_multimedia.Actividad2

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bateria_elementos_multimedia.R
import java.io.IOException

class Actividad2 : AppCompatActivity() {
    private var reproduciendo = false
    private var grabando = false
    private val mediaRecorder = MediaRecorder()
    private val mediaPlayer = MediaPlayer()
    private lateinit var tvContador: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var segundos = 0
    private val runnable = object : Runnable {
        override fun run() {
            segundos++
            tvContador.text = segundos.toString()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actividad2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tvContador = findViewById<TextView>(R.id.Contador)
    }

    fun reproducir(view: View) {
        val button = view.findViewById<Button>(R.id.buttRepro)
        if (reproduciendo) {
            button.text = "Reproducir"
            mediaPlayer.stop()
            mediaPlayer.reset()
            reproduciendo = false
        } else {
            button.text = "Reproduciendo"
            try {
                mediaPlayer.setDataSource("android/media/audio.mp3")
                mediaPlayer.prepare()
                mediaPlayer.start()
                reproduciendo = true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun grabacion(view: View) {
        val button = view.findViewById<Button>(R.id.buttGrab)
        if (grabando) {
            button.text = "Grabar"
            mediaRecorder.stop()
            mediaRecorder.reset()
            handler.removeCallbacks(runnable)
            grabando = false
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
            } else {
                startRecording(button)
            }
        }
    }

    private fun startRecording(button: Button) {
        button.text = "Grabando"
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder.setOutputFile("android/media/audio.mp3")
            mediaRecorder.prepare()
            mediaRecorder.start()
            segundos = 0
            handler.post(runnable)
            grabando = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            val button = findViewById<Button>(R.id.buttGrab)
            startRecording(button)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mediaRecorder.release()
        handler.removeCallbacks(runnable)
    }
}