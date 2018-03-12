package followininc.followin.Menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import followininc.followin.Backend.JSONParser;
import followininc.followin.R;
import followininc.followin.RelationShipAdapter;

public class relnotfollowing extends AppCompatActivity {
    private String url1 = "";
    private String url2 = "";
    private ListView lvRelationShipAllUser;
    private ArrayList<HashMap<String, String>> usersInfo = new ArrayList<HashMap<String, String>>();
    private Context context;
    private int WHAT_FINALIZE = 0;
    private static int WHAT_ERROR = 1;
    private ProgressDialog pd;

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (pd != null && pd.isShowing())
                pd.dismiss();
            if (msg.what == WHAT_FINALIZE) {
                setImageGridAdapter();
            }
            else {
                Toast.makeText(context, "Check your network.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });
    public static final String TAG_DATA = "data";
    public static final String TAG_ID = "id";
    public static final String TAG_PROFILE_PICTURE = "profile_picture";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_BIO = "bio";
    public static final String TAG_WEBSITE = "website";
    public static final String TAG_FULL_NAME = "full_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relnotfollowing);
        lvRelationShipAllUser = (ListView) findViewById(R.id.lvrelnotfollowing);
        url1 = getIntent().getStringExtra("userInfo1");
        url2 = getIntent().getStringExtra("userInfo2");
        context = relnotfollowing.this;
        getAllMediaImages();
    }

    private void setImageGridAdapter() {
        lvRelationShipAllUser.setAdapter(new RelationShipAdapter(context, usersInfo));
    }

    private void getAllMediaImages() {
        pd = ProgressDialog.show(context, "", "Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                int what = WHAT_FINALIZE;
                try {
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject1 = jsonParser.getJSONFromUrlByGet(url1);
                    JSONArray data1 = jsonObject1.getJSONArray(TAG_DATA);
                    JSONParser jsonParser2 = new JSONParser();
                    JSONObject jsonObject2 = jsonParser2.getJSONFromUrlByGet(url2);
                    JSONArray data2 = jsonObject2.getJSONArray(TAG_DATA);
                    Log.e("Here","1");
                    int ok;
                    for (int data_i = 0; data_i < data1.length(); data_i++) {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        JSONObject data_obj = data1.getJSONObject(data_i);
                        String str_id = data_obj.getString(TAG_ID);
                        ok = 1;
                        for (int data_i2 = 0; data_i2 < data2.length(); data_i2++) {
                            JSONObject data_obj2 = data2.getJSONObject(data_i2);
                            String str_id2 = data_obj2.getString(TAG_ID);
                            Log.e("Here",str_id + "  " + str_id2);
                            if(Objects.equals(str_id, str_id2))
                            {
                                ok = 0;
                            }
                        }
                        if(ok == 1)
                        {
                            hashMap.put(TAG_PROFILE_PICTURE, data_obj.getString(TAG_PROFILE_PICTURE));
                            hashMap.put(TAG_USERNAME, data_obj.getString(TAG_USERNAME));
                            usersInfo.add(hashMap);
                        }
                    }
                    System.out.println("jsonObject::" + jsonObject1);

                } catch (Exception exception) {
                    exception.printStackTrace();
                    what = WHAT_ERROR;
                }
                handler.sendEmptyMessage(what);
            }
        }).start();
    }
}
