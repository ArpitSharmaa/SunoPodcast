package com.example.sunopodcast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePodcast : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_podcast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val start = view.findViewById<Button>(R.id.start)
        val podcastname = view.findViewById<TextView>(R.id.nameofpodcast)
        start.setOnClickListener {
            val bundle= Bundle()
            bundle.putString("name",podcastname.text.toString())
            findNavController().navigate(R.id.action_createPodcast_to_livePodCast,bundle)
        }
    }
}