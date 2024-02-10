package the_null_pointer.preppal.ui.progress.by_summary_and_type

import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.educational_resource.model.EducationalResource
import the_null_pointer.preppal.data.educational_resource.model.EducationalResource.Type.Companion.stringResourceId
import the_null_pointer.preppal.data.event.model.Event
import the_null_pointer.preppal.data.event.model.Event.Type.Companion.completionStringResourceId
import the_null_pointer.preppal.data.event.model.Event.Type.Companion.stringResourceId
import the_null_pointer.preppal.ui.widget.CheckboxWithoutPadding
import the_null_pointer.preppal.ui.widget.Spinner

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProgressBySummaryAndTypeScreen(
    uiState: ProgressBySummaryAndTypeScreenUiState,
    onEventClick: (eventId: Long) -> Unit = {},
    onEventCompletionChange: (eventId: Long, completed: Boolean) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {},
    onUpdateEducationalResourceFavourite: (resourceId: Long, favourite: Boolean) -> Unit = { _, _ -> },
    onDeleteEducationalResource: (resourceId: Long) -> Unit = {},
    onAddEducationalResourceButtonClick: () -> Unit = {},
    onNewEducationalResourceTypeChange: (EducationalResource.Type) -> Unit = {},
    onNewEducationalResourceNameChange: (String) -> Unit = {},
    onNewEducationalResourceLinkChange: (String) -> Unit = {},
    onNewEducationalResourceSubmitClick: () -> Unit = {},
    onNewEducationalResourceCancelClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = stringResource(R.string.go_back),
                modifier = Modifier
                    .defaultMinSize(48.dp, 48.dp)
                    .clickable { onBackClick() }
                    .align(Alignment.CenterStart)
            )

            Text(
                text = stringResource(
                    R.string.progress_of_summary_and_type,
                    uiState.eventSummary,
                    stringResource(uiState.eventType.stringResourceId)
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }

        var columnSize by remember { mutableStateOf(Size.Zero) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { layoutCoordinates ->
                    columnSize = layoutCoordinates.size.toSize()
                },
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val maxHeight1 = if (!uiState.newEducationalResourceState.addingNewResource) {
                LocalDensity.current.run { (0.7f * columnSize.height).toDp() }
            } else {
                LocalDensity.current.run { (0.5f * columnSize.height).toDp() }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(0.dp, maxHeight1)
            ) {
                items(uiState.progressBySummaryAndTypeListItems, key = { it.time }) {
                    ProgressRow(
                        eventSummary = uiState.eventSummary,
                        eventType = uiState.eventType,
                        progressBySummaryAndTypeListItem = it,
                        onEventClick = onEventClick,
                        onEventCompletionChange = onEventCompletionChange
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = stringResource(R.string.educational_resources),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(
                        uiState.educationalResourceListItems,
                        key = { it.resourceId }
                    ) { resource ->
                        val deleteSwipeAction = SwipeAction(
                            onSwipe = {
                                onDeleteEducationalResource(resource.resourceId)
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = Color.White
                                )
                            }, background = Color.Red.copy(alpha = 0.5f)
                        )
                        SwipeableActionsBox(
                            modifier = Modifier.animateItemPlacement(),
                            swipeThreshold = 80.dp,
                            endActions = listOf(deleteSwipeAction)
                        ) {
                            EducationalResourceRow(
                                resource,
                                onUpdateFavourite = {
                                    onUpdateEducationalResourceFavourite(
                                        resource.resourceId,
                                        it
                                    )
                                }
                            )
                        }
                    }

                    if (uiState.newEducationalResourceState.addingNewResource) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItemPlacement()
                            ) {
                                NewEducationalResourceAdder(
                                    newEducationalResourceState = uiState.newEducationalResourceState,
                                    onResourceTypeChange = onNewEducationalResourceTypeChange,
                                    onResourceNameChange = onNewEducationalResourceNameChange,
                                    onResourceLinkChange = onNewEducationalResourceLinkChange,
                                    onCancelClick = onNewEducationalResourceCancelClick,
                                    onSubmitClick = onNewEducationalResourceSubmitClick
                                )
                            }
                        }
                    } else {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItemPlacement(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                IconButton(onClick = onAddEducationalResourceButtonClick) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = stringResource(R.string.add_new_educational_resource)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressRow(
    eventSummary: String,
    eventType: Event.Type,
    progressBySummaryAndTypeListItem: ProgressBySummaryAndTypeListItem,
    onEventClick: (eventId: Long) -> Unit,
    onEventCompletionChange: (eventId: Long, completed: Boolean) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .padding(6.dp),
        border = BorderStroke(
            width = 3.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clickable { onEventClick(progressBySummaryAndTypeListItem.eventId) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${stringResource(id = eventType.stringResourceId)}\n${eventSummary}",
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1.5f)
            )
            Text(
                text = progressBySummaryAndTypeListItem.time,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.9f)
            )

            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(eventType.completionStringResourceId))

                CheckboxWithoutPadding(
                    checked = progressBySummaryAndTypeListItem.completed,
                    onCheckedChange = {
                        onEventCompletionChange(
                            progressBySummaryAndTypeListItem.eventId,
                            it
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun EducationalResourceRow(
    educationalResourceListItem: EducationalResourceListItem,
    onUpdateFavourite: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Text(stringResource(educationalResourceListItem.type.stringResourceId) + ": ")

            val url = educationalResourceListItem.link
            val context = LocalContext.current

            if (Patterns.WEB_URL.matcher(url).matches()) {
                ClickableText(
                    text = AnnotatedString(educationalResourceListItem.name),
                    style = LocalTextStyle.current.copy(textDecoration = TextDecoration.Underline)
                ) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(browserIntent)
                }
            } else {
                Text(
                    text = AnnotatedString(educationalResourceListItem.name)
                )
            }
        }

        if (educationalResourceListItem.favourite) {
            Image(
                modifier = Modifier.clickable { onUpdateFavourite(false) },
                painter = painterResource(R.drawable.ic_star),
                contentDescription = stringResource(R.string.set_resource_as_not_favourite)
            )
        } else {
            Image(
                modifier = Modifier.clickable { onUpdateFavourite(true) },
                painter = painterResource(R.drawable.ic_star_inactive),
                contentDescription = stringResource(R.string.set_resource_as_favourite)
            )
        }
    }
}


@Composable
fun NewEducationalResourceAdder(
    newEducationalResourceState: NewEducationalResourceState,
    onResourceTypeChange: (EducationalResource.Type) -> Unit,
    onResourceNameChange: (String) -> Unit,
    onResourceLinkChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    onSubmitClick: () -> Unit
) {
    Column {
        Spinner(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            items = EducationalResource.Type.values()
                .map { it to stringResource(it.stringResourceId) },
            selected = newEducationalResourceState.type,
            onSelectionChanged = { onResourceTypeChange(it!!) },
            maxLines = 1
        )

        TextField(
            label = { Text(stringResource(R.string.educational_resource_name)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            value = newEducationalResourceState.name,
            onValueChange = onResourceNameChange,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            )
        )

        TextField(
            label = { Text(stringResource(R.string.educational_resource_link)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            value = newEducationalResourceState.link,
            onValueChange = onResourceLinkChange,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onCancelClick) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = stringResource(R.string.cancel)
                )
            }
            IconButton(onClick = onSubmitClick) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = stringResource(R.string.submit)
                )
            }
        }
    }
}
