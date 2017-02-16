package com.ruslan_hlushen.cleanroom.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.ruslan_hlushen.cleanroom.Firebase.MyFirebaseMessagingService;
import com.ruslan_hlushen.cleanroom.R;
import com.ruslan_hlushen.cleanroom.SharedPreferencesManager;
import com.ruslan_hlushen.cleanroom.adapters.NotificationsStickyHeadersAdapter;
import com.ruslan_hlushen.cleanroom.dialogs.TextAndButtonDialog;
import com.ruslan_hlushen.cleanroom.models.MyNotification;
import com.ruslan_hlushen.cleanroom.network.RxJavaSubscriber;

import java.util.ArrayList;
import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    public static final String EXTRA_broadcastAction = "broadcastAction";
    private static final String EXTRA_myNotificationList = "myNotificationList";
    private static boolean isInFocus = false;
    RecyclerView recyclerView;
    NotificationsStickyHeadersAdapter adapter;
    List<MyNotification> myNotificationList = new ArrayList<>();
    BroadcastReceiver broadcastReceiver;
    ProgressBar progressBar;
    SwipyRefreshLayout swipyRefreshLayout;
    private ImageView imageViewActionBar, imageView_actionBar_Exit;


    private static void setIsInFocus(boolean isInFocus) { MainActivity.isInFocus = isInFocus; }


    public static boolean isInFocus() { return isInFocus; }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setAdapter();
        initSwipyRefreshLayout();

        createAndRegisterBroadcast();

        setOnClickListeners();

        if (savedInstanceState != null) {

            adapter.addData(savedInstanceState.getParcelableArrayList(EXTRA_myNotificationList));

            if (myNotificationList.size() == 0) {
                firstCallToGetNotifications();
            }

        } else {
            firstCallToGetNotifications();
        }
    }


    @Override
    protected void onResume() {

        super.onResume();
        setIsInFocus(true);

        RxJavaSubscriber.setContext(MainActivity.this);
    }


    @Override
    protected void onPause() {

        super.onPause();
        setIsInFocus(false);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(EXTRA_myNotificationList, (ArrayList<? extends Parcelable>) myNotificationList);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onBackPressed() {

        new TextAndButtonDialog().setParams(getString(R.string.warning_to_exit),
                                            () -> {
                                                SharedPreferencesManager.setRememberMe(MainActivity.this, false);
                                                finish();
                                            })
                                 .show(getSupportFragmentManager(), TextAndButtonDialog.Tag);
    }


    private void initViews() {

        imageView_actionBar_Exit = (ImageView) findViewById(R.id.activity_main_imageView_actionBar_Exit);
        imageViewActionBar = (ImageView) findViewById(R.id.activity_main_imageView_actionBar);
        recyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_last_notifications);
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.activity_main_SwipyRefreshLayout);
        progressBar = (ProgressBar) findViewById(R.id.activity_main_progressBar);
    }


    private void firstCallToGetNotifications() {

        setCanClickAndProgressBar(false, progressBar);
        callToGetNotifications(1, false);
    }


    private void setAdapter() {

        adapter = new NotificationsStickyHeadersAdapter(MainActivity.this, myNotificationList);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new StickyHeaderDecoration(adapter));
    }


    private void initSwipyRefreshLayout() {

        swipyRefreshLayout.setOnRefreshListener(direction -> {

            if (direction == SwipyRefreshLayoutDirection.BOTTOM) {

                if (adapter.getLastItem() != null) {

                    callToGetNotifications(adapter.getLastItem().getTimeSend(), false);
                } else {
                    callToGetNotifications(1, false);
                }
            } else if (direction == SwipyRefreshLayoutDirection.TOP) {

                callToGetNotifications(1, true);
            }
        });
    }


    private void createAndRegisterBroadcast() {

        broadcastReceiver = new BroadcastReceiver() {

            public void onReceive(final Context context, Intent intent) {

                MyNotification myNotification = intent.getParcelableExtra(MyFirebaseMessagingService.EXTRA_notificationData);
                if (myNotification != null) {

                    adapter.addOneItem(0, myNotification);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(EXTRA_broadcastAction);
        registerReceiver(broadcastReceiver, intentFilter);
    }


    private void setOnClickListeners() {

        imageViewActionBar.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, NewNotificationActivity.class);
            startActivity(intent);
        });

        imageView_actionBar_Exit.setOnClickListener(view -> MainActivity.this.onBackPressed());
    }


    private void callToGetNotifications(long lastDateTime, boolean updateAll) {

        setCanClick(false);

        getApiInterface().getNotifications(SharedPreferencesManager.getMySecurityToken(MainActivity.this),
                                           lastDateTime)
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new RxJavaSubscriber<>(responseFromServer -> {

                             if (updateAll) {
                                 adapter.setData(responseFromServer.getData());
                             } else {
                                 adapter.addData(responseFromServer.getData());
                             }

                         }, null, () -> setCanClickAndProgressBar(true, progressBar)));

    }


    @Override
    protected void setCanClickAndProgressBar(boolean state, ProgressBar progressBar) {

        super.setCanClickAndProgressBar(state, progressBar);

        if (state) {
            swipyRefreshLayout.setRefreshing(false);
        }
    }
}