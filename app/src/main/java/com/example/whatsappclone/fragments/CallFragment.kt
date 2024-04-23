package com.example.whatsappclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.adapter.CallAdapter
import com.example.whatsappclone.utils.CallDataUtils


class CallFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CallAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_call, container, false)
        recyclerView = view.findViewById(R.id.callRecyclerView)
        adapter = CallAdapter(requireContext(), CallDataUtils.getCallData())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onResume() {
        super.onResume()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}