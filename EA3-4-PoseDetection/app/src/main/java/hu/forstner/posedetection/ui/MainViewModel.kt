package hu.forstner.posedetection.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hu.forstner.posedetection.logic.PoseImageAnalyzer
import hu.forstner.posedetection.logic.PoseInfoScreen
import hu.forstner.posedetection.logic.PoseLogic

class MainViewModel(application : Application) : AndroidViewModel(application), PoseInfoScreen {

    private val _infoText : MutableLiveData<String> = MutableLiveData()
    var infoText : LiveData<String> = _infoText

    var _squatCount : MutableLiveData<Int> = MutableLiveData(0)
    var squatCount : LiveData<Int> = _squatCount
    var _handsupCount : MutableLiveData<Int> = MutableLiveData(0)
    var handsupCount : LiveData<Int> = _handsupCount


    val poseLogic = PoseLogic(this)
    val analiser = PoseImageAnalyzer(poseLogic, this)


    override fun colorInfo(text: String) {
        _infoText.value = text
    }

    override fun increaseSquat() {
        _squatCount.value =  _squatCount.value!! + 1
    }

    override fun increaseHandsup() {
        _handsupCount.value =  _handsupCount.value!! + 1
    }


    override fun getContext(): Context = getApplication()
}