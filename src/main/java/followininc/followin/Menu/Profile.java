package followininc.followin.Menu;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import followininc.followin.Backend.ApplicationData;
import followininc.followin.Backend.ImageLoader;
import followininc.followin.Backend.InstagramApp;
import followininc.followin.Backend.JSONParser;
import followininc.followin.MyGridListAdapter;
import followininc.followin.Photo_dialog_fragment;
import followininc.followin.R;

import static followininc.followin.Backend.InstagramApp.WHAT_FINALIZE;

public class Profile extends AppCompatActivity {

    private InstagramApp mApp;
    DialogFragment dlg1;
    private TextView profile_followers;
    private TextView profile_followings;
    private TextView profile_posts;
    private ImageView profile_picture;
    private TextView profile_username;
    private TextView profile_fullname;
    private TextView profile_info;
    private GridView profile_images_fragment;
    private ArrayList<String> imageThumbList = new ArrayList<>();
    private ArrayList<String> imageThumbList_sorted_by_time = new ArrayList<>();
    private ArrayList<String> likesList = new ArrayList<>();
    private ArrayList<String> likesList_sorted_by_time = new ArrayList<>();
    private ArrayList<String> commentsList = new ArrayList<>();
    private ArrayList<String> commentsList_sorted_by_time = new ArrayList<>();
    private ArrayList<String> StandardSize_pictures = new ArrayList<>();
    private ArrayList<String> Standard_sorted_by_time = new ArrayList<>();
    ArrayList<String> user_data = new ArrayList<>();
    ArrayList<ArrayList<String>> Data_followers = new ArrayList<>();
    ArrayList<ArrayList<String>> Data_followings = new ArrayList<>();
    private static int spinnermode = 0;
    String[] sort = {"Posted time", "Amount of Likes", "Amount of Comments"};
    private Handler handler3 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_FINALIZE) {
                profile_images_fragment.setAdapter(new MyGridListAdapter(Profile.this, imageThumbList, likesList, commentsList));
            } else {
                Toast.makeText(Profile.this, "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    private Handler handler2 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(Profile.this, "Check your network.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_followers = (TextView) findViewById(R.id.profilefollowers_fragment);
        profile_followings = (TextView) findViewById(R.id.profilefollowings_fragment);
        profile_posts = (TextView) findViewById(R.id.profileposts_fragment);
        profile_picture = (ImageView) findViewById(R.id.profilepicture_fragment);
        profile_images_fragment = (GridView) findViewById(R.id.profile_images_fragment);
        profile_username = (TextView) findViewById(R.id.profileusername_fragment);
        profile_fullname = (TextView) findViewById(R.id.profilefullname_fragment);
        profile_info = (TextView) findViewById(R.id.profileinfo_fragment);
        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID, ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        user_data = getIntent().getStringArrayListExtra("user");
        Log.e("Check_user", user_data.get(1));
        profile_followings.setText(user_data.get(4));
        profile_followers.setText(user_data.get(3));
        profile_posts.setText(user_data.get(5));
        profile_fullname.setText(user_data.get(8));
        profile_username.setText(user_data.get(1));
        new ImageLoader(Profile.this).DisplayImage(user_data.get(2), profile_picture);
        getAllMediaImages();
        dlg1 = new Photo_dialog_fragment();
        profile_images_fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(spinnermode == 0)
                {
                    dlg1.show(getFragmentManager(), Standard_sorted_by_time.get(position));
                }
                else {
                    dlg1.show(getFragmentManager(), StandardSize_pictures.get(position));
                }
            }
        });
        final Spinner spinner = (Spinner) findViewById(R.id.profile_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sort);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    spinnermode = 1;
                    bubbleSort(likesList, commentsList, StandardSize_pictures, imageThumbList);
                    profile_images_fragment.setAdapter(new MyGridListAdapter(Profile.this,imageThumbList,likesList, commentsList));
                } else if (position == 2) {
                    spinnermode = 2;
                    bubbleSort(commentsList, likesList, StandardSize_pictures, imageThumbList);
                    profile_images_fragment.setAdapter(new MyGridListAdapter(Profile.this,imageThumbList,likesList, commentsList));
                } else if (position == 0) {
                    spinnermode = 0;
                    profile_images_fragment.setAdapter(new MyGridListAdapter(Profile.this, imageThumbList_sorted_by_time, likesList_sorted_by_time, commentsList_sorted_by_time));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private void getAllMediaImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int what = WHAT_FINALIZE;
                try {
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = jsonParser.getJSONFromUrlByGet("https://api.instagram.com/v1/users/" + user_data.get(0)
                            + "/media/recent/?access_token=" + mApp.getTOken());//New One

                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        JSONObject images_obj = data_obj.getJSONObject("images");
                        JSONObject thumbnail_obj = images_obj.getJSONObject("thumbnail");
                        String likes_obj = data_obj.getJSONObject("likes").getString("count");
                        likesList.add(likes_obj);
                        likesList_sorted_by_time.add(likes_obj);
                        String comments_obj = data_obj.getJSONObject("comments").getString("count");
                        commentsList.add(comments_obj);
                        commentsList_sorted_by_time.add(comments_obj);
                        String str_url = thumbnail_obj.getString("url");
                        imageThumbList.add(str_url);
                        imageThumbList_sorted_by_time.add(str_url);
                        String standard_obj = images_obj.getJSONObject("standard_resolution").getString("url");
                        StandardSize_pictures.add(standard_obj);
                        Standard_sorted_by_time.add(standard_obj);
                    }
                    System.out.println("jsonObject::" + jsonObject);
                } catch (Exception exception) {
                    Log.e("AllMediaFiles", "catch");
                    exception.printStackTrace();
                    what = 1;
                }
                handler3.sendEmptyMessage(what);
            }
        }).start();
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
}
