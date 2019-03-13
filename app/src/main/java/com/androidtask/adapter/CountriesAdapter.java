package com.androidtask.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidtask.R;
import com.androidtask.model.CountriesModel;

import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> {

    List<CountriesModel> list;

    public CountriesAdapter(List<CountriesModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CountriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.country_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesAdapter.ViewHolder viewHolder, int position) {

        viewHolder.countryName.setText(list.get(position).getCountryName());
        viewHolder.capitalName.setText(list.get(position).getCapital());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addCountriesList(List<CountriesModel> countriesList) {
        this.list = countriesList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView countryName, capitalName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.country_name_txt);
            capitalName = itemView.findViewById(R.id.capital_name_txt);
        }
    }
}
