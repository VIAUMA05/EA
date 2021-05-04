package hu.forstner.posedetection.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import hu.forstner.posedetection.R
import hu.forstner.posedetection.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private val SQUAT_CHANNEL_ID = "Squat channel"
    private val HANDSUP_CHANNEL_ID = "HandsUp channel"
    private val SQUAT_NOTI_ID = 1
    private val HANDSUP_NOTI_ID = 2


    private val viewModel : MainViewModel by viewModels()

    val SQUAT_PROGRESS_MAX = 10
    val HANDSUP_PROGRESS_MAX = 10


    lateinit var context : MainActivity
    private lateinit var binding : ActivityMainBinding

    private lateinit var cameraExecutor: ExecutorService

    private lateinit var  squat_noti_builder : NotificationCompat.Builder
    private lateinit var  handsup_noti_builder : NotificationCompat.Builder

    companion object {
        private const val TAG = "PoseDetection"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        createNotificationChannel()

        viewModel.infoText.observe(this, Observer<String> {
            infoText ->
            val tvInfo = binding.tvInfo
            tvInfo.setTextColor(Color.rgb(200, Random.nextInt(255), Random.nextInt(255)))
            tvInfo.setText(infoText)
        })

        squat_noti_builder = NotificationCompat.Builder(this, SQUAT_CHANNEL_ID).apply {
            setContentTitle(getString(R.string.squat_noti_title))
            setContentText(getString(R.string.squat_noti_text))
            setSmallIcon(R.drawable.guggol)
            setPriority(NotificationCompat.PRIORITY_LOW)
        }

        handsup_noti_builder = NotificationCompat.Builder(this, HANDSUP_CHANNEL_ID).apply {
            setContentTitle(getString(R.string.handsup_noti_title))
            setContentText(getString(R.string.handsup_noti_text))
            setSmallIcon(R.drawable.karemel)
            setPriority(NotificationCompat.PRIORITY_LOW)
        }


        viewModel.squatCount.observe(this, Observer<Int> {
            squat_noti_builder.setProgress(SQUAT_PROGRESS_MAX, viewModel.squatCount.value!!, false)
            with(NotificationManagerCompat.from(this)) {
                notify(SQUAT_NOTI_ID, squat_noti_builder.build())
            }
        })



        viewModel.handsupCount.observe(this, Observer<Int> {
            handsup_noti_builder.setProgress(HANDSUP_PROGRESS_MAX, viewModel.handsupCount.value!!, false)
            with(NotificationManagerCompat.from(this)) {
                notify(HANDSUP_NOTI_ID, handsup_noti_builder.build())
            }
        })



        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(480, 360))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), viewModel.analiser)


            val viewFinder = binding.viewFinder
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, imageAnalysis, preview
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }


    }, cameraExecutor /* ContextCompat.getMainExecutor(this)*/)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onStop() {
        with(NotificationManagerCompat.from(this)) {
            cancelAll()
        }

        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }





    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                var name = getString(R.string.squat_channel_name)
                var descriptionText = getString(R.string.squat_channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(SQUAT_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

                name = getString(R.string.handsup_channel_name)
                descriptionText = getString(R.string.handsup_channel_description)
                val channel2 = NotificationChannel(HANDSUP_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system

                notificationManager.createNotificationChannel(channel2)
            }
        }
    }



