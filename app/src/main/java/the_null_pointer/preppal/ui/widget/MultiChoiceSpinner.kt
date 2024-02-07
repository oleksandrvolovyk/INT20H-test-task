package the_null_pointer.preppal.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun <T> MultiChoiceSpinner(
    items: List<Pair<T, String>>,
    selected: List<T>,
    onSelectionChanged: (List<T>) -> Unit,
    modifier: Modifier = Modifier,
    initialExpandedState: Boolean = false
) {
    var expanded by remember { mutableStateOf(initialExpandedState) }

    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = selected.joinToString { item -> items.find { it.first == item }!!.second },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()   // delete this modifier and use .wrapContentWidth() if you would like to wrap the dropdown menu around the content
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            if (selected.contains(item.first)) {
                                // Add item to selected
                                onSelectionChanged(selected - item.first)
                            } else {
                                // Remove item from selected
                                onSelectionChanged(selected + item.first)
                            }
                        },
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = item.first in selected,
                                    onCheckedChange = {
                                        if (selected.contains(item.first)) {
                                            // Add item to selected
                                            onSelectionChanged(selected - item.first)
                                        } else {
                                            // Remove item from selected
                                            onSelectionChanged(selected + item.first)
                                        }
                                    }
                                )

                                Text(
                                    text = item.second
                                )
                            }
                        },
                    )
                }
            }
        }
    }
}