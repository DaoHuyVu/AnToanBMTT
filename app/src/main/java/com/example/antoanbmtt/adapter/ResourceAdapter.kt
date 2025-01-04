package com.example.antoanbmtt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.antoanbmtt.databinding.ResourceItemLayoutBinding
import com.example.antoanbmtt.repository.resource.Resource

class ResourceAdapter(
    private val onClick : (String) -> Unit,
    private val onShow : (Long,String,Boolean,Boolean,String) -> Unit
) : ListAdapter<Resource,ResourceAdapter.ResourceViewHolder>(diffCallback){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ResourceViewHolder.create(parent)
    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        holder.bind(getItem(position),onClick,onShow)
    }
    class ResourceViewHolder(
        private val binding : ResourceItemLayoutBinding
    ) : ViewHolder(binding.root){
        companion object {
            fun create(parent: ViewGroup) : ResourceViewHolder{
                val binding = ResourceItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return ResourceViewHolder(binding)
            }
        }
        fun bind(resource: Resource, onClick: (String) -> Unit, onShow: (Long,String,Boolean,Boolean,String) -> Unit){
            binding.apply {
                resourceItem.setOnClickListener{
                    onClick.invoke(resource.uri)
                }
                resourceName.text = resource.name
                resourceSize.text = resource.capacity
                resourceLastUpdate.text = resource.lastUpdate
                buttonMore.setOnClickListener {
                    onShow.invoke(
                        resource.id,
                        resource.uri,
                        resource.isFavourite,
                        resource.isTempDelete,
                        resource.name
                    )
                }
                favorite.visibility = if(resource.isFavourite) View.VISIBLE else View.INVISIBLE
            }
        }
    }
}
private val diffCallback = object : DiffUtil.ItemCallback<Resource>(){
    override fun areItemsTheSame(oldItem: Resource, newItem: Resource): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Resource, newItem: Resource): Boolean {
        return newItem == oldItem
    }

}