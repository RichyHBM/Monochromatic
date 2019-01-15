package uk.co.richyhbm.monochromatic.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Toast
import uk.co.richyhbm.monochromatic.BuildConfig
import uk.co.richyhbm.monochromatic.R

class NoPermissionsDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(R.string.permission_missing)

        val permission = context!!.getString(R.string.write_secure_settings_permission)
        val adbCommand = context!!.getString(R.string.adb_command, BuildConfig.APPLICATION_ID, permission)

        builder.setMessage(context!!.getString(R.string.grant_permission_adb, adbCommand))
            .setNeutralButton(android.R.string.copy, copyAdbCommand())
            .setPositiveButton(android.R.string.ok) { _, _ -> dismiss() }
        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    private fun copyAdbCommand(): (d: DialogInterface, i: Int) -> Unit {
        return { _: DialogInterface, _: Int ->
            val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val permission = context!!.getString(R.string.write_secure_settings_permission)
            val adbCommand = context!!.getString(R.string.adb_command, BuildConfig.APPLICATION_ID, permission)
            val clip = ClipData.newPlainText("adb_command", adbCommand)
            clipboard.primaryClip = clip
            Toast.makeText(context, "Command copied", Toast.LENGTH_SHORT).show()
        }
    }

}