package com.wht.addekho.Notification;

import android.app.NotificationManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wht.addekho.BaseActivity;
import com.wht.addekho.R;

import java.util.ArrayList;
import java.util.List;


public class ActivityNotification extends BaseActivity implements NotificationAdapter.NoData {

    private List<Notification> notificationList = new ArrayList<Notification>();
    private RecyclerView rv_notification_list;
    public NotificationAdapter notificationAdapter;
    private DatabaseSqliteHandler db;
    private LinearLayout noRecordLayout;
    private TextView tv_notification_count, tv_notification_delete_notification;
    private TableRow tbone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_notification);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //overridePendingTransition(R.animator.move_left, R.animator.move_right);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Notifications");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getApplicationContext().getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        tbone = findViewById(R.id.tbone);

        init();
    }

    private void init() {
        db = DatabaseSqliteHandler.getInstance(this);
        db.UpdateNotification();
        rv_notification_list = (RecyclerView) findViewById(R.id.rv_notification_list);
        noRecordLayout = (LinearLayout) findViewById(R.id.noRecordLayout);

        tv_notification_count = (TextView) findViewById(R.id.tv_notification_count);
        tv_notification_delete_notification = (TextView) findViewById(R.id.tv_notification_delete_notification);

        notificationList = db.getAllElements();
        rv_notification_list.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(ActivityNotification.this, notificationList, this);
        rv_notification_list.setAdapter(notificationAdapter);
        //Toast.makeText(this, ""+db.getNotificationCount(), Toast.LENGTH_SHORT).show();
        if (db.getNotificationCount()==0)
        {
            tbone.setVisibility(View.GONE);
        }
        else
        {
            tbone.setVisibility(View.VISIBLE);
        }


        if (notificationList.isEmpty()) {
            noRecordLayout.setVisibility(View.VISIBLE);
        }

        NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifManager.cancelAll();

        tv_notification_count.setText("" + db.getNotificationCount());
        tv_notification_delete_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getNotificationCount() > 0) {

                    db.deleteAllNotification();
                    init();
                }
            }
        });


        // Initialize the Mobile Ads SDK.
       /* MobileAds.initialize(this, getResources().getString(R.string.ad_app_id));
        ad_layout=(LinearLayout)findViewById(R.id.ad_layout);
        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d("Add", "Ad Received");
                ad_layout.setVisibility(View.VISIBLE);
                //code when the ad loaded
            }
            @Override
            public void onAdFailedToLoad(int errorcode) {
                Log.d("Add", "Ad Not Loaded Received");
                //code if the ad loading is failed
            }
        });*/
    }

    @Override
    public void on_Remove(List<Notification> notificationList) {
        //Log.d("onResume" + "", "onResume: ");
        this.notificationList = notificationList;
        notificationAdapter.notifyDataSetChanged();
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        //overridePendingTransition(R.animator.left_right, R.animator.right_left);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
           // overridePendingTransition(R.animator.left_right, R.animator.right_left);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }*/

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
