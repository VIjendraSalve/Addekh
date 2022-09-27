package com.wht.addekh.Activties.InitialActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wht.addekh.BaseActivity;
import com.wht.addekh.R;

public class GetStartedActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        findViewById(R.id.get_started_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(GetStartedActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }
}