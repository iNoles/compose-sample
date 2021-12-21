/*
 * Copyright 2021 Jonathan Steele
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.jonathansteele.dinertips

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonathansteele.dinertips.ui.theme.DinerTipsTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DinerTipsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Tips()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Tips() {
    var billAmountTextState by remember { mutableStateOf(TextFieldValue()) }
    var tipPercentageState by remember { mutableStateOf("Amazing") }
    var roundingOptionsState by remember { mutableStateOf("None") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var dropdownItemState by remember { mutableStateOf(1) }
    val focusManager = LocalFocusManager.current

    val tipPercentageOptions = listOf("Amazing", "Good", "Okay")
    val roundingOptions = listOf("None", "Tip", "Total")

    Column {
        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_store_24),
                contentDescription = "Store Icon",
                modifier = Modifier.padding(top = 26.dp, end = 16.dp)
            )

            OutlinedTextField(
                value = billAmountTextState,
                onValueChange = { billAmountTextState = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                label = { Text("Cost of Service") },
            )
        }

        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_room_service_24),
                contentDescription = "Room Service Icon",
                modifier = Modifier.padding(end = 16.dp)
            )

            Text(
                text = "How was the service?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        
        RadioGroup(
            options = tipPercentageOptions,
            selected = tipPercentageState,
            onSelectedChange = { text ->
                tipPercentageState = text
            })

        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_call_made_24),
                contentDescription = "Call Icon",
                modifier = Modifier.padding(end = 16.dp)
            )

            Text(
                text = "Rounding",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        RadioGroup(
            options = roundingOptions,
            selected = roundingOptionsState,
            onSelectedChange = { text ->
                roundingOptionsState = text
            })

        Text(
            text = "Split Tip",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )

        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            DropdownMenuItem(onClick = { dropdownItemState = 1 }) {
                Text(text = "No")
            }
            DropdownMenuItem(onClick = { dropdownItemState = 2 }) {
                Text(text = "2 ways")
            }
            DropdownMenuItem(onClick = { dropdownItemState = 3 }) {
                Text(text = "3 ways")
            }
            DropdownMenuItem(onClick = { dropdownItemState = 4 }) {
                Text(text = "4 ways")
            }
        }

        CalculateThenDisplay(
            billAmountTextState,
            tipPercentageState,
            roundingOptionsState,
            dropdownItemState,
        )
    }
}

@Composable
private fun CalculateThenDisplay(
    billAmountTextState: TextFieldValue,
    tipPercentageState: String,
    roundingOptionsState: String,
    dropdownItemState: Int,
) {
    val cost = if (billAmountTextState.text.isEmpty())
        0.0
    else
        billAmountTextState.text.toDouble()
    var tipAmount = 0.0
    var totalAmount = 0.0
    val tipPercentage = when(tipPercentageState) {
        "Good" -> .18
        "Okay" -> .15
        else -> .20
    }
    when (roundingOptionsState) {
        "None" -> {
            tipAmount = tipPercentage * cost
            totalAmount = cost + tipAmount
        }
        "Tip" -> {
            tipAmount = kotlin.math.ceil(tipPercentage * cost)
            totalAmount = cost + tipAmount
        }
        "Total" -> {
            val tipNotRounded = tipPercentage * cost
            totalAmount = kotlin.math.ceil(cost + tipNotRounded)
            tipAmount = totalAmount - cost
        }
    }

    var splitAmount = 0.0
    var perPersonEnabled = false
    if (dropdownItemState != 1) {
        splitAmount = totalAmount / dropdownItemState
        perPersonEnabled = true
    }
    val currency = NumberFormat.getCurrencyInstance()
    Row {
        Text(
            text = "Tip: ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(text = currency.format(tipAmount))
    }
    Row {
        Text(
            text = "Total: ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = currency.format(totalAmount))
    }

    if (perPersonEnabled) {
        Row {
            Text(text = "Per Person: ")
            Text(text = currency.format(splitAmount))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DinerTipsTheme {
        Tips()
    }
}
