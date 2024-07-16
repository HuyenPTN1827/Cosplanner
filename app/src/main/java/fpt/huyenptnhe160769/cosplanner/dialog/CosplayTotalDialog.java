package fpt.huyenptnhe160769.cosplanner.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.models.Element;

public class CosplayTotalDialog extends DialogFragment {
    Cos cos;
    List<Element> elementList;
    public CosplayTotalDialog (Cos cos, List<Element> elementList){
        this.cos = cos;
        this.elementList = elementList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("VND"));
        String msg = "";
        double cost = 0;
        for (Element e :
                elementList) {
            cost += e.cost;
        }

        msg += "Budget: " + format.format(cos.budget);
        msg += "\nTotal cost: " + format.format(cost);
        if (cost > cos.budget){
            msg += "\nOver budget: " + (-(cos.budget - cost));
        }
        else if (cost < cos.budget){
            msg += "\nRemaining budget: " + (cos.budget - cost);
        }
        else {
            msg += "\n\n*Cost matches the budget!";
        }
        builder
                .setMessage(msg)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CosplayTotalDialog.this.getDialog().cancel();
            }
        });

        return builder.create();
    }
}
