package com.example.popularnews;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.popularnews.api.ApiClient;
import com.example.popularnews.api.ApiInterface;
import com.example.popularnews.models.Article;
import com.example.popularnews.models.News;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY="3a95a374f5ea4d269c208cc1dd13df13";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles =new ArrayList<>();
    private Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Gson gson;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout=findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView=findViewById(R.id.recyclerView);
        layoutManager=new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setNestedScrollingEnabled(false);
        LoadJson();

        gson=new Gson();
        db = this.openOrCreateDatabase("test.db", Context.MODE_PRIVATE,null);
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + "NewsTable" + "(" + "KEY_ID" + " INTEGER PRIMARY KEY," + "KEY_NAME" + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public  boolean LoadJson()
    {
        swipeRefreshLayout.setRefreshing(true);
        Log.d("vib"," hi1 ");
        final ApiInterface apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        String country= Utils.getCountry();
        Call<News> call;
        call=apiInterface.getNews(country,API_KEY);
        Log.d("vib"," hi2 ");
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                Log.d("vib"," hi ");
                if(response.isSuccessful() && response.body().getArticle()!=null){

                    if(!articles.isEmpty()) {
                        articles.clear();
                    }
                    Log.d("vib"," "+response.body().getArticle());
                    articles=response.body().getArticle();
                    adapter=new Adapter(articles,MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    initListener();
                    swipeRefreshLayout.setRefreshing(false);
                    String json=gson.toJson(articles);

                    ContentValues values = new ContentValues();
                    values.put("KEY_ID",1);
                    values.put("KEY_NAME", json);
                    db.insert("NewsTable", null, values);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"No result",Toast.LENGTH_SHORT).show();
                    String selectQuery = "SELECT  * FROM " + "NewsTable";
                    Cursor mcursor = db.rawQuery(selectQuery, null);
                    if(mcursor.moveToLast()) {
                        TypeToken<List<Article>> token = new TypeToken<List<Article>>() {};
                        articles = gson.fromJson(mcursor.getString(1), token.getType());
                        adapter = new Adapter(articles, MainActivity.this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        initListener();
                    }
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d("vib"," hi not");
                swipeRefreshLayout.setRefreshing(false);
                String selectQuery = "SELECT  * FROM " + "NewsTable";
                Cursor mcursor = db.rawQuery(selectQuery, null);
                if(mcursor.moveToLast()) {
                    TypeToken<List<Article>> token = new TypeToken<List<Article>>() {};
                    articles = gson.fromJson(mcursor.getString(1), token.getType());
                    adapter = new Adapter(articles, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    initListener();
                }

            }
        });
    return true;
    }

    @Override
    public void onRefresh() {
        Log.d("vib"," In LoadJson()");
        LoadJson();
    }

    private void initListener()
    {
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(MainActivity.this,NewsDetailActivity.class);
                Article article=articles.get(position);
                intent.putExtra("url",article.getUrl());
                startActivity(intent);
            }
        });
    }
}