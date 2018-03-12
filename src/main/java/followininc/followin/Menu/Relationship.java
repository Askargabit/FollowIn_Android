package followininc.followin.Menu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

import followininc.followin.Backend.JSONParser;
import followininc.followin.R;
import followininc.followin.RelationShipAdapter;

@SuppressWarnings("unchecked")
public class Relationship extends Activity {
	private String user_id = "";
	private ListView lvRelationShipAllUser;
	private ArrayList<HashMap<String, String>> usersInfo = new ArrayList<HashMap<String, String>>();
	private Context context;
	private int WHAT_FINALIZE = 0;
	private ProgressDialog pd;
	private String Token = "";

	static ArrayList<ArrayList<String>> Data = new ArrayList<>();
	String[] cities = {"Amount of Followers", "Amount of Followings", "Amount of Posts", "Liked me most", "Likes/post", "Recent active"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relationship);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		Data = (ArrayList<ArrayList<String>>) bundle.getSerializable("value");
		Token = getIntent().getStringExtra("Token");
		lvRelationShipAllUser = (ListView) findViewById(R.id.lvRelationShip);
		Spinner spinner = (Spinner) findViewById(R.id.sorting_spinner);
		user_id = getIntent().getStringExtra("userId");
        context = Relationship.this;
		download a = new download();
		a.execute();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					Collections.sort(Data, new Comparator<ArrayList<String>>() {
						@Override
						public int compare(ArrayList<String> o1, ArrayList<String> o2) {
							int i = 0, i1 = 0;
							if(Objects.equals(o1.get(3), "unknown")) {i = 0;} else {i = Integer.parseInt(o1.get(3));}
							if(Objects.equals(o2.get(3), "unknown")) {i1 = 0;} else {i1 = Integer.parseInt(o2.get(3));}
							return i1 - i;
						}
					});
					usersInfo.clear();
					getAllMediaImages(3);
				} else if (position == 1) {
					Collections.sort(Data, new Comparator<ArrayList<String>>() {
						@Override
						public int compare(ArrayList<String> o1, ArrayList<String> o2) {
							int i = 0, i1 = 0;
							if(Objects.equals(o1.get(4), "unknown")) {i = 0;} else {i = Integer.parseInt(o1.get(4));}
							if(Objects.equals(o2.get(4), "unknown")) {i1 = 0;} else {i1 = Integer.parseInt(o2.get(4));}
							return i1 - i;
						}
					});
					usersInfo.clear();
					getAllMediaImages(4);
				} else if (position == 2) {
					Collections.sort(Data, new Comparator<ArrayList<String>>() {
						@Override
						public int compare(ArrayList<String> o1, ArrayList<String> o2) {
							int i = 0, i1 = 0;
							if(Objects.equals(o1.get(5), "unknown")) {i = 0;} else {i = Integer.parseInt(o1.get(5));}
							if(Objects.equals(o2.get(5), "unknown")) {i1 = 0;} else {i1 = Integer.parseInt(o2.get(5));}
							return i1 - i;
						}
					});
					usersInfo.clear();
					getAllMediaImages(5);
				} else if(position == 3)
				{
					Collections.sort(Data, new Comparator<ArrayList<String>>() {
						@Override
						public int compare(ArrayList<String> o1, ArrayList<String> o2) {
							int i = 0, i1 = 0;
							i = Integer.parseInt(o1.get(6));
							i1 = Integer.parseInt(o2.get(6));
							return i1 - i;
						}
					});
					usersInfo.clear();
					getAllMediaImages(6);
				}
				else if(position == 4)
				{
					Collections.sort(Data, new Comparator<ArrayList<String>>() {
						@Override
						public int compare(ArrayList<String> o1, ArrayList<String> o2) {
							int i = 0, i1 = 0;
							i = Integer.parseInt(o1.get(7));
							i1 = Integer.parseInt(o2.get(7));
							return i1 - i;
						}
					});
					usersInfo.clear();
					getAllMediaImages(7);
				}
				else if(position == 5)
				{
					Collections.sort(Data, new Comparator<ArrayList<String>>() {
						@Override
						public int compare(ArrayList<String> o1, ArrayList<String> o2) {
							int i = 0, i1 = 0;
							i = Integer.parseInt(o1.get(9));
							i1 = Integer.parseInt(o2.get(9));
							return i1 - i;
						}
					});
					usersInfo.clear();
					getAllMediaImages(9);
				}
 			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};
		spinner.setOnItemSelectedListener(itemSelectedListener);
		getAllMediaImages(0);
		lvRelationShipAllUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(Relationship.this, Profile.class).putExtra("user",Data.get(position)));
			}
		});
	}

	private void setImageGridAdapter() {
		lvRelationShipAllUser.setAdapter(new RelationShipAdapter(context, usersInfo));
	}

	void getAllMediaImages(int x) {
				int what = WHAT_FINALIZE;
					for (int data_i = 0; data_i < Data.size(); data_i++) {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        String str_id = Data.get(data_i).get(0);
                        hashMap.put("profile_picture", Data.get(data_i).get(2));
                        hashMap.put("username", Data.get(data_i).get(1));
						if(x != 0)
						{
							hashMap.put("Info", Data.get(data_i).get(x));
						}
                        usersInfo.add(hashMap);
					}
					setImageGridAdapter();
	}

    class download extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(context, "", "Loading...");
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			pd.dismiss();
		}

		@Override
		protected Void doInBackground(Void... params) {
			publishProgress(new Void[]{});
			final ArrayList<String> Media_url = new ArrayList<String>();
			final ArrayList<String> Likes_id = new ArrayList<>();
			String url_media = "https://api.instagram.com/v1/users/" + user_id +"/media/recent/?access_token=" + Token;
					try {
						JSONParser jsonParser = new JSONParser();
						JSONObject jsonObject = jsonParser.getJSONFromUrlByGet(url_media);
						JSONArray data = jsonObject.getJSONArray("data");
						for (int data_i = 0; data_i < data.length(); data_i++) {
							String Media_id = (String) data.getJSONObject(data_i).get("id");
							Media_url.add(Media_id);
							JSONParser jsonParser1 = new JSONParser();
							JSONObject jsonObject1 = jsonParser1.getJSONFromUrlByGet("https://api.instagram.com/v1/media/" + Media_id +"/likes?access_token=" + Token);
							JSONArray data_1 = jsonObject1.getJSONArray("data");
							for (int data_j = 0; data_j < data_1.length(); data_j++)
							{
								String Like_id = (String) data_1.getJSONObject(data_j).get("id");
								Likes_id.add(Like_id);
							}
						}
						for(int i = 0; i < Data.size(); i++)
						{
							url_media = "https://api.instagram.com/v1/users/" + Data.get(i).get(0) + "/media/recent/?access_token=" + Token;
							if(Objects.equals(Data.get(i).get(4), "unknown"))
							{
								continue;
							}
							JSONParser jsonParser2 = new JSONParser();
							JSONObject jsonObject2 = jsonParser2.getJSONFromUrlByGet(url_media);
							JSONArray data_r = jsonObject2.getJSONArray("data");
							int totalLikes = 0;
							long postedtime = 0;
							long firstpostedtime = 0;
							for(int j = 0; j < data_r.length(); j++)
							{
								totalLikes = totalLikes + Integer.parseInt(data_r.getJSONObject(j).getJSONObject("likes").getString("count"));
								postedtime = postedtime + Integer.parseInt(data_r.getJSONObject(j).getString("created_time"));
								if(j == data_r.length() - 1)
								{
									firstpostedtime = Integer.parseInt(data_r.getJSONObject(j).getString("created_time"));
									//Log.e(Data.get(i).get(1), data_r.getJSONObject(j).getString("created_time"));
								}
							}
							int likesperpost = totalLikes/data_r.length();
							long averpostedtime = postedtime/data_r.length();
							Data.get(i).set(7, String.valueOf(likesperpost));
							Data.get(i).set(9, String.valueOf(averpostedtime - firstpostedtime));
							Log.e(Data.get(i).get(1), Data.get(i).get(9));
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
					for(int i = 0; i < Data.size(); i++)
					{
						Data.get(i).set(6, String.valueOf(Collections.frequency(Likes_id, Data.get(i).get(0))));
					}
			return null;
		}
	}
}