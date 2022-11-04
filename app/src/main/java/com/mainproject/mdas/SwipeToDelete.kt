package com.mainproject.mdas

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class SwipeToDelete(context: Context) : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

    private val deleteCol = ContextCompat.getColor(context,android.R.color.holo_red_light)
    private val Dicon =R.drawable.ic_baseline_delete_24
    private val editCol = ContextCompat.getColor(context,R.color.mainColor)
    private val Eicon =R.drawable.ic_baseline_edit_24
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

        RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
            .addSwipeLeftActionIcon(Dicon)
            .addSwipeLeftBackgroundColor(deleteCol)
            .addSwipeLeftLabel("Delete...")
            .addSwipeRightActionIcon(Eicon)
            .addSwipeRightBackgroundColor(editCol)
            .addSwipeRightLabel("Update Scheme for 10 days...")
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}