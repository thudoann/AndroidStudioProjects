package fr.cytech.b3.tp3;



import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnUserGuide;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.about_btn) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View formElementsView = inflater.inflate(R.layout.about, null, false);
            //Create a dialog
            new AlertDialog.Builder(this)
                    .setView(formElementsView)
                    .setTitle(R.string.about_title)
                    .setPositiveButton(R.string.close,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }


    private int multiple = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences("MathPrefs", 0);
        multiple = settings.getInt("multiple", 2);

        Button selected_btn = getButtonForMultiple();
        if (selected_btn != null) {
            selected_btn.setTextColor(Color.RED);
        }

        selected_btn = (Button) findViewById(R.id.one_btn);
        selected_btn.setOnLongClickListener(longClick);
        selected_btn = (Button) findViewById(R.id.two_btn);
        selected_btn.setOnLongClickListener(longClick);
        selected_btn = (Button) findViewById(R.id.three_btn);
        selected_btn.setOnLongClickListener(longClick);
        selected_btn = (Button) findViewById(R.id.four_btn);
        selected_btn.setOnLongClickListener(longClick);
        selected_btn = (Button) findViewById(R.id.five_btn);
        selected_btn.setOnLongClickListener(longClick);
        selected_btn = (Button) findViewById(R.id.six_btn);
        selected_btn.setOnLongClickListener(longClick);
        selected_btn = (Button) findViewById(R.id.seven_btn);
        selected_btn.setOnLongClickListener(longClick);
        selected_btn = (Button) findViewById(R.id.eight_btn);
        selected_btn.setOnLongClickListener(longClick);
        selected_btn = (Button) findViewById(R.id.nine_btn);
        selected_btn.setOnLongClickListener(longClick);

    }

    View.OnLongClickListener longClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Button selected_btn = (Button) view;
            getButtonForMultiple().setTextColor(Color.BLACK);

            if (!selected_btn.getText().toString().equals("Del"))
                multiple = Integer.parseInt(selected_btn.getText().toString());

            selected_btn.setTextColor(Color.RED);
            selected_btn = (Button) findViewById(R.id.compute_btn);
            selected_btn.setText("@string/multiple " + multiple + "?");

            SharedPreferences settings = getSharedPreferences("MathPrefs", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("multiple", multiple);

            editor.apply();
            return true;
        }
    };

    public void setValue(View view) {
        Button selected_btn = (Button) view;
        EditText resultText = (EditText) findViewById(R.id.resultTxt);
        if (!resultText.getText().toString().equals("0")) {
            resultText.getText().append(selected_btn.getText());
        } else {
            resultText.setText(selected_btn.getText());
        }
    }

    public void clear(View view) {
        EditText resultText = (EditText) findViewById(R.id.resultTxt);
        resultText.getText().clear();
    }

    public void backspace(View view) {
        EditText resultText = (EditText) findViewById(R.id.resultTxt);
        if (resultText.getText().length() > 0) {
            resultText.getText().delete(resultText.getText().length() - 1, resultText.getText().length());
        } else if (resultText.getText().length() == 0) {
            clear(view);
        }
    }

    public Button getButtonForMultiple() {
        Button selected_btn = null;
        switch (multiple) {
            case 1:
                selected_btn = (Button) findViewById(R.id.one_btn);
                break;
            case 2:
                selected_btn = (Button) findViewById(R.id.two_btn);
                break;
            case 3:
                selected_btn = (Button) findViewById(R.id.three_btn);
                break;
            case 4:
                selected_btn = (Button) findViewById(R.id.four_btn);
                break;
            case 5:
                selected_btn = (Button) findViewById(R.id.five_btn);
                break;
            case 6:
                selected_btn = (Button) findViewById(R.id.six_btn);
                break;
            case 7:
                selected_btn = (Button) findViewById(R.id.seven_btn);
                break;
            case 8:
                selected_btn = (Button) findViewById(R.id.eight_btn);
                break;
            case 9:
                selected_btn = (Button) findViewById(R.id.nine_btn);
                break;
        }
        return selected_btn;
    }

    public void calculateMultiple(View view) {

        EditText resultText = (EditText) findViewById(R.id.resultTxt);

        if (!resultText.getText().toString().equals("")) {
            int number = Integer.parseInt(resultText.getText().toString());

            if (number % multiple == 0)
                Toast.makeText(getApplicationContext(), number + getResources().getString(R.string.correct)+ multiple + "!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), number + getResources().getString(R.string.incorrect) + multiple + ".", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.please), Toast.LENGTH_SHORT).show();
        }
    }
}