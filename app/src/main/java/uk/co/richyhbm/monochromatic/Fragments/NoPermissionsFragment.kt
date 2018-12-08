package uk.co.richyhbm.monochromatic.Fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import uk.co.richyhbm.monochromatic.Constants
import uk.co.richyhbm.monochromatic.R

class NoPermissionsFragment : Fragment() {

    companion object {
        fun newInstance() = NoPermissionsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.no_permissions_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        val adbCommandText = view?.findViewById<TextView>(R.id.adb_command_text)
        adbCommandText?.text = Constants.getAdbCommand(context!!)
        adbCommandText?.setOnClickListener {
            val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("adb_command", Constants.getAdbCommand(context!!))
            clipboard.primaryClip = clip
            Toast.makeText(context, "Command copied", Toast.LENGTH_SHORT).show()
        }
    }
}