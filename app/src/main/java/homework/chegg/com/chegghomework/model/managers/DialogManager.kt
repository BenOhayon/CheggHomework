package homework.chegg.com.chegghomework.model.managers

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import homework.chegg.com.chegghomework.R

/**
 * A utility class for displaying alert dialogs to convey messages to the user.
 */
object DialogManager {

    fun showRequestFailedDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
                .setTitle(R.string.chegghomeassignment_dialog_request_failed_title)
                .setMessage(R.string.chegghomeassignment_dialog_request_failed_message)
                .setPositiveButton(R.string.chegghomeassignment_dialog_request_failed_positive_button_text, null)
                .create()

        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setOnShowListener {
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.colorPrimary))
        }
        alertDialog.show()
    }

}