package com.wht.addekh.Activties;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wht.addekh.BaseActivity;
import com.wht.addekh.R;

public class ProductDetailActivity extends BaseActivity {

    ImageView food_back;
    TextView product_name, product_price;
    ImageView product_image;
    TextView ordernow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        food_back = findViewById(R.id.product_back);
        food_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();

        product_name = findViewById(R.id.product_name);
//        product_price = findViewById(R.id.product_price);
        product_image = findViewById(R.id.product_image);

        product_name.setText(intent.getStringExtra("product_name"));
//        product_price.setText(intent.getStringExtra("product_price"));
        product_image.setImageResource(intent.getIntExtra("product_image", R.drawable.img));

//        ordernow = findViewById(R.id.food_order);
//        ordernow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent orderFood = new Intent(Intent.ACTION_SENDTO);
//                orderFood.setData(Uri.parse("mailto:"));
//                String[] email = {"YourEmail@gmail.com", "", ""};
//                String subject = "New Order for " + intent.getStringExtra("product_name" + "!");
//                String text = "Order Details \n\n " + "Food Name: " + intent.getStringExtra("product_name" + "\nFood Price: " + intent.getStringExtra("product_price")
//                        + "Address: ");
//                orderFood.putExtra(Intent.EXTRA_EMAIL, email);
//                orderFood.putExtra(Intent.EXTRA_SUBJECT, subject);
//                orderFood.putExtra(Intent.EXTRA_TEXT, text);
//                if (orderFood.resolveActivity(getApplicationContext().getPackageManager()) != null) {
//                    startActivity(orderFood);
//                }
//            }
//        });

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