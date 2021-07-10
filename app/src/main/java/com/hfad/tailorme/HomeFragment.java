package com.hfad.tailorme;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import Database.Customer;


public class HomeFragment extends Fragment {
    View view;
    RecyclerView inProgressRecycler;
    List<Customer> customerList;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> phoneNumbers = new ArrayList<String>();
    ArrayList<String> styles = new ArrayList<String>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view =  inflater.inflate(
                R.layout.fragment_home, container, false);



        return view;
    }



}