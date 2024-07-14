package fpt.huyenptnhe160769.cosplanner.recycler_view.details_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import fpt.huyenptnhe160769.cosplanner.R;
import fpt.huyenptnhe160769.cosplanner.dialog.EditItemDialog;
import fpt.huyenptnhe160769.cosplanner.models.Element;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsViewHolder> {
    List<Element> items;
    Context context;

    public ItemsAdapter (List<Element> items, Context context){
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater =LayoutInflater.from(context);
        View ItemView = inflater.inflate(R.layout.items, parent, false);
        ItemsViewHolder vh = new ItemsViewHolder(ItemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        Element item = items.get(position);

        holder.name.setText(item.getName());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditItemDialog editItemDialog = new EditItemDialog(item, context) {
                    @Override
                    public void EditItem(String name, String price, boolean done, boolean priority) {

                    }

                    @Override
                    public void DeleteElement(long id) {

                    }

                    @Override
                    public void AddPicture(String url) {

                    }

                    @Override
                    public void RemovePicture(String url) {

                    }
                };
            }
        });

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("VND"));
        holder.price.setText(String.valueOf(format.format(item.getCost())));

        if (item.isPriority()) holder.isComplete.setImageResource(R.drawable.ic_ready);
        else holder.isComplete.setImageResource(R.drawable.ic_not_ready);

        if (item.hasPhoto()) holder.hasPicture.setImageResource(R.drawable.ic_row_picture_on);
        else holder.isComplete.setImageResource(R.drawable.ic_row_picture_off);

        if (item.getNotes() != null) holder.hasNote.setImageResource(R.drawable.ic_row_notes_on);
        else holder.isComplete.setImageResource(R.drawable.ic_row_notes_off);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
