package fr.cytech.b3.tp4;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_FILE_RESULT_CODE = 1;
    DatabaseAdapter databaseAdapter;
    ListView taskView;
    FloatingActionButton btnAdd;
    SimpleCursorAdapter adapter;
    Runnable updateView;
    SharedPreferences sp;
    Button btnBackup;
    String src;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnEditName) {
            updateName(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Shared Preferences
        sp = getApplicationContext().getSharedPreferences("mypref" , Context.MODE_PRIVATE);
        //Get name from Shared Preferences
        String name = sp.getString("MY_NAME", "Stefan");
        this.setTitle(name + "'s jobs");
        // Float button
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(v);
            }
        });
        // List view
        taskView = findViewById(R.id.taskView);
        databaseAdapter = new DatabaseAdapter(this);
        adapter = databaseAdapter.populateListViewFromDB();
        taskView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateView = new Runnable() {
            public void run() {
                //reload content
                adapter.changeCursor(databaseAdapter.reloadCursor());
                adapter.notifyDataSetChanged();
                taskView.invalidateViews();
                taskView.refreshDrawableState();
            }
        };
        taskView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteItem(view, (int) id);

                return false;
            }
        });
        // Backup
        btnBackup = findViewById(R.id.btnBackup);
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getRootView().getContext();
                //Create a dialog
                new AlertDialog.Builder(context)
                        .setTitle("Confirm")
                        .setMessage("Export your ToDo List?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // Export task
                                boolean exportSuccessful = databaseAdapter.exportDatabase();
                                if(exportSuccessful){
                                    Toast.makeText(context, "Export successfully.", Toast.LENGTH_SHORT).show();
                                    runOnUiThread(updateView);
                                }else{
                                    Toast.makeText(context, "Unable to export.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        btnBackup.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Context context = v.getRootView().getContext();
                //Create a dialog
                new AlertDialog.Builder(context)
                        .setTitle("Confirm")
                        .setMessage("Import into your ToDo List?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // Import task
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("text/comma-separated-values");
                                startActivityForResult(Intent.createChooser(intent, "Import:"), PICK_FILE_RESULT_CODE);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                            }
                        })
                        .show();

                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_FILE_RESULT_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    // File path contain garbage path before storage
                    Uri uri = data.getData();
                    if (databaseAdapter.importDatabase(uri)) {
                        Toast.makeText(this, "Import successfully.", Toast.LENGTH_SHORT).show();
                        runOnUiThread(updateView);
                    }
                }
        }
    }

    private void updateName(MainActivity activity) {
        Context context =  findViewById(R.id.taskView).getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View formElementsView = inflater.inflate(R.layout.add_form, null, false);
        EditText editTxt = formElementsView.findViewById(R.id.editText);

        //Create a dialog
        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("New Name: ")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                // Update new name
                                String name = editTxt.getText().toString();
                                activity.setTitle(name + "'s jobs");
                                // Save to Shared Preferences
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("MY_NAME", name);
                                editor.commit();
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void addItem(View view) {
        Context context = view.getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View formElementsView = inflater.inflate(R.layout.add_form, null, false);
        EditText editTxt = formElementsView.findViewById(R.id.editText);

        //Create a dialog
        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("New Task: ")
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                // Add new task to database
                                boolean createSuccessful = new DatabaseAdapter(context).create(editTxt.getText().toString());
                                if(createSuccessful){
                                    Toast.makeText(context, "New task was added successfully.", Toast.LENGTH_SHORT).show();
                                    runOnUiThread(updateView);
                                }else{
                                    Toast.makeText(context, "Unable to add new task.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteItem(View view, int id){
        Context context = view.getRootView().getContext();
        //Create a dialog
        new AlertDialog.Builder(context)
                .setTitle("Confirm")
                .setMessage("Delete this task?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Delete task
                        boolean deleteSuccessful = new DatabaseAdapter(context).delete(id);
                        if(deleteSuccessful){
                            Toast.makeText(context, "Task was deleted successfully.", Toast.LENGTH_SHORT).show();
                            runOnUiThread(updateView);
                        }else{
                            Toast.makeText(context, "Unable to delete task.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                })
                .show();
    }
}