package the_null_pointer.preppal.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp

@Composable
fun <T> Spinner(
    items: List<Pair<T, String>>,
    selected: T,
    onSelectionChanged: (T?) -> Unit,
    modifier: Modifier = Modifier,
    cardColors: CardColors = CardDefaults.outlinedCardColors(),
    initialExpandedState: Boolean = false,
    canSelectNothing: Boolean = false,
    nothingOptionString: String = ""
) {
    var expanded by remember { mutableStateOf(initialExpandedState) }

    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        },
        colors = cardColors
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = items.find { it.first == selected }?.second ?: nothingOptionString,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()   // delete this modifier and use .wrapContentWidth() if you would like to wrap the dropdown menu around the content
            ) {
                if (canSelectNothing) {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onSelectionChanged(null)
                        },
                        text = {
                            Text(
                                text = nothingOptionString,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Start)
                            )
                        },
                    )
                }

                items.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onSelectionChanged(item.first)
                        },
                        text = {
                            Text(
                                text = item.second,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Start)
                            )
                        },
                    )
                }
            }
        }
    }
}