package com.example.antoanbmtt.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.antoanbmtt.api.resource.SharedResource
import com.example.antoanbmtt.databinding.ReceivedResourceLayoutBinding
import com.example.antoanbmtt.databinding.ResourceItemLayoutBinding
import com.example.antoanbmtt.helper.toByteRepresentation
import com.example.antoanbmtt.repository.resource.Resource

class ReceivedResourceAdapter(
    private val onClick : (String) -> Unit,
    private val onShow : (String,String) -> Unit
) : ListAdapter<SharedResource,ReceivedResourceAdapter.ReceivedViewHolder>(diffCallback){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReceivedViewHolder.create(parent)
    override fun onBindViewHolder(holder: ReceivedViewHolder, position: Int) {
        holder.bind(getItem(position),onClick,onShow)
    }
    class ReceivedViewHolder(
        private val binding : ReceivedResourceLayoutBinding
    ) : ViewHolder(binding.root){
        companion object {
            fun create(parent: ViewGroup) : ReceivedViewHolder{
                val binding = ReceivedResourceLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return ReceivedViewHolder(binding)
            }
        }
        fun bind(resource: SharedResource, onClick: (String) -> Unit, onShow: (String,String) -> Unit){
            binding.apply {
                resourceItem.setOnClickListener{
                    onClick.invoke(resource.uri)
                }
                resourceName.text = resource.name
                resourceSize.text = resource.capacity.toByteRepresentation()
                buttonMore.setOnClickListener {
                    onShow.invoke(
                        resource.uri,
                        resource.name,
                    )
                }
            }
        }
    }
}
private val diffCallback = object : DiffUtil.ItemCallback<SharedResource>(){
    override fun areItemsTheSame(oldItem: SharedResource, newItem: SharedResource): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SharedResource, newItem: SharedResource): Boolean {
        return oldItem.uri == newItem.uri
    }

}