package com.denizsubasi.creditcardview

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.denizsubasi.creditcardview.databinding.ViewHolderCreditCardBinding
import com.denizsubasi.creditcardview.lib.CreditCardItem

class CreditCardsAdapter(private val itemClickFunc: ((item: CreditCardItem) -> Unit)) :
    ListAdapter<CreditCardItem, CreditCardsAdapter.CreditCardHolder>(
        object : DiffUtil.ItemCallback<CreditCardItem>() {
            override fun areItemsTheSame(
                oldItem: CreditCardItem,
                newItem: CreditCardItem
            ): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(
                oldItem: CreditCardItem,
                newItem: CreditCardItem
            ): Boolean {
                return newItem == oldItem
            }

        }) {

    inner class CreditCardHolder(private val viewBinding: ViewHolderCreditCardBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bindView(item: CreditCardItem) {
            viewBinding.item = item
            viewBinding.editTextView.setOnClickListener {
                itemClickFunc(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardHolder {
        return CreditCardHolder(
            ViewHolderCreditCardBinding.inflate(
                from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CreditCardHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    fun addCard(item: CreditCardItem) {
       val currentList = currentList.toMutableList()
        if (currentList.any { it.id == item.id }) {
            submitList(currentList.map { if(it.id == item.id)  item else it })
        } else {
            submitList(currentList.apply { add(item) })
        }
    }

}