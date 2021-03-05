package com.pg.exo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pg.exo.databinding.AdapterMainBinding

// https://medium.com/swlh/how-to-use-view-binding-in-recyclerview-adapter-f818b96c678a
class MainAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AdapterMainBinding) : RecyclerView.ViewHolder(binding.root)

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onItemClick(selected: String);
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterMainBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            with(list[position]) {
                binding.tvContent.text = this
                binding.tvContent.setOnClickListener {
                    listener?.onItemClick(this)
                }
            }
        }
    }

    override fun getItemCount() = list.size
}