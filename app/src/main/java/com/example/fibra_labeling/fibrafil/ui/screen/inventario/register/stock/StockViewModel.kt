package com.example.fibra_labeling.fibrafil.ui.screen.inventario.register.stock

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.model.fibrafil.StockResponse
import com.example.fibra_labeling.data.remote.fibrafil.FillRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class StockViewModel(private val fillRepository: FillRepository): ViewModel() {
    private val _stock= MutableStateFlow<StockResponse>(StockResponse(onHand = 0))
    val stock: MutableStateFlow<StockResponse> = _stock

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> = _loading

    private val _codigo= MutableStateFlow("")
    val codigo: MutableStateFlow<String> = _codigo

    private val _whsCode= MutableStateFlow("CH3-RE")
    val whsCode: MutableStateFlow<String> = _whsCode

    fun setCodigo(value:String){
        _codigo.value=value
    }
    fun setWhsCode(value:String){
        _whsCode.value=value
    }


    fun getStock(){
        viewModelScope.launch {
            _loading.value=true
            fillRepository.getStockAlmacen(_codigo.value,_whsCode.value).catch {e->
                Log.e("Error", e.message.toString())
                _loading.value=false
            }.collect {
                _stock.value=it
                _loading.value=false
            }

        }
    }
}