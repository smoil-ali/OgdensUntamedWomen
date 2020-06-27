package com.cgit.ogdensuntamedwomen.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cgit.ogdensuntamedwomen.Activities.MainActivity;
import com.cgit.ogdensuntamedwomen.Activities.PlaceDetail;
import com.cgit.ogdensuntamedwomen.R;
import com.cgit.ogdensuntamedwomen.Utility.DistanceTraveledService;
import com.cgit.ogdensuntamedwomen.model.Places;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    Context context;
    ArrayList<Places> placesArrayList;

    public PlacesAdapter(Context context, ArrayList<Places> placesArrayList) {
        this.context = context;
        this.placesArrayList = placesArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_recyclerview,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Places places=placesArrayList.get(position);
        holder.title.setText(places.getTitle());
        holder.description.setText(places.getDescription());
        displayDistance(holder,places.getLang(),places.getLng());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, PlaceDetail.class);
                intent.putExtra("placedetail",places);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView title,description,distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.card);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            distance=itemView.findViewById(R.id.distance);
        }
    }

    private void displayDistance(ViewHolder holder,String lat, String lng) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double distance = 0;
                if(MainActivity.mDistanceTraveledService != null){
                    double latitude=Double.parseDouble(lat);
                    double lngitude=Double.parseDouble(lng);
                    distance = MainActivity.mDistanceTraveledService.getDistanceTraveled(latitude,lngitude)* 3.28084;
                    holder.distance.setText(String.valueOf(distance));
                    /*textView.setText(String.valueOf(distance));*/
                }
                handler.postDelayed(this, 100);
            }
        });
    }
}
