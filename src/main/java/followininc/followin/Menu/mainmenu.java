package followininc.followin.Menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import followininc.followin.AllMediaFiles;
import followininc.followin.ApplicationData;
import followininc.followin.ImageLoader;
import followininc.followin.InstagramApp;
import followininc.followin.R;
import followininc.followin.Relationship;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class mainmenu extends AppCompatActivity implements OnClickListener {
    private InstagramApp mApp;
    private BottomNavigationView mBottomNav;
    private Button btnDisconnect;
    private Button btnViewInfo;
    private Button btnGetAllImages;
    private Button btnFollowers;
    private Button btnFollwing;
    private Button btnConnect;
    private Button btnNotfollowing;
    LinearLayout FirstMenuButton, SecondMenuButton, ThirdMenuButton;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(mainmenu.this, "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        mBottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        FirstMenuButton = (LinearLayout) findViewById(R.id.FirstMenuButton);
        SecondMenuButton = (LinearLayout) findViewById(R.id.SecondMenuButton);
        ThirdMenuButton = (LinearLayout) findViewById(R.id.ThirdMenuButton);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
        btnViewInfo = (Button) findViewById(R.id.btnViewInfo);
        btnGetAllImages = (Button) findViewById(R.id.btnGetAllImages);
        btnFollowers = (Button) findViewById(R.id.btnFollows);
        btnFollwing = (Button) findViewById(R.id.btnFollowing);
        btnNotfollowing = (Button) findViewById(R.id.btnNotfollowing);
        bindEventHandlers();


        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID, ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                mApp.fetchUserName(handler);
                btnConnect.setVisibility(Button.GONE);
                mBottomNav.setVisibility(View.VISIBLE);
                FirstMenuButton.setVisibility(LinearLayout.VISIBLE);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(mainmenu.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        FirstMenuButton.setVisibility(LinearLayout.VISIBLE);
                        SecondMenuButton.setVisibility(LinearLayout.GONE);
                        ThirdMenuButton.setVisibility(LinearLayout.GONE);
                        break;
                    case R.id.navigation_dashboard:
                        FirstMenuButton.setVisibility(LinearLayout.GONE);
                        SecondMenuButton.setVisibility(LinearLayout.VISIBLE);
                        ThirdMenuButton.setVisibility(LinearLayout.GONE);
                        break;
                    case R.id.navigation_notifications:
                        FirstMenuButton.setVisibility(LinearLayout.GONE);
                        SecondMenuButton.setVisibility(LinearLayout.GONE);
                        ThirdMenuButton.setVisibility(LinearLayout.VISIBLE);
                        break;
                }
                return true;
            }
        });

        connected();
    }

    private void bindEventHandlers() {
        btnConnect.setOnClickListener(this);
        btnDisconnect.setOnClickListener(this);
        btnViewInfo.setOnClickListener(this);
        btnGetAllImages.setOnClickListener(this);
        btnFollwing.setOnClickListener(this);
        btnFollowers.setOnClickListener(this);
        btnNotfollowing.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnDisconnect)
        {
            connectOrDisconnectUser();
        }
        else if (v == btnConnect)
        {
            mApp.authorize();
        }
        else if (v == btnViewInfo)
        {
            displayInfoDialogView();
        }
        else if (v == btnGetAllImages)
        {
            startActivity(new Intent(mainmenu.this, AllMediaFiles.class).putExtra("userInfo", userInfoHashmap));
        }
        else if (v == btnFollowers || v == btnFollwing)
        {
            String url = "";
            if (v == btnFollowers) {
                url = "https://api.instagram.com/v1/users/" + userInfoHashmap.get(InstagramApp.TAG_ID) + "/follows?access_token=" + mApp.getTOken();
            } else if (v == btnFollwing) {
                url = "https://api.instagram.com/v1/users/" + userInfoHashmap.get(InstagramApp.TAG_ID) + "/followed-by?access_token=" + mApp.getTOken();
            }
            startActivity(new Intent(mainmenu.this, Relationship.class).putExtra("userInfo", url));
        }
        else if(v == btnNotfollowing)
        {
            Notfollowing();
        }
    }

    private void Notfollowing() {
        String url1 = "https://api.instagram.com/v1/users/" + userInfoHashmap.get(InstagramApp.TAG_ID) + "/follows?access_token=" + mApp.getTOken();
        String url2 = "https://api.instagram.com/v1/users/" + userInfoHashmap.get(InstagramApp.TAG_ID) + "/followed-by?access_token=" + mApp.getTOken();
        startActivity(new Intent(mainmenu.this, relnotfollowing.class).putExtra("userInfo1", url1).putExtra("userInfo2", url2));
    }

    private void connected()
    {
        if(mApp.hasAccessToken())
        {
            mApp.fetchUserName(handler);
            btnConnect.setVisibility(Button.GONE);
            mBottomNav.setVisibility(View.VISIBLE);
            FirstMenuButton.setVisibility(LinearLayout.VISIBLE);
        }
    }

    private void connectOrDisconnectUser() {
        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mainmenu.this);
            builder.setMessage("Disconnect from Instagram?").setCancelable(false).setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mApp.resetAccessToken();
                            btnConnect.setVisibility(Button.VISIBLE);
                            mBottomNav.setVisibility(View.GONE);
                            FirstMenuButton.setVisibility(LinearLayout.GONE);
                        }
                    })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            //mApp.authorize();
        }
    }

    private void displayInfoDialogView() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mainmenu.this);
        alertDialog.setTitle("Profile Info");

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.profile_view, null);
        alertDialog.setView(view);
        ImageView ivProfile = (ImageView) view.findViewById(R.id.ivProfileImage);
        TextView tvName = (TextView) view.findViewById(R.id.tvUserName);
        TextView tvNoOfFollwers = (TextView) view.findViewById(R.id.tvNoOfFollowers);
        TextView tvNoOfFollowing = (TextView) view.findViewById(R.id.tvNoOfFollowing);

        new ImageLoader(mainmenu.this).DisplayImage(userInfoHashmap.get(InstagramApp.TAG_PROFILE_PICTURE), ivProfile);
        tvName.setText(userInfoHashmap.get(InstagramApp.TAG_USERNAME));
        tvNoOfFollowing.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWS));
        tvNoOfFollwers.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWED_BY));
        alertDialog.create().show();
    }
}
