package fr.cytech.b3.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

public class Activity2 extends AppCompatActivity {
    RadioGroup radio_color;
    String color;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        radio_color = findViewById(R.id.radio_color);
        radio_color.clearCheck();

        radio_color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId){
            switch (checkedId){
                case (R.id.radio_red):
                    color = "RED";
                    break;
                case (R.id.radio_green):
                    color = "GREEN";
                    break;
                case (R.id.radio_blue):
                    color = "BLUE";
                    break;
            }


            Intent r = new Intent();
            r.putExtra("color",color);
            setResult(RESULT_OK, r);
            finish();
        }
        });
        }
    }
