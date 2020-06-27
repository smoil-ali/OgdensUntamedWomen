package com.cgit.ogdensuntamedwomen.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cgit.ogdensuntamedwomen.R;
import com.cgit.ogdensuntamedwomen.adapters.PlaceContentAdapter;
import com.cgit.ogdensuntamedwomen.adapters.PlacesAdapter;
import com.cgit.ogdensuntamedwomen.model.CSVFile;
import com.cgit.ogdensuntamedwomen.model.PlaceContent;
import com.cgit.ogdensuntamedwomen.model.Places;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PlaceDetail extends AppCompatActivity {

    ImageView placeImage;
    TextView title,description;
    RecyclerView recyclerView;
    ArrayList<PlaceContent> arrayList;
    PlaceContentAdapter adapter;
    Places places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        setUpRecyclerView();


    }

    private void init(){
        placeImage=findViewById(R.id.placeImage);
        title=findViewById(R.id.placeTitle);
        description=findViewById(R.id.description);
        recyclerView=findViewById(R.id.mRecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        places= (Places) getIntent().getExtras().getSerializable("placedetail");
        String[] imageName=places.getImage().split("\\.");
        Log.i("name",imageName[0]);
        int id=getResources().getIdentifier(imageName[0], "drawable", getPackageName());
        placeImage.setImageResource(id);
        title.setText(places.getTitle());
        description.setText(places.getDescription());

    }

    //Get list of strings from CSV ready to use
    private ArrayList<PlaceContent> prepArray() {

        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("LocationContent.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSVFile csvFile = new CSVFile(inputStream);
        ArrayList<PlaceContent> myList = csvFile.readPlacesContent(places.getId());
        Log.i("check title",myList.get(0).getTitle());
        return myList;


    }

    private void setUpRecyclerView(){
        arrayList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new PlaceContentAdapter(this,prepArray());
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       if (item.getItemId()== android.R.id.home){
          onBackPressed();
       }
        return super.onOptionsItemSelected(item);
    }
}