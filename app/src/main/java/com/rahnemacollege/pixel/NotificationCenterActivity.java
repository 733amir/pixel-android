package com.rahnemacollege.pixel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Locale;

public class NotificationCenterActivity extends AppCompatActivity {

    String TAG = "NotificationCenterActivity";

    private RecyclerView notificationsRecyclerView;
    private NotificationsAdapter notificationsAdapter;
    private static final int NUM_NOTIFICATION_ITEM = 10;

    public String username;

    public void setArgs(String username) {
        this.username = username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_center);

        getSupportActionBar().setTitle(R.string.notification_center_title);

        notificationsRecyclerView = findViewById(R.id.notifications_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notificationsRecyclerView.setLayoutManager(layoutManager);
        notificationsRecyclerView.setHasFixedSize(true);

        notificationsAdapter = new NotificationsAdapter(NUM_NOTIFICATION_ITEM);
        notificationsRecyclerView.setAdapter(notificationsAdapter);

        //TODO get notifications from server here
        //mock notifications to test:
//        notificationsAdapter.addNotificationForAdapter("Parsa", "",getString(R.string.notification_friend_request_accept_report));
//        notificationsAdapter.addNotificationForAdapter("Amir Khazaii", "",getString(R.string.notification_friend_request_report));
//        notificationsAdapter.addNotificationForAdapter("Hey", "",getString(R.string.notification_friend_request_accept_report));
//        notificationsAdapter.addNotificationForAdapter("Yeah", "",getString(R.string.notification_post_like_report));
    }

    @Override
    public void finish() {
        super.finish();
        if (Locale.getDefault().getDisplayLanguage().equals("English"))
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        else
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}