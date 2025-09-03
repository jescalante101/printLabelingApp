package com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOITMEntity
import com.example.fibra_labeling.data.local.repository.fibrafil.oitm.FibOitmRepository
import com.example.fibra_labeling.data.model.OitmResponse
import com.example.fibra_labeling.data.remote.OitmRepository
import com.example.fibra_labeling.datastore.UserLoginPreference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: OitmRepository,
    private val fibOitmRepository: FibOitmRepository,
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

    private val _filtro = MutableStateFlow("")

    fun changeIsPrint(isPrint: Boolean){
        _isPrint.value=isPrint
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val productos: StateFlow<List<FibOITMEntity>> = _filtro
        .flatMapLatest { filtroRaw ->
            val terms = filtroRaw.split("\\s+".toRegex()).filter { it.isNotBlank() }
            val term1 = terms.getOrNull(0)?.replace("*", "%")?.let { "%$it%" }
            val term2 = terms.getOrNull(1)?.replace("*", "%")?.let { "%$it%" }
            val term3 = terms.getOrNull(2)?.replace("*", "%")?.let { "%$it%" }
            fibOitmRepository.search(term1,term2,term3)
                .catch { e ->
                    _totalResult.value = 0
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFiltro(filtro: String) {
        _filtro.value = filtro
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
            }
        }

    }

    fun updateUser(){
        viewModelScope.launch {
            userLoginPreference.saveUserLogin("","","")
        }

    }


}