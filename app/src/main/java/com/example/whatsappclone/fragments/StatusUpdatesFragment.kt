package com.example.whatsappclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.adapter.RecentStatusAdapter
import com.example.whatsappclone.adapter.ViewStatusAdapter
import com.example.whatsappclone.utils.StatusDataUtil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class StatusUpdatesFragment : Fragment() {
    private lateinit var recentStatusRecyclerView: RecyclerView
    private lateinit var viewedStatusRecyclerView: RecyclerView
    private lateinit var recentStatusAdapter: RecentStatusAdapter
    private lateinit var viewedStatusAdapter: ViewStatusAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_status_updates, container, false)
        recentStatusRecyclerView = view.findViewById(R.id.recentStatusRecyclerView)
        viewedStatusRecyclerView = view.findViewById(R.id.viewedStatusRecyclerView)
        recentStatusAdapter =
            RecentStatusAdapter(requireContext(), StatusDataUtil.getRecentStatusData())
        viewedStatusAdapter =
            ViewStatusAdapter(requireContext(), StatusDataUtil.getRecentStatusData())
        recentStatusRecyclerView.layoutManager = LinearLayoutManager(context)
        viewedStatusRecyclerView.layoutManager = LinearLayoutManager(context)
        recentStatusRecyclerView.adapter = recentStatusAdapter
        viewedStatusRecyclerView.adapter = viewedStatusAdapter

        return view
    }

    override fun onResume() {
        super.onResume()
        recentStatusRecyclerView.layoutManager = LinearLayoutManager(context)
        viewedStatusRecyclerView.layoutManager = LinearLayoutManager(context)
        recentStatusRecyclerView.adapter = recentStatusAdapter
        viewedStatusRecyclerView.adapter = viewedStatusAdapter
    }

}