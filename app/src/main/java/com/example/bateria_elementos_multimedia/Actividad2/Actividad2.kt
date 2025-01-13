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
import java.io.File
import java.io.IOException

class Actividad2 : AppCompatActivity() {
    private var reproduciendo = false
    private var grabando = false
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
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

    private lateinit var audioFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actividad2)

        // Ajustar los insets del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvContador = findViewById(R.id.Contador)

        // Crear archivo de audio
        audioFile = File(getExternalFilesDir(null), "audio.3gp")
    }

    fun reproducir(view: View) {
        val button = findViewById<Button>(R.id.buttRepro)
        if (reproduciendo) {
            button.text = "Reproducir"
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            reproduciendo = false
        } else {
            button.text = "Reproduciendo"
            segundos = 0
            handler.post(runnable)
            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(audioFile.absolutePath)
                    prepare()
                    start()
                }
                reproduciendo = true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun grabacion(view: View) {
        val button = findViewById<Button>(R.id.buttGrab)
        if (grabando) {
            detenerGrabacion(button)
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0
                )
                button.text = "Sin permisos"
            } else {
                iniciarGrabacion(button)
            }
        }
    }

    private fun iniciarGrabacion(button: Button) {
        button.text = "Grabando"
        try {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(audioFile.absolutePath)
                prepare()
                start()
            }
            segundos = 0
            handler.post(runnable)
            grabando = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun detenerGrabacion(button: Button) {
        button.text = "Grabar"
        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            handler.removeCallbacks(runnable)
            grabando = false
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            iniciarGrabacion(findViewById(R.id.buttGrab))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaRecorder?.release()
        handler.removeCallbacks(runnable)
    }
}
