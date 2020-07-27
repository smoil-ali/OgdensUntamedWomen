package com.cgit.ogdensuntamedwomen.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cgit.ogdensuntamedwomen.Activities.PlaceDetail;
import com.cgit.ogdensuntamedwomen.CgitListener;
import com.cgit.ogdensuntamedwomen.Constants;
import com.cgit.ogdensuntamedwomen.R;
import com.cgit.ogdensuntamedwomen.model.PlaceContent;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.zip.Inflater;

import static android.content.ContentValues.TAG;
import static com.cgit.ogdensuntamedwomen.Activities.PlaceDetail.lastIndex;

public class PlaceContentAdapter extends RecyclerView.Adapter<PlaceContentAdapter.ViewHolder> {
    Context context;
    ArrayList<PlaceContent> placeContentArrayList;
    MediaPlayer mediaPlayer=null;
    Runnable runnable;
    Handler handler;
    CgitListener cgitListener;


    public PlaceContentAdapter(Context context, ArrayList<PlaceContent> placeContentArrayList) {
        this.context = context;
        this.placeContentArrayList = placeContentArrayList;
    }

    public void setCgitListener(CgitListener cgitListener){
        this.cgitListener = cgitListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_place_content,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaceContent placeContent=placeContentArrayList.get(position);
        holder.title.setText(placeContent.getTitle());
        holder.description.setText(placeContent.getDescription());
        if (mediaPlayer != null){

        }else {
            holder.pause.setVisibility(View.GONE);
            holder.start.setVisibility(View.VISIBLE);
            String [] audioname=placeContent.getAudio().split("\\.");
            Log.i("audioname","check "+audioname[0]);
            String uriPath = "android.resource://" + context.getPackageName() + "/raw/" +audioname[0];
            Uri uri = Uri.parse(uriPath);
        }


    }

    public void cyclerplay(ViewHolder holder){
        handler=new Handler();
        if (mediaPlayer!=null){
            if (mediaPlayer.isPlaying()){
                Log.i("seekbar", String.valueOf(mediaPlayer.getCurrentPosition()));
                Constants.seekbarValue = mediaPlayer.getCurrentPosition();
            holder.audiofile.setProgress(mediaPlayer.getCurrentPosition());
                runnable=new Runnable(){
                    @Override
                    public void run() {
                        cyclerplay(holder);
                    }
                };
                handler.postDelayed(runnable,100);
            }
        }

    }


    @Override
    public int getItemCount() {
        return placeContentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,description,audioduration;
        ImageButton start,pause;
        SeekBar audiofile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.titleContent);
            description=itemView.findViewById(R.id.descriptionContent);
            start=itemView.findViewById(R.id.start);
            audiofile=itemView.findViewById(R.id.audiofile);
            pause=itemView.findViewById(R.id.pause);
            audioduration=itemView.findViewById(R.id.audioDuration);
        }
    }


    public void onstop(){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
            handler=null;
        }



    }

}
