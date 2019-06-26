package sk.libco.bestiaryfive.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sk.libco.bestiaryfive.Bestiaries;
import sk.libco.bestiaryfive.R;
import sk.libco.bestiaryfive.ui.adapter.BestiaryAdapter;

public class ImportActivity extends AppCompatActivity implements Bestiaries.BestiaryEvent {

    private static final String TAG = "ImportActivity";

    BestiaryAdapter recyclerViewAdapter;

    private Bestiaries bestiaries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        bestiaries = new Bestiaries(this, this);
        //

        setContentView(R.layout.activity_import);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.button_import_bestiary);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "add new bestiary");
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                startActivityForResult(intent, 42);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //

        //View view = inflater.inflate(R.layout.fragment_monsterlist_list, container, false);
        RecyclerView recyclerView = findViewById(R.id.import_bestiaries_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewAdapter = new BestiaryAdapter(bestiaries);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    public void onBestiaryChange() {
        if (recyclerViewAdapter != null) {
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSelectedBestiaryChange() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 42) {
            if (resultCode == Activity.RESULT_OK) {
                Uri pathUri = data.getData();
                //Log.d(TAG,"ActivityResult: " + pathUri.toString());

                // Parse monsters from file
                new Bestiaries.ParseTask(bestiaries).execute(pathUri);

            }
            //if (resultCode == Activity.RESULT_CANCELED) {
            //    //Write your code if there's no result
            //}
        }

    }


}
