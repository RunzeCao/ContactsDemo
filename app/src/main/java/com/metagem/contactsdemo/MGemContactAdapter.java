package com.metagem.contactsdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


class MGemContactAdapter extends RecyclerView.Adapter<MGemContactAdapter.ViewHolder> {

    public static final int ITEM_ADD = 1;
    public static final int ITEM_NORMAL = 2;
    private Context context;
    private List<MGemContact> mGemContacts;

    MGemContactAdapter(Context context, List<MGemContact> mGemContacts) {
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
        holder.imageView.setImageBitmap(mGemContacts.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return mGemContacts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private CircleImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycle_tv);
            imageView = (CircleImageView) itemView.findViewById(R.id.recycle_iv);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /*   public void setData(List<MGemContact> list){
        mGemContacts.addAll(list);
        notifyDataSetChanged();
    }*/

}
