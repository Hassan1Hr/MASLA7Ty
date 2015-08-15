package com.hassan.masla7ty.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.MainClasses.Post;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.activities.CommentActivity;
import com.hassan.masla7ty.fragments.PostFragment;
import com.hassan.masla7ty.pojo.ApplicationURL;
import com.hassan.masla7ty.pojo.MyApplication;
import com.hassan.masla7ty.pojo.RecyclerClick;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hassan on 10/21/2014.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Post> mData;
    private LruCache<Integer, Bitmap> imageCache;
    private PostFragment postFragment;
    private RequestQueue imagequeue;


    String username;

    public PostAdapter(Context mContext, ArrayList<Post> mData, String username) {

        this.mContext = mContext;
        this.mData = mData;
        this.username = username;
        postFragment = PostFragment.newInstance();
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);
        imagequeue = Volley.newRequestQueue(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_shape, parent, false);
        final Button comment = (Button) itemView.findViewById(R.id.comment);
        final Button unlike = (Button) itemView.findViewById(R.id.unlike);
        final Button like = (Button) itemView.findViewById(R.id.like);
        final TextView numOfLikes = (TextView) itemView.findViewById(R.id.numOfLikes);
        MyViewHolder viewHolder = new MyViewHolder(itemView, new RecyclerClick() {
            @Override
            public void item(View v,int position) {
                final Post postData = mData.get(position);
                if (v == comment && postData == mData.get(position) ) {
                    Intent intent = new Intent(mContext,CommentActivity.class);
                    intent.putExtra("postId", postData.getPostId());
                    v.getContext().startActivity(intent);
                } else if (v == like && postData == mData.get(position)) {
                    if(like.isClickable()==true){
                        like.setClickable(false);
                        unlike.setClickable(true);
                        final List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                        pairs.add(new BasicNameValuePair("username", username));
                        pairs.add(new BasicNameValuePair("postId", postData.getPostId() + ""));
                        pairs.add(new BasicNameValuePair("like", "1"));
                        postData.setNumOfLikes((postData.getNumOfLikes()) + 1);
                        numOfLikes.setText(postData.getNumOfLikes() + "");
                        if(postData.getLikes().equals(1+""))
                        {
                            like.setAlpha((float) 0.5);
                        }

                        new LikeTask().execute(pairs);}
                    else
                        Toast.makeText(MyApplication.getAppContext(),"press unlike if you unlike",Toast.LENGTH_LONG).show();


                } else {
                    if(unlike.isClickable()==true) {
                        unlike.setClickable(false);
                        like.setClickable(true);
                        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                        pairs.add(new BasicNameValuePair("username", username));
                        pairs.add(new BasicNameValuePair("postId", postData.getPostId() + ""));
                        pairs.add(new BasicNameValuePair("like", "0"));
                        postData.setNumOfLikes(postData.getNumOfLikes() - 1);
                        numOfLikes.setText(postData.getNumOfLikes() + "");
                        if(postData.getLikes().equals(0+""))
                        {
                            unlike.setAlpha((float) 0.5);
                        }

                        new UnLikeTask().execute(pairs);
                    }else
                        Toast.makeText(MyApplication.getAppContext(),"press like if you like ",Toast.LENGTH_LONG).show();



                }


            }
        });
        return viewHolder;

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


        String username = postData.getmNewsFirstName() + "  " + postData.getmNewsLastName();
        holder.userName.setText(username);

        holder.date.setText(postData.getmNewsDate());

        holder.body.setText(postData.getmNewsBody());
        holder.numOfLikes.setText(postData.getNumOfLikes() + "");

        String url = postData.getPhoto();
        if ((url != null) && url != "") {


            Glide.with(mContext)
                    .load(url)
                    .into(holder.postImage);
        }

        Bitmap userPhoto = imageCache.get(postData.getPostId());
        if (userPhoto != null) {
            holder.UserImage.setImageBitmap(userPhoto);
        } else {
            String userURl = postData.getUserPhoto();
            ImageRequest request = new ImageRequest(userURl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            holder.UserImage.setImageBitmap(bitmap);
                            imageCache.put(postData.getPostId(), bitmap);
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

    public final class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userName;
        com.pkmmte.view.CircularImageView UserImage;
        TextView date;
        public RecyclerClick mListener;
        TextView body;
        ImageView postImage;
        TextView numOfLikes;
        Button like;
        Button unlike;
        Button comment;


        public MyViewHolder(View itemView, RecyclerClick listener) {

            super(itemView);
            mListener = listener;
            userName = (TextView) itemView.findViewById(R.id.news_title_tv);
            UserImage = (com.pkmmte.view.CircularImageView) itemView.findViewById(R.id.userImage);
            date = (TextView) itemView.findViewById(R.id.news_date_tv);
            body = (TextView) itemView.findViewById(R.id.news_body);
            numOfLikes = (TextView) itemView.findViewById(R.id.numOfLikes);
            postImage = (ImageView) itemView.findViewById(R.id.postImage);
            like = (Button) itemView.findViewById(R.id.like);
            unlike = (Button) itemView.findViewById(R.id.unlike);
            comment = (Button) itemView.findViewById(R.id.comment);
            comment.setOnClickListener(this);
            like.setOnClickListener(this);
            unlike.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            final Post postData = mData.get(position);
            mListener.item(v, position);
        }


    }


        class LikeTask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
            private JSONObject jsonObjectResult = null;
            private JSONParser jsonParser = new JSONParser();
            String url = ApplicationURL.appDomain + "like.php";
            String error;

            @Override
            protected Boolean doInBackground(List<NameValuePair>... params) {
                jsonObjectResult = jsonParser.makeHttpRequest(url, params[0]);
                if (jsonObjectResult == null) {
                    error = "Error in the connection";
                    return false;

                } else {
                    return true;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                if (aBoolean) {
                    try {
                        if (jsonObjectResult.getInt("success") == 1) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
            }
        }
        class UnLikeTask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
            private JSONObject jsonObjectResult = null;
            private JSONParser jsonParser = new JSONParser();
            String url = ApplicationURL.appDomain + "like.php";
            String error;

            @Override
            protected Boolean doInBackground(List<NameValuePair>... params) {
                jsonObjectResult = jsonParser.makeHttpRequest(url, params[0]);
                if (jsonObjectResult == null) {
                    error = "Error in the connection";
                    return false;

                } else {
                    return true;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                if (aBoolean) {
                    try {
                        if (jsonObjectResult.getInt("success") == 1) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
            }
        }

    }




