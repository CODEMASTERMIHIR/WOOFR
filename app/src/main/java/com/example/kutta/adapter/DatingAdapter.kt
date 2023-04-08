package com.example.kutta.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kutta.activity.MessageActivity

import com.example.kutta.databinding.ItemUserLayoutBinding
import com.example.kutta.model.UserModel

class DatingAdapter (val context: Context, val datinglist: ArrayList<UserModel>): RecyclerView.Adapter<DatingAdapter.DatingViewHolder>(){
    inner class DatingViewHolder(val binding: ItemUserLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {
        return DatingViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {
        holder.binding.textView3.text=datinglist[position].name
        holder.binding.textView2.text=datinglist[position].email

        Glide.with(context).load(datinglist[position].image).into(holder.binding.userImage)

        holder.binding.chat.setOnClickListener {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("userId",datinglist[position].number)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return datinglist.size
    }
}