package com.example.sunopodcast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sunopodcast.Viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.launch
import java.lang.Exception


@AndroidEntryPoint
class ListOfPodcast() : Fragment() {
    val viewModel: MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        try {
            viewModel.getallpodcastlist()
        }catch (_:Exception){}

        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_of_podcast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val create = view.findViewById<Button>(R.id.Create)
        create.setOnClickListener {
            findNavController().navigate(R.id.action_listOfPodcast_to_createPodcast)
        }

        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        swipe.setOnRefreshListener {

            try {
                viewModel.getallpodcastlist()
            }catch (_:Exception){}
        }
        val recylelist = view.findViewById<RecyclerView>(R.id.listoflivepodcast)
        recylelist.layoutManager = LinearLayoutManager(context)
        swipe.setOnRefreshListener {
            viewModel.getallpodcastlist()
            val job = lifecycleScope.launch {
                viewModel.padcastlist.collect(){
                    recylelist.adapter = ListofLivePodcastAdapter(it){
                        val bundle = Bundle()
                        bundle.putString("param1",it)
                        findNavController().navigate(R.id.action_listOfPodcast_to_listenpodcast,bundle)
                    }
                    swipe.isRefreshing= false
                }
            }



        }

        lifecycleScope.launch {
            viewModel.padcastlist.collect(){
                recylelist.adapter = ListofLivePodcastAdapter(it){
                    val bundle = Bundle()
                    bundle.putString("param1",it)
                    findNavController().navigate(R.id.action_listOfPodcast_to_listenpodcast,bundle)
                }
            }
        }



    }


}