package com.example.administrator.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/4/24.
 */
public class HomeworkDetail extends BaseActivity implements View.OnClickListener {

    //private List<HomeworkMessage> homeworkList = new ArrayList<HomeworkMessage>();
    private static List<HomeworkMessage> homeworkList = new ArrayList<>();
    private Button publishHomework;
    private RadioGroup radioGroup;
    private TextView detailText;
    private TextView detailTime;
    private TextView detailEditor;
    private HomeworkDetailAdapter adapter;
    private Bundle extras;
    private TextView courseName;

    private Context myContext = null;

    private static Set<String> courses;

    private String time;
    private String cname;

    private String Str;

    private int test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.homework_detail);

        //获取SharedPreferences对象
        SharedPreferences sp = HomeworkDetail.this.getSharedPreferences("DATA", MODE_PRIVATE);
        courses = sp.getStringSet("courses", new HashSet<String>());
        extras = getIntent().getExtras();
        if(extras!=null) {
            time = extras.getString("deadline");
            cname = extras.getString("cname");
        }

        initHomework();

        myContext = this;

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        courseName = (TextView) findViewById(R.id.homework_detail_course_name);

        //菜单适配器
        adapter = new HomeworkDetailAdapter(HomeworkDetail.this, R.layout.homework_detail_item, homeworkList);
        ListView listView = (ListView) findViewById(R.id.homework_detail_list_view);
        listView.setAdapter(adapter);

        //菜单项点击事件
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                //点击菜单项，弹出窗口
                HomeworkMessage tmp = homeworkList.get(position);
                showPopupWindow(view, tmp.getContent(), tmp.getTime(), tmp.getEditor(), tmp.getPubTime());
            }
        });




        publishHomework = (Button) findViewById(R.id.homework_detail_publish);
        publishHomework.setOnClickListener(this);

    }

    @Override
    protected void onDestroy(){
        //

//        Str = "kind=setMark&choose=" + HomeworkDetailAdapter.checkedRadio[0] + "&hid=" + HomeworkDetail.homeworkList.get(0).getHid();
//        setMark();
//        Str = "kind=setMark&choose=" + HomeworkDetailAdapter.checkedRadio[1] + "&hid=" + HomeworkDetail.homeworkList.get(1).getHid();
//        setMark();
//        Str = "kind=setMark&choose=" + HomeworkDetailAdapter.checkedRadio[2] + "&hid=" + HomeworkDetail.homeworkList.get(2).getHid();
//        setMark();
//        Str = "kind=setMark&choose=" + HomeworkDetailAdapter.checkedRadio[3] + "&hid=" + HomeworkDetail.homeworkList.get(3).getHid();
//        setMark();
//        Str = "kind=setMark&choose=" + HomeworkDetailAdapter.checkedRadio[4] + "&hid=" + HomeworkDetail.homeworkList.get(4).getHid();
//        setMark();
//        Toast.makeText(this, "checkedRadio长度：" + HomeworkDetailAdapter.checkedRadio.length, Toast.LENGTH_SHORT).show();
        for(int i = 0; i < homeworkList.size(); i++) {
            test = i;
//            Toast.makeText(this, "i：" + i + " choose:" +  HomeworkDetailAdapter.checkedRadio[i], Toast.LENGTH_SHORT).show();
            try{
                Thread thread = Thread.currentThread();
                thread.sleep(0500);//暂停0.5秒后程序继续执行
                Str = "kind=setMark&choose=" + HomeworkDetailAdapter.checkedRadio[test] + "&hid=" + HomeworkDetail.homeworkList.get(test).getHid();
                setMark();
            }catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        super.onDestroy();

    }

    //弹出窗口实现代码
    @SuppressLint("NewApi")
    private void showPopupWindow(View view,String str1, String str2, String str3, String str4){
        View contentView = LayoutInflater.from(myContext).inflate(R.layout.pop_window_detail, null);

        //窗口内容
        detailText = (TextView) contentView.findViewById(R.id.pop_detail);
        detailText.setText(str1);
        detailText.setMovementMethod(ScrollingMovementMethod.getInstance());
        detailTime = (TextView) contentView.findViewById(R.id.pop_time);
        detailTime.setText(" 于" + str2 + "截止");
        detailEditor = (TextView) contentView.findViewById(R.id.pop_editor);
        detailEditor.setText(str3);
        detailEditor = (TextView) contentView.findViewById(R.id.pop_pub_time);
        detailEditor.setText(str4.substring(5, 16));


        //窗口构造函数
        PopupWindow popupWindow = new PopupWindow(contentView,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

        //窗口背景
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.bgrd));

/*
 *    	 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
 *       popupWindow.setBackgroundDrawable(new BitmapDrawable());
 *       使其聚集
 *       popupWindow.setFocusable(true);
 *       设置允许在外点击消失
 *       popupWindow.setOutsideTouchable(true);
 *       刷新状态
 *       popupWindow.update();
 */
        //窗口弹出位置
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void initHomework() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                HttpURLConnection connection = null;
                Message message = new Message();
                try {
                    String str = "kind=getHWList&number=" + courses.size();
                    Integer i_count = 0;
                    for(String s:courses){
                        str = str + "&" + i_count.toString() + "=" + s;
                        i_count++;
                    }
//                    Toast.makeText(HomeworkDetail.this, str, Toast.LENGTH_SHORT).show();
                    URL url = new URL("http://119.29.148.205:8080/hwServer/main");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.getOutputStream().write(str.getBytes("utf8"));
                    connection.getOutputStream().flush();
                    connection.getOutputStream().close();
                    InputStream input = connection.getInputStream();

                    BufferedReader in = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null){
                        buffer.append(line);
                    }
                    String sdata = buffer.toString();
                    JSONObject json = new JSONObject(sdata);
                    message.what = json.getInt("state");
                    message.obj = json;
                    handler.sendMessage(message);
                } catch (ConnectException e) {
                    message.what = 9;
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    message = Message.obtain(); // 重新获取message
                    handler.sendMessage(message);
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }

        }).start();

    }

    MyHandler handler = new MyHandler(HomeworkDetail.this);
    class MyHandler extends Handler {
        WeakReference<HomeworkDetail>HomeworkDetailWeakReference;
        JSONArray jsonArray;
        ClassActivity classTable;

        public MyHandler(HomeworkDetail Activity) {
            HomeworkDetailWeakReference = new WeakReference<>(Activity);
            classTable = new ClassActivity();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 0: // 显示同一次作业
                        JSONObject json_all = (JSONObject) msg.obj;
                        jsonArray = json_all.getJSONArray("HWData");
                        ArrayList<Integer> int_hw = new ArrayList<>();
                        //Toast.makeText(HomeworkDetail.this, "课程名称："+cname+" deadline:"+time, Toast.LENGTH_SHORT).show();
                        courseName.setText(cname);

                        for(Integer i = 0; i < jsonArray.length(); i++) {
                            if(jsonArray.getJSONObject(i).getInt("mark") != -1
                                    &&jsonArray.getJSONObject(i).getString("cname").equals(cname)
                                    &&jsonArray.getJSONObject(i).getString("deadline").equals(time+".0")) {
                                int_hw.add(i);
                            }
                        }
                        // 对评分排序
                        Collections.sort(int_hw, new myCompare());

                        // 显示作业列表
                        homeworkList.clear(); // 清除原来的作业
                        for(Integer i = 0; i < int_hw.size(); i++) {
                            JSONObject currentJson = jsonArray.getJSONObject(int_hw.get(i));
                            //Toast.makeText(HomeworkDetail.this, "课程名称："+currentJson.getString("upload_time")+" deadline:"+time, Toast.LENGTH_SHORT).show();
                            // 日期时间，去掉结尾的“.0”
                            // 详细内容参数顺序为：截止日期，内容，发布人，作业类型，发布时间，是否已赞，id
                            HomeworkMessage firstHomework = new HomeworkMessage(currentJson.getString("deadline").substring(0, 19),
                                    currentJson.getString("question"), "by  " + currentJson.getString("user"), currentJson.getString("hwtype"), currentJson.getString("upload_time").substring(0, 19), false, currentJson.getInt("hid"));
                            homeworkList.add(firstHomework);
                            //Toast.makeText(HomeworkDetail.this, "课程名称："+currentJson.getString("deadline")+" deadline:"+time, Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();

                        break;
                    case 3: // 数据库操作失败
                        Toast.makeText(HomeworkDetailWeakReference.get(), "数据库操作失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 9: // 网络连接错误
                        Toast.makeText(HomeworkDetailWeakReference.get(), "网络连接错误。可能网络没有开启，或服务端停止服务。",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(HomeworkDetailWeakReference.get(), "传输失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        class myCompare implements Comparator {
            @Override
            public int compare(Object lhs, Object rhs) {
                Integer int1 = (Integer) lhs;
                Integer int2 = (Integer) rhs;

                int result = -1;
                try {
                    Integer mark1 = jsonArray.getJSONObject(int1).getInt("mark");
                    Integer mark2 = jsonArray.getJSONObject(int2).getInt("mark");
                    if (mark1 < mark2)
                        result = 1;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return result;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.homework_detail_publish) {
            Intent intent = new Intent(HomeworkDetail.this, HomeworkPublish.class);
            intent.putExtra("cname",cname);
            startActivity(intent);
            HomeworkDetail.this.finish();
        }
        /*else if (view.getId() == R.id.homework_detail_publish) {

        }
        else if (view.getId() == R.id.homework_detail_publish) {

        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private void setMark() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                HttpURLConnection connection = null;
                Message message = new Message();
                try {
//                    Thread.sleep(1000);
                    URL url = new URL("http://119.29.148.205:8080/hwServer/main");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.getOutputStream().write(Str.getBytes("utf8"));
                    connection.getOutputStream().flush();
                    connection.getOutputStream().close();
                    InputStream input = connection.getInputStream();

                    BufferedReader in = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null){
                        buffer.append(line);
                    }
                    String sdata = buffer.toString();
                    JSONObject json = new JSONObject(sdata);
//                    message.what = json.getInt("state");
                    message.obj = json.getString("sql");

                    handler2.sendMessage(message);
                } catch (ConnectException e) {
                    message.what = 9;
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }

        }).start();

    }

    MyHandler2 handler2 = new MyHandler2(HomeworkDetail.this);
    class MyHandler2 extends Handler {
        WeakReference<HomeworkDetail> HomeworkDetailWeakReference;
        public MyHandler2(HomeworkDetail Activity) {
            HomeworkDetailWeakReference = new WeakReference<>(Activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 0: // 评价成功
//                        Toast.makeText(HomeworkDetailWeakReference.get(), "评价成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 3: // 数据库操作失败
                        Toast.makeText(HomeworkDetailWeakReference.get(), "数据库操作失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 9: // 网络连接错误
                        Toast.makeText(HomeworkDetailWeakReference.get(), "网络连接错误。可能网络没有开启，或服务端停止服务。",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(HomeworkDetailWeakReference.get(), "传输失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}