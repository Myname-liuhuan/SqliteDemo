package com.example.sqlitedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 刘欢 on 2018/4/27.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_STAFF ="create table staff(" +
            "id integer primary key autoincrement," +
            "name text," +
            "sex text," +
            "department text," +//所在部门
            "salary float)";

    private Context mContext;//因为Context的对象只会在服务和Activity里面自动创建，但是在后面需要用到Context对象，所以手动创建

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;//将传入的context赋给本类的mContext
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STAFF);
        Toast.makeText(mContext,"Created Test.db",Toast.LENGTH_SHORT).show();//由于构造方法将使用这个类的Context赋给mContext，所以此Toast将会在传入的类的界面显示
}
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//当传入的数据库的版本变化时会自动调用这个函数
    }
}