package com.example.rummysequencemodule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rummysequencemodule.databinding.CardListItemBinding

class CardAdapter(val cardList: ArrayList<AssignedCards>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(val binding: CardListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = CardListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]
        holder.binding.ivCard.setImageResource(card.cardPath)
    }

    override fun getItemCount(): Int = cardList.size
}