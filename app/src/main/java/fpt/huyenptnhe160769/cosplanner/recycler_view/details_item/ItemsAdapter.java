package fpt.huyenptnhe160769.cosplanner.recycler_view.details_item;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import fpt.huyenptnhe160769.cosplanner.DetailsActivity;
import fpt.huyenptnhe160769.cosplanner.R;
import fpt.huyenptnhe160769.cosplanner.dao.AppDatabase;
import fpt.huyenptnhe160769.cosplanner.dialog.EditItemDialog;
import fpt.huyenptnhe160769.cosplanner.models.Element;
import fpt.huyenptnhe160769.cosplanner.services.ImageSaver;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsViewHolder> {
    List<Element> items;
    Context context;
    AppDatabase db;
    int cosId;

    public ItemsAdapter (int id, Context context, AppDatabase db){
        this.context = context;
        this.db = db;
        cosId = id;
        items = db.elementDao().getByCosId(id);
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
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("VND"));
        format.setParseIntegerOnly(true);
        format.setMaximumFractionDigits(0);
        holder.price.setText(String.valueOf(format.format(item.cost)));

        if (item.isComplete) holder.isComplete.setImageResource(R.drawable.ic_ready);
        else holder.isComplete.setImageResource(R.drawable.ic_not_ready);

        if (item.isPriority) {
            holder.holdr.setBackgroundColor(context.getColor(R.color.action_bar));
            holder.isPriority.setImageResource(R.drawable.ic_menu_rate);
        }
        else {
            holder.holdr.setBackgroundColor(context.getColor(R.color.row));
            holder.isPriority.setImageDrawable(null);
        }

        ImageSaver saver = new ImageSaver(context);
//        Toast.makeText(context, item.pictureURL, Toast.LENGTH_SHORT).show();
        if (item.pictureURL != null && saver.loadImageFromStorage(item.pictureURL) != null) holder.hasPicture.setImageResource(R.drawable.ic_row_picture_on);
        else holder.hasPicture.setImageResource(R.drawable.ic_row_picture_off);

        if (item.note != null && item.note.length() > 0) holder.hasNote.setImageResource(R.drawable.ic_row_notes_on);
        else holder.hasNote.setImageResource(R.drawable.ic_row_notes_off);

        holder.isComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isComplete = !item.isComplete;
                db.elementDao().update(item);
                items = db.elementDao().getByCosId(cosId);
                notifyDataSetChanged();
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditItemDialog editItemDialog = new EditItemDialog(item, context, db);
                try {
                    editItemDialog.setOnFinishListener(((DetailsActivity) context));
                    editItemDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), editItemDialog.getClass().getName());
                } catch (Exception ex) {
                    Toast.makeText(context, "Cannot open edit window!", Toast.LENGTH_SHORT).show();
                    Log.e(ItemsAdapter.class.getName(), ex.getMessage());
                }
            }
        });
        holder.isPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isPriority = !item.isPriority;
                db.elementDao().update(item);
                items = db.elementDao().getByCosId(cosId);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
