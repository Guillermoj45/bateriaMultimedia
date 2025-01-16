package com.example.bateria_elementos_multimedia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bateria_elementos_multimedia.Actividad1.Actividad1
import com.example.bateria_elementos_multimedia.Actividad2.Actividad2
import com.example.bateria_elementos_multimedia.Actividad3.Actividad3

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.buttActividad1).setOnClickListener {
            val intent = Intent(this, Actividad1::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttActividad2).setOnClickListener {
            val intent = Intent(this, Actividad2::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.buttActividad3).setOnClickListener {
            val intent = Intent(this, Actividad3::class.java)
            startActivity(intent)
        }
    }
}