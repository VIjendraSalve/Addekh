package com.wht.addekh.Activties;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wht.addekh.BaseActivity;
import com.wht.addekh.R;

public class ActivityStoreList extends BaseActivity {

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_list_layout);
        floatingActionButton= findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(),ActivityAddStore.class);
                intent.putExtra("flag", "0");
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}