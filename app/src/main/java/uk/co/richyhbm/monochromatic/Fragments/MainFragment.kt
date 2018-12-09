package uk.co.richyhbm.monochromatic.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_fragment.*
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Services.MonochromeService
import uk.co.richyhbm.monochromatic.Utilities.Permissions
import uk.co.richyhbm.monochromatic.Utilities.Settings


class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val settings = Settings(requireContext())

        enabled_switch.isEnabled = Permissions.hasSecureSettingsPermission(requireContext())
        enabled_switch.isChecked = settings.isEnabled() && MonochromeService.isRunning(requireContext())
        service_status.text = if(enabled_switch.isChecked) getString(R.string.service_enabled) else getString(R.string.service_disabled)

        enabled_switch.setOnCheckedChangeListener {_, b ->
            settings.setEnabled(b)
            service_status.text = if(settings.isEnabled()) getString(R.string.service_enabled) else getString(R.string.service_disabled)

            if (settings.isEnabled()) MonochromeService.startService(requireContext())
            else MonochromeService.stopService(requireContext())
        }
    }
}