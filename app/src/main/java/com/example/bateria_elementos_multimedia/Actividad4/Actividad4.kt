package com.example.bateria_elementos_multimedia.Actividad4

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bateria_elementos_multimedia.R

class Actividad4 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actividad4)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val gridView: GridView = findViewById(R.id.gridView)
        val selectedImageView: ImageView = findViewById(R.id.selectedImageView)
        val imagePathTextView: TextView = findViewById(R.id.imagePathTextView)


        val imageList = getImageUris()

        val adapter = ImageAdapter(this, imageList)
        gridView.adapter = adapter

        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val imageUri = imageList[position]
            selectedImageView.setImageURI(imageUri)
            selectedImageView.visibility = android.view.View.VISIBLE
            imagePathTextView.text = imageUri.toString()
            imagePathTextView.visibility = android.view.View.VISIBLE
        }
    }

    private fun getImageUris(): List<Uri> {
        val imageUris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)

        val cursor: Cursor? = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                imageUris.add(contentUri)
            }
        }

        return imageUris
    }
}
