package com.bassam.todo_applicaton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bassam.todo_applicaton.Adapter.TodoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private TodoAdapter adapter;

    public RecyclerItemTouchHelper(TodoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are You Sure You Want To Delete This Task ?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.deleteItem(position);
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
            adapter.editItem(position);
        }
    }
    @Override
    public void onChildDraw(Canvas c,RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,float dX,float dY,int actionState,boolean isCurrentlyActive){
        super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View view = viewHolder.itemView;
        int backgroundCornerOffset =20;

        if(dX>0){
            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.ic_baseline_edit);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(),R.color.blue));
        }else {
            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.ic_baseline_delete);
            background = new ColorDrawable(Color.RED);
        }
        int iconMargin =(view.getHeight() - icon.getIntrinsicHeight())/2;
        int iconTop = view.getTop() + (view.getHeight() - icon.getIntrinsicHeight())/2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if(dX>0){
            int iconLeft = view.getLeft() +iconMargin;
            int iconRight = view.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(view.getLeft(),view.getTop(),
                    view.getLeft()+((int)dX)+backgroundCornerOffset,view.getBottom());
        }else if(dX<0) {
            int iconLeft = view.getLeft() +iconMargin - icon.getIntrinsicWidth();
            int iconRight = view.getLeft() + iconMargin;
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(view.getRight() + ((int)dX) - backgroundCornerOffset,view.getTop(),
                    view.getRight(),view.getBottom());
        }else {
            background.setBounds(0,0,0,0);
        }
        background.draw(c);
        icon.draw(c);
    }

}
