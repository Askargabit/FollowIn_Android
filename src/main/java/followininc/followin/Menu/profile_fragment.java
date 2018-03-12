package followininc.followin.Menu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import followininc.followin.R;

public class profile_fragment extends Fragment{
    TextView textView;
    TextView textView2;
    TextView textView3;
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    public void setTextViewText(String value){
        textView.setText(value);
    }

    public void setTextViewText2(String value){
        textView2.setText(value);
    }

    public void setTextViewText3(String value){
        textView3.setText(value);
    }


}
