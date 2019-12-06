package com.mano.hillsongpodcast.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mano.hillsongpodcast.R
import com.mano.hillsongpodcast.ui.adapter.LocationAdapter
import com.mano.hillsongpodcast.ui.adapter.MediaItemAdapter
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var mediaItemAdapter: MediaItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        locationAdapter = LocationAdapter()
        root.rv_location.run {
            layoutManager = GridLayoutManager(context, 3)
            adapter = locationAdapter
        }

        mediaItemAdapter = MediaItemAdapter()
        root.rv_media_item.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mediaItemAdapter
        }

        homeViewModel.locationList.observe(this, Observer { locationList ->
            locationAdapter.submitList(locationList)
        })

        homeViewModel.mediaItemList.observe(this, Observer { mediaItemList ->
            mediaItemAdapter.submitList(mediaItemList)
        })

        homeViewModel.updateContent()

        return root
    }

}