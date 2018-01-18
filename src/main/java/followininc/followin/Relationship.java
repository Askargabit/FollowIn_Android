package followininc.followin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Relationship extends Activity {
	private String url = "";
	private ListView lvRelationShipAllUser;
	private ArrayList<HashMap<String, String>> usersInfo = new ArrayList<HashMap<String, String>>();
	private Context context;
	private int WHAT_FINALIZE = 0;
	private static int WHAT_ERROR = 1;
	private ProgressDialog pd;

	private Handler handler = new Handler(new Callback() {

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
		setContentView(R.layout.relationship);
		lvRelationShipAllUser = (ListView) findViewById(R.id.lvRelationShip);
		url = getIntent().getStringExtra("userInfo");
		context = Relationship.this;
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
					JSONObject jsonObject = jsonParser.getJSONFromUrlByGet(url);
					JSONArray data = jsonObject.getJSONArray(TAG_DATA);

					for (int data_i = 0; data_i < data.length(); data_i++) {
						HashMap<String, String> hashMap = new HashMap<String, String>();
						JSONObject data_obj = data.getJSONObject(data_i);
						String str_id = data_obj.getString(TAG_ID);
						Log.e("Here", data_obj.getString(TAG_PROFILE_PICTURE));
						hashMap.put(TAG_PROFILE_PICTURE, data_obj.getString(TAG_PROFILE_PICTURE));
						hashMap.put(TAG_USERNAME, data_obj.getString(TAG_USERNAME));
						usersInfo.add(hashMap);
					}
					System.out.println("jsonObject::" + jsonObject);

				} catch (Exception exception) {
					exception.printStackTrace();
					what = WHAT_ERROR;
				}
				handler.sendEmptyMessage(what);
			}
		}).start();
	}
}