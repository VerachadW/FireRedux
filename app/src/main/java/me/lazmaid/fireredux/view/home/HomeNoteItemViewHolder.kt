package me.lazmaid.fireredux.view.home

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_note.view.*
import me.lazmaid.fireredux.model.Note

/**
 * Created by VerachadW on 12/31/2016 AD.
 */

class HomeNoteItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(item: Note) {
        itemView.apply {
            tvNoteTitle.text = item.title
        }
    }
}
