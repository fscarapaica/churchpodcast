package com.mano.hillsongpodcast.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mano.hillsongpodcast.R

class LibraryFragment : Fragment() {

    private lateinit var libraryViewModel: LibraryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        libraryViewModel =
            ViewModelProviders.of(this).get(LibraryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_library, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        libraryViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

}