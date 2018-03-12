package followininc.followin.Menu;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import followininc.followin.Backend.ApplicationData;
import followininc.followin.Backend.ImageLoader;
import followininc.followin.Backend.InstagramApp;
import followininc.followin.Backend.JSONParser;
import followininc.followin.Backend.MySQLiteHelper;
import followininc.followin.Backend.User;
import followininc.followin.MyGridListAdapter;
import followininc.followin.MyGridListAdapterForDialog;
import followininc.followin.Photo_dialog_fragment;
import followininc.followin.R;

import static followininc.followin.Backend.InstagramApp.WHAT_FINALIZE;

public class test extends FragmentActivity implements View.OnClickListener {

    private InstagramApp mApp; private Button btnDisconnect; private RelativeLayout bottom_nav;
    private Button btnGetAllImages; private Button btnFollowers;
    private Button btnUnfollowed; private Button btnNewFollower;
    private Button btnFollwing; private Button btnNotfollowing;
    private Button btnNotfollowing2; private ProgressDialog pd; private GridView profile_images_fragment;
    private TextView profile_followers; private TextView profile_followings; private TextView profile_posts;
    private ImageView profile_picture; private TextView profile_username; private TextView profile_fullname;
    private TextView profile_info; private ImageView addimage1; private ImageView addimage2;
    LinearLayout FirstMenuButton, SecondMenuButton, ThirdMenuButton;
    private ImageView btnHome; private ImageView btnDashboard; private ImageView btnProfile;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Button btncalculate;
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == WHAT_FINALIZE) {
                Toast.makeText(test.this, "Check your network.1", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    private ArrayList<ArrayList<String>> Data_followers = new ArrayList<>();
    private ArrayList<ArrayList<String>> Data_followings = new ArrayList<>();
    private ArrayList<String> imageid = new ArrayList<>();
    private ArrayList<String> imageThumbList = new ArrayList<>();
    private ArrayList<String> imageThumbList_sorted_by_time = new ArrayList<>();
    private ArrayList<String> likesList = new ArrayList<>();
    private ArrayList<String> likesList_sorted_by_time = new ArrayList<>();
    private ArrayList<String> commentsList = new ArrayList<>();
    private ArrayList<String> commentsList_sorted_by_time = new ArrayList<>();
    private ArrayList<String> StandardSize_pictures = new ArrayList<>();
    private ArrayList<String> Standard_sorted_by_time = new ArrayList<>();
    private Integer profile = 0;
    AlertDialog _dialog;
    MySQLiteHelper db;
    private static long last_refresh = -1;
    DialogFragment dlg1;
    String[] sort = {"Posted time", "Amount of Likes", "Amount of Comments"};
    int first = -1;
    int second = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Qwerty();
        db = new MySQLiteHelper(getApplicationContext());
        bindEventHandlers();
        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID, ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mApp.fetchUserName(handler);
        test.downloaddata2 a = new downloaddata2();
        a.execute();
        connected();
        db.closeDB();
        dlg1 = new Photo_dialog_fragment();
        final Spinner spinner = (Spinner) findViewById(R.id.profile_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sort);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    bubbleSort(likesList, commentsList, StandardSize_pictures, imageThumbList);
                    profile_images_fragment.setAdapter(new MyGridListAdapter(test.this,imageThumbList,likesList, commentsList));
                } else if (position == 2) {
                    bubbleSort(commentsList, likesList, StandardSize_pictures, imageThumbList);
                    profile_images_fragment.setAdapter(new MyGridListAdapter(test.this,imageThumbList,likesList, commentsList));
                } else if (position == 0) {
                    profile_images_fragment.setAdapter(new MyGridListAdapter(test.this, imageThumbList_sorted_by_time, likesList_sorted_by_time, commentsList_sorted_by_time));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

        profile_images_fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItemPosition() == 0)
                {
                    dlg1.show(getFragmentManager(), Standard_sorted_by_time.get(position));
                }
                else {
                    dlg1.show(getFragmentManager(), StandardSize_pictures.get(position));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private void Qwerty()
    {
        bottom_nav = (RelativeLayout) findViewById(R.id.bottom_nav);
        FirstMenuButton = (LinearLayout) findViewById(R.id.FirstMenuButton);
        SecondMenuButton = (LinearLayout) findViewById(R.id.SecondMenuButton);
        ThirdMenuButton = (LinearLayout) findViewById(R.id.ThirdMenuButton);
        btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
        btnFollowers = (Button) findViewById(R.id.btnFollows);
        btnFollwing = (Button) findViewById(R.id.btnFollowing);
        btnNotfollowing = (Button) findViewById(R.id.btnNotfollowing);
        btnNotfollowing2 = (Button) findViewById(R.id.btnNotfollowing2);
        btnNewFollower = (Button) findViewById(R.id.btnNewFollower);
        btnUnfollowed = (Button) findViewById(R.id.btnUnfollowed);
        profile_followers = (TextView) findViewById(R.id.profilefollowers_fragment);
        profile_followings = (TextView) findViewById(R.id.profilefollowings_fragment);
        profile_posts = (TextView) findViewById(R.id.profileposts_fragment);
        profile_username = (TextView) findViewById(R.id.profileusername_fragment);
        profile_fullname = (TextView) findViewById(R.id.profilefullname_fragment);
        profile_info = (TextView) findViewById(R.id.profileinfo_fragment);
        profile_picture = (ImageView) findViewById(R.id.profilepicture_fragment);
        profile_images_fragment = (GridView) findViewById(R.id.profile_images_fragment);
        addimage1 = (ImageView) findViewById(R.id.addimage1);
        addimage2 = (ImageView) findViewById(R.id.addimage2);
        btncalculate = (Button) findViewById(R.id.calculate);
        btnHome = (ImageView) findViewById(R.id.btnhome);
        btnProfile = (ImageView) findViewById(R.id.btnprofile);
        btnDashboard = (ImageView) findViewById(R.id.btndashboard);
    }

    private void setProfile() {
        if(profile == 1)
        {
            return;
        }
        profile_followings.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWS));
        profile_followers.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWED_BY));
        profile_posts.setText(userInfoHashmap.get(InstagramApp.TAG_MEDIA));
        profile_fullname.setText(userInfoHashmap.get(InstagramApp.TAG_FULL_NAME));
        profile_username.setText(userInfoHashmap.get(InstagramApp.TAG_USERNAME));
        profile_info.setText(userInfoHashmap.get(InstagramApp.TAG_BIO));
        new ImageLoader(test.this).DisplayImage(userInfoHashmap.get(InstagramApp.TAG_PROFILE_PICTURE), profile_picture);
        imageThumbList_sorted_by_time = new ArrayList<>(imageThumbList);
        likesList_sorted_by_time = new ArrayList<>(likesList);
        commentsList_sorted_by_time = new ArrayList<>(commentsList);
        Standard_sorted_by_time = new ArrayList<>(StandardSize_pictures);
        profile = 1;
    }

    private void bindEventHandlers() {
        btnDisconnect.setOnClickListener(this);
        btnFollwing.setOnClickListener(this);
        btnFollowers.setOnClickListener(this);
        btnNotfollowing.setOnClickListener(this);
        btnNotfollowing2.setOnClickListener(this);
        btnUnfollowed.setOnClickListener(this);
        btnNewFollower.setOnClickListener(this);
        addimage1.setOnClickListener(this);
        addimage2.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnDashboard.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == btnDisconnect)
        {
            DisconnectUser();
        }
        else if (v == btnFollowers || v == btnFollwing)
        {
            Bundle bundle = new Bundle();
            if (v == btnFollowers) {
                bundle.putSerializable("value", Data_followings);
                startActivity(new Intent(test.this, Relationship.class).putExtra("userId", userInfoHashmap.get(InstagramApp.TAG_ID)).putExtra("Token", mApp.getTOken()).putExtras(bundle));
            } else {
                bundle.putSerializable("value", Data_followers);
                startActivity(new Intent(test.this, Relationship.class).putExtra("userId", userInfoHashmap.get(InstagramApp.TAG_ID)).putExtra("Token", mApp.getTOken()).putExtras(bundle));
            }
        }
        else if(v == btnNotfollowing)
        {
            String url1 = "https://api.instagram.com/v1/users/" + userInfoHashmap.get(InstagramApp.TAG_ID) + "/follows?access_token=" + mApp.getTOken();
            String url2 = "https://api.instagram.com/v1/users/" + userInfoHashmap.get(InstagramApp.TAG_ID) + "/followed-by?access_token=" + mApp.getTOken();
            startActivity(new Intent(test.this, relnotfollowing.class).putExtra("userInfo1", url1).putExtra("userInfo2", url2));
        }
        else if(v == btnNotfollowing2)
        {
            String url2 = "https://api.instagram.com/v1/users/" + userInfoHashmap.get(InstagramApp.TAG_ID) + "/follows?access_token=" + mApp.getTOken();
            String url1 = "https://api.instagram.com/v1/users/" + userInfoHashmap.get(InstagramApp.TAG_ID) + "/followed-by?access_token=" + mApp.getTOken();
            startActivity(new Intent(test.this, relnotfollowing.class).putExtra("userInfo1", url1).putExtra("userInfo2", url2));
        }
        else if(v == btnNewFollower)
        {
            startActivity(new Intent(test.this, RelationshipChanges.class).putExtra("Token", "2"));
        }
        else if(v == btnUnfollowed)
        {
            startActivity(new Intent(test.this, RelationshipChanges.class).putExtra("Token", "1"));
        }
        else if(v == addimage1 || v == addimage2)
        {
            pickupimage();
        }
        else if(v == btncalculate && first != -1 && second != -1)
        {
            startActivity(new Intent(test.this, ComparePosts.class).putExtra("First", imageid.get(first)).putExtra("Second",imageid.get(second)).putExtra("Token",mApp.getTOken()));
        }
        else if(v == btnHome)
        {
            FirstMenuButton.setVisibility(LinearLayout.GONE);
            SecondMenuButton.setVisibility(LinearLayout.GONE);
            ThirdMenuButton.setVisibility(LinearLayout.VISIBLE);
        }
        else if(v == btnDashboard)
        {
            FirstMenuButton.setVisibility(LinearLayout.GONE);
            SecondMenuButton.setVisibility(LinearLayout.VISIBLE);
            ThirdMenuButton.setVisibility(LinearLayout.GONE);
        }
        else if(v == btnProfile)
        {
            FirstMenuButton.setVisibility(LinearLayout.VISIBLE);
            SecondMenuButton.setVisibility(LinearLayout.GONE);
            ThirdMenuButton.setVisibility(LinearLayout.GONE);
        }
    }

    private void connected()
    {
        if(mApp.hasAccessToken())
        {
            bottom_nav.setVisibility(View.VISIBLE);
            FirstMenuButton.setVisibility(LinearLayout.VISIBLE);
            db.onUpdate();
        }
    }

    private void DisconnectUser() {
        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(test.this);
            builder.setMessage("Disconnect from Instagram?").setCancelable(false).setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mApp.resetAccessToken();
                            startActivity(new Intent(test.this, Login.class));
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
        }
    }

    static void bubbleSort(ArrayList<String> arr, ArrayList<String> arr2, ArrayList<String> arr3, ArrayList<String> arr4) {
        int n = arr.size();
        String temp = "";
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (Integer.parseInt(arr.get(j - 1)) < Integer.parseInt(arr.get(j))) {
                    temp = arr.get(j - 1);
                    arr.set(j - 1, arr.get(j));
                    arr.set(j, temp);
                    temp = arr2.get(j - 1);
                    arr2.set(j - 1, arr2.get(j));
                    arr2.set(j, temp);
                    temp = arr3.get(j - 1);
                    arr3.set(j - 1, arr3.get(j));
                    arr3.set(j, temp);
                    temp = arr4.get(j - 1);
                    arr4.set(j - 1, arr4.get(j));
                    arr4.set(j, temp);
                }
            }
        }

    }

    private void pickupimage() {
        // Prepare grid view
        final GridView gridView = new GridView(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        gridView.setAdapter(new MyGridListAdapterForDialog(test.this,imageThumbList_sorted_by_time,likesList_sorted_by_time, commentsList_sorted_by_time));
        gridView.setNumColumns(3);
        gridView.setHorizontalSpacing(10);
        gridView.setVerticalSpacing(10);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("GRIDVIEW-pos", String.valueOf(position));
                Log.e("GRIDVIEW-fir", String.valueOf(first));
                Log.e("GRIDVIEW-sec", String.valueOf(second));
                if(first == -1)
                {
                    view.findViewById(R.id.ivfirstpick).setVisibility(View.VISIBLE);
                    first = position;
                }
                else if(second == -1)
                {
                    view.findViewById(R.id.ivsecondpick).setVisibility(View.VISIBLE);
                    second = position;
                }
                else if(first == position || second == position)
                {
                    if(position == first)
                    {
                        view.findViewById(R.id.ivfirstpick).setVisibility(View.GONE);
                        first = -1;
                    }
                    else {
                        view.findViewById(R.id.ivsecondpick).setVisibility(View.GONE);
                        second = -1;
                    }
                }
            }
        });
        builder.setView(gridView);
        builder.setNegativeButton("Choose", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(first != -1)
                {
                    new ImageLoader(test.this).DisplayImage(imageThumbList.get(first), addimage1);
                }
                if(second != -1)
                {
                    new ImageLoader(test.this).DisplayImage(imageThumbList.get(second), addimage2);
                }
                _dialog.dismiss();
            }
        });
        first = -1;
        second = -1;
        _dialog = builder.show();
    }

    class downloaddata2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(test.this, "", "Loading...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            profile_images_fragment.setAdapter(new MyGridListAdapter(test.this,imageThumbList,likesList, commentsList));
            setProfile();
            pd.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            int what = 0;
            String url = "https://api.instagram.com/v1/users/self/follows?access_token=" + mApp.getTOken();
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = jsonParser.getJSONFromUrlByGet(url);
                ArrayList<String> optionlist2;
                JSONArray data = jsonObject.getJSONArray("data");
                for (int data_i = 0; data_i < data.length(); data_i++) {
                    optionlist2 = new ArrayList<String>();
                    JSONObject data_obj = data.getJSONObject(data_i);
                    String str_id = data_obj.getString("id");
                    String str_fullname = data_obj.getString("full_name");
                    //String str_bio = data_obj.getString("bio");
                    String str_username = data_obj.getString("username");
                    String info = "https://api.instagram.com/v1/users/" + str_id + "/?access_token=" + mApp.getTOken();
                    String profile_picture = data_obj.getString("profile_picture");
                    JSONParser jsonParser2 = new JSONParser();
                    JSONObject jsonObject2;
                    JSONObject Counts;
                    try {
                        jsonObject2 = jsonParser2.getJSONFromUrlByGet(info);
                        Counts = jsonObject2.getJSONObject("data").getJSONObject("counts");
                        String followers = Counts.getString("followed_by");
                        String followings = Counts.getString("follows");
                        String posts = Counts.getString("media");
                        //Data
                        // 0. Id  1.username 2.profile_picture 3.followers 4.followings 5.posts 6.likes to me 7.Likes/Post 8.fullname 9.Recent activity
                        optionlist2.add(str_id); optionlist2.add(str_username); optionlist2.add(profile_picture);
                        optionlist2.add(followers); optionlist2.add(followings); optionlist2.add(posts); optionlist2.add("0");
                        optionlist2.add("0"); optionlist2.add(str_fullname); optionlist2.add("0");//optionlist2.add(str_bio); //likes from me
                        Data_followings.add(optionlist2);
                    }
                    catch (Exception exception)
                    {
                        optionlist2.add(str_id); optionlist2.add(str_username); optionlist2.add(profile_picture);
                        optionlist2.add("unknown"); optionlist2.add("unknown"); optionlist2.add("unknown"); optionlist2.add("0");
                        optionlist2.add("0"); optionlist2.add(str_fullname); optionlist2.add("0");//optionlist2.add(str_bio);//likes from me
                        exception.printStackTrace();
                        Data_followings.add(optionlist2);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                what = 1;
            }
            url = "https://api.instagram.com/v1/users/self/followed-by?access_token=" + mApp.getTOken();
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = jsonParser.getJSONFromUrlByGet(url);
                JSONArray data = jsonObject.getJSONArray("data");
                ArrayList<String> optionlist;
                for (int data_i = 0; data_i < data.length(); data_i++) {
                    optionlist = new ArrayList<String>();
                    JSONObject data_obj = data.getJSONObject(data_i);
                    String str_id = data_obj.getString("id");
                    String str_fullname = data_obj.getString("full_name");
                    //String str_bio = data_obj.getString("bio");
                    String str_username = data_obj.getString("username");
                    String profile_picture = data_obj.getString("profile_picture");
                    String info = "https://api.instagram.com/v1/users/" + str_id + "/?access_token=" + mApp.getTOken();
                    JSONParser jsonParser2 = new JSONParser();
                    JSONObject jsonObject2;
                    JSONObject Counts;
                    try {
                        jsonObject2 = jsonParser2.getJSONFromUrlByGet(info);
                        Counts = jsonObject2.getJSONObject("data").getJSONObject("counts");
                        String followers = Counts.getString("followed_by");
                        String followings = Counts.getString("follows");
                        String posts = Counts.getString("media");
                        User user = new User( str_id,str_username,profile_picture,Integer.getInteger(followers),Integer.getInteger(followings),Integer.getInteger(posts));
                        long id = db.CreateRow(user, new long[]{data_i});
                        optionlist.add(str_id); optionlist.add(str_username); optionlist.add(profile_picture);
                        optionlist.add(followers); optionlist.add(followings); optionlist.add(posts); optionlist.add("0");
                        optionlist.add("0"); optionlist.add(str_fullname); optionlist.add("0");//optionlist.add(str_bio);//likes from me
                        Data_followers.add(optionlist);
                    }
                    catch (Exception exception)
                    {
                        optionlist.add(str_id); optionlist.add(str_username); optionlist.add(profile_picture);
                        optionlist.add("unknown"); optionlist.add("unknown"); optionlist.add("unknown"); optionlist.add("0"); // for likes to me
                        optionlist.add("0"); optionlist.add(str_fullname); optionlist.add("0");//optionlist.add(str_bio);//likes from me
                        User user = new User( str_id,str_username,profile_picture,-1,-1,-1);
                        long id = db.CreateRow(user, new long[]{data_i});
                        Data_followers.add(optionlist);
                        exception.printStackTrace();
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                what = 1;
            }
            what = WHAT_FINALIZE;
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = jsonParser.getJSONFromUrlByGet("https://api.instagram.com/v1/users/" + userInfoHashmap.get(InstagramApp.TAG_ID)
                        + "/media/recent/?access_token=" + mApp.getTOken());//New One

                JSONArray data = jsonObject.getJSONArray("data");
                for (int data_i = 0; data_i < data.length(); data_i++) {
                    JSONObject data_obj = data.getJSONObject(data_i);
                    JSONObject images_obj = data_obj.getJSONObject("images");
                    JSONObject thumbnail_obj = images_obj.getJSONObject("thumbnail");
                    String standard_obj = images_obj.getJSONObject("standard_resolution").getString("url");
                    String str_url = thumbnail_obj.getString("url");
                    String id_obj = data_obj.getString("id");
                    imageid.add(id_obj);
                    imageThumbList.add(str_url);
                    StandardSize_pictures.add(standard_obj);
                    String likes_obj = data_obj.getJSONObject("likes").getString("count");
                    likesList.add(likes_obj);
                    String comments_obj = data_obj.getJSONObject("comments").getString("count");
                    commentsList.add(comments_obj);
                }
                System.out.println("jsonObject::" + jsonObject);

            } catch (Exception exception) {
                Log.e("AllMediaFiles", "catch");
                exception.printStackTrace();
                what = 1;
            }
            return null;
        }
    }


}
