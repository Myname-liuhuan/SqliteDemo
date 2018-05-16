package com.example.sqlitedemo;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SqliteActivity extends AppCompatActivity {

    MyDatabaseHelper dbhelper;
    private boolean namechange=false;
    private List<SqliteTableData> list=new ArrayList<>();
    ListView listView001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        Button button01 =(Button)findViewById(R.id.button01);
        Button button02 =(Button)findViewById(R.id.button02);
        final Button button03 =(Button)findViewById(R.id.button03);
        final Button button04 =(Button)findViewById(R.id.button04);
        Button button05 =(Button)findViewById(R.id.button05);


        dbhelper=new MyDatabaseHelper(this,"Test.db",null,1);//要使用数据库一定要先创建

        //创建数据库以及创建两个表
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbhelper.getWritableDatabase();//版本变化，所以调用onUpgrade//该方法的返回值是SQLiteDatabase类型
            }
        });

        //向表中添加数据
        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db =dbhelper.getWritableDatabase();//以数据库为对象才可以进行增删改查
                ContentValues values = new ContentValues();//ContentValues用于存储内容解析器可以处理的一组值//就是存放数据的，不仅仅可以使用在数据库上面
                //开始组装第一条数据
                values.put("name","Tom");
                values.put("sex","male");
                values.put("department","computer");
                values.put("salary",5400.0);
                db.insert("staff",null,values);
                values.clear();
                //开始组装第二条数据
                values.put("name","Einstein");
                values.put("sex","male");
                values.put("department","computer");
                values.put("salary",4800.0);
                db.insert("staff",null,values);
                values.clear();
                //第三条数据
                values.put("name","Lily");
                values.put("sex","female");
                values.put("department",1.68);
                values.put("salary",5000.0);
                db.insert("staff",null,values);
                values.clear();
                //第四条数据
                values.put("name","Warner");
                values.put("sex","male");
                db.insert("staff",null,values);
                values.clear();
                //第五条数据
                values.put("name","Napoleon");
                values.put("sex","male");
                db.insert("staff",null,values);
                values.clear();
                Toast.makeText(SqliteActivity.this, "表完成插入数据", Toast.LENGTH_SHORT).show();
                db.close();
            }
        });
        //更新数据
        button03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db =dbhelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                String temp="张三";

                if(namechange){
                    values.put("name","Tom");
                    db.update("staff",values,"id = 1 and name=?",new String[]{temp});
                    values.clear();
                    values.put("name","Einstein");
                    db.update("staff",values,"id = 2 and name=?",new String[]{temp});
                    values.clear();
                    values.put("name","Lily");
                    db.update("staff",values,"id = 3 and name=?",new String[]{temp});
                    values.clear();
                    values.put("name","Warner");
                    db.update("staff",values,"id = 4 and name=?",new String[]{temp});
                    values.clear();
                    values.put("name","Napoleon");
                    db.update("staff",values,"id = 5 and name=?",new String[]{temp});
                    values.clear();
                    Toast.makeText(SqliteActivity.this,"表更新成功-正",Toast.LENGTH_SHORT).show();
                    namechange=false;
                }else{
                    values.put("name",temp);
                    db.update("staff",values,null,null);
                    Toast.makeText(SqliteActivity.this,"表更新成功-反",Toast.LENGTH_SHORT).show();
                    values.clear();
                    namechange=true;
                }
                db.close();
            }
        });

        //删除数据
        button04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db =dbhelper.getWritableDatabase();
                db.delete("staff",null,null);//删除表中全部数据
                Toast.makeText(SqliteActivity.this,"表删除成功",Toast.LENGTH_SHORT).show();
                db.close();
                list.clear();//解决 清空数据表后再点击查询，listView上还存在表信息(第一行不算) 的问题
            }
        });

        //查询语句
        button05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(SqliteActivity.this,TempActivity.class);
//                startActivity(intent);
                SQLiteDatabase db =dbhelper.getWritableDatabase();
                Cursor cursor = db.query("staff",null,null,null,null,null,null);
                //Cursor是专用来查询语句的
                if(cursor.moveToFirst()){
                    list.clear();//清除上次加载的数据库（解决  连点多次查询，显示的ListView为多个查询结果叠加  的问题）
                    do{
                        //遍历cursor对象,并以行为单位把数据传入到List数组
                        int id=cursor.getInt(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String sex = cursor.getString(cursor.getColumnIndex("sex"));
                        String department = cursor.getString(cursor.getColumnIndex("department"));
                        float salary = cursor.getFloat(cursor.getColumnIndex("salary"));
                        SqliteTableData temp1=new SqliteTableData(id,name,sex,department,salary);
                        list.add(temp1);
                    }while(cursor.moveToNext());//跳到下一行
                    cursor.close();
                }
                DialogListView();
                db.close();
            }
        });
    }


    public class SqliteTableData{//建立一个类将五个数据集成为一个类型
        int id;
        String name,sex,department;
        float salary;
        SqliteTableData(int id,String name,String sex,String department,float salary){
            this.id=id;
            this.name=name;
            this.sex=sex;
            this.department=department;
            this.salary=salary;
        }

        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public String getSex() {
            return sex;
        }
        public String getDepartment() {
            return department;
        }
        public float getSalary() {
            return salary;
        }
    }


    public class MyAdapter extends ArrayAdapter<SqliteTableData>{
        private int ListViewItemlayout;
        MyAdapter(Context context,int textViewResourceId,List<SqliteTableData> objects){
            super(context,textViewResourceId,objects);
            ListViewItemlayout=textViewResourceId;
        }

        @Override
        public int getCount(){
            return list.size();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SqliteTableData std=getItem(position);
            View view=LayoutInflater.from(getContext()).inflate(ListViewItemlayout,parent,false);

            TextView textView001=(TextView)view.findViewById(R.id.textView001);
            TextView textView002=(TextView)view.findViewById(R.id.textView002);
            TextView textView003=(TextView)view.findViewById(R.id.textView003);
            TextView textView004=(TextView)view.findViewById(R.id.textView004);
            TextView textView005=(TextView)view.findViewById(R.id.textView005);

            textView001.setText(String.valueOf(std.getId()));
            textView002.setText(std.getName());
            textView003.setText(std.getSex());
            textView004.setText(std.getDepartment());
            textView005.setText(String.valueOf(std.getSalary()));

            return view;
        }

    }

    public void DialogListView(){
        Dialog dialog = new Dialog(this,R.style.dialog);
        View dialogLayout=getLayoutInflater().inflate(R.layout.displaysqlite,null);
        dialog.setContentView(dialogLayout);
        Window dialogWindow=dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();//获得窗体管理器对象，对相应的窗体进行管理
        layoutParams.y=10;

        WindowManager windowManager=getWindowManager();//获取屏幕Manager对象
        Display display=windowManager.getDefaultDisplay();//显示器对象
        Point size=new Point();
        display.getSize(size);
        int width=size.x;
        int height=((size.y)/2);
        layoutParams.width=width;
        layoutParams.height=height;//设置dialog的高度为屏幕的一半

        listView001=(ListView)dialog.findViewById(R.id.listView001);//要在dialog里面使用的控件都必须在dialog里面注册引用
        MyAdapter adapter = new MyAdapter(SqliteActivity.this,R.layout.listview_item,list);//这里的R.layout.listview_item是listView里面每一个Item的布局
        listView001.setAdapter(adapter);
        Toast.makeText(SqliteActivity.this,"完成查询数据库",Toast.LENGTH_SHORT).show();


        dialogWindow.setAttributes(layoutParams);
        dialog.show();
    }
}
