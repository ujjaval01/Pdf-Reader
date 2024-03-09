package com.cobrapdf.reader.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat.getActionView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cobrapdf.reader.R
import com.cobrapdf.reader.adapter.PdfAdapter
import com.cobrapdf.reader.databinding.FragmentMainBinding
import com.cobrapdf.reader.viewmodel.PdfViewModel

class MainFragment : Fragment() , SwipeRefreshLayout.OnRefreshListener {

    private var adapter: PdfAdapter? = null
    private val viewModel by lazy {
        ViewModelProvider(this)[PdfViewModel::class.java]
    }
    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        getData()
    }

    private fun getData() {

        adapter = PdfAdapter(activity,viewModel)
        viewModel.getAllPdf().observe(this) {
            if (it?.isEmpty()!!){
                binding.recyclerView.visibility = View.GONE
                binding.messageLayout.visibility = View.VISIBLE
            }else {
                binding.messageLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter?.submitList(it)
            }
        }
        binding.recyclerView?.adapter = adapter
    }

    override fun onRefresh() {
        getData()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu,menu)
        val searchViewItem = menu.findItem(R.id.menu_search)
        val searchView = getActionView(searchViewItem) as SearchView

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    Log.e("change", " data search$newText");

                   adapter?.filter?.filter(newText)
                    return true
                }
            })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_search->{
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}