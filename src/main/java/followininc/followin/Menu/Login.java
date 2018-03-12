package followininc.followin.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

import followininc.followin.Backend.ApplicationData;
import followininc.followin.Backend.InstagramApp;
import followininc.followin.R;

import static followininc.followin.Backend.InstagramApp.WHAT_FINALIZE;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private InstagramApp mApp;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == WHAT_FINALIZE) {
                Toast.makeText(Login.this, "Check your network.1", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });
    private ImageView btnconnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnconnect = (ImageView) findViewById(R.id.btnconnect);
        btnconnect.setOnClickListener(this);

        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID, ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                mApp.fetchUserName(handler);
                startActivity(new Intent(Login.this, test.class));
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(Login.this, error, Toast.LENGTH_SHORT).show();
            }
        });
        if(mApp.hasAccessToken())
        {
            mApp.fetchUserName(handler);
            startActivity(new Intent(Login.this, test.class));
        }
        mApp.authorize();
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public void onClick(View v) {
        if(v == btnconnect)
        {
            //startActivity(new Intent(Login.this, animationtest.class));
            mApp.authorize();
        }
    }
}
