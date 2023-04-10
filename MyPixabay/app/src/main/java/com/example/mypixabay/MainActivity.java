package com.example.mypixabay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.RuleBasedCollator;
import java.util.function.LongToDoubleFunction;

// 使用到的三方
// volley => 更快捷地联网的 HTTP 库
// https://developer.android.com/training/volley?hl=zh-cn

// glide => media management and image loading
// https://github.com/bumptech/glide

// swiperefreshlayout => 下拉刷新
// https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout?hl=zh-cn

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mylog";

    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://www.google.com",
                response -> {
                    Log.d(TAG, "response:" + response);
                },
                error -> {
                    Log.e(TAG, "requestError: " + error.getMessage());
                }
        );

        queue.add(stringRequest);

        ImageView imageView = findViewById(R.id.imageView);

        String imageUrl = "https://pixabay.com/get/g47c45e6e7cc7fea5b472b9fa81d6e0e3a8c124a6620bdda0d91a902c3be1f0df21cf8c4f0359ee744a0f3962690b7054fbeb20c60c9b9229d48f6083ffaaf3bb_1280.jpg";

        // volley 自帶的圖片加載元件，可以再自已決定 cache 的使用，也可以不指定 cache
        // ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
        //     private LruCache<String, Bitmap> lruCache = new LruCache<>(50);
        //
        //     @Nullable
        //     @Override
        //     public Bitmap getBitmap(String url) {
        //         return lruCache.get(url);
        //     }
        //
        //     @Override
        //     public void putBitmap(String url, Bitmap bitmap) {
        //         lruCache.put(url, bitmap);
        //     }
        // });
        // imageLoader.get(
        //         imageUrl,
        //         new ImageLoader.ImageListener() {
        //             @Override
        //             public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
        //                 imageView.setImageBitmap(response.getBitmap());
        //             }
        //
        //             @Override
        //             public void onErrorResponse(VolleyError error) {
        //             }
        //         });

        //Glide 極簡化
        //Glide.with(this).load(imageUrl).into(imageView);

        //Glide 進階化
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground) // 佔位圖
                .listener(new RequestListener<Drawable>() {     // 進階控制
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                })
                .into(imageView);

        //拉下更新
        swipeRefreshLayout.setOnRefreshListener(() -> {
            String url = "https://pixabay.com/get/gf54fb1040f39ccb1b866849d1cbd1741e2d3320452c7c03e0265a640a377e908932e7f333d637b57ad90a7181d8c3d61581bcb57ba49541bd2a6166f94675bb7_1280.jpg";
            Glide.with(this).load(url).into(imageView);
            swipeRefreshLayout.setRefreshing(false);
        });
    }
}