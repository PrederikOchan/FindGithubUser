package com.rozedfrozzy.findgithubuser.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rozedfrozzy.findgithubuser.R
import com.rozedfrozzy.findgithubuser.data.response.Item
import kotlinx.android.synthetic.main.item_list.view.*
import org.jetbrains.anko.toast

class UserItemsAdapter(val context: Context) : RecyclerView.Adapter<UserItemsAdapter.ViewHolder>() {

    private var userList: ArrayList<Item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = userList[position]
        holder.setModel(model)
    }

    fun updateList(list: List<Item>) {
        userList.addAll(list)
        if (userList.isEmpty()) {
            context.toast(context.getString(R.string.empty_result))
        }
        notifyDataSetChanged()
    }

    fun clearData() {
        userList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setModel(user: Item) {
            user?.login?.let { itemView.itemUsername.text = user.login }
            user?.avatarUrl?.let {
                Glide.with(context)
                    .load(user.avatarUrl)
                    .into(itemView.itemImage)
            }
        }
    }
}