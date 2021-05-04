package hu.forstner.schoolcaller.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hu.forstner.schoolcaller.R
import hu.forstner.schoolcaller.data.model.People
import hu.forstner.schoolcaller.data.model.PeopleRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _people : MutableLiveData<MutableList<People>> = MutableLiveData()
    var people : LiveData<MutableList<People>> = _people

    fun loadPeople() {


        _people.value=PeopleRepository().list

    }



}

