package com.hassan.masla7ty.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.hassan.masla7ty.mainclasses.Service;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.activities.ServiceDetailsActivity;
import com.hassan.masla7ty.pojo.MyApplication;
import com.hassan.masla7ty.pojo.RecyclerClick;

import java.util.ArrayList;


/**
 * Created by Hassan on 22-12-2014.
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder>{
    static Context mContext;
    View itemView;
    private ArrayList<Service> mData;
    private LruCache<String,Bitmap> imageCache;
    private RequestQueue imagequeue;
    Service mService;
    public ServiceAdapter(Context mContext, ArrayList<Service> mData) {
        //super();
        this.mContext = mContext;
        this.mData = mData;
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/8;
        imageCache = new LruCache<>(cacheSize);
        imagequeue = Volley.newRequestQueue(MyApplication.getAppContext());
        mService = null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.servicelayout,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(itemView, new RecyclerClick() {
            @Override
            public void item(View caller,int position) {
                mService = mData.get(position);
                Intent intent = new Intent(mContext,ServiceDetailsActivity.class);
                intent.putExtra("serviceId",mService.getEnterpriseId());
                intent.putExtra("serviceName",mService.getEnterpriseName());
                intent.putExtra("serviceDistance",mService.getDistance());
                intent.putExtra("serviceURl",mService.getEnterpriseImage());
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        mService = mData.get(position);

        holder.service.setText(mService.getEnterpriseName());
        String url = mService.getEnterpriseImage();
        String dis = mService.getDistance();
        holder.distance.setText(dis);
        Bitmap userPhoto = imageCache.get(mService.getEnterpriseId());
        if(((url != "")) && ((url!=null))) {
            if (userPhoto != null) {
                holder.serviceImage.setImageBitmap(userPhoto);
            } else {

                ImageRequest request = new ImageRequest(url,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                holder.serviceImage.setImageBitmap(bitmap);
                                imageCache.put(mService.getEnterpriseId(), bitmap);
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



    public final  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView service;
        TextView distance;
        public RecyclerClick mListener;
        com.pkmmte.view.CircularImageView serviceImage;
        public MyViewHolder(View itemView,RecyclerClick listener)
        { super(itemView);
            mListener = listener;

            service = (TextView)itemView.findViewById(R.id.service_name);
            distance = (TextView)itemView.findViewById(R.id.service_distance);

            serviceImage = (com.pkmmte.view.CircularImageView)itemView.findViewById(R.id.service_image);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            int position =getAdapterPosition();
            mListener.item(view , position);




        }
    }
}
