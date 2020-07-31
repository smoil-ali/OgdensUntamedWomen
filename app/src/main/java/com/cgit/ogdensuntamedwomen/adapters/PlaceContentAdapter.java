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

public class PlaceContentAdapter extends RecyclerView.Adapter<PlaceContentAdapter.ViewHolder>   {
    Context context;
    ArrayList<PlaceContent> placeContentArrayList;
    MediaPlayer mediaPlayer=null;
    Runnable runnable;
    Handler handler;
    CgitListener cgitListener;
    int ResumePosition = -1;


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
        if (PlaceDetail.listPos == position && placeContent.isPlaying()){
            resume(position,placeContent,holder);
        }else if (PlaceDetail.listPos == position){
            String [] audioname=placeContent.getAudio().split("\\.");
            String uriPath = "android.resource://" + context.getPackageName() + "/raw/" +audioname[0];
            Uri uri = Uri.parse(uriPath);
            mediaPlayer=MediaPlayer.create(context,uri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            int fileduration=mediaPlayer.getDuration();
            holder.audiofile.setMax(fileduration);
            holder.audiofile.setProgress(PlaceDetail.seekBarPosition);
            mediaPlayer.seekTo(PlaceDetail.seekBarPosition);
        }

        if (placeContent.getAudio()==null){
            holder.start.setVisibility(View.GONE);
            holder.audiofile.setVisibility(View.GONE);
        }else{

            if (placeContent.isPlaying()){
                Log.i("adapter","pause"+position);
                holder.pause.setVisibility(View.VISIBLE);
                holder.start.setVisibility(View.GONE);
            }else {
                Log.i("adapter","start"+position);
                holder.start.setVisibility(View.VISIBLE);
                holder.pause.setVisibility(View.GONE);
            }
            holder.pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer!=null){
                        if (mediaPlayer.isPlaying()){
                            ResumePosition = mediaPlayer.getCurrentPosition();
                            holder.start.setVisibility(View.VISIBLE);
                            holder.pause.setVisibility(View.GONE);
                            placeContentArrayList.get(position).setPlaying(false);
                            mediaPlayer.pause();
                        }

                    }
                }
            });


            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("adapter",""+lastIndex);
                    int temp = lastIndex;
                    lastIndex=position;
                    if (lastIndex != -1 && temp!=position){
                        placeContentArrayList.get(lastIndex).setPlaying(false);
                        notifyItemChanged(lastIndex);
                    }
                    placeContentArrayList.get(position).setPlaying(true);
                    if (mediaPlayer!=null) {
                        if (ResumePosition != -1) {
                            mediaPlayer.seekTo(ResumePosition);
                            mediaPlayer.start();
                            holder.pause.setVisibility(View.VISIBLE);
                            holder.start.setVisibility(View.GONE);
                            cyclerplay(holder);
                        } else {
                            resume(position, placeContent, holder);
                        }
                    }else {
                        if (PlaceDetail.listPos == -1) {
                            cgitListener.onClicked(position);
                            String[] audioName = placeContent.getAudio().split("\\.");
                            String uriPath = "android.resource://" + context.getPackageName() + "/raw/" + audioName[0];
                            Uri uri = Uri.parse(uriPath);
                            mediaPlayer = MediaPlayer.create(context, uri);
                            try {
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                int fileDuration = mediaPlayer.getDuration();
                                Log.i(TAG, "in adater else 1 " + fileDuration);
                                holder.audiofile.setMax(fileDuration);
                                mediaPlayer.start();
                                cyclerplay(holder);
                                holder.pause.setVisibility(View.VISIBLE);
                                holder.start.setVisibility(View.GONE);
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer1) {
                                        holder.start.setVisibility(View.VISIBLE);
                                        holder.pause.setVisibility(View.GONE);
                                        holder.audiofile.setProgress(0);
                                        mediaPlayer.stop();
                                        mediaPlayer.reset();
                                        mediaPlayer.release();
                                        mediaPlayer = null;
                                    }
                                });

                            } catch (Exception e) {
                                Log.i("media file", e.getMessage());
                                Toast.makeText(context, "Audio not Available", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                }
            });

            holder.audiofile.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b){
                        ResumePosition = i;
                        mediaPlayer.seekTo(i);
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


    }

    public void cyclerplay(ViewHolder holder){
        handler=new Handler();
        if (mediaPlayer!=null){
            if (mediaPlayer.isPlaying()){
                Log.i(TAG,"here");
                 holder.audiofile.setProgress(mediaPlayer.getCurrentPosition());
                runnable=new Runnable(){
                    @Override
                    public void run() {
                        cyclerplay(holder);
                    }
                };
                handler.postDelayed(runnable,100);
            }
        }else {
            Log.i(TAG,"killed");
        }
    }

    public void resume(int position,PlaceContent placeContent,ViewHolder holder){
        Log.i(TAG,"in position "+position);
        String [] audioname=placeContent.getAudio().split("\\.");
        String uriPath = "android.resource://" + context.getPackageName() + "/raw/" +audioname[0];
        Uri uri = Uri.parse(uriPath);
        if (mediaPlayer!=null){
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        mediaPlayer=MediaPlayer.create(context,uri);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        int fileduration=mediaPlayer.getDuration();
        holder.audiofile.setMax(fileduration);
        Log.i(TAG,PlaceDetail.seekBarPosition+"");
        holder.audiofile.setProgress(PlaceDetail.seekBarPosition);
        mediaPlayer.seekTo(PlaceDetail.seekBarPosition);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer1) {
                holder.start.setVisibility(View.VISIBLE);
                holder.pause.setVisibility(View.GONE);
                holder.audiofile.setProgress(0);
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
                PlaceDetail.listPos = -1;
                PlaceDetail.seekBarPosition = -1;
            }
        });
        holder.pause.setVisibility(View.VISIBLE);
        holder.start.setVisibility(View.GONE);
        cyclerplay(holder);
    }

    public int getCurrentPosition(){
        if (mediaPlayer != null){
            Log.i(TAG,"in adapter else 2"+mediaPlayer.getCurrentPosition());
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
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
