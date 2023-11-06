package com.example.bdreminder.Controller

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ReminderTouchHelper(val adapter: ReminderAdapter) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = 0 // 0 para deshabilitar el arrastre
        val swipeFlags = ItemTouchHelper.START // Habilita el deslizamiento hacia la izquierda
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val iconSize = 5
            val itemView = viewHolder.itemView
            val maxDx = (iconSize * 2).toFloat() // Máxima cantidad de desplazamiento permitida
            val actualDx = if (dX < -maxDx) -maxDx else dX // Limita el desplazamiento

            // Dibuja el fondo
            val background = RectF(
                itemView.right + actualDx,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            val paint = Paint()
            paint.color = Color.RED // Color de fondo
            c.drawRect(background, paint)

            // Dibuja el icono
            val iconLeftMargin = (itemView.right - iconSize + actualDx).toInt()
            val iconTopMargin = (itemView.top + (itemView.height - iconSize) / 2).toInt()
            val iconRightMargin = (itemView.right + actualDx).toInt()
            val iconBottomMargin = (iconTopMargin + iconSize).toInt()
            paint.color = Color.WHITE // Color del icono
            c.drawRect(
                iconLeftMargin.toFloat(),
                iconTopMargin.toFloat(),
                iconRightMargin.toFloat(),
                iconBottomMargin.toFloat(),
                paint
            )

            // Detecta si el usuario presiona el icono
            if (dX < -iconSize) {
                // El usuario ha deslizado lo suficiente para revelar el icono
                // Puedes mostrar una indicación visual o realizar la eliminación directamente
                if (isCurrentlyActive) {
                    // Realiza la eliminación aquí (opcional)
                    // adapter.removeItem(viewHolder.adapterPosition)
                }
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}