package com.example.fibra_labeling.ui.screen.print.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.model.OitmResponse
import com.example.fibra_labeling.data.remote.FillRepository
import com.example.fibra_labeling.data.remote.OitmRepository
import com.example.fibra_labeling.datastore.UserLoginPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: OitmRepository,
    private val fillRepository: FillRepository,
    private val userLoginPreference: UserLoginPreference
): ViewModel() {
    private val _oitmResponse = MutableStateFlow<Result<OitmResponse>>(
        Result.success(
            OitmResponse(
                data = null,
                page = 0,
                pageSize = 0,
                total = 0
            )
        )
    )
    val oitmResponse: MutableStateFlow<Result<OitmResponse>> = _oitmResponse

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> = _loading

    private val _totalResult= MutableStateFlow(0)
    val totalResult: MutableStateFlow<Int> = _totalResult

    private val _isPrint= MutableStateFlow(true)
    val isPrint: MutableStateFlow<Boolean> = _isPrint

    fun changeIsPrint(isPrint: Boolean){
        _isPrint.value=isPrint
    }

    fun getOitm(filter: String="", page: Int=1, pageSize: Int=20,isFill: Boolean=false){

        viewModelScope.launch {
            _loading.value = true
            if(!isFill) {
                repository.getOitms(filter, page, pageSize)
                    .catch { e ->
                        _oitmResponse.value = Result.failure(e)
                        _loading.value = false
                        _totalResult.value = 0;
                    }
                    .collect {
                        _oitmResponse.value = Result.success(it)
                        _totalResult.value = it.data?.size!!
                        _loading.value = false
                    }
            }else{
                fillRepository.getOitms(filter, page, pageSize)
                    .catch { e ->
                        _oitmResponse.value = Result.failure(e)
                        _loading.value = false
                        _totalResult.value = 0;
                    }.collect {
                        _oitmResponse.value = Result.success(it)
                        _totalResult.value = it.data?.size!!
                        _loading.value = false
                    }
            }
        }

    }

    fun updateUser(){
        viewModelScope.launch {
            userLoginPreference.saveUserLogin("","")
        }

    }


}