package com.wht.addekh.Activties;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.wht.addekh.BaseActivity;
import com.wht.addekh.R;

public class BottomsheetActivity extends BaseActivity {
    private BottomAppBar botAppBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomsheet);
        botAppBar = findViewById(R.id.bottomMenuBar);
        //  attach menu to your BottomAppBar
        botAppBar.replaceMenu(R.menu.bottom_nav_menu);
        botAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_open_bot_sheet:
                        openBottomSheetDialog();
                        return true;
                }
                return false;
            }
        });
    }
    private void openBottomSheetDialog() {
        BottomDialogFragment addBottomDialogFragment =
                BottomDialogFragment.newInstance(this);
        addBottomDialogFragment.show(getSupportFragmentManager(),
                BottomDialogFragment.TAG);
    }
}