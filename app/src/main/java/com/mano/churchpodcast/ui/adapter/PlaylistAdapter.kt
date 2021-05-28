package com.mano.churchpodcast.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.mano.churchpodcast.R
import com.mano.churchpodcast.databinding.AdapterItemMediaBinding
import com.mano.churchpodcast.databinding.AdapterItemPlaylistBinding
import com.mano.churchpodcast.model.MediaItem
import com.mano.churchpodcast.model.Playlist

class PlaylistAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_PLAYLIST = 0
        const val TYPE_MEDIA_ITEM = 1

        private val REQUEST_OPTIONS = RequestOptions().apply {
            circleCrop()
            override(400, 400)
        }

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExpandableModel>() {

            override fun areItemsTheSame(oldItem: ExpandableModel, newItem: ExpandableModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ExpandableModel, newItem: ExpandableModel): Boolean {
                return oldItem == newItem
            }

        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_PLAYLIST) {
            PlaylistViewHolder(
                AdapterItemPlaylistBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                interaction
            )
        } else {
            MediaItemViewHolder(
                AdapterItemMediaBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PlaylistViewHolder -> {
                holder.bind(differ.currentList[position].toTitlePlayList())
            }
            is MediaItemViewHolder -> {
                val mediaItem = differ.currentList[position].toMediaItemModel().mediaItem
                holder.bind(mediaItem)
                holder.itemView.setOnClickListener {
                    interaction?.onMediaItemSelected(mediaItem)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].type
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    fun submitList(list: List<Playlist>) {
        differ.submitList(list.toListExpandableModel())
    }

    class PlaylistViewHolder constructor(
        private val viewBinding: AdapterItemPlaylistBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(playlistModel: ExpandableModel.PlaylistModel) = with(itemView) {
            setOnClickListener {
                playlistModel.run {
                    interaction?.onPlaylistSelected(playlist, position, isExpanded)
                }
            }
            playlistModel.playlist.run {
                viewBinding.playlistName.text = name
                viewBinding.playlistDescription.text = context.getString(R.string.playlist_description, season)
                if (imageUrl.isNotEmpty()) {
                    Glide.with(context)
                        .load(imageUrl)
                        .apply(REQUEST_OPTIONS)
                        .transition(
                            DrawableTransitionOptions.withCrossFade(
                                DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true)
                                    .build()
                            )
                        ).into(viewBinding.playlistImage)
                }
            }
        }
    }

    class MediaItemViewHolder
    constructor(
        private val viewBinding: AdapterItemMediaBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: MediaItem) = with(itemView) {
            viewBinding.mediaItemTitle.text = item.title
            viewBinding.mediaItemDescription.text = item.description

            Glide.with(context)
                .load(item.image)
                .transition(
                    DrawableTransitionOptions.withCrossFade(
                        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true)
                            .build()
                    )
                )
                .into(viewBinding.mediaItemImage)
        }

    }

    interface Interaction {
        fun onPlaylistSelected(item: Playlist, position: Int, isExpanded: Boolean)
        fun onMediaItemSelected(mediaItem: MediaItem)
    }

    private fun List<Playlist>.toListExpandableModel(): List<ExpandableModel> =
        mutableListOf<ExpandableModel>().also { tempList ->
            forEachIndexed { index, playlist ->
                tempList.add(ExpandableModel.PlaylistModel(playlist, index))
                playlist.mediaItems.forEach { mediaItem ->
                    tempList.add(ExpandableModel.MediaItemModel(mediaItem))
                }
            }
        }

    sealed class ExpandableModel(val type: Int) {
        data class PlaylistModel(val playlist: Playlist,
                                 val position: Int,
        ): ExpandableModel(TYPE_PLAYLIST) {
            var isExpanded: Boolean = playlist.mediaItems.isNotEmpty()
        }

        data class MediaItemModel(val mediaItem: MediaItem
        ): ExpandableModel(TYPE_MEDIA_ITEM)
    }

    private fun ExpandableModel.toTitlePlayList() = this as ExpandableModel.PlaylistModel

    private fun ExpandableModel.toMediaItemModel() = this as ExpandableModel.MediaItemModel

}
