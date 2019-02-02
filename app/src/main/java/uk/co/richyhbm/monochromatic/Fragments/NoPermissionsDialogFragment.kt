package uk.co.richyhbm.monochromatic.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import uk.co.richyhbm.monochromatic.BuildConfig
import uk.co.richyhbm.monochromatic.R


class NoPermissionsDialogFragment : androidx.fragment.app.DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(R.string.permission_missing)

        val permission = context!!.getString(R.string.write_secure_settings_permission)
        val adbCommand = context!!.getString(R.string.adb_command, BuildConfig.APPLICATION_ID, permission)

        builder.setMessage(context!!.getString(R.string.grant_permission_adb, adbCommand))
            .setNeutralButton(R.string.share_command, shareAdbCommand())
            .setPositiveButton(R.string.wiki) { _, _ ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/RichyHBM/Monochromatic/wiki")))
            }

        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
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
            startActivity(Intent.createChooser(sendIntent, getString(R.string.share_command)))
        }
    }

}