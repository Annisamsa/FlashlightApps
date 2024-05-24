package com.annisams.flashlight

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraID: String
    private var flashLightState: Boolean = false
    private lateinit var imageView: ImageView
    private lateinit var turnFlashOffButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        cameraID = cameraManager.cameraIdList[0]

        val isFlashAvailable = applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

        imageView = findViewById(R.id.flash_light_imageView)
        turnFlashOffButton = findViewById(R.id.turn_flash_off_button)

        if (isFlashAvailable){
            turnFlashOffButton.setOnClickListener {
                flashLightState = !flashLightState
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraID, flashLightState)
                        if (flashLightState) {
                            imageView.setImageResource(R.drawable.light_on)
                        } else {
                            imageView.setImageResource(R.drawable.light_off)
                        }
                    }
                } catch (e: CameraAccessException){
                    Toast.makeText(this@MainActivity, "error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            imageView.setOnClickListener {
                flashLightState = !flashLightState

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraID, flashLightState)
                        if (flashLightState) {
                            imageView.setImageResource(R.drawable.light_on)
                        } else {
                            imageView.setImageResource(R.drawable.light_off)
                        }
                    }
                } catch (e: CameraAccessException){
                    Toast.makeText(this@MainActivity, "error: ${e.message}", Toast.LENGTH_SHORT).show()
                }

                if (flashLightState) {
                    turnFlashOffButton.visibility = View.VISIBLE
                } else {
                    turnFlashOffButton.visibility = View.GONE
                }

            }
        } else {
            Toast.makeText(
                this@MainActivity,
                "flash light is not available on this device",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}