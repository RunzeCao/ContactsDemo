package com.metagem.contactsdemo;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MGemContactAdapter extends RecyclerView.Adapter<MGemContactAdapter.ViewHolder> {
    private Context context;
    private List<MGemContact> mGemContacts;

    public MGemContactAdapter(Context context, List<MGemContact> mGemContacts) {
        this.context = context;
        this.mGemContacts = mGemContacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.textView.setText(mGemContacts.get(position).getName());
        holder.textView.setCompoundDrawables(null,new BitmapDrawable(mGemContacts.get(position).getPhoto()),null,null );
    }

    @Override
    public int getItemCount() {
        return mGemContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycle_tv);
        }
    }
}
