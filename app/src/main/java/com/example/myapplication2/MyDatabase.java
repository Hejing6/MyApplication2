package com.example.myapplication2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MyDatabase {
    Context context;
    DBHelper dbhelper;
    SQLiteDatabase mydatabase;
    public MyDatabase(Context context){
        this.context = context;
        dbhelper = new DBHelper(context);
    }

    public void toDelete(int ids){            //在表中删除数据
        mydatabase  = dbhelper.getWritableDatabase();
        mydatabase.execSQL("delete from mybook where ids="+ids+"");
        mydatabase.close();
    }

    public void toUpdate(Data data){           //修改表中数据
        mydatabase = dbhelper.getWritableDatabase();
        mydatabase.execSQL(
                "update mybook set title='"+ data.getTitle()+
                        "',times='"+data.getTimes()+
                        "',content='"+data.getContent() +
                        "' where ids='"+ data.getIds()+"'");
        mydatabase.close();
    }


    //在参考老师所讲的数据库时同时也参考了借鉴了csdn中Gahui_Liao的便签设计进行修改
    public ArrayList<Data> getarray(){            //获取listview中要显示的数据
        ArrayList<Data> arr = new ArrayList<Data>();
        ArrayList<Data> arr1 = new ArrayList<Data>();
        mydatabase = dbhelper.getWritableDatabase();
        Cursor cursor = mydatabase.rawQuery("select ids,title,times from mybook",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex("ids"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String times = cursor.getString(cursor.getColumnIndex("times"));
            Data data = new Data(id, title, times);
            arr.add(data);
            cursor.moveToNext();
        }
        mydatabase.close();
        for (int i = arr.size(); i >0; i--) {
            arr1.add(arr.get(i-1));
        }
        return arr1;
    }

    public Data getTiandCon(int id){           //listview子项想要修改数据时，获取数据显示在新建页面
        mydatabase = dbhelper.getWritableDatabase();
        Cursor cursor=mydatabase.rawQuery("select title,content from mybook where ids='"+id+"'" , null);
        cursor.moveToFirst();
        String title=cursor.getString(cursor.getColumnIndex("title"));
        String content=cursor.getString(cursor.getColumnIndex("content"));
        Data data=new Data(title,content);
        mydatabase.close();
        return data;
    }


    public void toInsert(Data data){           //在表中插入新建的便签的数据
        mydatabase = dbhelper.getWritableDatabase();
        mydatabase.execSQL("insert into mybook(title,content,times)values('"
                + data.getTitle()+"','"
                +data.getContent()+"','"
                +data.getTimes()
                +"')");
        mydatabase.close();
    }


}
