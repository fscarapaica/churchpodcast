package com.mano.churchpodcast.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mano.churchpodcast.R
import com.mano.churchpodcast.ui.adapter.LocationAdapter
import com.mano.churchpodcast.ui.adapter.MediaItemAdapter
import com.mano.churchpodcast.ui.player.PlayerActivity
import com.mano.churchpodcast.util.ACTIVITY_PLAYER_REQUEST_CODE
import com.mano.churchpodcast.util.getLocation
import com.mano.churchpodcast.util.getSharedPreferences
import com.mano.churchpodcast.util.putExtraJson
import kotlinx.android.synthetic.main.fragment_home.*
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
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        locationAdapter = LocationAdapter(homeViewModel)
        root.rv_location.run {
            layoutManager = GridLayoutManager(context, 3)
            adapter = locationAdapter
        }

        mediaItemAdapter = MediaItemAdapter(homeViewModel)
        root.rv_media_item.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mediaItemAdapter
        }

        homeViewModel.locationList.observe(viewLifecycleOwner, Observer { locationList ->
            locationAdapter.submitList(locationList, context?.getSharedPreferences()?.getLocation())
        })

        homeViewModel.mediaItemList.observe(viewLifecycleOwner, Observer { mediaItemList ->
            mediaItemAdapter.submitList(mediaItemList)
        })

        homeViewModel.mediaItemSelected.observe(viewLifecycleOwner, Observer { mediaItemSelected ->
            // TODO: Set an extra key for the media item
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtraJson(mediaItemSelected)
            }

            activity?.startActivityForResult(intent, ACTIVITY_PLAYER_REQUEST_CODE)
        })

        homeViewModel.networkStatus.observe(viewLifecycleOwner, Observer { networkStatatus ->
            networkStatatus?.let {
                if (networkStatatus == HomeViewModel.NetworkStatus.LOADING) {
                    loaderOverlapVisibility(true)
                } else if (networkStatatus == HomeViewModel.NetworkStatus.DONE) {
                    loaderOverlapVisibility(false)
                }
            }
        })

        homeViewModel.updateContent()

        return root
    }

    private fun loaderOverlapVisibility(isVisible: Boolean) {
        if (isVisible) {
            loading_overlap.visibility = VISIBLE
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            loading_overlap.visibility = INVISIBLE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

}
