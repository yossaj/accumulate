package com.lingoal.accumulate.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun <T> DropdownMenu(
    modifier: Modifier = Modifier,
    selectedLabel: String,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    label: String,
    enabled: Boolean = true,
    getItemLabel: @Composable (T) -> String,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { if (items.isNotEmpty() && enabled) { expanded = !expanded } }) {

        OutlinedTextField(
            modifier = modifier
                .menuAnchor(),
            readOnly = true,
            enabled = enabled,
            value = selectedLabel,
            onValueChange = { },
            label = { Text(text = label) },
            leadingIcon = leadingIcon,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
        )

        //ExposedDropdownMenu performance is bad & does not support LazyColumn out of the box
        //https://issuetracker.google.com/issues/242398344
        val sizeOfOneItem by remember {
            mutableStateOf(50.dp) //assuming height of one menu item 50dp
        }
        val configuration = LocalConfiguration.current
        val screenHeight50 by remember {
            val screenHeight = configuration.screenHeightDp.dp
            mutableStateOf(screenHeight / 2) //assuming the drop down menu anchor is in middle of the screen. This is the maximum height that popup menu can take.
        }
        val height by remember(items.size) {
            val itemsSize = sizeOfOneItem * items.size
            mutableStateOf(minOf(itemsSize, screenHeight50))
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .width(500.dp)
                    .height(height)
            ) {
                items(items) { item ->
                    DropdownMenuItem(
                        text = { Text(text = getItemLabel(item)) },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
