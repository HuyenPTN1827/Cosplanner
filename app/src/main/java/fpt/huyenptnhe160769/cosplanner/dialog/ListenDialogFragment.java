package fpt.huyenptnhe160769.cosplanner.dialog;

import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public abstract class ListenDialogFragment extends DialogFragment {
    protected DialogFinishListener listener = new DialogFinishListener() {
        @Override
        public void notifyDialogFinish(DialogFragment dialog) {

        }
    };

    public void setOnFinishListener(DialogFinishListener listener){
        this.listener = listener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.notifyDialogFinish(this);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        listener.notifyDialogFinish(this);
    }
}
