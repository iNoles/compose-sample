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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
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
    val billAmountTextState = remember { mutableStateOf(TextFieldValue()) }
    val tipPercentageState = remember { mutableStateOf(.20) }
    val roundingOptionsState = remember { mutableStateOf(0) }
    val dropdownExpanded = remember { mutableStateOf(false) }
    val dropdownItemState = remember { mutableStateOf(1) }
    val focusManager = LocalFocusManager.current
    ConstraintLayout(modifier = Modifier.padding(16.dp)) {
        // Create references for the composable to constrain
        val (
            roomIcon,
            outfieldText,
            storeIcon,
            serviceText,
            firstTipOptions,
            amazingText,
            secondTipOptions,
            goodText,
            thirdTipOptions,
            okayText,
            callIcon,
            roundingText,
            roundingNone,
            roundingTip,
            roundingTotal,
            splitTip,
        ) = createRefs()

        val splitMenu = createRef()

        Image(
            painter = painterResource(id = R.drawable.ic_baseline_room_service_24),
            contentDescription = "Room Service Icon",
            modifier = Modifier.padding(top = 26.dp, end = 16.dp).constrainAs(roomIcon) {
                top.linkTo(parent.top)
            }
        )

        OutlinedTextField(
            value = billAmountTextState.value,
            onValueChange = { billAmountTextState.value = it },
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
            modifier = Modifier.constrainAs(outfieldText) {
                start.linkTo(roomIcon.end)
            }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_baseline_store_24),
            contentDescription = "Store Icon",
            modifier = Modifier.constrainAs(storeIcon) {
                top.linkTo(outfieldText.bottom, margin = 16.dp)
            }
        )

        Text(
            text = "How was the service?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(serviceText) {
                start.linkTo(storeIcon.end, margin = 8.dp)
                top.linkTo(outfieldText.bottom, margin = 16.dp)
            }
        )

        RadioButton(
            selected = tipPercentageState.value == .20,
            onClick = { tipPercentageState.value = .20 },
            modifier = Modifier.constrainAs(firstTipOptions) {
                top.linkTo(serviceText.bottom, margin = 16.dp)
            }
        )

        Text(
            text = "Amazing (20%)",
            modifier = Modifier.constrainAs(amazingText) {
                start.linkTo(firstTipOptions.end)
                top.linkTo(serviceText.bottom, margin = 15.dp)
            }
        )

        RadioButton(
            selected = tipPercentageState.value == .18,
            onClick = { tipPercentageState.value = .18 },
            modifier = Modifier.constrainAs(secondTipOptions) {
                top.linkTo(firstTipOptions.bottom, margin = 16.dp)
            }
        )
        Text(
            text = "Good (18%)",
            modifier = Modifier.constrainAs(goodText) {
                start.linkTo(secondTipOptions.end)
                top.linkTo(amazingText.bottom, margin = 16.dp)
            }
        )

        RadioButton(
            selected = tipPercentageState.value == .15,
            onClick = { tipPercentageState.value = .15 },
            modifier = Modifier.constrainAs(thirdTipOptions) {
                top.linkTo(secondTipOptions.bottom, margin = 16.dp)
            }
        )
        Text(
            text = "Okay (15%)",
            modifier = Modifier.constrainAs(okayText) {
                start.linkTo(thirdTipOptions.end)
                top.linkTo(goodText.bottom, margin = 16.dp)
            }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_baseline_call_made_24),
            contentDescription = "Call Icon",
            modifier = Modifier.constrainAs(callIcon) {
                top.linkTo(thirdTipOptions.bottom, margin = 16.dp)
            }
        )
        Text(
            text = "Rounding",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(roundingText) {
                start.linkTo(callIcon.end, margin = 8.dp)
                top.linkTo(thirdTipOptions.bottom, margin = 16.dp)
            }
        )

        Row(
            modifier = Modifier.constrainAs(roundingNone) {
                top.linkTo(roundingText.bottom, 16.dp)
            }
        ) {
            RadioButton(
                selected = roundingOptionsState.value == 0,
                onClick = { roundingOptionsState.value = 0 },
            )
            Text(text = "None")
        }
        Row(
            modifier = Modifier.constrainAs(roundingTip) {
                top.linkTo(roundingNone.bottom, 16.dp)
            }
        ) {
            RadioButton(
                selected = roundingOptionsState.value == 1,
                onClick = { roundingOptionsState.value = 1 },
            )
            Text(text = "Tip")
        }
        Row(
            modifier = Modifier.constrainAs(roundingTotal) {
                top.linkTo(roundingTip.bottom, 16.dp)
            }
        ) {
            RadioButton(
                selected = roundingOptionsState.value == 2,
                onClick = { roundingOptionsState.value = 2 },
            )
            Text(text = "Total")
        }

        Text(
            text = "Split Tip",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(splitTip) {
                top.linkTo(roundingTotal.bottom, margin = 16.dp)
            }
        )

        ExposedDropdownMenuBox(
            expanded = dropdownExpanded.value,
            onExpandedChange = { dropdownExpanded.value = false },
            modifier = Modifier.fillMaxWidth().constrainAs(splitMenu) {
                start.linkTo(splitTip.end)
                top.linkTo(roundingTotal.bottom, margin = 16.dp)
            },
        ) {
            DropdownMenuItem(onClick = { dropdownItemState.value = 1 }) {
                Text(text = "No")
            }
            DropdownMenuItem(onClick = { dropdownItemState.value = 2 }) {
                Text(text = "2 ways")
            }
            DropdownMenuItem(onClick = { dropdownItemState.value = 3 }) {
                Text(text = "3 ways")
            }
            DropdownMenuItem(onClick = { dropdownItemState.value = 4 }) {
                Text(text = "4 ways")
            }
        }

        CalculateThenDisplay(
            billAmountTextState,
            tipPercentageState,
            roundingOptionsState,
            dropdownItemState,
            this,
            splitTip,
        )
    }
}

