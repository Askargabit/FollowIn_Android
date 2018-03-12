package followininc.followin;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class animationtest extends AppCompatActivity {

    private AnimationDrawable mAnimationDrawable;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animationtest);

        imageView = (ImageView) findViewById(R.id.myanimation);
        imageView.setBackgroundResource(R.drawable.animation);

        mAnimationDrawable = (AnimationDrawable) imageView.getBackground();

        final Button btnStart = (Button) findViewById(R.id.buttonStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAnimationDrawable.start();
            }
        });

        final Button btnStop = (Button) findViewById(R.id.buttonStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAnimationDrawable.stop();
            }
        });
    }
}
