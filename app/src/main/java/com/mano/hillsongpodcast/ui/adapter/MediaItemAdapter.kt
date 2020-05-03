package com.mano.hillsongpodcast.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.mano.hillsongpodcast.R
import com.mano.hillsongpodcast.model.MediaItem
import kotlinx.android.synthetic.main.adapter_item_media.view.*

class MediaItemAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaItem>() {

        override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
            return  oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MediaItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_item_media,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MediaItemViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<MediaItem>) {
        differ.submitList(list)
    }

    class MediaItemViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: MediaItem) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onMediaItemSelected(adapterPosition, item)
            }

            itemView.media_item_title.text = item.title
            itemView.media_item_description.text = item.description

            Glide.with(context)
                .load(item.image)
                .transition(
                    DrawableTransitionOptions.withCrossFade(
                        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true)
                            .build()
                    )
                )
                .into(itemView.media_item_image)
        }
    }

    interface Interaction {
        fun onMediaItemSelected(position: Int, item: MediaItem)
    }
}