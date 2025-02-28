package com.example.trackpregnancy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddVitalsDialog(
    onDissmiss: ()->Unit,
    onSave: (Vital)->Unit
) {

    var bloodPressureSys by remember { mutableStateOf("") }
    var bloodPressureDia by remember { mutableStateOf("") }
    var heartRate by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var babyKicks by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest ={onDissmiss()},
        title = { Text("Add Vitals", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(value = heartRate,
                    onValueChange = {heartRate = it},
                    label = { Text("Heart Rate") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Row(modifier =  Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = bloodPressureSys,
                        onValueChange = {bloodPressureSys = it},
                        label = { Text("Sys BP") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(value = bloodPressureDia,
                        onValueChange = {bloodPressureDia = it},
                        label = { Text("Dia BP") },
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = weight,
                    onValueChange = {weight = it},
                    label = { Text("Weight in 1Kg") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                OutlinedTextField(value = babyKicks,
                    onValueChange = {babyKicks = it},
                    label = { Text("Baby Kicks") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

            }
        },
        confirmButton = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    if (heartRate.isNotEmpty()&& bloodPressureDia.isNotEmpty()&& bloodPressureSys.isNotEmpty() && weight.isNotEmpty() && babyKicks.isNotEmpty()){
                        onSave(
                            Vital(heartRate = heartRate.toInt(),
                                bloodPressureSys = bloodPressureSys.toInt(),
                                bloodPressureDia = bloodPressureDia.toInt(),
                                weight = weight.toFloat(),
                                babyKicks = babyKicks.toInt()
                            )
                        )
                    }
                }, shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF9C4DB9),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(horizontal = 35.dp)
                ) {
                    Text("Submit")
                }
            }
        }
    )
}