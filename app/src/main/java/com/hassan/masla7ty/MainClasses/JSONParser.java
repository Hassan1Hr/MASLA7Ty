package com.hassan.masla7ty.MainClasses;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.List;


public class JSONParser {

    public JSONObject makeHttpRequest(String url, List<NameValuePair> pairs)
    {
        try
        {
            HttpParams httpParams = new BasicHttpParams();

            int timeoutConnection = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);



            DefaultHttpClient defaultHttpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(url);

            if (pairs != null)
            {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs,"UTF-8"));
            }

            HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null)
            {
                String ret = EntityUtils.toString(httpEntity);
                return new JSONObject(ret.substring(ret.indexOf("{"), ret.lastIndexOf("}") + 1));
            }
        }
        catch (Exception ex)
        {

        }
        return null;
    }
}
