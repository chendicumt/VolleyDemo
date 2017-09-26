package com.panda.volleydemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    private ImageView imageView;
    private NetworkImageView networkImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Logger.addLogAdapter(new AndroidLogAdapter());
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.d("---------->", "Max memory is " + maxMemory + "KB");

        imageView=(ImageView)findViewById(R.id.image);
        networkImageView=(NetworkImageView)findViewById(R.id.network_image);
//        RequestQueue是一个请求队列对象，它可以缓存所有的HTTP请求，然后按照一定的算法并发地发出这些请求。
//        RequestQueue内部的设计就是非常合适高并发的，因此我们不必为每一次HTTP请求都创建一个RequestQueue对象
//        这是非常浪费资源的，基本上在每一个需要和网络交互的Activity中创建一个RequestQueue对象就足够了。
        queue= Volley.newRequestQueue(this);
    }

    /**
     * get
     * @param view
     */
    public void internetBtn(View view)
    {
        StringRequest request=new StringRequest("https://www.baidu.com", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("----->",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("------>",volleyError.getMessage(),volleyError);
            }
        });

        queue.add(request);
    }

    /**
     * post
     *
     * @param view
     */
    public void internetPostBtn(View view)
    {
//        ?userId=ss&loginPassword=123456
        StringRequest request=new StringRequest(Request.Method.POST,
                "http://172.16.15.154:8080/pandadasbe/logincheck", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("----->",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("------>",volleyError.getMessage(),volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("userId","ss");
                map.put("loginPassword","123456");
                return map;
            }
        };
        queue.add(request);
    }

    /**
     * 请求json数据
     * @param view
     */
    public void jsonRequestBtn(View view)
    {
        JsonObjectRequest request=new JsonObjectRequest("http://172.16.15.154:8080/pandadasbe/workorderlist",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("----->",jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("------>",volleyError.getMessage(),volleyError);
            }
        });
        queue.add(request);
    }

    /**
     * 加载图片ImageRequest
     * @param view
     */
    public void imageRequestBtn(View view)
    {
        ImageRequest request=new ImageRequest("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505117229004&di=80fc17232e7ebdfcd19fa4d5a3536657&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F80cb39dbb6fd5266adea5c32a118972bd50736d6.jpg", new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        });
        queue.add(request);
    }

    /**
     * ImageLoader
     * @param view
     */
    public void imageLoaderBtn(View view)
    {

/*//        未利用图片缓存
        ImageLoader loader=new ImageLoader(queue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {

            }
        });*/
//      使用图片缓存
        ImageLoader loader=new ImageLoader(queue,new BitmapCache());
        ImageLoader.ImageListener listener= ImageLoader.getImageListener(imageView,R.drawable.loader,R.mipmap.ic_launcher);
        loader.get("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505118339689&di=b395915cc5d69f65299edf983e830bbe&imgtype=0&src=http%3A%2F%2Fpic41.nipic.com%2F20140522%2F18613197_131208318168_2.jpg",listener);
    }

    /**
     * NetworkIamgeView
     * @param view
     */
    public void netWorkImgViewBtn(View view)
    {
        String url="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505120264454&di=62997346dc8770cfe75b47515d4139d1&imgtype=0&src=http%3A%2F%2Fpic40.nipic.com%2F20140423%2F18525978_165601519138_2.jpg";
        ImageLoader loader=new ImageLoader(queue,new BitmapCache());
        networkImageView.setDefaultImageResId(R.drawable.loader);
        networkImageView.setErrorImageResId(R.mipmap.ic_launcher);
        networkImageView.setImageUrl(url,loader);
    }


}
