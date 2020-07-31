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
        Log.i(TAG,PlaceDetail.listPos+" in adapter");

        if (PlaceDetail.listPos == position){
            Log.i(TAG,"in position "+position);
            String [] audioname=placeContent.getAudio().split("\\.");
            Log.i("audioname","check "+audioname[0]);
            String uriPath = "android.resource://" + context.getPackageName() + "/raw/" +audioname[0];
            Uri uri = Uri.parse(uriPath);
            mediaPlayer=MediaPlayer.create(context,uri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            int fileduration=mediaPlayer.getDuration();
            holder.audiofile.setMax(fileduration);
            Log.i(TAG,PlaceDetail.seekBarPosition+"");
            holder.audiofile.setProgress(PlaceDetail.seekBarPosition);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.seekTo(PlaceDetail.seekBarPosition);
                    mp.start();
                    cyclerplay(holder,mp);
                    holder.pause.setVisibility(View.VISIBLE);
                    holder.start.setVisibility(View.GONE);

                }
            });
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
                    placeContentArrayList.get(position).setPlaying(false);

                    if (mediaPlayer!=null){
                        if (mediaPlayer.isPlaying()){
                            holder.start.setVisibility(View.VISIBLE);
                            holder.pause.setVisibility(View.GONE);
                            mediaPlayer.pause();
                        }

                    }
                }
            });


            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("adapter",""+lastIndex);
                    if (lastIndex != -1){
                        placeContentArrayList.get(lastIndex).setPlaying(false);
                        notifyItemChanged(lastIndex);
                    }
                    cgitListener.onClicked(position);
                    lastIndex=position;
                    placeContentArrayList.get(position).setPlaying(true);
                    String [] audioname=placeContent.getAudio().split("\\.");
                    Log.i("audioname","check "+audioname[0]);
                    String uriPath = "android.resource://" + context.getPackageName() + "/raw/" +audioname[0];
                    Uri uri = Uri.parse(uriPath);
                    if (mediaPlayer!=null){
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            holder.pause.setVisibility(View.GONE);
                            holder.start.setVisibility(View.VISIBLE);

                        }
                        Log.i("check audio play","audio is playing");
                    }

                    mediaPlayer=MediaPlayer.create(context,uri);

                    try {
                        /* mediaPlayer.stop();*/

                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        int fileduration=mediaPlayer.getDuration();
                        holder.audiofile.setMax(fileduration);

                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                                cyclerplay(holder,mp);
                                holder.pause.setVisibility(View.VISIBLE);
                                holder.start.setVisibility(View.GONE);

                            }
                        });

                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                holder.pause.setVisibility(View.GONE);
                                holder.start.setVisibility(View.VISIBLE);
                                PlaceDetail.listPos = -1;
                                PlaceDetail.seekBarPosition = -1;
                                placeContentArrayList.get(position).setPlaying(false);
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
                                Log.i("seekbar",progress+"");
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


    }

    public void cyclerplay(ViewHolder holder,MediaPlayer mp){
        handler=new Handler();
        if (mp!=null){
            if (mp.isPlaying()){
            holder.audiofile.setProgress(mp.getCurrentPosition());
                runnable=new Runnable(){
                    @Override
                    public void run() {
                        cyclerplay(holder,mp);
                    }
                };
                handler.postDelayed(runnable,100);
            }
        }

    }

    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
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
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
            handler=null;
        }



    }

}
