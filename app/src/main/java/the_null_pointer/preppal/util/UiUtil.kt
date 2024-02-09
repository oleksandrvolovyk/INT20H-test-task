package the_null_pointer.preppal.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
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

    val AnimatedContentTransitionScope<NavBackStackEntry>.defaultEnterTransition: EnterTransition
        get() = slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(300)
        )

    val AnimatedContentTransitionScope<NavBackStackEntry>.defaultExitTransition: ExitTransition
        get() = slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(300)
        )

    val AnimatedContentTransitionScope<NavBackStackEntry>.defaultPopEnterTransition: EnterTransition
        get() = slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(300)
        )

    val AnimatedContentTransitionScope<NavBackStackEntry>.defaultPopExitTransition: ExitTransition
        get() = slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(300)
        )

    val AnimatedContentTransitionScope<NavBackStackEntry>.defaultVerticalEnterTransition: EnterTransition
        get() = slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(300)
        )

    val AnimatedContentTransitionScope<NavBackStackEntry>.defaultVerticalExitTransition: ExitTransition
        get() = slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(300)
        )

    val AnimatedContentTransitionScope<NavBackStackEntry>.defaultVerticalPopEnterTransition: EnterTransition
        get() = slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(300)
        )

    val AnimatedContentTransitionScope<NavBackStackEntry>.defaultVerticalPopExitTransition: ExitTransition
        get() = slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(300)
        )
}