package com.example.android.wallmart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Item0 extends AppCompatActivity {

    Button button;
    TextView title;
    ImageView imageView;
    TextView price;
    TextView Description;
    TextView Delivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = (TextView) findViewById(R.id.productTitle);
        price = (TextView) findViewById(R.id.productPrice);
        imageView = (ImageView) findViewById(R.id.imageView);
        Description= (TextView) findViewById(R.id.productDescription);
        Delivery = (TextView) findViewById(R.id.deliveryDetails);
        button = (Button) findViewById(R.id.button);

        title.setText("SALT");
        imageView.setImageResource(R.drawable.salt);
        price.setText("27$");
        Description.setText("Tata Companies Ltd");
        Delivery.setText("Standard:Tomorrow,between 2pm to 9 pm");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Item0.this,Payment.class);
                startActivity(intent);
            }
        });
    }
}
