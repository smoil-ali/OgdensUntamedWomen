package com.cgit.ogdensuntamedwomen.adapters;

import android.content.ContentResolver;
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
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cgit.ogdensuntamedwomen.Activities.MainActivity;
import com.cgit.ogdensuntamedwomen.Activities.PlaceDetail;
import com.cgit.ogdensuntamedwomen.CgitListener;
import com.cgit.ogdensuntamedwomen.Constants;
import com.cgit.ogdensuntamedwomen.Listener.addCallbackListener;
import com.cgit.ogdensuntamedwomen.R;
import com.cgit.ogdensuntamedwomen.model.PlaceContent;
import com.google.android.material.tabs.TabLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.zip.Inflater;

import static android.content.ContentValues.TAG;
import static com.cgit.ogdensuntamedwomen.Activities.PlaceDetail.lastIndex;
import static com.cgit.ogdensuntamedwomen.Activities.PlaceDetail.listPos;

public class PlaceContentAdapter extends RecyclerView.Adapter<PlaceContentAdapter.ViewHolder>   {
    Context context;
    ArrayList<PlaceContent> placeContentArrayList;
    MediaPlayer mediaPlayer=null;
    Runnable runnable;
    Handler handler;
    CgitListener cgitListener;
    int ResumePosition = -1;
    Uri uri;
    int fileduration;
    boolean check=false;
    addCallbackListener listener;

    public void setListener(addCallbackListener listener) {
        this.listener = listener;
    }

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
        if (placeContent.getType().equals("audio")){
            if (PlaceDetail.listPos == position && placeContent.isPlaying()){
                resume(position,placeContent,holder);
            }else if (PlaceDetail.listPos == position){
                String [] audioname=placeContent.getAudio().split("\\.");
                String uriPath = "android.resource://" + context.getPackageName() + "/raw/" +audioname[0];
                Log.i("uripath",uriPath);
                uri = Uri.parse(uriPath);
                mediaPlayer=MediaPlayer.create(context,uri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                fileduration=mediaPlayer.getDuration();

                Log.i("file duration end"," "+fileduration);

                holder.audiofile.setMax(fileduration);
                holder.audiofile.setProgress(PlaceDetail.seekBarPosition);
                mediaPlayer.seekTo(PlaceDetail.seekBarPosition);
            }
            String [] audioname=placeContent.getAudio().split("\\.");
            listRaw(audioname[0],holder);

            if (placeContent.getAudio()==null||!check){
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
                                Log.i("uri path"," "+uri);
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
        }else if (placeContent.getType().equals("image")){
            holder.image.setVisibility(View.VISIBLE);
            holder.audiofile.setVisibility(View.GONE);
            holder.start.setVisibility(View.GONE);
            String[] imageName=placeContent.getAudio().split("\\.");
            int id=context.getResources().getIdentifier(imageName[0].toLowerCase(), "drawable", context.getPackageName());
            holder.image.setImageResource(id);
        }else if(placeContent.getType().equals("video")){
            holder.audiofile.setVisibility(View.GONE);
            holder.start.setVisibility(View.GONE);
            if (!placeContent.getAudio().isEmpty()){
                Log.i("video id"," "+placeContent.getAudio());
                holder.video.setVisibility(View.VISIBLE);

                listener.addLifeCycleCallBack(holder.video);
                holder.video.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(placeContent.getAudio(),0);
                    }
                });
            }

        }else if (placeContent.getType().equals("text")){
            holder.audiofile.setVisibility(View.GONE);
            holder.start.setVisibility(View.GONE);
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
        Log.i("file duration"," "+fileduration);
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
        ImageView image;
        YouTubePlayerView video;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.titleContent);
            description=itemView.findViewById(R.id.descriptionContent);
            start=itemView.findViewById(R.id.start);
            audiofile=itemView.findViewById(R.id.audiofile);
            pause=itemView.findViewById(R.id.pause);
            audioduration=itemView.findViewById(R.id.audioDuration);
            image=itemView.findViewById(R.id.image);
            video=itemView.findViewById(R.id.video);
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
    public void listRaw(String file,ViewHolder holder){
        Field[] fields=R.raw.class.getFields();
        for(int count=0; count < fields.length; count++){
           /* Log.i("Raw Asset: ", fields[count].getName());
            Log.i("file: ", file);*/
            if (file.equals(fields[count].getName())){
                Log.i("Raw Asset: ", fields[count].getName());
                check=true;
            }

        }
    }
}
