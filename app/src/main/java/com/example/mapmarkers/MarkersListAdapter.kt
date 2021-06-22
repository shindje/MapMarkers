package com.example.mapmarkers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mapmarkers.room.MarkerEntity

class MarkersListAdapter(
    private val onSaveClick: (MarkerEntity) -> Unit,
    private val onDeleteClick: (MarkerEntity) -> Unit)
: ListAdapter<MarkerEntity, MarkersListAdapter.MarkerViewHolder>(MarkerComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item, parent, false)
        return MarkerViewHolder(view, onSaveClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class MarkerViewHolder(
        itemView: View,
        val onSaveClick: (MarkerEntity) -> Unit,
        val onDeleteClick: (MarkerEntity) -> Unit
    ): RecyclerView.ViewHolder(itemView) {
        private val etName: TextView = itemView.findViewById(R.id.item_et_name)
        private val etAnnotation: TextView = itemView.findViewById(R.id.item_et_annotation)
        private var currentMarker: MarkerEntity? = null

        init {
            itemView.findViewById<Button>(R.id.btn_item_save).setOnClickListener {
                currentMarker?.let {
                    it.name = etName.text.toString()
                    it.annotation = etAnnotation.text.toString()
                    onSaveClick(it)
                }
            }
            itemView.findViewById<Button>(R.id.btn_item_delete).setOnClickListener {
                currentMarker?.let {
                    onDeleteClick(it)
                }
            }
        }


        fun bind(mark: MarkerEntity) {
            currentMarker = mark

            etName.text = mark.name
            etAnnotation.text = mark.annotation
        }
    }

    class MarkerComparator : DiffUtil.ItemCallback<MarkerEntity>() {
        override fun areItemsTheSame(oldItem: MarkerEntity, newItem: MarkerEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MarkerEntity, newItem: MarkerEntity): Boolean {
            return oldItem.id == newItem.id
        }
    }
}