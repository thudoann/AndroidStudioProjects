package fr.cytech.b3.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    String[] CurrencyCodes = {"USD", "EUR", "RMB", "CHF", "AUD"};
    Double[] FromUSDRatios = {1.0, 1.16, 0.16, 1.1, 0.75};
    Double[] ToUSDRatios = {1.0, 0.86, 6.4, 0.91, 1.33};
    EditText editText1, editTxtFromSpinner, editTxtToSpinner;
    TextView txtTo;
    Button btn;
    RadioGroup radioGroup1, radioGroup2;
    RadioButton radioBtn1, radioBtn2;
    Spinner spinnerFrom, spinnerTo;
    CheckBox checkReverse;
    double ratioFrom, ratioTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
        addListenerOnSpinner();
    }

    public void addListenerOnButton(){
        editText1 = findViewById(R.id.editText1);
        txtTo = findViewById(R.id.txtTo);
        btn = findViewById(R.id.btn);
        radioGroup1= findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int radio1 = radioGroup1.getCheckedRadioButtonId();
                int radio2 = radioGroup2.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioBtn1 = findViewById(radio1);
                radioBtn2 = findViewById(radio2);

                // get convert ratio
                String CurrencyFrom = radioBtn1.getText().toString();
                String CurrencyTo = radioBtn2.getText().toString();
                int idFrom = Arrays.asList(CurrencyCodes).indexOf(CurrencyFrom);
                int idTo = Arrays.asList(CurrencyCodes).indexOf(CurrencyTo);
                double ratio = FromUSDRatios[idFrom] * ToUSDRatios[idTo];

                // convert
                String converted = (Double.parseDouble(editText1.getText().toString()) * ratio) + "";

                txtTo.setText(converted);
            }
        });
    }

    public void addListenerOnSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                CurrencyCodes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerFrom = findViewById(R.id.spinnerFrom);
        this.spinnerFrom.setAdapter(adapter);
        this.spinnerTo = findViewById(R.id.spinnerTo);
        this.spinnerTo.setAdapter(adapter);
        editTxtFromSpinner = findViewById(R.id.editTxtFromSpinner);
        editTxtToSpinner = findViewById(R.id.editTxtToSpinner);
        checkReverse = findViewById(R.id.checkReverse);



        this.spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ratioFrom = FromUSDRatios[position];

                if (checkReverse.isChecked()) {
                    // convert
                    String converted = (Double.parseDouble(editTxtToSpinner.getText().toString()) * (ratioFrom * ratioTo)) + "";

                    editTxtFromSpinner.setText(converted);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ratioTo = ToUSDRatios[position];

                if (!checkReverse.isChecked()) {
                    // convert
                    String converted = (Double.parseDouble(editTxtFromSpinner.getText().toString()) * (ratioFrom * ratioTo)) + "";

                    editTxtToSpinner.setText(converted);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}