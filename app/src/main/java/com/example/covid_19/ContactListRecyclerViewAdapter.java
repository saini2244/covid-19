package com.example.covid_19;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.covid_19.ContactListFragment.OnListFragmentInteractionListener;
import com.example.covid_19.model.Contact.DummyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ContactListRecyclerViewAdapter extends RecyclerView.Adapter<ContactListRecyclerViewAdapter.ViewHolder> {

    private final JSONArray mValues;
    private final OnListFragmentInteractionListener mListener;

    public ContactListRecyclerViewAdapter(JSONArray items, OnListFragmentInteractionListener listener) {
        mValues = (JSONArray) items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contact_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = mValues.getJSONObject(position);
//            holder.mItem = jsonObject.getString("Company");
            holder.mIdView.setText("Country:- " +jsonObject.getString("Country"));
            holder.mContentView.setText("Ambulance:- " +jsonObject.getString("Ambulance"));
            holder.mIdView1.setText("Fire:- " +jsonObject.getString("Fire"));
            holder.mContentView1.setText("Police:- " +jsonObject.getString("Police"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView, mIdView1;
        public final TextView mContentView, mContentView1;
        public ExcelReadWrite.ExcelModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.state);
            mContentView = (TextView) view.findViewById(R.id.ambulance);
            mIdView1 = (TextView) view.findViewById(R.id.fire);
            mContentView1 = (TextView) view.findViewById(R.id.police);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
