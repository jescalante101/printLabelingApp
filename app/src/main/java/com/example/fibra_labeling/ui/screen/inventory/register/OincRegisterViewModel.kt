package com.example.fibra_labeling.ui.screen.inventory.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FilUserEntity
import com.example.fibra_labeling.data.local.repository.fibrafil.oinc.FibOincRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.user.FUserRepository
import com.example.fibra_labeling.data.remote.SyncRepository
import com.example.fibra_labeling.ui.screen.inventory.register.form.OncForm
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OincRegisterViewModel(private val fUserRepository: FUserRepository,private val fibOincRepository: FibOincRepository) : ViewModel() {


    private val _eventoNavegacion = MutableSharedFlow<String>() // O usa un sealed class para destinos
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()

    // Lista de usuarios filtrados para el autocompletado
    private val _usuarios = MutableStateFlow<List<FilUserEntity>>(emptyList())
    val usuarios: StateFlow<List<FilUserEntity>> = _usuarios

    var formState = mutableStateOf(OncForm())
        private set

    var errorMsg = mutableStateOf<String?>(null)
        private set

    fun onUsuarioChange(newValue: FilUserEntity) {
        formState.value = formState.value.copy(usuario = newValue)
        validate()
    }

    fun onReferenciaChange(newValue: String) {
        formState.value = formState.value.copy(referencia = newValue)
        validate()
    }

    fun onRemarksChange(newValue: String) {
        formState.value = formState.value.copy(remarks = newValue)
        validate()
    }

    fun validate(): Boolean {
        val current = formState.value
        return if (current.usuario == null) {
            errorMsg.value = "Usuario es obligatorio"
            false
        } else {
            errorMsg.value = null
            true
        }
    }


    // Buscar usuarios por filtro (nombre o código)
    fun buscarUsuarios(filtro: String) {
        viewModelScope.launch {
            // Si el filtro está vacío, muestra todos, si no, filtra
            val lista = if (filtro.isBlank()) {
                fUserRepository.getAll()
            } else {
                fUserRepository.searchByName(filtro)
            }
            _usuarios.value = lista
        }
    }


    fun reset() {
        formState.value = OncForm()
        errorMsg.value = null
    }

    fun insertOinc(){
        viewModelScope.launch {
            val hoursFormatter= DateTimeFormatter.ofPattern("HH:mm")
            val dateFormate= DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val now= LocalDateTime.now()
            val oinc = FibOincEntity(
                u_CountDate = now.format(dateFormate),
                u_Ref = formState.value.referencia,
                u_Remarks = formState.value.remarks,
                u_StartTime =now.format(hoursFormatter),
                u_UserNameCount = formState.value.usuario?.uNAME,
                u_userCodeCount = formState.value.usuario?.useRCODE
            )
            var isInsert= fibOincRepository.insert(oinc)
            if(isInsert>0){
                _eventoNavegacion.emit("inventory")
            }else{
                _eventoNavegacion.emit("error")
            }

        }
    }


}
