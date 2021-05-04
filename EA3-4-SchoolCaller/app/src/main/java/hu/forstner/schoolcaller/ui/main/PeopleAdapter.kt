package hu.forstner.schoolcaller.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.forstner.schoolcaller.R
import hu.forstner.schoolcaller.data.model.People


class PeopleAdapter(
    private val context: Context,
    private val dataset: List<People>,
    private val onClickListener: ListItemClickListener
) : RecyclerView.Adapter<PeopleAdapter.ItemViewHolder>() {

    interface ListItemClickListener {
        fun onListItemClick(position: Int)
    }

    class ItemViewHolder(private val view: View, private val onClickListener: ListItemClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }
        val tvName: TextView = view.findViewById(R.id.tvName)
        val imgHead : ImageView = view.findViewById(R.id.imgHead)
        override fun onClick(v: View?) {
            val position = adapterPosition
            onClickListener.onListItemClick(position)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(
            R.layout.peopleitem,
            parent,
            false
        )
        return ItemViewHolder(adapterLayout, onClickListener)

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.tvName.text = item.name
        holder.imgHead.setImageDrawable(context.resources.getDrawable(item.resId))
    }

    override fun getItemCount() = dataset.size



}