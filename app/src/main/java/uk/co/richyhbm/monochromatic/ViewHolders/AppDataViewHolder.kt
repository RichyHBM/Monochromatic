package uk.co.richyhbm.monochromatic.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.richyhbm.monochromatic.Data.AppData
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Utilities.Settings

class AppDataViewHolder(itemView: View, private val onClick: () -> Unit) : RecyclerView.ViewHolder(itemView) {

    fun bind(appData: AppData) {
        itemView.findViewById<ImageView>(R.id.app_list_row_imageView).setImageDrawable(appData.icon)
        itemView.findViewById<TextView>(R.id.app_list_row_textView).text = appData.appName

        val settings = Settings(itemView.context)

        val switch = itemView.findViewById<Switch>(R.id.app_list_row_switch1)
        switch.setOnCheckedChangeListener(null)

        switch.isChecked = settings.isWhiteListed(appData.packageName)

        switch.setOnCheckedChangeListener { _, isChecked ->
            val packageName = appData.packageName
            when (isChecked) {
                true -> settings.addAppWhiteList(packageName)
                false -> settings.removeAppWhiteList(packageName)
            }

            onClick()
        }
    }
}