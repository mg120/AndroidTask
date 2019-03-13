package com.androidtask.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.androidtask.R;
import com.androidtask.adapter.ContactsAdapter;
import com.androidtask.model.ContactsModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class Contacts extends Fragment {

    TextView no_contacts;
    ListView contacts_list;
    ContactsAdapter adapter;

    List<ContactsModel> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        no_contacts = view.findViewById(R.id.no_contacts_txt_id);
        contacts_list = view.findViewById(R.id.contacts_list_id);
        // set LayoutManager for Recycler
//        contacts_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//        contacts_recycler.setHasFixedSize(true);
//        adapter = new ContactsAdapter(list);
//        contacts_recycler.setAdapter(adapter);
//
//        // To Add Dividers between recyclerView items
//        contacts_recycler.setItemAnimator(new DefaultItemAnimator());
//        contacts_recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        loadContacts();
        return view;
    }

    private void getContactList() {
        list.clear();
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i(TAG, "Name: " + name);
                        Log.i(TAG, "Phone Number: " + phoneNo);

                        list.add(new ContactsModel(name, phoneNo));
                    }
                    pCur.close();
                }
            }
//            if (list.isEmpty()) {
//                no_contacts.setVisibility(View.VISIBLE);
//                contacts_recycler.setVisibility(View.GONE);
//            } else {
//                contacts_recycler.setVisibility(View.VISIBLE);
//                adapter.addCountriesList(list);
//                no_contacts.setVisibility(View.GONE);
//            }
        }
        if (cur != null) {
            cur.close();
        }
    }

    private void loadContacts() {
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        getActivity().startManagingCursor(cursor);
        String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone._ID};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, cursor, from, to);
        contacts_list.setAdapter(simpleCursorAdapter);
        contacts_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}
