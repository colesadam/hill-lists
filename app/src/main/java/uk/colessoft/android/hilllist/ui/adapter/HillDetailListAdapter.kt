package uk.colessoft.android.hilllist.ui.adapter

import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.colessoft.android.hilllist.R
import uk.colessoft.android.hilllist.ui.activity.PreferencesActivity
import uk.colessoft.android.hilllist.ui.fragment.HillListFragment
import uk.colessoft.android.hilllist.domain.HillDetail



class HillDetailListAdapter(private var dataset: List<HillDetail>, private var listener: HillListFragment.OnHillSelectedListener,
                            private var cListener: HillListFragment.CheckBoxListener
) :
        RecyclerView.Adapter<HillDetailListAdapter.HillDetailViewHolder>() {


    class HillDetailViewHolder(val view: RelativeLayout) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HillDetailListAdapter.HillDetailViewHolder {

        return HillDetailViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.simple_hill_item, parent, false) as RelativeLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: HillDetailListAdapter.HillDetailViewHolder, position: Int) {
        val context = holder.view.context
        val preferences = PreferenceManager
                .getDefaultSharedPreferences(context)
        val useMetricHeights = preferences.getBoolean(PreferencesActivity.PREF_METRIC_HEIGHTS, false)

        val hill = dataset[position]
        val hillname = holder.view.findViewById<TextView>(R.id.name_entry)
        val hillHeight = holder.view.findViewById<TextView>(R.id.number_entry)
        val checked = holder.view.findViewById<CheckBox>(R.id.check_hill_climbed)
        hillname.text = hill.hill.hillname

        if(useMetricHeights)
            hillHeight.text = hill.hill.heightm?.toString() + "m"
        else hillHeight.text = hill.hill.heightf?.toString() + "ft"

        holder.view.setBackgroundColor(context.getResources().getColor(R.color.white))
        hill.bagging?.let {
            if (it.isNotEmpty()) {
                holder.view.setBackgroundColor(context.getResources().getColor(R.color.paler_light_green))
                checked.isChecked = true
            }
        }
        holder.view.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
            listener.onHillSelected(hill);
        } })

        checked.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                cListener.onCheckBoxClicked(hill.hill.h_id.toInt(),checked.isChecked);
            } })

    }

    fun setData(newData: List<HillDetail>) {
        this.dataset = newData
        notifyDataSetChanged()
    }
}