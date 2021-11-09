package fr.cytech.b3.tp3;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment {

    Button btnUserGuide;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        btnUserGuide = view.findViewById(R.id.btnUserGuide);
        btnUserGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View formElementsView = inflater.inflate(R.layout.userguide, null, false);
                //Create a dialog
                new AlertDialog.Builder(context)
                        .setView(formElementsView)
                        .setTitle(R.string.userguide_title)
                        .setPositiveButton(R.string.close,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .show();
            }
        });

        return inflater.inflate(R.layout.fragment_2, container, false);
    }

}
