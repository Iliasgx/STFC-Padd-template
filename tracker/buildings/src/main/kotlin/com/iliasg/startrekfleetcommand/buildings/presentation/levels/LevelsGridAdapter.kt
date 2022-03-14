package com.iliasg.startrekfleetcommand.buildings.presentation.levels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iliasg.startrekfleetcommand.buildings.databinding.ItemLevelBinding

internal class LevelsGridAdapter(
    private val levelsCount: Int,
    private val onLevelClick: (level: Int) -> Unit
) : RecyclerView.Adapter<LevelsGridAdapter.LevelsGridViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelsGridViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return LevelsGridViewHolder(
            ItemLevelBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LevelsGridViewHolder, position: Int) {
        val level = position + 1
        holder.binding.tvLevel.text = level.toString()

        holder.binding.root.setOnClickListener {
            onLevelClick(level)
        }
    }

    override fun getItemCount(): Int = levelsCount

    inner class LevelsGridViewHolder(val binding: ItemLevelBinding) : RecyclerView.ViewHolder(binding.root)
}