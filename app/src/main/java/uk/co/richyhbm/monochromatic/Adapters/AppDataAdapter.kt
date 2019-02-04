package uk.co.richyhbm.monochromatic.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.co.richyhbm.monochromatic.Data.AppData
import uk.co.richyhbm.monochromatic.ViewHolders.AppDataViewHolder


class AppDataAdapter(private val appsList: ArrayList<AppData>) : RecyclerView.Adapter<AppDataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppDataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(uk.co.richyhbm.monochromatic.R.layout.app_data_list_row, parent, false)
        return AppDataViewHolder(view)
    }

    override fun getItemCount(): Int = appsList.size

    override fun onBindViewHolder(holder: AppDataViewHolder, position: Int) {
        holder.bind(appsList[position])
    }

    fun swap(data: List<AppData>) {
        appsList.clear()
        appsList.addAll(data)
        notifyDataSetChanged()
    }
}