package the_null_pointer.preppal.ui.progress.by_summary_and_type

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import the_null_pointer.preppal.R
import the_null_pointer.preppal.data.educational_resource.EducationalResourceRepository
import the_null_pointer.preppal.data.educational_resource.model.EducationalResource
import the_null_pointer.preppal.data.event.EventRepository
import the_null_pointer.preppal.data.event.model.Event
import the_null_pointer.preppal.ui.SideEffect
import the_null_pointer.preppal.util.TimeUtil.getReadableTimePeriod
import javax.inject.Inject

data class ProgressBySummaryAndTypeScreenUiState(
    val eventSummary: String,
    val eventType: Event.Type,
    val progressBySummaryAndTypeListItems: List<ProgressBySummaryAndTypeListItem> = emptyList(),
    val educationalResourceListItems: List<EducationalResourceListItem> = emptyList(),
    val newEducationalResourceState: NewEducationalResourceState = NewEducationalResourceState()
)

data class NewEducationalResourceState(
    val addingNewResource: Boolean = false,
    val name: String = "",
    val type: EducationalResource.Type = EducationalResource.Type.Resource,
    val link: String = ""
)

data class ProgressBySummaryAndTypeListItem(
    val eventId: Long,
    val time: String,
    val completed: Boolean
)

data class EducationalResourceListItem(
    val resourceId: Long,
    val name: String,
    val type: EducationalResource.Type,
    val link: String,
    val favourite: Boolean
)

@HiltViewModel
class ProgressBySummaryAndTypeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository,
    private val educationalResourceRepository: EducationalResourceRepository
) : ViewModel() {

    private val eventSummary = savedStateHandle.get<String>("summary")!!
    private val eventType = Event.Type.valueOf(savedStateHandle.get<String>("type")!!)

    private val newEducationalResourceStateFlow = MutableStateFlow(NewEducationalResourceState())

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    val uiState: StateFlow<ProgressBySummaryAndTypeScreenUiState> = combine(
        eventRepository.observeEvents(),
        educationalResourceRepository.observeByEventSummaryAndEventType(eventSummary, eventType),
        newEducationalResourceStateFlow
    ) { events, educationalResources, newEducationalResourceState ->
        val progressBySummaryAndTypeListItems = events
            .filter { event -> event.completed != null }
            .filter { event -> event.summary == eventSummary }
            .filter { event -> event.type == eventType }
            .mapToProgressBySummaryAndTypeListItems()

        val educationalResourceListItems = educationalResources
            .mapToEducationalResourceListItems()

        ProgressBySummaryAndTypeScreenUiState(
            eventSummary = eventSummary,
            eventType = eventType,
            progressBySummaryAndTypeListItems = progressBySummaryAndTypeListItems,
            educationalResourceListItems = educationalResourceListItems,
            newEducationalResourceState = newEducationalResourceState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ProgressBySummaryAndTypeScreenUiState(
            eventSummary = eventSummary,
            eventType = eventType
        )
    )

    private fun List<Event>.mapToProgressBySummaryAndTypeListItems(): List<ProgressBySummaryAndTypeListItem> =
        this.sortedBy { it.start }
            .map { event ->
                ProgressBySummaryAndTypeListItem(
                    eventId = event.id,
                    time = event.start.getReadableTimePeriod(event.end),
                    completed = event.completed!!
                )
            }

    private fun List<EducationalResource>.mapToEducationalResourceListItems(): List<EducationalResourceListItem> =
        this.sortedByDescending { it.favourite }
            .map { educationalResource ->
                EducationalResourceListItem(
                    resourceId = educationalResource.id,
                    name = educationalResource.name,
                    type = educationalResource.type,
                    link = educationalResource.link,
                    favourite = educationalResource.favourite
                )
            }

    fun updateEventCompletion(eventId: Long, completed: Boolean) = viewModelScope.launch {
        eventRepository.setEventCompletion(eventId, completed)
    }

    fun updateEducationalResourceFavourite(resourceId: Long, favourite: Boolean) =
        viewModelScope.launch {
            educationalResourceRepository.setFavourite(resourceId, favourite)
        }

    fun deleteEducationalResource(resourceId: Long) = viewModelScope.launch {
        educationalResourceRepository.deleteById(resourceId)
    }

    fun startAddingNewEducationalResource() = newEducationalResourceStateFlow.update {
        it.copy(addingNewResource = true)
    }

    fun cancelAddingNewEducationalResource() = newEducationalResourceStateFlow.update {
        it.copy(addingNewResource = false)
    }

    fun submitNewEducationalResource() = viewModelScope.launch {
        if (uiState.value.newEducationalResourceState.name.isBlank()) {
            _sideEffectChannel.trySend(SideEffect.ShowToast(R.string.resource_name_empty))
            return@launch
        }
        educationalResourceRepository.insert(
            EducationalResource(
                eventSummary = eventSummary,
                eventType = eventType,
                name = uiState.value.newEducationalResourceState.name,
                type = uiState.value.newEducationalResourceState.type,
                link = uiState.value.newEducationalResourceState.link,
                favourite = false
            )
        )
        newEducationalResourceStateFlow.update { NewEducationalResourceState() }

    }

    fun updateNewEducationalResourceName(name: String) = newEducationalResourceStateFlow.update {
        it.copy(name = name)
    }

    fun updateNewEducationalResourceLink(link: String) = newEducationalResourceStateFlow.update {
        it.copy(link = link)
    }

    fun updateNewEducationalResourceType(type: EducationalResource.Type) =
        newEducationalResourceStateFlow.update {
            it.copy(type = type)
        }
}