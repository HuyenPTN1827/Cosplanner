package fpt.huyenptnhe160769.cosplanner.recycler_view.details_item;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import fpt.huyenptnhe160769.cosplanner.R;
import fpt.huyenptnhe160769.cosplanner.dao.AppDatabase;
import fpt.huyenptnhe160769.cosplanner.dialog.EditItemDialog;
import fpt.huyenptnhe160769.cosplanner.models.Element;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsViewHolder> {
    List<Element> items;
    Context context;
    AppDatabase db;

    public ItemsAdapter (List<Element> items, Context context, AppDatabase db){
        this.items = items;
        this.context = context;
        this.db = db;
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

//        if (db.elementDao().findById(item.eid) == null){
//            Log.e(this.getClass().getName(), "Cannot find item in database");
//            return;
//        }

        holder.name.setText(item.name);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditItemDialog editItemDialog = new EditItemDialog(item, context) {
                    @Override
                    public void EditItem(String name, String price, boolean done, boolean priority) {
                        Element e = db.elementDao().findById(item.eid);
                        if(e == null) {
                            Log.e(this.getClass().getName(), "Cannot find item in database");
                            return;
                        }
                        e.name = name;
                        e.cost = Double.valueOf(price);
                        e.isComplete = done;
                        e.isPriority = priority;

                        db.elementDao().update(e);
                    }

                    @Override
                    public void DeleteElement() {
                        Element e = db.elementDao().findById(item.eid);
                        if(e == null) {
                            Log.e(this.getClass().getName(), "Cannot find item in database");
                            return;
                        }
                        db.elementDao().delete(e);
                    }

                    @Override
                    public void AddPicture(String url) {
                        Element e = db.elementDao().findById(item.eid);
                        if(e == null) {
                            Log.e(this.getClass().getName(), "Cannot find item in database");
                            return;
                        }
                        e.pictureURL = url;
                        db.elementDao().update(e);
                    }

                    @Override
                    public void RemovePicture() {
                        Element e = db.elementDao().findById(item.eid);
                        if(e == null) {
                            Log.e(this.getClass().getName(), "Cannot find item in database");
                            return;
                        }
                        e.pictureURL = "";
                        db.elementDao().update(e);
                    }
                };
            }
        });

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("VND"));
        holder.price.setText(String.valueOf(format.format(item.cost)));

        if (item.isComplete) holder.isComplete.setImageResource(R.drawable.ic_ready);
        else holder.isComplete.setImageResource(R.drawable.ic_not_ready);

        if (item.isPriority) holder.holdr.setBackgroundColor(context.getColor(R.color.row));
        else holder.holdr.setBackgroundColor(context.getColor(R.color.action_bar));

        if (item.pictureURL != null) holder.hasPicture.setImageResource(R.drawable.ic_row_picture_on);
        else holder.isComplete.setImageResource(R.drawable.ic_row_picture_off);

        if (item.note != null) holder.hasNote.setImageResource(R.drawable.ic_row_notes_on);
        else holder.isComplete.setImageResource(R.drawable.ic_row_notes_off);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
