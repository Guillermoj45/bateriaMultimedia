package com.example.bateria_elementos_multimedia.Actividad1

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bateria_elementos_multimedia.R

class Actividad1 : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private val REQUEST_CAMERA_PERMISSION = 3
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad1)

        imageView = findViewById(R.id.imageView) // Asegúrate de tener un ImageView en tu diseño
        val buttonCamera = findViewById<Button>(R.id.buttCamara)
        val buttonGallery = findViewById<Button>(R.id.buttGaleria)

        // Botón para abrir la cámara
        buttonCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            } else {
                dispatchTakePictureIntent()
            }
        }

        // Botón para abrir la galería
        buttonGallery.setOnClickListener {
            openGallery()
        }
    }

    // Método para abrir la cámara
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    // Método para abrir la galería
    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK)
    }

    // Manejo de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            }
        }
    }

    // Manejo de resultados de cámara y galería
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    // Resultado de la cámara
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(imageBitmap) // Mostrar imagen en el ImageView
                }

                REQUEST_IMAGE_PICK -> {
                    // Resultado de la galería
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        imageView.setImageURI(selectedImageUri) // Mostrar imagen seleccionada
                    }
                }
            }
        }
    }
}
