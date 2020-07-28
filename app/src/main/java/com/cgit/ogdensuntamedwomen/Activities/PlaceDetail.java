package com.cgit.ogdensuntamedwomen.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cgit.ogdensuntamedwomen.CgitListener;
import com.cgit.ogdensuntamedwomen.R;
import com.cgit.ogdensuntamedwomen.adapters.PlaceContentAdapter;
import com.cgit.ogdensuntamedwomen.adapters.PlacesAdapter;
import com.cgit.ogdensuntamedwomen.model.CSVFile;
import com.cgit.ogdensuntamedwomen.model.PlaceContent;
import com.cgit.ogdensuntamedwomen.model.Places;
import com.cgit.ogdensuntamedwomen.womenFactory;
import com.cgit.ogdensuntamedwomen.womenViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PlaceDetail extends AppCompatActivity implements CgitListener {

    private static final String TAG = PlaceDetail.class.getSimpleName();
    ImageView placeImage;
    TextView title,description;
    RecyclerView recyclerView;
    ArrayList<PlaceContent> arrayList = new ArrayList<>();
    PlaceContentAdapter adapter;
    Places places;
    MediaPlayer mediaPlayer;
    public static int lastIndex=-1;
    womenViewModel viewModel;
    public static int listPos =-1;
    public static int seekBarPosition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        if (savedInstanceState != null){
            listPos = savedInstanceState.getInt("listPos");
            seekBarPosition = savedInstanceState.getInt("seekBarPos");
            Log.i(TAG,listPos+" in saved");
            setUpRecyclerView();
        }else {
            Log.i(TAG,listPos+" in saved else");
            setUpRecyclerView();
        }

        viewModel = new ViewModelProvider(this,new womenFactory(this,places)).get(womenViewModel.class);
        viewModel.getMutableLiveData().observe(this, new Observer<ArrayList<PlaceContent>>() {
            @Override
            public void onChanged(ArrayList<PlaceContent> placeContents) {
                Log.i(TAG,"changed");
                arrayList.clear();
                arrayList.addAll(placeContents);
                adapter.notifyDataSetChanged();
                adapter.setCgitListener(PlaceDetail.this);
            }
        });


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
        int id=getResources().getIdentifier(imageName[0].toLowerCase(), "drawable", getPackageName());
        placeImage.setImageResource(id);
        title.setText(places.getTitle());
        description.setText(places.getDescription());
        mediaPlayer=new MediaPlayer();

    }

    //Get list of strings from CSV ready to use

    private void setUpRecyclerView(){
        arrayList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new PlaceContentAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       if (item.getItemId()== android.R.id.home){
          onBackPressed();
       }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG,"back pressed");
    }


    @Override
    public void onClicked(int listPosition) {
        Log.i(TAG, String.valueOf(listPosition)+"pos");
        listPos = listPosition;
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"stopped");
        adapter.onstop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listPos = -1;
        seekBarPosition = -1;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        seekBarPosition=adapter.getCurrentPosition();
        Log.i(TAG,"in out state");
        Log.i(TAG, seekBarPosition + "seek bar position");
        outState.putInt("listPos",listPos);
        outState.putInt("seekBarPos",seekBarPosition);
    }

}