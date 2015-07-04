package com.hassan.masla7ty.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.hassan.masla7ty.MainClasses.Friend;

import java.util.ArrayList;

import com.hassan.masla7ty.R;


/**
 * Created by Hassan on 22-12-2014.
 */
public class FriendAdapters extends RecyclerView.Adapter<FriendAdapters.MyViewHolder>{
    private Context mContext;
    private ArrayList<Friend> mData;
    private LruCache<String,Bitmap> imageCache;
    private RequestQueue imagequeue;

    public FriendAdapters(Context mContext, ArrayList<Friend> mData) {
        //super();
        this.mContext = mContext;
        this.mData = mData;
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/8;
        imageCache = new LruCache<>(cacheSize);
        imagequeue = Volley.newRequestQueue(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlayout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Friend friend = mData.get(position);

        String firstName = friend.getFirstName();
        String lastName = friend.getLastName();
        String friendName = firstName+" "+lastName;
        int status = friend.getStatus();
        holder.friend.setText(friendName);
        String url = friend.getFriendImage();

        Bitmap userPhoto = imageCache.get(friend.getFriendId());
        if((url!="") && (url!=null)) {
            if (userPhoto != null) {
                holder.friendImage.setImageBitmap(userPhoto);
            } else {

                ImageRequest request = new ImageRequest(url,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                holder.friendImage.setImageBitmap(bitmap);
                                imageCache.put(friend.getFriendId(), bitmap);
                            }
                        },
                        80, 80,
                        Bitmap.Config.ARGB_8888,
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.d("userImage ", volleyError.getMessage());
                            }
                        }
                );
                imagequeue.add(request);
            }
        }
        if(status==1) {
            holder.status.setEnabled(true);
            holder.status.setChecked(true);
        }
        else
            holder.status.setEnabled(false);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    public final static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView friend;
        RadioButton status;
        com.pkmmte.view.CircularImageView friendImage;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            friend = (TextView)itemView.findViewById(R.id.friendName);
            status = (RadioButton)itemView.findViewById(R.id.userStatus);
            friendImage = (com.pkmmte.view.CircularImageView)itemView.findViewById(R.id.friendImage);

        }
    }
}
