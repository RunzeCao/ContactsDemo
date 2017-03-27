package com.metagem.contactsdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


class MGemContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<MGemContact> mGemContacts;

    MGemContactAdapter(Context context, List<MGemContact> mGemContacts) {
        this.context = context;
        this.mGemContacts = mGemContacts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        NormalViewHolder viewHolder = (NormalViewHolder) holder;
        viewHolder.textView.setText(mGemContacts.get(position).getName());
        viewHolder.circleImageView.setImageBitmap(mGemContacts.get(position).getPhoto());
        if (mOnItemClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(v, holder.getAdapterPosition());
                    return false;
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return mGemContacts.size();
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private CircleImageView circleImageView;

        NormalViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycle_tv);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.recycle_iv);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

     OnItemClickListener mOnItemClickListener;

    void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    interface OnItemClickListener {
        void onItemLongClick(View view, int position);
    }
}
