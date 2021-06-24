package com.example.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    ListView listView;
    FloatingActionButton flb;
    LayoutInflater layoutInflater;
    ArrayList<Data> arrayList;
    MyDatabase myDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//
        setContentView(R.layout.activity_main);//

        listView = (ListView)findViewById(R.id.layout_listview);

        layoutInflater = getLayoutInflater();

        myDatabase = new MyDatabase(this);
        arrayList = myDatabase.getarray();
        Adapter adapter = new Adapter(layoutInflater,arrayList);
        listView.setAdapter(adapter);


        flb = (FloatingActionButton)findViewById(R.id.floatingActionButton);//点击悬浮按钮时，跳转到新建页面
        flb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),NewNote.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {   //点击一下跳转到编辑页面
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),NewNote.class);
                intent.putExtra("ids",arrayList.get(position).getIds());
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {   //长按删除
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //弹出一个对话框
                builder.setTitle("提示")
                        .setMessage("确定要删除此便签？")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myDatabase.toDelete(arrayList.get(position).getIds());//这里的删除方法借鉴了csdn中Gahui_Liao的便签中删除方式
                                Adapter myAdapter = new Adapter(layoutInflater,arrayList);
                                listView.setAdapter(myAdapter);

//                                intent.putExtra("data",arrayList.getClass());
//
                                //由于删除后无法更新数据 新增页面进行更新
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);//更新页面
                                MainActivity.this.finish();
                                startActivity(intent);

                            }
                        })
                        .create().show();
                return true;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.create:
                Intent intent = new Intent(getApplicationContext(),NewNote.class);
                startActivity(intent);
                MainActivity.this.finish();
                break;
            case R.id.exit:
                MainActivity.this.finish();
                break;
            default:
                break;
        }
        return  true;

    }
}

