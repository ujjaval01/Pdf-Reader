package com.cobrapdf.reader.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.cobrapdf.reader.databinding.RecentListItemBinding

class RecentViewHolder(private val recentListItemBinding: RecentListItemBinding?) : RecyclerView.ViewHolder(
    recentListItemBinding?.root!!
){
    val binding = recentListItemBinding
}