package com.cobrapdf.reader.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cobrapdf.reader.adapter.RecentAdapter
import com.cobrapdf.reader.databinding.FragmentRecentBinding
import com.cobrapdf.reader.viewmodel.PdfViewModel

class RecentFragment : Fragment(),SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by lazy {
        ViewModelProvider(this)[PdfViewModel::class.java]
    }
    private val binding by lazy { FragmentRecentBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        getData()
    }

    private fun getData() {

        val adapter = RecentAdapter(requireContext(),viewModel)
        viewModel.getAllPdfDatabase()?.observe(this) {
            if (it.isEmpty()){
                binding.recyclerView.visibility = View.GONE
                binding.messageLayout.visibility = View.VISIBLE
            }else {
                binding.messageLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(it)
            }
        }
        binding.recyclerView.adapter = adapter
    }

    override fun onRefresh() {
        getData()
        binding.swipeRefreshLayout.isRefreshing = false
    }

}