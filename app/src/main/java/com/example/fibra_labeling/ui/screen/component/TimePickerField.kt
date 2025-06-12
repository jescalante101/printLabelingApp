package com.example.fibra_labeling.ui.screen.component

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.fibra_labeling.R
import java.util.Calendar

@Composable
fun TimePickerField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton (onClick = {
                val dialog = TimePickerDialog(
                    context,
                    { _, h, m ->
                        val hora = "%02d:%02d:00".format(h, m)
                        onValueChange(hora)
                    },
                    hour, minute, true
                )
                dialog.show()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = "Elegir hora"
                )
            }
        }
    )
}
