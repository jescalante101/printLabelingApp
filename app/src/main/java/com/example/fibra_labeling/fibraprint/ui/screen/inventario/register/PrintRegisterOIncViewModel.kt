package com.example.fibra_labeling.fibraprint.ui.screen.inventario.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOincDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOusrDao
import com.example.fibra_labeling.data.local.entity.fibraprint.POincEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POusrEntity
import com.example.fibra_labeling.fibraprint.ui.screen.inventario.register.form.PrintOincFormState
import com.example.fibra_labeling.fibraprint.ui.screen.inventario.register.form.TipoRegistro
import com.example.fibra_labeling.fibraprint.ui.screen.inventario.register.form.validate
import com.example.fibra_labeling.ui.util.getLocalDateNow
import com.example.fibra_labeling.ui.util.getLocalTimeNow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrintRegisterOIncViewModel(
    private val printOusrDao: PrintOusrDao,
    private val oincDao: PrintOincDao
): ViewModel() {

    private val _eventoNavegacion = MutableSharedFlow<String>() // O usa un sealed class para destinos
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()
    private val _searchUser = MutableStateFlow<String?>(null)
    fun onSearchChange(search: String){
        _searchUser.value = search
    }

    val fecha= getLocalDateNow
    val hora= getLocalTimeNow

    var formState by mutableStateOf(PrintOincFormState())
        private set

    fun onSelectedOptionChange(option: TipoRegistro) {
        formState = formState.copy(selectedOption = option, errorMessage = null)
    }

    fun onSelectedUserChange(user: POusrEntity) {
        formState = formState.copy(selectedUser = user, errorMessage = null)
    }

    fun onSelectedEmpleadoChange(value: String) {
        formState = formState.copy(selectedEmpleado = value, errorMessage = null)
    }

    fun onReferenciaChange(value: String) {
        formState = formState.copy(referencia = value, errorMessage = null)
    }

    fun onObservacionesChange(value: String) {
        formState = formState.copy(observaciones = value, errorMessage = null)
    }

    fun validate(): Boolean {
        val validated = formState.validate()
        formState = validated
        return validated.errorMessage == null
    }

    fun reset() {
        formState = PrintOincFormState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val allUsers: StateFlow<List<POusrEntity>> = _searchUser
        .flatMapLatest { query ->
            printOusrDao.buscarPorNombreOCodigo(filtro = query ?:"")
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun insertOinc(){
        viewModelScope.launch {

            var usuario=""
            if(formState.selectedOption== TipoRegistro.USUARIO){
                usuario=formState.selectedUser?.uNAME.toString()
            }else{
                usuario=formState.selectedEmpleado.toString()
            }


            val oinc = POincEntity(
                u_CountDate = fecha,
                u_Ref = formState.referencia,
                u_Remarks = formState.observaciones,
                u_StartTime =hora,
                u_UserNameCount = usuario,
                u_userCodeCount = formState.selectedUser?.useRCODE
            )
            val isInsert= oincDao.insertar(oinc)
            if(isInsert>0){
                _eventoNavegacion.emit("success")
            }else{
                _eventoNavegacion.emit("error")
            }
        }
    }

}