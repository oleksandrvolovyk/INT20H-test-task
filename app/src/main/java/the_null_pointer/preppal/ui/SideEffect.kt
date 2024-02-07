package the_null_pointer.preppal.ui

import android.content.Context
import android.widget.Toast

sealed interface SideEffect {
    data class ShowToast(val stringId: Int, val stringFormatArg: String? = null) : SideEffect
    object NavigateBack : SideEffect
}

/**
 * Try to handle SideEffect.
 * @param context       Context.
 * @param sideEffect    SideEffect.
 * @return              True, if sideEffect was handled.
 */
fun handleSideEffect(context: Context, sideEffect: SideEffect): Boolean = when (sideEffect) {
    is SideEffect.ShowToast -> {
        Toast.makeText(
            context,
            context.getString(sideEffect.stringId, sideEffect.stringFormatArg),
            Toast.LENGTH_SHORT
        ).show()
        true
    }

    else -> {
        false
    }
}