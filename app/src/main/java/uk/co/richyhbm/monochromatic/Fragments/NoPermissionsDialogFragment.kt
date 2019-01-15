package uk.co.richyhbm.monochromatic.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import uk.co.richyhbm.monochromatic.BuildConfig
import uk.co.richyhbm.monochromatic.R

class NoPermissionsDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(R.string.permission_missing)

        val permission = context!!.getString(R.string.write_secure_settings_permission)
        val adbCommand = context!!.getString(R.string.adb_command, BuildConfig.APPLICATION_ID, permission)

        builder.setMessage(context!!.getString(R.string.grant_permission_adb, adbCommand))
            .setNeutralButton(R.string.share, shareAdbCommand())
            .setPositiveButton(android.R.string.ok) { _, _ -> dismiss() }
        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    private fun shareAdbCommand(): (d: DialogInterface, i: Int) -> Unit {
        return { _: DialogInterface, _: Int ->
            val permission = context!!.getString(R.string.write_secure_settings_permission)
            val adbCommand = context!!.getString(R.string.adb_command, BuildConfig.APPLICATION_ID, permission)

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, adbCommand)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, getString(R.string.share)))
        }
    }

}