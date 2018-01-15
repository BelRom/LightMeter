package com.romanbel.lightmeter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by roman on 04.11.17.
 */

public class RecyclerDictionaryAdapter extends RecyclerView.Adapter<RecyclerDictionaryAdapter.ViewHolder> {

    private static ArrayList<Line> mList;
    private static Context mContext;


    public RecyclerDictionaryAdapter(ArrayList<Line> list, Context context) {
        mList = list;
        mContext = context;
    }

    @Override
    public RecyclerDictionaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mFirstItemTextView.setText(mList.get(position).getDate());
        holder.mSecondItemTextView.setText(mList.get(position).getLux());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void removeItem(int position) {
        mList.remove(position);

        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void remuveAll(){
        mList.clear();
        notifyDataSetChanged();
    }

    public void restoreItem(Line item, int position) {
        mList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mFirstItemTextView, mSecondItemTextView, mCountItemTextView;
        public RelativeLayout viewBackground;
        public RelativeLayout viewForeground;

        public ViewHolder(View v) {
            super(v);
            mFirstItemTextView = (TextView) v.findViewById(R.id.firstItemTextView);
            mSecondItemTextView = (TextView) v.findViewById(R.id.secondItemTextView);
            viewForeground = (RelativeLayout) v.findViewById(R.id.view_foreground);
            viewBackground = (RelativeLayout) v.findViewById(R.id.view_background);
        }
    }


}
