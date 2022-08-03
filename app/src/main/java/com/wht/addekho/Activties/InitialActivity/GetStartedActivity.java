package com.wht.addekho.Activties.InitialActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wht.addekho.BaseActivity;
import com.wht.addekho.R;

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