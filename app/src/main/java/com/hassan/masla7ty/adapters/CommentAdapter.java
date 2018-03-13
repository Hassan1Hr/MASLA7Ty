package com.hassan.masla7ty.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.hassan.masla7ty.mainclasses.FriendComment;
import com.hassan.masla7ty.R;

import java.util.ArrayList;

/**
 * Created by Hassan on 6/28/2015.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<FriendComment> mData;
    private LruCache<String,Bitmap> imageCache;
    private RequestQueue imagequeue;

    public CommentAdapter(Context mContext, ArrayList<FriendComment> mData) {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_shape,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FriendComment comment = mData.get(position);

        String firstName = comment.getFirstName();
        String lastName = comment.getLastName();
        String friendName = firstName+" "+lastName;
        String commentstr =comment.getComment();
        holder.friend.setText(friendName);
        holder.comment.setText(commentstr);
        holder.date.setText(comment.getDate());
        String url = comment.getFriendImage();

        Bitmap userPhoto = imageCache.get(comment.getFriendId());
        if((!(url=="")) && (!(url==null))) {
            if (userPhoto != null) {
                holder.friendImage.setImageBitmap(userPhoto);
            } else {

                ImageRequest request = new ImageRequest(url,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                holder.friendImage.setImageBitmap(bitmap);
                                imageCache.put(comment.getFriendId(), bitmap);
                            }
                        },
                        80, 80,
                        Bitmap.Config.ARGB_8888,
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

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



    public final static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView friend;
        TextView comment;
        TextView date;
        com.pkmmte.view.CircularImageView friendImage;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            friend = (TextView)itemView.findViewById(R.id.userCommentName);
            date = (TextView)itemView.findViewById(R.id.commentDate);
            comment = (TextView)itemView.findViewById(R.id.commentBody);
            friendImage = (com.pkmmte.view.CircularImageView)itemView.findViewById(R.id.userCommentImage);

        }
    }

}
