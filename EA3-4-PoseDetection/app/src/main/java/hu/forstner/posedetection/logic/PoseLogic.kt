package hu.forstner.posedetection.logic

import com.google.mlkit.vision.pose.PoseLandmark
import hu.forstner.posedetection.audio.Player

class PoseLogic(val infoScreen : PoseInfoScreen) {

    private val player = Player(infoScreen.getContext())

    var baseinitialized = false

    var noseToRightKnee = 0.0f
    val squatThreshold = 60.0f
    val handsUpThreshold =-40.0f

    var poseLandmarks : List<PoseLandmark> = listOf()

    fun updatePoseLandmarks(newList : List<PoseLandmark>) {
        poseLandmarks = newList

        val nose = poseLandmarks.find{it.landmarkType==PoseLandmark.NOSE}
        val rightKnee = poseLandmarks.find{it.landmarkType==PoseLandmark.RIGHT_KNEE}
        val leftWrist = poseLandmarks.find{it.landmarkType==PoseLandmark.RIGHT_WRIST}
        val rightWrist = poseLandmarks.find{it.landmarkType==PoseLandmark.LEFT_WRIST}

        if(baseinitialized==null && nose != null && rightKnee != null ) {
            baseInitialize(nose, rightKnee)
            infoScreen.colorInfo("Base initialization complete")
        }
        else {
            if(checkSquat()) {
                infoScreen.colorInfo("Jump!")
                infoScreen.increaseSquat()
                player.playJump()
            }
            if(checkRaiseHands()) {
                infoScreen.colorInfo("Hands up!")
                infoScreen.increaseHandsup()
                player.playHandsUp()
            }
        }
    }

    fun baseInitialize(nose : PoseLandmark, rightKnee : PoseLandmark) {
        baseinitialized = true
        noseToRightKnee = rightKnee.position.y - nose.position.y
    }

    fun checkSquat() : Boolean {
        val nose = poseLandmarks.find{it.landmarkType==PoseLandmark.NOSE}
        val rightKnee = poseLandmarks.find{it.landmarkType==PoseLandmark.RIGHT_KNEE}

        if(nose != null && rightKnee != null) {
            val diff = rightKnee.position.y - nose.position.y
            if (noseToRightKnee - diff > squatThreshold)
                return true
        }
        return false
    }

    fun checkRaiseHands() : Boolean {

        val nose = poseLandmarks.find{it.landmarkType==PoseLandmark.NOSE}
        val leftWrist = poseLandmarks.find{it.landmarkType==PoseLandmark.RIGHT_WRIST}
        val rightWrist = poseLandmarks.find{it.landmarkType==PoseLandmark.LEFT_WRIST}

        if(nose!=null && leftWrist != null && rightWrist != null) {

            val leftdiff = leftWrist.position.y - nose.position.y
            val rightdiff = rightWrist.position.y - nose.position.y

            if ((rightdiff < handsUpThreshold) && (leftdiff < handsUpThreshold))
                return true
        }
        return false
    }



}