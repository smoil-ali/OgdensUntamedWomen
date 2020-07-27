package com.cgit.ogdensuntamedwomen.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cgit.ogdensuntamedwomen.Activities.MainActivity;
import com.cgit.ogdensuntamedwomen.Activities.PlaceDetail;
import com.cgit.ogdensuntamedwomen.R;
import com.cgit.ogdensuntamedwomen.Utility.DistanceTraveledService;
import com.cgit.ogdensuntamedwomen.compass.Compass;
import com.cgit.ogdensuntamedwomen.model.Places;
import com.cgit.ogdensuntamedwomen.model.demo;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> implements Compass.CompassListener {
    Context context;
    ArrayList<Places> placesArrayList;
    float currentAzimuth;
    Compass compass;
    float data,temp;
    String tag;
    demo democlass ;

    public PlacesAdapter(Context context, ArrayList<Places> placesArrayList,String tag) {
        this.context = context;
        this.placesArrayList = placesArrayList;
        this.tag=tag;

        compass = new Compass(context);
        compass.setListener(PlacesAdapter.this);
        compass.start();


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (tag.equals("compass")){
             v= LayoutInflater.from(context).inflate(R.layout.navigation,parent,false);
        }else{
            v= LayoutInflater.from(context).inflate(R.layout.item_recyclerview,parent,false);
        }


        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (tag.equals("compass")){
            Places places=placesArrayList.get(position);
            double dist=Double.parseDouble(places.getDistance());
            int distanceInyard=(int)(dist*1760);
            int distanceInFeet=(int)dist*5280;
            String distance;
            if (dist<1) {
                if (distanceInyard > 30) {
                    distance = (int) (dist * 1760) + " Yd";
                    Log.i("distance", distance + " yd");
                    holder.distance.setText(distance);
                } else if (distanceInyard < 30) {
                    distance = (int) (dist * 5280) + " Ft";
                    Log.i("distancein", distanceInyard+ " ft");
                    holder.distance.setText(distance);
                }
            }else{
                distance=(int)dist+" ml";
                holder.distance.setText(distance);
            }




            if(MainActivity.locationInDegree != null ){
                Animation an = new RotateAnimation(-(currentAzimuth)+MainActivity.locationInDegree[position], -data,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
                currentAzimuth = (data);
                an.setDuration(500);
                an.setRepeatCount(0);
                an.setFillAfter(true);
                holder.arrowView.startAnimation(an);
            }
        }else{
            Places places=placesArrayList.get(position);
            holder.title.setText(places.getTitle());
            holder.description.setText(places.getDescription());
            holder.card.setEnabled(true);
            holder.cardRelativeLayout.setBackground(context.getDrawable(R.drawable.cardbackground));
            holder.cardRelativeLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.disabled));
            holder.card.setElevation(0);
            if (places.isNear()){
                Log.i("is near",places.getDistance()+String.valueOf(places.isNear()));
                if ((int)Double.parseDouble(places.getDistance())<MainActivity.nearestDestance){
                    Log.i("is near","greater 200");

                    holder.card.setEnabled(true);
                    holder.card.setElevation(6);
                    holder.cardRelativeLayout.setBackground(context.getDrawable(R.drawable.cardbackground));


                }
            }

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, PlaceDetail.class);
                    intent.putExtra("placedetail",places);
                    context.startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return placesArrayList.size();
    }

    @Override
    public void onNewAzimuth(float azimuth) {
        // adjustArrow(azimuth);
        Log.i("setupcompass","on new azimuth");
        data=azimuth;
        if (tag.equals("compass")){
            notifyDataSetChanged();
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView title,description,distance;
        RelativeLayout cardRelativeLayout;
        ImageView arrowView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.card);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            distance=itemView.findViewById(R.id.distance);
            cardRelativeLayout=itemView.findViewById(R.id.cardRelativeLayout);
            arrowView=itemView.findViewById(R.id.arrowView);
        }
    }
}
