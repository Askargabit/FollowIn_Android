package followininc.followin;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import followininc.followin.Backend.ImageLoader;

public class Photo_dialog_fragment extends DialogFragment {
    ImageView imageView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.posted_photo_dialogfragment, null);
        String tag = getTag();
        v.findViewById(R.id.posted_photo_dialogfragment);
        imageView = (ImageView) v.findViewById(R.id.posted_photo_dialogfragment);
        new ImageLoader(v.getContext()).DisplayImage(tag, imageView);
        return v;
    }

    public void onClick(View v) {
        dismiss();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

}
