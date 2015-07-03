package com.hassan.masla7ty.adapters;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.hassan.masla7ty.MainClasses.Friend;

import java.util.ArrayList;

import com.hassan.masla7ty.R;


/**
 * Created by Hassan on 6/25/2015.
 */
public class UserFriendsAdapter extends RecyclerView.Adapter<UserFriendsAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Friend> mData;
    private LruCache<String, Bitmap> imageCache;
    private RequestQueue imagequeue;

    public UserFriendsAdapter(Context mContext, ArrayList<Friend> mData) {
        //super();
        this.mContext = mContext;
        this.mData = mData;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);
        imagequeue = Volley.newRequestQueue(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_friends, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Friend friend = mData.get(position);

        String firstName = friend.getFirstName();
        String lastName = friend.getLastName();
        String friendName = firstName + " " + lastName;

        holder.friend.setText(friendName);
        if(friend.getStatus() ==1) {

            holder.addFriend.setText("Freind");
            holder.addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "you are already friends", Toast.LENGTH_LONG).show();

                    holder.addFriend.setAlpha((float) 0.90);
                }
            });
        }else {
            holder.addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "message send to be friends", Toast.LENGTH_LONG).show();
                    holder.addFriend.setText("wait request");
                    holder.addFriend.setAlpha((float) 0.90);
                }
            });
        }




        String url = friend.getFriendImage();

        Bitmap userPhoto = imageCache.get(friend.getFriendId());
        if ((!url.equals("")) && (!url.equals(null))) {
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


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public final static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView friend;

        Button addFriend;
        com.pkmmte.view.CircularImageView friendImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            friend = (TextView) itemView.findViewById(R.id.friendName);
            addFriend = (Button) itemView.findViewById(R.id.addFriend);
            friendImage = (com.pkmmte.view.CircularImageView) itemView.findViewById(R.id.friendImage);

        }

        @Override
        public void onClick(View v) {

        }
    }
}


