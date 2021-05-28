package com.mano.churchpodcast.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.mano.churchpodcast.R
import com.mano.churchpodcast.databinding.AdapterItemYoutubeVideoBinding
import com.mano.churchpodcast.model.YoutubeVideo

class YoutubeVideoAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<YoutubeVideo>() {

            override fun areItemsTheSame(oldItem: YoutubeVideo, newItem: YoutubeVideo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: YoutubeVideo, newItem: YoutubeVideo): Boolean {
                return oldItem == newItem
            }

        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return YoutubeVideoViewHolder(
            AdapterItemYoutubeVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is YoutubeVideoViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<YoutubeVideo>) {
        differ.submitList(list)
    }

    class YoutubeVideoViewHolder
    constructor(
        private val adapterBinding: AdapterItemYoutubeVideoBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(adapterBinding.root) {

        fun bind(video: YoutubeVideo) {
            adapterBinding.root.setOnClickListener {
                interaction?.onYoutubeVideoSelected(adapterPosition, video)
            }

            adapterBinding.tvVideoTitle.text = video.title
            adapterBinding.tvVideoDescription.text = video.description

            val videoTypeResource = when {
                video.isUpcoming -> R.drawable.ic_baseline_schedule_24
                video.isLive -> R.drawable.ic_baseline_live_tv_24
                else -> R.drawable.ic_baseline_play_circle_outline_24
            }
            adapterBinding.ivVideoType.setImageResource(videoTypeResource)

            if (video.thumbnailUrl.isNotBlank()) {
                Glide.with(itemView)
                    .load(video.thumbnailUrl)
                    .transition(
                        DrawableTransitionOptions.withCrossFade(
                            DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true)
                                .build()
                        )
                    )
                    .into(adapterBinding.ivVideoThumbnail)
            } else adapterBinding.ivVideoThumbnail.setImageResource(R.drawable.hillsong_logo)
        }
    }

    interface Interaction {
        fun onYoutubeVideoSelected(position: Int, video: YoutubeVideo)
    }

}