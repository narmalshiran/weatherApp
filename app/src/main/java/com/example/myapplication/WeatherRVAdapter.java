package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.VeiwHolder> {

    private Context context;
    private ArrayList<WetherRVModel>wetherRVModelsArrayList;

    public WeatherRVAdapter(Context context, ArrayList<WetherRVModel> wetherRVModels) {
        this.context = context;
        this.wetherRVModelsArrayList = wetherRVModels;
    }

    @Override
    public WeatherRVAdapter.VeiwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_items,parent,false);
      return new VeiwHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.VeiwHolder holder, int position) {
        WetherRVModel wetherRVModel = wetherRVModelsArrayList.get(position);
        holder.tempeture.setText(wetherRVModel.getTemperature()+"°C");

        Picasso.get().load("http".concat(wetherRVModel.getIcon())).into(holder.condition);
        holder.windspeed.setText(wetherRVModel.getWindSpeed()+"Km/h");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output= new SimpleDateFormat("hh:mm aa" );
            try {
                Date t = input.parse(wetherRVModel.getTime());
                holder.time.setText(output.format(t));
            }catch (ParseException e)
            {
                e.printStackTrace();
            }


    }

    @Override
    public int getItemCount() {
        return wetherRVModelsArrayList.size();
    }

    public class VeiwHolder extends RecyclerView.ViewHolder {
        private TextView time,tempeture,windspeed;
     private   ImageView condition;
        public VeiwHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.idTVtime);
            tempeture=itemView.findViewById(R.id.idTVTempeture);
            windspeed = itemView.findViewById(R.id.idTVWindSpeed);
            condition=itemView.findViewById(R.id.idIVCondition);

        }
    }
}
