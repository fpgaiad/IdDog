package br.com.iddog.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.iddog.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_dog_list.view.*

class DogListAdapter(
    private val cardDogListener: (String) -> Unit
) : RecyclerView.Adapter<DogListAdapter.DogListViewHolder>() {

    inner class DogListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogListViewHolder {
        return DogListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_dog_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: DogListViewHolder, position: Int) {
        val urlDog = differ.currentList[position]
        holder.itemView.apply {
            cvDogItem.setOnClickListener { cardDogListener(urlDog) }
            Glide
                .with(this)
                .load(urlDog)
                .placeholder(R.drawable.placeholder)
                .into(ivDogItem)
        }
    }
}