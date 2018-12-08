package com.android.renly.plusclub.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.renly.plusclub.module.schedule.edu.detail.ScheduleDetailActivity;
import com.android.renly.plusclub.api.bean.Course;
import com.android.renly.plusclub.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleGridAdapter extends BaseAdapter{
    private Context mContext;

    private String[][] contents;
    List<Course> scheduleList = new ArrayList<>();


    private int rowTotal;

    private int columnTotal;

    private int positionTotal;

    public ScheduleGridAdapter(Context context, List<Course> scheduleList) {
        this.mContext = context;
        this.scheduleList = scheduleList;
    }

    public int getCount() {
        return positionTotal;
    }

    public long getItemId(int position) {
        return position;
    }

    public String getItem(int position) {
        //求余得到二维索引
        int column = position % columnTotal;
        //求商得到二维索引
        int row = position / columnTotal;
        if (contents[row][column] == null){

            Log.e("print","contents[" + row + "][" + column + "] == null");
        }
        return contents[row][column];
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if( convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_course, null);
        }
        TextView textView = convertView.findViewById(R.id.text);
        //如果有课,那么添加数据
        if( !getItem(position).equals("")) {
            textView.setText((String)getItem(position));
            textView.setTextColor(Color.WHITE);
            //变换颜色
            int rand = position % 11;
            switch( rand ) {
                case 0:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_schedule11));
                    break;
                case 1:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_schedule01));
                    break;
                case 2:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_schedule02));
                    break;
                case 3:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_schedule03));
                    break;
                case 4:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_schedule04));
                    break;
                case 5:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_schedule05));
                    break;
                case 6:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_schedule06));
                    break;
                case 7:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_schedule07));
                    break;
                case 8:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_schedule08));
                    break;
                case 9:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_schedule09));
                    break;
                case 10:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_schedule10));
                    break;
            }
            convertView.setOnClickListener(v -> {
                int row = position / columnTotal;
                int column = position % columnTotal;
                Course obj = new Course();
                for (int i = 0;i < scheduleList.size();i++)
                    if ((scheduleList.get(i).getRows() - 1) / 2 == row && scheduleList.get(i).getWeekday() - 1 == column)
                        obj = scheduleList.get(i);
                String str = obj.getRows() + " " + obj.getWeekday();
                Log.e("print",str);
                Intent intent = new Intent(mContext, ScheduleDetailActivity.class);
                intent.putExtra("JsonObj", JSON.toJSONString(obj));
                mContext.startActivity(intent);
            });
        }
        return convertView;
    }

    /**
     * 设置内容、行数、列数
     */
    public void setContent(String[][] contents, int row, int column) {
        this.contents = contents;
        this.rowTotal = row;
        this.columnTotal = column;
        positionTotal = rowTotal * columnTotal;
    }
}
