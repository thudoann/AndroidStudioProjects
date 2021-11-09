

package fr.cytech.b3.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.provider.ContactsContract;

public class MainActivity extends AppCompatActivity {

    public static final int Color_picker = 1, contact_picker=3;
    String color ,BFF ;
    TextView picked_color_txt, picked_bff_txt;
    Button pick_color_btn, pick_bff_btn;
    LinearLayout background;

    public void browsergg(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.fr/"));
        startActivity(browserIntent);
    }
    public void gotoActivity2(View view) {
        Intent intent = new Intent(MainActivity.this, Activity2.class);
        startActivityForResult(intent, Color_picker);
    }

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);
    }

    protected void onStop(){
        super.onStop();

        Intent intent = new Intent(MainActivity.this, MyService.class);
        stopService(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        picked_color_txt = findViewById(R.id.picked_color_txt);
        pick_color_btn = findViewById(R.id.pick_color_btn);
        background = findViewById(R.id.background);
        pick_color_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                startActivityForResult(intent, Color_picker);
            }
        });
        picked_bff_txt = findViewById(R.id.picked_bff_txt);
        pick_bff_btn = findViewById(R.id.pick_bff_btn);
        pick_bff_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, contact_picker);
            }
        });

    }


    @SuppressLint("Range")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (Color_picker):
                if (resultCode == Activity.RESULT_OK) {
                    color = data.getStringExtra("color");
                    picked_color_txt.setText(color);
                    background.setBackgroundColor(Color.parseColor(color));
                }
                break;
            case (contact_picker):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        BFF = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        picked_bff_txt.setText(BFF);
                    }
                }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("BFF", BFF);
        savedInstanceState.putString("color", color);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        BFF = savedInstanceState.getString("BFF");
        color = savedInstanceState.getString("color");
        picked_bff_txt.setText(BFF);
        picked_color_txt.setText(color);
        background.setBackgroundColor(Color.parseColor(color));
    }

}