@Composable
private fun CalculateThenDisplay(
    billAmountTextState: MutableState<TextFieldValue>,
    tipPercentageState: MutableState<Double>,
    roundingOptionsState: MutableState<Int>,
    dropdownItemState: MutableState<Int>,
    constraintLayoutScope: ConstraintLayoutScope,
    splitTip: ConstrainedLayoutReference,
) {
    constraintLayoutScope.apply {
        val tipText = createRef()
        val currencyTip = createRef()
        val totalText = createRef()
        val currencyTotal = createRef()
        val personText = createRef()
        val currencyPerson = createRef()

        val cost = if (billAmountTextState.value.text.isEmpty())
            0.0
        else
            billAmountTextState.value.text.toDouble()
        var tipAmount = 0.0
        var totalAmount = 0.0
        when (roundingOptionsState.value) {
            0 -> {
                tipAmount = tipPercentageState.value * cost
                totalAmount = cost + tipAmount
            }
            1 -> {
                tipAmount = kotlin.math.ceil(tipPercentageState.value * cost)
                totalAmount = cost + tipAmount
            }
            2 -> {
                val tipNotRounded = tipPercentageState.value * cost
                totalAmount = kotlin.math.ceil(cost + tipNotRounded)
                tipAmount = totalAmount - cost
            }
        }

        var splitAmount = 0.0
        var perPersonEnabled = false
        if (dropdownItemState.value != 1) {
            splitAmount = totalAmount / dropdownItemState.value
            perPersonEnabled = true
        }
        val currency = NumberFormat.getCurrencyInstance()
        Text(
            text = "Tip",
            modifier = Modifier.constrainAs(tipText) {
                top.linkTo(splitTip.bottom, margin = 16.dp)
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = currency.format(tipAmount),
            modifier = Modifier.constrainAs(currencyTip) {
                start.linkTo(tipText.end, margin = 16.dp)
                top.linkTo(splitTip.bottom, margin = 16.dp)
            }
        )

        Text(
            text = "Total",
            modifier = Modifier.constrainAs(totalText) {
                top.linkTo(tipText.bottom, margin = 16.dp)
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = currency.format(totalAmount),
            modifier = Modifier.constrainAs(currencyTotal) {
                start.linkTo(totalText.end, margin = 16.dp)
                top.linkTo(tipText.bottom, margin = 16.dp)
            }
        )

        if (perPersonEnabled) {
            Text(
                text = "Per Person",
                modifier = Modifier.constrainAs(personText) {
                    top.linkTo(totalText.bottom, margin = 16.dp)
                }
            )
            Text(
                text = currency.format(splitAmount),
                modifier = Modifier.constrainAs(currencyPerson) {
                    start.linkTo(personText.end, margin = 16.dp)
                    top.linkTo(totalText.bottom, margin = 16.dp)
                }
            )
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
