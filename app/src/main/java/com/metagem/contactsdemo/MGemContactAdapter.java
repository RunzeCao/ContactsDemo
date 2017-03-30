package com.metagem.contactsdemo;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
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
        Bitmap bitmap = loadContactPhoto(context, Uri.parse(mGemContacts.get(position).getPhotoUrl()), 0);
        viewHolder.circleImageView.setImageBitmap(bitmap);
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

    private OnItemClickListener mOnItemClickListener;

    void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    interface OnItemClickListener {
        void onItemLongClick(View view, int position);
    }

    private Bitmap loadContactPhoto(Context context, Uri imageUri, int imageSize) {
        AssetFileDescriptor afd = null;
        try {
            afd = context.getContentResolver().openAssetFileDescriptor(imageUri, "r");
            if (afd != null) {
                return decodeSampledBitmapFromDescriptor(afd.getFileDescriptor(), imageSize, imageSize);
            }
        } catch (FileNotFoundException e) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.no_photo);
        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromDescriptor(
            FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            final float totalPixels = width * height;

            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
}
