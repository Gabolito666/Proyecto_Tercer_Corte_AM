package com.uts.serviciosestudiantiles.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uts.serviciosestudiantiles.ui.theme.BlancoSuperficie
import com.uts.serviciosestudiantiles.ui.theme.GrisPlata
import com.uts.serviciosestudiantiles.ui.theme.GrisTextoSecundario
import com.uts.serviciosestudiantiles.ui.theme.NegroTexto
import com.uts.serviciosestudiantiles.ui.theme.VinoTintoOleo
import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel

@Composable
fun CambioPasswordDialog(
    viewModel: ServiciosViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (!state.mostrarDialogoCambioPassword) return

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = VinoTintoOleo,
        unfocusedBorderColor = GrisPlata,
        focusedLabelColor = VinoTintoOleo,
        unfocusedLabelColor = GrisTextoSecundario,
        cursorColor = VinoTintoOleo,
        focusedLeadingIconColor = VinoTintoOleo,
        unfocusedLeadingIconColor = GrisTextoSecundario,
        focusedTrailingIconColor = VinoTintoOleo,
        unfocusedTrailingIconColor = GrisTextoSecundario,
        focusedContainerColor = BlancoSuperficie,
        unfocusedContainerColor = BlancoSuperficie,
        focusedTextColor = NegroTexto,
        unfocusedTextColor = NegroTexto
    )

    Dialog(
        onDismissRequest = { viewModel.cancelarCambioPassword() },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = BorderStroke(1.5.dp, VinoTintoOleo)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = VinoTintoOleo.copy(alpha = 0.12f),
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = VinoTintoOleo,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Text(
                    text = "Cambio de Contraseña Requerido",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroTexto
                )

                Text(
                    text = "Por motivos de seguridad institucional de las UTS, debes cambiar tu contraseña inicial (que es tu código de estudiante) por una contraseña nueva diferente.",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTextoSecundario
                )

                if (state.errorCambioPassword != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = VinoTintoOleo.copy(alpha = 0.08f)),
                        border = BorderStroke(1.dp, VinoTintoOleo)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = VinoTintoOleo,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = state.errorCambioPassword ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = VinoTintoOleo,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = state.campoNuevoPassword,
                    onValueChange = viewModel::onNuevoPasswordCambiado,
                    label = { Text("Nueva Contraseña") },
                    placeholder = { Text("Ingresa tu nueva clave") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(onClick = viewModel::toggleNuevoPasswordVisible) {
                            Icon(
                                imageVector = if (state.nuevoPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Mostrar u ocultar contraseña"
                            )
                        }
                    },
                    visualTransformation = if (state.nuevoPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = state.campoConfirmarNuevoPassword,
                    onValueChange = viewModel::onConfirmarNuevoPasswordCambiado,
                    label = { Text("Confirmar Nueva Contraseña") },
                    placeholder = { Text("Repite tu nueva clave") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(onClick = viewModel::toggleConfirmarPasswordVisible) {
                            Icon(
                                imageVector = if (state.confirmarPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Mostrar u ocultar contraseña"
                            )
                        }
                    },
                    visualTransformation = if (state.confirmarPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = textFieldColors
                )

                Button(
                    onClick = { viewModel.cambiarContrasenaObligatoria() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = VinoTintoOleo)
                ) {
                    Text("Guardar Nueva Contraseña", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { viewModel.cancelarCambioPassword() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GrisTextoSecundario),
                    border = BorderStroke(1.dp, GrisPlata)
                ) {
                    Text("Cancelar y Salir")
                }
            }
        }
    }
}
