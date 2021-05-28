package com.mano.churchpodcast.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mano.churchpodcast.databinding.FragmentHomeBinding
import com.mano.churchpodcast.ext.navigateYoutubePlayer
import com.mano.churchpodcast.ui.MainActivityViewModel
import com.mano.churchpodcast.ui.adapter.YoutubeVideoAdapter

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var youtubeVideoAdapter: YoutubeVideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentHomeBinding.inflate(inflater)

        activityViewModel.selectedLocation.observe(viewLifecycleOwner, { location ->
            homeViewModel.onLocationChange(location)
        })

        fragmentBinding.swipeRefresh.setOnRefreshListener {
            homeViewModel.updateContent()
        }

        fragmentBinding.rvMediaItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                fragmentBinding.swipeRefresh.apply {
                    if ((isEnabled xor !recyclerView.canScrollVertically(-1))
                        and !isRefreshing
                    ) {
                        isEnabled = isEnabled.not()
                    }
                }
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    homeViewModel.getNextPage()
                }
            }
        })

        youtubeVideoAdapter = YoutubeVideoAdapter(homeViewModel)
        fragmentBinding.rvMediaItem.run {
            layoutManager = LinearLayoutManager(context)
            adapter = youtubeVideoAdapter
        }

        homeViewModel.youtubeVideoList.observe(viewLifecycleOwner, { youtubeVideoList ->
            youtubeVideoAdapter.submitList(youtubeVideoList)
        })

        homeViewModel.networkStatus.observe(viewLifecycleOwner, { networkStatus ->
            networkStatus?.let {
                if (networkStatus == HomeViewModel.NetworkStatus.LOADING) {
                    fragmentBinding.swipeRefresh.isRefreshing = true
                } else if (networkStatus == HomeViewModel.NetworkStatus.DONE) {
                    fragmentBinding.swipeRefresh.isRefreshing = false
                }
            }
        })

        homeViewModel.selectedVideo.observe(viewLifecycleOwner, { selectedYoutubeVideo ->
            if (selectedYoutubeVideo != null) {
                navigateYoutubePlayer(selectedYoutubeVideo)
            }
        })

        return fragmentBinding.root
    }

}
