package com.yusufaydin.mobilliumcase.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufaydin.mobilliumcase.model.Doctor
import com.yusufaydin.mobilliumcase.repository.DoctorRepository
import com.yusufaydin.mobilliumcase.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorListViewModel @Inject constructor(
    private val repository: DoctorRepository
) : ViewModel() {

    var doctorList = mutableStateOf<List<Doctor>>(listOf())
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    var initialDoctorList = listOf<Doctor>()
    var isSearchStarting = true
    var isFilterStarting = true

    init {
        downloadDoctors()
    }

    fun searchDoctorList(query: String) {
        val listToSearch = if (isSearchStarting) {
            doctorList.value
        } else {
            initialDoctorList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                doctorList.value = initialDoctorList
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter { doctors ->
                doctors.fullName.contains(query.trim(), ignoreCase = true)
            }
            if (isSearchStarting) {
                initialDoctorList = doctorList.value
                isSearchStarting = false
            }
            doctorList.value = results
        }
    }

    fun filterDoctorList(gender: String) {
        val listToSearch = if (isFilterStarting) {
            doctorList.value
        } else {
            initialDoctorList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (gender.equals("")) {
                doctorList.value = initialDoctorList
                isFilterStarting = true
                return@launch
            }
            val results = listToSearch.filter { doctors ->
                doctors.gender.equals(gender)
            }
            if (isFilterStarting) {
                initialDoctorList = doctorList.value
                isFilterStarting = false
            }
            doctorList.value = results
        }
    }

    fun downloadDoctors() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getDoctorList()
            when (result) {
                is Resource.Success -> {
                    val doctorItems = result.data!!.doctors.mapIndexed { index, doctor ->
                        Doctor(doctor.fullName, doctor.gender, doctor.image, doctor.userStatus)
                    }
                    errorMessage.value = ""
                    isLoading.value = false
                    doctorList.value += doctorItems
                }

                is Resource.Error -> {
                    errorMessage.value = result.message!!
                    isLoading.value = false
                }
            }
        }

    }
}