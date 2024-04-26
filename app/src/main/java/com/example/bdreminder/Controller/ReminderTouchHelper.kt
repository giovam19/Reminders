package com.example.bdreminder.Controller

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class ReminderTouchHelper(val adapter: ReminderAdapter, val context: Context) : ItemTouchHelper.Callback() {
    lateinit var deleteButton: ImageView
    lateinit var editButton: ImageView

    private val buttonWidth = 40

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlags = ItemTouchHelper.START // Habilita el deslizamiento hacia la izquierda
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
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
            val itemView = viewHolder.itemView
            val buttonHeight = itemView.height
            val isActive = dX < 0 // Asegúrate de que el deslizamiento es hacia la izquierda
            val leftButtonArea = RectF(
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat() - buttonWidth,
                itemView.bottom.toFloat()
            )
            val rightButtonArea = RectF(
                itemView.right.toFloat() - buttonWidth,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )

            val paint = Paint()
            if (isActive) {
                // Dibuja el fondo para el botón izquierdo
                paint.color = Color.RED // Puedes cambiar el color del fondo
                c.drawRect(leftButtonArea, paint)

                // Dibuja el fondo para el botón derecho
                paint.color = Color.BLUE // Puedes cambiar el color del fondo
                c.drawRect(rightButtonArea, paint)

                // Dibuja el icono para el botón izquierdo
                paint.color = Color.WHITE // Color del icono
                paint.textSize = 40f
                paint.textAlign = Paint.Align.CENTER
                c.drawText("Acción 1", rightButtonArea.centerX(), rightButtonArea.centerY() + 15, paint)

                // Dibuja el icono para el botón derecho
                c.drawText("Acción 2", leftButtonArea.centerX(), leftButtonArea.centerY() + 15, paint)
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Acciones según la dirección del deslizamiento
        val position = viewHolder.absoluteAdapterPosition
        if (direction == ItemTouchHelper.START) {
            // Ejemplo de cómo podrías manejar las acciones de botones al deslizar
            // Esta parte del código puede manejar lo que sucede cuando se completa el deslizamiento
        }
    }

    private fun buildButtons() {
        val density = context.resources.displayMetrics.density

        deleteButton.layoutParams.width = (40*density).toInt()
        deleteButton.layoutParams.height = (40*density).toInt()
        //deleteButton.setImageDrawable()
    }
}