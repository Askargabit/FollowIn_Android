package followininc.followin.Backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;
    private static int UPDATE_VERSION = 0;
    private static int TABLE_EXIST = 0;

    private static final String PREV_VER = "currency";
    private static final String OLD_VER = "old_data";
    private static final String UNFOLLOWED = "unfollowed";
    private static final String NEWFOLLOWERS = "newfollowers";

    private static final String DATABASE_NAME = "USERS_DATA";

    private static final String id = "id";
    private static final String username = "username";
    private static final String profile_picture = "profile_picture";
    private static final String followers = "followers";
    private static final String followings = "followings";
    private static final String posts = "posts";

    private static final String CREATE_TABLE_CURRENCY = "CREATE TABLE " + PREV_VER + "(" + id + " TEXT," + username + " TEXT," + profile_picture
            + " TEXT," + followers + " TEXT," + followings + " TEXT," + posts + " TEXT" + ")";
    private static final String CREATE_TABLE_OLD = "CREATE TABLE " + OLD_VER + "(" + id + " TEXT," + username + " TEXT," + profile_picture
            + " TEXT," + followers + " TEXT," + followings + " TEXT," + posts + " TEXT" + ")";

    private static final String CREATE_TABLE_UNFOLLOWED = "CREATE TABLE " + UNFOLLOWED + "(" + id + " TEXT," + username + " TEXT," + profile_picture
            + " TEXT," + followers + " TEXT," + followings + " TEXT," + posts + " TEXT" + ")";

    private static final String CREATE_TABLE_NEWFOLLOWERS = "CREATE TABLE " + NEWFOLLOWERS + "(" + id + " TEXT," + username + " TEXT," + profile_picture
            + " TEXT," + followers + " TEXT," + followings + " TEXT," + posts + " TEXT" + ")";


    private static final String RECREATE_TABLE = "INSERT INTO " + OLD_VER + " SELECT * FROM " + PREV_VER;
   // private static final String CREATE_TABLE_FAVOURITE = "CREATE TABLE " + FAVOURITE + "(" + name + " TEXT" + ")";
    //private static final String CREATE_TABLE_MAIN = "CREATE TABLE " + MAIN + "(" + name  + " TEXT" + ")";

    public MySQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CURRENCY);
        db.execSQL(CREATE_TABLE_OLD);
        db.execSQL(CREATE_TABLE_NEWFOLLOWERS);
        db.execSQL(CREATE_TABLE_UNFOLLOWED);
        Log.e("SQL", "onCreate");
    //    db.execSQL(CREATE_TABLE_MAIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + OLD_VER);
        db.execSQL("DROP TABLE IF EXISTS " + PREV_VER);
        db.execSQL("DROP TABLE IF EXISTS " + UNFOLLOWED);
        db.execSQL("DROP TABLE IF EXISTS " + NEWFOLLOWERS);
        onCreate(db);
    }

    public long CreateRow(User user, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.e("MySQL-CreateRow", user.getUsername());
        values.put(id, user.getId());
        values.put(username, user.getUsername());
        values.put(profile_picture, user.getProfile_picture());
        values.put(followers, user.getFollowers());
        values.put(followings, user.getFollowings());
        values.put(posts, user.getPosts());
        long todo_id = db.insert(PREV_VER, null, values);
        return 1;//todo_id;
    }

    public void updatenewfollowerslist()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + NEWFOLLOWERS);
        db.execSQL(CREATE_TABLE_NEWFOLLOWERS);
    }


    public long CreateRowinfollowerstable(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.e("MySQL-CreateRow-followers", user.getUsername());
        values.put(id, user.getId());
        values.put(username, user.getUsername());
        values.put(profile_picture, user.getProfile_picture());
        values.put(followers, user.getFollowers());
        values.put(followings, user.getFollowings());
        values.put(posts, user.getPosts());
        long todo_id = db.insert(NEWFOLLOWERS, null, values);
        return 1;//todo_id;
    }


    public void updateunfollowerslist()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + UNFOLLOWED);
        db.execSQL(CREATE_TABLE_UNFOLLOWED);
    }

    public long CreateRowinunfollowerslist(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.e("MySQL-CreateRow-unfollowed", user.getUsername());
        values.put(id, user.getId());
        values.put(username, user.getUsername());
        values.put(profile_picture, user.getProfile_picture());
        values.put(followers, user.getFollowers());
        values.put(followings, user.getFollowings());
        values.put(posts, user.getPosts());
        long todo_id = db.insert(UNFOLLOWED, null, values);
        return 1;//todo_id;
    }

    public void onUpdate()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + OLD_VER);
        db.execSQL(CREATE_TABLE_OLD);
        db.execSQL(RECREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PREV_VER);
        db.execSQL(CREATE_TABLE_CURRENCY);
    }

    public User getData(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + PREV_VER + " WHERE " + id + " LIKE " + id;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        User td = new User("","","",0,0,0);
        td.setId(c.getString(c.getColumnIndex(id)));
        td.setUsername((c.getString(c.getColumnIndex(username))));
        td.setProfile_picture(c.getString(c.getColumnIndex(profile_picture)));
        td.setFollowers(c.getInt(c.getColumnIndex(followers)));
        td.setFollowings(c.getInt(c.getColumnIndex(followings)));
        td.setPosts(c.getInt(c.getColumnIndex(posts)));

        return td;
    }

    public List<User> getUserDATA() {
        List<User> todos;
        todos = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + PREV_VER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                User td = new User("","","",0,0,0);
                Log.e("MySQL-getUserData", (c.getString(c.getColumnIndex(username))));
                td.setId(c.getString(c.getColumnIndex(id)));
                td.setUsername((c.getString(c.getColumnIndex(username))));
                td.setProfile_picture(c.getString(c.getColumnIndex(profile_picture)));
                td.setFollowers(c.getInt(c.getColumnIndex(followers)));
                td.setFollowings(c.getInt(c.getColumnIndex(followings)));
                td.setPosts(c.getInt(c.getColumnIndex(posts)));
                todos.add(td);
            } while (c.moveToNext());
        }
        return todos;
    }

    public List<User> getUserOldDATA() {
        List<User> todos;
        todos = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + OLD_VER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                User td = new User("","","",0,0,0);
                Log.e("MySQL-getUserData", (c.getString(c.getColumnIndex(username))));
                td.setId(c.getString(c.getColumnIndex(id)));
                td.setUsername((c.getString(c.getColumnIndex(username))));
                td.setProfile_picture(c.getString(c.getColumnIndex(profile_picture)));
                td.setFollowers(c.getInt(c.getColumnIndex(followers)));
                td.setFollowings(c.getInt(c.getColumnIndex(followings)));
                td.setPosts(c.getInt(c.getColumnIndex(posts)));
                todos.add(td);
            } while (c.moveToNext());
        }
        return todos;
    }

    public List<User> getnewfollowersDATA() {
        List<User> todos;
        todos = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + NEWFOLLOWERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                User td = new User("","","",0,0,0);
                Log.e("MySQL-getUserData", (c.getString(c.getColumnIndex(username))));
                td.setId(c.getString(c.getColumnIndex(id)));
                td.setUsername((c.getString(c.getColumnIndex(username))));
                td.setProfile_picture(c.getString(c.getColumnIndex(profile_picture)));
                td.setFollowers(c.getInt(c.getColumnIndex(followers)));
                td.setFollowings(c.getInt(c.getColumnIndex(followings)));
                td.setPosts(c.getInt(c.getColumnIndex(posts)));
                todos.add(td);
            } while (c.moveToNext());
        }
        return todos;
    }

    public List<User> getUnfollowersDATA() {
        List<User> todos;
        todos = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + UNFOLLOWED;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                User td = new User("","","",0,0,0);
                Log.e("MySQL-getUserData", (c.getString(c.getColumnIndex(username))));
                td.setId(c.getString(c.getColumnIndex(id)));
                td.setUsername((c.getString(c.getColumnIndex(username))));
                td.setProfile_picture(c.getString(c.getColumnIndex(profile_picture)));
                td.setFollowers(c.getInt(c.getColumnIndex(followers)));
                td.setFollowings(c.getInt(c.getColumnIndex(followings)));
                td.setPosts(c.getInt(c.getColumnIndex(posts)));
                todos.add(td);
            } while (c.moveToNext());
        }
        return todos;
    }

    public int updateCurrence(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(id, user.getId());
        values.put(username, user.getUsername());
        values.put(profile_picture, user.getProfile_picture());
        values.put(followers, user.getFollowers());
        values.put(followings, user.getFollowings());
        values.put(posts, user.getPosts());

        return db.update(PREV_VER, values, id + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


}
