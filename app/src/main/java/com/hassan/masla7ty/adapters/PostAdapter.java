package com.hassan.masla7ty.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.activities.CommentActivity;
import com.hassan.masla7ty.fragments.PostFragment;
import com.hassan.masla7ty.MainClasses.Post;
import com.hassan.masla7ty.pojo.RecyclerClick;

import java.util.ArrayList;

/**
 * Created by Hassan on 10/21/2014.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<Post> mData;
    private LruCache<Integer,Bitmap> imageCache;
    private PostFragment postFragment;
    private RequestQueue imagequeue;

    public PostAdapter(Context mContext, ArrayList<Post> mData) {

        this.mContext = mContext;
        this.mData = mData;
        postFragment = PostFragment.newInstance();
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/8;
        imageCache = new LruCache<>(cacheSize);
        imagequeue = Volley.newRequestQueue(mContext);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_shape,parent,false);
        return new MyViewHolder(itemView);

    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        final Post postData = mData.get(position);


        String username = postData.getmNewsFirstName()+"  "+postData.getmNewsLastName();
        holder.userName.setText(username);

        holder.date.setText(postData.getmNewsDate());

        holder.body.setText(postData.getmNewsBody());


        String url = postData.getPhoto();
        if((url!=null)&& url!="") {


            Glide.with(mContext)
                   .load(url)
                    .into(holder.postImage);
        }

        Bitmap userPhoto = imageCache.get(postData.getPostId());
        if(userPhoto != null)
        {
            holder.UserImage.setImageBitmap(userPhoto);
        }
        else
        {
            String userURl = postData.getUserPhoto();
            ImageRequest request = new ImageRequest(userURl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            holder.UserImage.setImageBitmap(bitmap);
                            imageCache.put(postData.getPostId(),bitmap);
                        }
                    },
                    80,80,
                    Bitmap.Config.ARGB_8888,
                    new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("userImage ",volleyError.getMessage());
                        }
                    }
            );
            imagequeue.add(request);
        }




    }
    public final class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView userName;
        com.pkmmte.view.CircularImageView UserImage;
        TextView date;
        public RecyclerClick mListener;
        TextView body;
        ImageView postImage;
        Button like;
        Button unlike;
        Button comment;


        public MyViewHolder(View itemView)
        {

            super(itemView);

            userName = (TextView)itemView.findViewById(R.id.news_title_tv);
            UserImage = (com.pkmmte.view.CircularImageView)itemView.findViewById(R.id.userImage);
            date = (TextView)itemView.findViewById(R.id.news_date_tv);
            body = (TextView)itemView.findViewById(R.id.news_body);
            postImage = (ImageView)itemView.findViewById(R.id.postImage);
             like = (Button)itemView.findViewById(R.id.like);
            unlike = (Button)itemView.findViewById(R.id.unlike);
            comment = (Button)itemView.findViewById(R.id.comment);
            comment.setOnClickListener(this);
            like.setOnClickListener(this);
            unlike.setOnClickListener(this);




        }

        @Override
        public void onClick(View v) {
            int position =getAdapterPosition();
            Post postData = mData.get(position);
            if(v == comment)
            {
                Intent intent = new Intent().setClass(v.getContext(), CommentActivity.class);
                intent.putExtra("postId",postData.getPostId());
                v.getContext().startActivity(intent);
            }
            else
                if(v == like)
                {
                   like.setBackgroundColor(Color.BLUE);
                }
            else
                    unlike.setBackgroundColor(Color.BLUE);

        }
    }
}
