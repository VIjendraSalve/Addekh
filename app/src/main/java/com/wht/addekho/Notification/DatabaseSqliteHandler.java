package com.wht.addekho.Notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DatabaseSqliteHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
    private Context context;
    private Calendar cal = Calendar.getInstance();

    // Database Name
    private static final String DATABASE_NAME = "LMS";

    //table name
    private static final String TABLE_NOTIFICATION = "tbl_notification";
    //private static final String TABLE_CART = "tbl_cart";
    //private static final String TABLE_FAVORITE = "tbl_favorite";
    private static final String TABLE_SEARCH_HISTORY = "tbl_search_history";

    private static final String KEY_NOTIFICATION_ID = "notification_id";
    private static final String KEY_NOTIFICATION_TITLE = "notification_title";
    private static final String KEY_NOTIFICATION_MESSAGE = "notification_msg";
    private static final String KEY_NOTIFICATION_IMAGE = "notification_image";
    private static final String KEY_NOTIFICATION_DATE_TIME = "notification_date_time";
    private static final String KEY_NOTIFICATION_READ = "notification_read";

    /*private static final String KEY_C_ID = "c_id";
    private static final String KEY_CART_ID = "cart_id";
    private static final String KEY_CART_QUANTITY = "cart_quantity";
    //private static final String KEY_IMAGE_URL = "product_image_url";

    private static final String KEY_F_ID = "f_id";
    private static final String KEY_FAVORITE_ID = "favorite_id";
    private static final String KEY_PRODUCT_ID = "product_id";

    private static final String KEY_SEARCH_ID = "search_id";
    private static final String KEY_SEARCH_LABEL = "search_label";
    private static final String KEY_SEARCH_VALUE = "search_value";
    private static final String KEY_SEARCH_DATA = "search_data";*/


    private static DatabaseSqliteHandler instance;


    public static DatabaseSqliteHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseSqliteHandler(context);
        }
        return instance;
    }

    public DatabaseSqliteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NOTIFICATION + "(" +
                KEY_NOTIFICATION_ID + " INTEGER PRIMARY KEY," +
                KEY_NOTIFICATION_TITLE + " TEXT," +
                KEY_NOTIFICATION_MESSAGE + " TEXT," +
                KEY_NOTIFICATION_IMAGE + " TEXT," +
                KEY_NOTIFICATION_DATE_TIME + " TIMESTAMP DEFAULT (datetime('now','localtime')) NOT NULL," +
                KEY_NOTIFICATION_READ + " INTEGER"
                + ")";

        /*String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + KEY_C_ID + " INTEGER PRIMARY KEY," + KEY_CART_ID + " TEXT," + KEY_CART_QUANTITY + " TEXT" + ")";


        String CREATE_FAVORITE_TABLE = "CREATE TABLE " + TABLE_FAVORITE + "("
                + KEY_F_ID + " INTEGER PRIMARY KEY," + KEY_FAVORITE_ID + " TEXT," + KEY_PRODUCT_ID + " TEXT" + ")";
        //+ KEY_IMAGE_URL + " TEXT" +")";

        String CREATE_SEARCH_TABLE = "CREATE TABLE " + TABLE_SEARCH_HISTORY + "("
                + KEY_SEARCH_ID + " INTEGER PRIMARY KEY," + KEY_SEARCH_LABEL + " TEXT, "
                + KEY_SEARCH_VALUE + " TEXT," + KEY_SEARCH_DATA + " TEXT" + ")";*/

        db.execSQL(CREATE_NOTIFICATION_TABLE);
        /*db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);
        db.execSQL(CREATE_SEARCH_TABLE);*/
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_HISTORY);*/
        // Create tables again
        onCreate(db);
    }


    //public void insert_notification(String message,String image)
    public void insert_notification(String title, String message, String image) {
        final SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOTIFICATION_TITLE, title);
        values.put(KEY_NOTIFICATION_MESSAGE, message);
        values.put(KEY_NOTIFICATION_READ, 0);
        values.put(KEY_NOTIFICATION_IMAGE, image);

        // Inserting Row
        db.insert(TABLE_NOTIFICATION, null, values);
        Log.d("Values_check-->", "record inserted");
        db.close();
        //db.execSQL("INSERT INTO " + TABLE_NOTIFICATION + "(" + KEY_NOTIFICATION_MESSAGE +") " + "VALUES ("+message+")");
    }

    public List<Notification> getAllElements() {

        ArrayList<Notification> list = new ArrayList<Notification>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION+" order by notification_date_time desc";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                 //FormatedDate formatedDate= FormatedDate.getInstance(context);
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Notification obj = new Notification(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),(cursor.getString(4)));
                        //you could add additional columns here..
                        list.add(obj);
                    } while (cursor.moveToNext());
                }

            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
            try { db.close(); } catch (Exception ignore) {}
        }
        return list; }
    public long getNotificationCount() {
        //String countQuery = "SELECT * FROM " + TABLE_NOTIFICATION +" WHERE "+KEY_NOTIFICATION_READ+" == '0'";
        String countQuery = "SELECT * FROM " + TABLE_NOTIFICATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        //db.close();
        return cnt;
    }

    public void UpdateNotification() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + TABLE_NOTIFICATION + " SET " + KEY_NOTIFICATION_READ + "='dash1'");
        db.close();
    }

    //public void delete_notification(String note_id, String image_path){
    public void delete_notification(String note_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(contact.getID()) });
        //Log.d("Values_delete-->", s_id + "  " + p_id);

        //String Query = "delete from " + TABLE_NAME + "where " + KEY_PRODUCT_ID + " = " + p_id+" and "+KEY_SIGNUP_ID+ " = " + s_id;
        db.delete(TABLE_NOTIFICATION, KEY_NOTIFICATION_ID + "=" + note_id + "", null);

     /* if (image_path!=null) {
            File file = new File(image_path);
            file.delete();
        }*/

        //db.execSQL(Query, null);
        db.close();
    }

    public void deleteAllNotification() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NOTIFICATION);
        //db.delete(TABLE_CART, null, null);
        db.close();
    }


    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATION, null, null);
        //db.delete(TABLE_CART, null, null);
        //db.delete(TABLE_FAVORITE, null, null);
        db.close();
    }

}
