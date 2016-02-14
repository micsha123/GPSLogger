package com.dukgo.copter.gpslogger.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dukgo.copter.gpslogger.R;
import com.dukgo.copter.gpslogger.activity.MapActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MainFragment extends Fragment {

    EditText editText;
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button addButton = (Button) rootView.findViewById(R.id.btn_add);
        Button shareButton = (Button) rootView.findViewById(R.id.btn_share);
        editText = (EditText) rootView.findViewById(R.id.et_text);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MapActivity.class);
                startActivityForResult(i, 0);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File myFile = new File("/sdcard/mygpsfile.txt");
                    myFile.createNewFile();
                    FileOutputStream out = new FileOutputStream(myFile);
                    OutputStreamWriter outWriter =
                            new OutputStreamWriter(out);
                    outWriter.append(editText.getText().toString());
                    outWriter.close();
                    out.close();
                    Toast.makeText(getActivity(), "Файл сформирован", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("application/txt");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myFile));
                    startActivity(Intent.createChooser(intent, "Отправить файл..."));
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == Activity.RESULT_OK) {
            editText.append(" " + data.getStringExtra("location"));

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
