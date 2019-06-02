package com.fub.smart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;


public class ChooseItemTypeActivity extends AppCompatActivity implements View.OnClickListener{


    ImageView barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_item_type);

        barcode=(ImageView)findViewById(R.id.viewImageBarcode);
        barcode.setClickable(true);
        barcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewImageBarcode:
                Intent myIntent = new Intent(getBaseContext(),   ScannerActivity.class);
                myIntent.putExtra("scanGoal",2);
                startActivity(myIntent);
                break;
            default:

                Intent addItemIntent = new Intent(getBaseContext(), AddItemActivity.class);
                addItemIntent.putExtra("scanResult",view.getTag().toString());
                startActivity(addItemIntent);

             break;
        }
    }
}
