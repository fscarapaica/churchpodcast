package com.mano.churchpodcast.ui.discover

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mano.churchpodcast.databinding.FragmentDiscoverBinding
import com.mano.churchpodcast.ext.navigateExoPlayer
import com.mano.churchpodcast.ui.adapter.PlaylistAdapter

class DiscoverFragment : Fragment() {

    private lateinit var discoverViewModel: DiscoverViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        discoverViewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)
        val fragmentBinding = FragmentDiscoverBinding.inflate(inflater)

        val playlistAdapter = PlaylistAdapter(discoverViewModel)
        fragmentBinding.rvPlaylist.run {
            layoutManager = LinearLayoutManager(context)
            adapter = playlistAdapter
        }

        discoverViewModel.playlistList.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                playlistAdapter.submitList(it)
            }
        }

        discoverViewModel.mediaItemSelected.observe(viewLifecycleOwner) { selectedMediaItem ->
            if (selectedMediaItem != null) {
                navigateExoPlayer(selectedMediaItem)
            }
        }

        return fragmentBinding.root
    }

}