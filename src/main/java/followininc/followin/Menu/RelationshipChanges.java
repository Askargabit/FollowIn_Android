                    package followininc.followin.Menu;

                    import android.app.ProgressDialog;
                    import android.content.Context;
                    import android.content.Intent;
                    import android.os.Bundle;
                    import android.os.Handler;
                    import android.os.Message;
                    import android.support.v7.app.AppCompatActivity;
                    import android.util.Log;
                    import android.widget.ListView;
                    import android.widget.Toast;

                    import java.util.ArrayList;
                    import java.util.HashMap;
                    import java.util.Objects;

                    import followininc.followin.Backend.MySQLiteHelper;
                    import followininc.followin.Backend.User;
                    import followininc.followin.R;
                    import followininc.followin.RelationShipAdapter;

public class RelationshipChanges extends AppCompatActivity {

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (pd != null && pd.isShowing())
                pd.dismiss();
            if (msg.what == WHAT_FINALIZE) {
                setImageGridAdapter();
            } else {
                Toast.makeText(context, "Check your network.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });
    private ProgressDialog pd;
    private int WHAT_FINALIZE = 0;
    private Context context;
    private ArrayList<HashMap<String, String>> usersInfo = new ArrayList<HashMap<String, String>>();
    private ListView lvRelationShipAllUser;
    static ArrayList<ArrayList<String>> Data = new ArrayList<>();
    static ArrayList<ArrayList<String>> Old_Data = new ArrayList<>();
    private String Token = "";
    MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship_changes);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        Token = getIntent().getStringExtra("Token");
        context = RelationshipChanges.this;
        lvRelationShipAllUser = (ListView) findViewById(R.id.RelationShipChanges);
        db = new MySQLiteHelper(getApplicationContext());
        if(Objects.equals(Token, "1"))
        {
            unfollowed();
        }
        else if(Objects.equals(Token, "2"))
        {
            newfollowed();
        }
        db.closeDB();
    }

    private void unfollowed()
    {
        ArrayList<User> Current = (ArrayList<User>) db.getUserDATA();
        ArrayList<User> Old = (ArrayList<User>) db.getUserOldDATA();
        ArrayList<User> unfollowers = (ArrayList<User>) db.getUnfollowersDATA();
        ArrayList<User> Excess = new ArrayList<>();
        String forcheckbox = "";
        for(int i = 0; i < Old.size(); i++)
        {
            int check = 0;
            for(int j = 0; j < Current.size(); j++)
            {
                if(Objects.equals(Old.get(i).getId(), Current.get(j).getId()))
                {
                    check = 1;
                    break;
                }
            }
            if (check == 0)
            {
                Excess.add(Old.get(i));
                Log.e("Excess-unfollowed",Excess.get(Excess.size() - 1).getUsername());
            }
        }
        for(int i = 0; i < Current.size(); i++)
        {
            int check = 0;
            for(int j = 0; j < unfollowers.size(); j++)
            {
                if(Objects.equals(Current.get(i).getId(), unfollowers.get(j).getId()))
                {
                    unfollowers.remove(j);
                    check = 1;
                    break;
                }
            }
        }
        for(int i = 0; i < Excess.size(); i++)
        {
            int check = 0;
            for(int j = 0; j < unfollowers.size(); j++)
            {
                if(Objects.equals(Excess.get(i).getId(), unfollowers.get(j).getId()))
                {
                    check = 1;
                    break;
                }
            }
            if(check == 0)
            {
                unfollowers.add(Excess.get(i));
                Log.e("Excess-unfollowers2", unfollowers.get(unfollowers.size() - 1).getUsername());
            }
        }
        db.updateunfollowerslist();
        pd = ProgressDialog.show(context, "", "Loading...");
        int what = WHAT_FINALIZE;
        for (int data_i = 0; data_i < unfollowers.size(); data_i++) {
            db.CreateRowinunfollowerslist(unfollowers.get(data_i));
            HashMap<String, String> hashMap = new HashMap<String, String>();
            String str_id = unfollowers.get(data_i).getId();
            hashMap.put("profile_picture", unfollowers.get(data_i).getProfile_picture());
            hashMap.put("username", unfollowers.get(data_i).getUsername());
            usersInfo.add(hashMap);
            Log.e("Unfollowers-qqqq", unfollowers.get(data_i).getUsername());
        }
        handler.sendEmptyMessage(what);
    }

    private void newfollowed()
    {
        ArrayList<User> Current = (ArrayList<User>) db.getUserDATA();
        ArrayList<User> Old = (ArrayList<User>) db.getUserOldDATA();
        ArrayList<User> newfollowers = (ArrayList<User>) db.getUnfollowersDATA();
        ArrayList<User> Excess = new ArrayList<>();
        String forcheckbox = "";
        for(int i = 0; i < Current.size(); i++)
        {
            int check = 0;
            for(int j = 0; j < Old.size(); j++)
            {
                if(Objects.equals(Old.get(j).getId(), Current.get(i).getId()))
                {
                    check = 1;
                    break;
                }
            }
            if (check == 0)
            {
                Excess.add(Current.get(i));
                Log.e("Excess-newfollowed",Excess.get(Excess.size() - 1).getUsername());
            }
        }
        for(int i = 0; i < newfollowers.size(); i++)
        {
            int check = 0;
            for(int j = 0; j < Current.size(); j++)
            {
                if(Objects.equals(newfollowers.get(i).getId(), Current.get(j).getId()))
                {
                    check = 1;
                    break;
                }
            }
            if (check == 0)
            {
                newfollowers.remove(i);
            }
        }
        for(int i = 0; i < Excess.size(); i++)
        {
            int check = 0;
            for(int j = 0; j < newfollowers.size(); j++)
            {
                if(Objects.equals(Excess.get(i).getId(), newfollowers.get(j).getId()))
                {
                    check = 1;
                    break;
                }
            }
            if(check == 0)
            {
                newfollowers.add(Excess.get(i));
                Log.e("Excess-unfollowers2", newfollowers.get(newfollowers.size() - 1).getUsername());
            }
        }
        db.updatenewfollowerslist();
        pd = ProgressDialog.show(context, "", "Loading...");
        int what = WHAT_FINALIZE;
        for (int data_i = 0; data_i < newfollowers.size(); data_i++) {
            db.CreateRowinfollowerstable(newfollowers.get(data_i));
            HashMap<String, String> hashMap = new HashMap<String, String>();
            String str_id = newfollowers.get(data_i).getId();
            hashMap.put("profile_picture", newfollowers.get(data_i).getProfile_picture());
            hashMap.put("username", newfollowers.get(data_i).getUsername());
            usersInfo.add(hashMap);
            Log.e("Unfollowers-qqqq", newfollowers.get(data_i).getUsername());
        }
        handler.sendEmptyMessage(what);
    }

    private void setImageGridAdapter() {
        lvRelationShipAllUser.setAdapter(new RelationShipAdapter(context, usersInfo));
    }

}
