package fpt.huyenptnhe160769.cosplanner.recycler_view.details_item;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fpt.huyenptnhe160769.cosplanner.R;

public class ItemsViewHolder extends RecyclerView.ViewHolder {
    public TextView name, price;
    public ImageView hasNote, hasPicture, isComplete;
    public LinearLayout holdr;

    public ItemsViewHolder(@NonNull View itemView) {
        super(itemView);

        holdr = itemView.findViewById(R.id.lin_itemHolder);
        name = itemView.findViewById(R.id.txt_itemName);
        price = itemView.findViewById(R.id.txt_price);
        hasNote = itemView.findViewById(R.id.ic_rowNote);
        hasPicture = itemView.findViewById(R.id.ic_rowPicture);
        isComplete = itemView.findViewById(R.id.ic_status);
    }
}
