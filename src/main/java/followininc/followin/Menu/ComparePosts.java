package followininc.followin.Menu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import followininc.followin.Backend.JSONParser;
import followininc.followin.R;
import followininc.followin.RelationShipAdapter;

import static followininc.followin.Backend.InstagramApp.WHAT_FINALIZE;

public class ComparePosts extends AppCompatActivity {

    String firstpostid;
    String secondpostid;
    String Token;
    ArrayList<String> firstpostlikes = new ArrayList<>();
    ArrayList<String> secondpostlikes = new ArrayList<>();
    private ListView lvcompareposts_1;
    private ListView lvcompareposts_2;
    private ListView lvcompareposts_3;
    private ArrayList<HashMap<String, String>> usersInfo_1 = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> usersInfo_2 = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> usersInfo_3 = new ArrayList<HashMap<String, String>>();
    private HashMap<String, HashMap<String,String>> allusers = new HashMap<>();
    private ProgressDialog pd;
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (pd != null && pd.isShowing())
                pd.dismiss();
            if (msg.what == WHAT_FINALIZE) {
                sort();
                lvcompareposts_1.setAdapter(new RelationShipAdapter(ComparePosts.this, usersInfo_1));
                lvcompareposts_2.setAdapter(new RelationShipAdapter(ComparePosts.this, usersInfo_2));
                lvcompareposts_3.setAdapter(new RelationShipAdapter(ComparePosts.this, usersInfo_3));
            } else {
                Toast.makeText(ComparePosts.this, "Check your network.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_posts);
        firstpostid = getIntent().getStringExtra("First");
        secondpostid = getIntent().getStringExtra("Second");
        Token = getIntent().getStringExtra("Token");
        lvcompareposts_1 = (ListView) findViewById(R.id.compareposts_1);
        lvcompareposts_2 = (ListView) findViewById(R.id.compareposts_2);
        lvcompareposts_3 = (ListView) findViewById(R.id.compareposts_3);
        getAllLikesToMe();
        setupTabLayout();
    }

    private void setupTabLayout() {
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabTapped(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabTapped(tab.getPosition());
            }
        });
    }

    private void onTabTapped(int position) {
        switch (position) {
            case 0:
                lvcompareposts_1.setVisibility(View.VISIBLE);
                lvcompareposts_2.setVisibility(View.GONE);
                lvcompareposts_3.setVisibility(View.GONE);
                break;
            case 1:
                lvcompareposts_1.setVisibility(View.GONE);
                lvcompareposts_2.setVisibility(View.VISIBLE);
                lvcompareposts_3.setVisibility(View.GONE);
                break;
            case 2:
                lvcompareposts_1.setVisibility(View.GONE);
                lvcompareposts_2.setVisibility(View.GONE);
                lvcompareposts_3.setVisibility(View.VISIBLE);
                break;
            default:
                Toast.makeText(this, "Tapped " + position, Toast.LENGTH_SHORT).show();
        }
    }

    void getAllLikesToMe()
    {
        final String url_media = "https://api.instagram.com/v1/users/self/media/recent/?access_token=" + Token;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONParser jsonParser1 = new JSONParser();
                    JSONObject jsonObject1 = jsonParser1.getJSONFromUrlByGet("https://api.instagram.com/v1/media/" + firstpostid +"/likes?access_token=" + Token);
                    JSONArray data_1 = jsonObject1.getJSONArray("data");
                    for (int data_j = 0; data_j < data_1.length(); data_j++)
                    {
                        String Like_id = (String) data_1.getJSONObject(data_j).get("id");
                        firstpostlikes.add(Like_id);
                        Log.e("1111111", Like_id);
                        HashMap<String, String> info = new HashMap<>();
                        info.put("profile_picture", (String) data_1.getJSONObject(data_j).get("profile_picture"));
                        info.put("username", (String) data_1.getJSONObject(data_j).get("username"));
                        info.put("Info", (String) data_1.getJSONObject(data_j).get("full_name"));
                        allusers.put(Like_id,info);
                    }
                    JSONParser jsonParser2 = new JSONParser();
                    JSONObject jsonObject2 = jsonParser2.getJSONFromUrlByGet("https://api.instagram.com/v1/media/" + secondpostid +"/likes?access_token=" + Token);
                    JSONArray data_2 = jsonObject2.getJSONArray("data");
                    for (int data_j = 0; data_j < data_2.length(); data_j++)
                    {
                        String Like_id = (String) data_2.getJSONObject(data_j).get("id");
                        secondpostlikes.add(Like_id);
                        Log.e("222222", Like_id);
                        HashMap<String, String> info = new HashMap<>();
                        info.put("profile_picture", (String) data_2.getJSONObject(data_j).get("profile_picture"));
                        info.put("username", (String) data_2.getJSONObject(data_j).get("username"));
                        info.put("Info", (String) data_2.getJSONObject(data_j).get("full_name"));
                        if(!allusers.containsKey(Like_id))
                            allusers.put(Like_id,info);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                handler.sendEmptyMessage(WHAT_FINALIZE);
            }
        }).start();
    }

    void sort()
    {
        Log.e("QQQQQQQ", "WWWWWWWWWW");
        ArrayList<String> common = new ArrayList<>(firstpostlikes);
        common.retainAll(secondpostlikes);
        for(String id:common)
        {
            Log.e("33333", id);
            usersInfo_2.add(allusers.get(id));
        }
        for (String s : firstpostlikes) {
            if (!secondpostlikes.contains(s))
            {
                Log.e("4444444", s);
                usersInfo_1.add(allusers.get(s));
            }
        }
        for (String s : secondpostlikes) {
            if (!firstpostlikes.contains(s))
            {
                Log.e("555555", s);
                usersInfo_3.add(allusers.get(s));
            }
        }
    }
}
