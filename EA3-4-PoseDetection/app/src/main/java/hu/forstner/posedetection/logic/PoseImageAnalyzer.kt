package hu.forstner.posedetection.logic

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions

class PoseImageAnalyzer(val poseLogic: PoseLogic, val infoScreen : PoseInfoScreen) : ImageAnalysis.Analyzer {


    private lateinit var poseDetector : PoseDetector

    init {
        val options = AccuratePoseDetectorOptions.Builder()
                .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
                .build()
        poseDetector = PoseDetection.getClient(options)
    }


    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                    mediaImage,
                    imageProxy.imageInfo.rotationDegrees
            )
            poseDetector.process(image)
                    .addOnSuccessListener { results ->
                        // Task completed successfully
                        poseLogic.updatePoseLandmarks(results.allPoseLandmarks)

                    }
                    .addOnFailureListener { e ->
                        infoScreen.colorInfo("{${e.toString()}")

                        // ...
                    }.addOnCompleteListener {
                        mediaImage.close()
                        imageProxy.close()

                    }
        }
    }
}
