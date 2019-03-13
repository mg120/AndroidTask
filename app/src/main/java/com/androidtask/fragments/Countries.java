package com.androidtask.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtask.R;
import com.androidtask.adapter.CountriesAdapter;
import com.androidtask.model.CountriesModel;
import com.androidtask.networking.ApiClient;
import com.androidtask.networking.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Countries extends Fragment {
    ProgressBar progressBar;
    TextView no_data;
    EditText search_ed;
    RecyclerView countries_recycler;
    CountriesAdapter adapter;

    List<CountriesModel> countriesList = new ArrayList<CountriesModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_countries, container, false);
        countries_recycler = view.findViewById(R.id.counries_recycler_id);
        no_data = view.findViewById(R.id.no_data_text_view);
        progressBar = view.findViewById(R.id.countries_progress_id);
        search_ed = view.findViewById(R.id.search_ed_id);

        // set LayoutManager for Recycler
        countries_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        countries_recycler.setHasFixedSize(true);
        adapter = new CountriesAdapter(countriesList);
        countries_recycler.setAdapter(adapter);

        // To Add Dividers between recyclerView items
        countries_recycler.setItemAnimator(new DefaultItemAnimator());
        countries_recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // get Countries
        if (isNetworkAvailable())
            getCountries();
        else
            Toast.makeText(getActivity(), getString(R.string.no_connection), Toast.LENGTH_SHORT).show();

        search_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString().toLowerCase();

                List<CountriesModel> filteredList = new ArrayList<CountriesModel>();
                for (int i = 0; i < countriesList.size(); i++) {
                    if (countriesList.get(i).getCapital().toLowerCase().contains(s) || countriesList.get(i).getCountryName().toLowerCase().contains(s)) {
                        filteredList.add(countriesList.get(i));
                    }
                }
                if (filteredList.isEmpty()) {
                    no_data.setVisibility(View.VISIBLE);
                    countries_recycler.setVisibility(View.GONE);
                } else {
                    countries_recycler.setVisibility(View.VISIBLE);
                    adapter.addCountriesList(filteredList);
                    no_data.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getCountries() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<CountriesModel>> call = apiService.getCountries();
        call.enqueue(new Callback<List<CountriesModel>>() {
            @Override
            public void onResponse(Call<List<CountriesModel>> call, Response<List<CountriesModel>> response) {
                countriesList = response.body();
                if (countriesList.isEmpty()) {
                    no_data.setVisibility(View.VISIBLE);
                    countries_recycler.setVisibility(View.GONE);
                } else {
                    countries_recycler.setVisibility(View.VISIBLE);
                    adapter.addCountriesList(countriesList);
                    no_data.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<CountriesModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
        /// if no network is available networkInfo will be null
        return networkInfo != null && networkInfo.isConnected();
    }
}
