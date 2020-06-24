package com.mano.churchpodcast.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.mano.churchpodcast.R
import com.mano.churchpodcast.model.Location
import kotlinx.android.synthetic.main.adapter_item_site.view.*

class LocationAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Location>() {

        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    private var selectedPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LocationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_item_site,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocationViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Location>?, selectedLocation: Location?) {
        list?.run {
            differ.submitList(list)
            selectedLocation?.let {
                selectedPosition = list.indexOf(selectedLocation)
            }
        }
    }

    inner class LocationViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Location) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onLocationItemSelected(adapterPosition, item)
                selectedPosition = adapterPosition
                notifyDataSetChanged()
            }

            itemView.site_name.apply {
                text = item.name
                isSelected = selectedPosition == adapterPosition
            }

            Glide.with(context)
                .load(item.image)
                .apply(RequestOptions.circleCropTransform())
                .transition(
                    withCrossFade(
                        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true)
                            .build()
                    )
                )
                .into(itemView.site_image)
        }
    }

    interface Interaction {
        fun onLocationItemSelected(position: Int, item: Location)
    }

}
