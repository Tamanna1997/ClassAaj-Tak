package com.example.hp.classaajtaknotice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editTextName;
    Button buttonAdd;

    DatabaseReference databaseArtists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtists= FirebaseDatabase.getInstance().getReference("artists");

        editTextName=(EditText)findViewById(R.id.editTextName);
        buttonAdd=(Button)findViewById(R.id.buttonAddArtist);

        listViewArtists=(ListView) findViewById(R.id.listViewArists);


artistList=new ArrayList<>();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
             {
                addArtist();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                artistList.clear();
                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()){
                    Artist artist=artistSnapshot.getValue(Artist.class);
                    artistList.add(artist);
                }
                ArtistList adapter= new ArtistList(MainActivity.this,artistList);
                listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addArtist(){
        String name=editTextName.getText().toString().trim();


        if(!TextUtils.isEmpty(name)) {

            String id=databaseArtists.push().getKey();

            Artist artist = new Artist(id,name);

            databaseArtists.child(id).setValue(artist);

            Toast.makeText(this,"Notice Posted",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"You should enter the notice",Toast.LENGTH_LONG).show();
        }
    }
}
