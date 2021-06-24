package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

public class NewNote extends AppCompatActivity {
    EditText ed_title;
    EditText ed_content;
    FloatingActionButton floatingActionButton;
    MyDatabase myDatabase;
    Data data;
    int ids;
    EditText input;
    EditText input2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//
        setContentView(R.layout.activity_new_note);//
        input = findViewById(R.id.write_title);
        input2 = findViewById(R.id.content);

        ed_title = (EditText)findViewById(R.id.write_title);
        ed_content = (EditText)findViewById(R.id.content);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.finish);
        myDatabase = new MyDatabase(this);
        Intent intent = this.getIntent();
        ids = intent.getIntExtra("ids",0);
        if (ids != 0){
            data = myDatabase.getTiandCon(ids);
            ed_title.setText(data.getTitle());
            ed_content.setText(data.getContent());
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {//为悬浮按钮设置监听事件
            @Override
            public void onClick(View v) {
                String str = input.getText().toString();
                if( str == null || str.length() == 0){
                    Toast.makeText(NewNote.this, "请输入标题", Toast.LENGTH_SHORT).show();//当标题为空时不能进行保存
                }else{
                    isSave();
                }
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //统计内容框中的字数
            case R.id.statistics:
                String str1 = input2.getText().toString();
                int i = str1.length();
                Toast.makeText(this,"字数统计为" + i, Toast.LENGTH_SHORT).show();
                break;


//                intent.setType("text/plain"); //纯文本
//
//                it.setType("image/png"); //添加图片 File f = new
//                File(Environment.getExternalStorageDirectory()+"/name.png");
//
//                Uri uri = Uri.fromFile(f); intent.putExtra(Intent.EXTRA_STREAM, uri);

            //实现分享功能 参考了csdn上lebulangzhen的Android的系统分享功能
            case R.id.share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                return true;

            case R.id.save:
                String str = input.getText().toString();
                if( str == null || str.length() == 0){
                    Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();//当标题为空时不能进行保存
                }else{
                    isSave();
                }
                break;
            case R.id.exit:
                Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
                NewNote.this.finish();
                break;
            default:
                break;
        }
        return  false;

    }


    private void isSave(){   //写保存的方法，如果是属于新建则插入数据表并返回主页面，如果是修改，修改表中数据并返回主页面，在参考老师所讲的数据库时同时也参考了借鉴了csdn中Gahui_Liao的便签设计进行修改
        //获取时间
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final String time = sdf.format(date);
        Log.d("new_note", "isSave: "+time);
        String title = ed_title.getText().toString();
        String content = ed_content.getText().toString();
        if(ids!=0){
            data=new Data(title,ids, content, time);
            myDatabase.toUpdate(data);
            Intent intent=new Intent(NewNote.this,MainActivity.class);
            startActivity(intent);
            NewNote.this.finish();
        }
        //新建日记
        else{
            data=new Data(title,content,time);
            myDatabase.toInsert(data);
            Intent intent=new Intent(NewNote.this,MainActivity.class);
            startActivity(intent);
            NewNote.this.finish();
        }
    }
//到这里截止

    @Override
    public void onBackPressed() {     //重写返回建方法，如果是属于新建则插入数据表并返回主页面，如果是修改，修改表中数据并返回主页面
        //获取时间
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final String time = sdf.format(date);
        String title = ed_title.getText().toString();
        String content = ed_content.getText().toString();
        if(ids!=0){
            data=new Data(title,ids, content, time);
            myDatabase.toUpdate(data);
            Intent intent=new Intent(NewNote.this,MainActivity.class);
            startActivity(intent);
            NewNote.this.finish();
        }
        //新建日记
        else{
            data=new Data(title,content,time);
            myDatabase.toInsert(data);
            Intent intent=new Intent(NewNote.this,MainActivity.class);
            startActivity(intent);
            NewNote.this.finish();
        }

    }


}