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
import com.cgit.ogdensuntamedwomen.R;
import com.cgit.ogdensuntamedwomen.model.PlaceContent;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class PlaceContentAdapter extends RecyclerView.Adapter<PlaceContentAdapter.ViewHolder> {
    Context context;
    ArrayList<PlaceContent> placeContentArrayList;
    MediaPlayer mediaPlayer;
    Runnable runnable;
    Handler handler;

    public PlaceContentAdapter(Context context, ArrayList<PlaceContent> placeContentArrayList) {
        this.context = context;
        this.placeContentArrayList = placeContentArrayList;
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


        holder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.start.setVisibility(View.VISIBLE);
                holder.pause.setVisibility(View.GONE);
                mediaPlayer.pause();
            }
        });


        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] audioname=placeContent.getAudio().split("\\.");
                Log.i("audioname","check "+audioname[0]);
                String uriPath = "android.resource://" + context.getPackageName() + "/raw/" +audioname[0];
                Uri uri = Uri.parse(uriPath);

                mediaPlayer=MediaPlayer.create(context,uri);

                try {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    int fileduration=mediaPlayer.getDuration();
                    holder.audiofile.setMax(fileduration);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {

                            mediaPlayer.start();
                            cyclerplay(holder);
                            holder.pause.setVisibility(View.VISIBLE);
                            holder.start.setVisibility(View.GONE);
                        }
                    });

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            holder.pause.setVisibility(View.GONE);
                            holder.start.setVisibility(View.VISIBLE);
                        }
                    });
                }catch (Exception e){

                    Log.i("media file",e.getMessage());
                    Toast.makeText(context,"Audio not Available",Toast.LENGTH_LONG).show();
                }




                holder.audiofile.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser){
                            Log.i("seekbar","progress change");
                            mediaPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        });
    }

    public void cyclerplay(ViewHolder holder){
        handler=new Handler();
        holder.audiofile.setProgress(mediaPlayer.getCurrentPosition());
        Log.i("progress",String.valueOf(mediaPlayer.getCurrentPosition()));

        if (mediaPlayer.isPlaying()){
            runnable=new Runnable(){
                @Override
                public void run() {
                    cyclerplay(holder);
                }
            };
            handler.postDelayed(runnable,100);
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

}
