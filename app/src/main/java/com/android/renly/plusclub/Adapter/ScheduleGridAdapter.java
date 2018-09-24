package com.android.renly.plusclub.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.plusclub.R;

public class ScheduleGridAdapter extends BaseAdapter{
    private Context mContext;

    private String[][] contents;

    private int rowTotal;

    private int columnTotal;

    private int positionTotal;

    public ScheduleGridAdapter(Context context) {
        this.mContext = context;
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
                String con = "当前选中的是" + contents[row][column] + "课";
                Toast.makeText(mContext, con, Toast.LENGTH_SHORT).show();
            });
        }
        return convertView;
    }

    /**
     * 设置内容、行数、列数
     */
    public void setContent(String[][] contents, int row, int column) {
        this.contents = contents;
        Log.e("print","content.lenth == " + contents.length);
        this.rowTotal = row;
        this.columnTotal = column;
        positionTotal = rowTotal * columnTotal;
    }
}
