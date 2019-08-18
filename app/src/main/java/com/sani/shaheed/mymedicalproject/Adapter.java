package com.sani.shaheed.mymedicalproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sani.shaheed.mymedicalproject.models.Medicine;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private List<Medicine> medList;
    private CustomItemClickListener listener;

    public Adapter(Context context, List<Medicine> medList, CustomItemClickListener listener){
        this.context = context;
        this.medList = medList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_layout, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter.ViewHolder holder, int position) {
        holder.medName.setText(medList.get(position).getName());
        holder.medDescription.setText(medList.get(position).getDescription());
        holder.theLetterText.setText(String.valueOf(medList.get(position).getName().charAt(0)).toUpperCase());
        holder.medCounting.setText(getCountOfDays(medList.get(position).getFinish_date(),
                medList.get(position).getStart_date()));
    }

    @Override
    public int getItemCount() {
        if (medList.isEmpty()){
            return 0;
        }
        return medList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final View mView;
        TextView medName, medDescription, theLetterText, medCounting;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            medName = mView.findViewById(R.id.medicationName);
            medDescription = mView.findViewById(R.id.medicationDescription);
            theLetterText = mView.findViewById(R.id.theLetterText);
            medCounting = mView.findViewById(R.id.medicationCounting);
        }
    }

    public String getCountOfDays(String startDate, String finishDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d", Locale.getDefault());
        Date s_date = null, f_date = null;
        long dayCount;

        try {
            s_date = dateFormat.parse(startDate);
            f_date = dateFormat.parse(finishDate);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (s_date != null && f_date != null){
            long diff = s_date.getTime() - f_date.getTime();
            dayCount = diff / (24 * 60 * 60 * 1000);
            return (dayCount + " Days");
        }else {
            return "No Difference";
        }
    }
}