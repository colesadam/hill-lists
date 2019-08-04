package uk.colessoft.android.hilllist.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.colessoft.android.hilllist.model.HillDetail

class HillDetailListAdapter(private val dataset: List<HillDetail>) :
        RecyclerView.Adapter<HillDetailListAdapter.HillDetailViewHolder>() {

    class HillDetailViewHolder(val view: View) : RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HillDetailListAdapter.HillDetailViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: HillDetailListAdapter.HillDetailViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}