/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.wangtianya.aspeed.activity;

import com.wangtianya.abase.core.activity.ABaseActivity;
import com.wangtianya.abase.ioc.annotation.InjectView;
import com.wangtianya.aspeed.R;
import com.wangtianya.aspeed.core.ASContext;
import com.wangtianya.aspeed.event.PageSwitchEvent;
import com.wangtianya.aspeed.wigets.slidingmenu.SlidingMenu;
import com.wangtianya.aspeed.wigets.topbar.TopBarView;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import de.greenrobot.event.EventBus;

/**
 * Created by tianya on 2015/8/31.
 */
public class DrawerMainActivity extends ABaseActivity {

    @InjectView(id = R.id.topBar)
    private TopBarView mTopBar;

    private SlidingMenu menu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        ASContext.Caches.mainActivity = this;
        EventBus.getDefault().register(this);

        initViews();
    }

    public void initViews() {
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.activity_welcome);

        ASContext.Caches.topbar = mTopBar;
        setLeftOnclick();

        PageSwitchEvent.gotoPage(PageSwitchEvent.DELAY);
    }

    private void setLeftOnclick() {
        mTopBar.setLeftButton(TopBarView.LeftButtonType.List, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.showMenu();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ASContext.Caches.topbar = null;
        ASContext.Caches.mainActivity = null;
    }

    @Override
    public void onBackPressed() {
        if (menu.isMenuShowing()) {
            menu.toggle();
            return;
        }
        super.onBackPressed();
    }

    public void onEventMainThread(PageSwitchEvent event) {
        if (event.pageName.equals(PageSwitchEvent.DELAY)) {
            setLeftOnclick();
        }

        mTopBar.setTitle(event.pageName);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (PageSwitchEvent.TransitionAnimation.Go.equals(event.transitionAnimation)) {
            ft.setCustomAnimations(R.animator.slide_go_right, R.animator.slide_go_left);
        } else if (PageSwitchEvent.TransitionAnimation.Back.equals(event.transitionAnimation)) {
            ft.setCustomAnimations(R.animator.slide_back_left, R.animator.slide_back_right);
        }
        this.setFragmentShouldAskOnBackPressed(event.fragment);
        ft.add(R.id.rlContainer, event.fragment);
        ft.disallowAddToBackStack();
        ft.commit();
    }
}