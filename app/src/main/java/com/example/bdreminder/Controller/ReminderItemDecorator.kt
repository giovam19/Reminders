package com.example.bdreminder.Controller

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ReminderItemDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val totalItems = parent.adapter?.itemCount ?: 0
        if (position == totalItems - 1) {
            outRect.bottom = 64
        }
    }
}