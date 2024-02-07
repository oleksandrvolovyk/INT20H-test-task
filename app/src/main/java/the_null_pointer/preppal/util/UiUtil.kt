package the_null_pointer.preppal.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import the_null_pointer.preppal.R

object UiUtil {
    fun createConfirmationDialog(
        context: Context,
        message: String,
        onPositiveButtonClick: () -> Unit
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.app_name)
        builder.setMessage(message)
        builder.setIcon(R.drawable.ic_launcher_foreground)
        builder.setPositiveButton(context.getString(R.string.yes)) { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            onPositiveButtonClick.invoke()
        }
        builder.setNegativeButton(context.getString(R.string.no)) { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        return builder.create()
    }
}