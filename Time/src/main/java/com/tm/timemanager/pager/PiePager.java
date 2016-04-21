package com.tm.timemanager.pager;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tm.timemanager.R;
import com.tm.timemanager.bean.AppDailyUsage;
import com.tm.timemanager.dao.DBOpenHelperdao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by CHENQIAO on 2016/4/20.
 */
public class PiePager extends BasePager implements OnChartValueSelectedListener {


    public View mView;
    private PieChart mChart;

    private Typeface tf;
    private ArrayList<String> packNameList;
    private ArrayList<AppDailyUsage> appDailyUsagesList;
    int totalTime = 0;
    int totalCount = 0;
    private ListView lv_piepager_applist;

    public PiePager(Activity activity) {
        super(activity);
        initView();
    }


    public void initView() {
        mView = View.inflate(mActivity, R.layout.pager_pie, null);
        mChart = (PieChart) mView.findViewById(R.id.chart1);

        int screenWidth = mActivity.getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽
        int screenHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高


        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mChart.getLayoutParams();
        int i = (screenHeight > screenWidth) ? screenWidth : screenHeight;
        params.height = i;
        params.width = i;
        mChart.setLayoutParams(params);

        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        tf = Typeface.createFromAsset(mActivity.getAssets(), "fonts/OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(mActivity.getAssets(), "fonts/OpenSans-Light.ttf"));
        mChart.setCenterText(generateCenterSpannableText());
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.getLegend().setEnabled(false);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);

        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);


        //获取当前时间，得到当天数据库cursor信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        Log.i("当前时间", date);

            //          数据库字段
            // id integer primary key autoincrement,
            // date varchar(10),
            // packname varchar(40),
            // appname varchar(20),
            // starttime long,
            // runtime int,
            // clickcount int

            //          bean构造
            //        public AppDailyUsage(String packname, String appname, int runtime, String date, long starttime, int clickcount) {
            //            this.packname = packname;
            //            this.appname = appname;
            //            this.runtime = runtime;
            //            this.date = date;
            //            this.starttime = starttime;
            //            this.clickcount = clickcount;
            //        }
        appDailyUsagesList = new ArrayList<>();
        packNameList = new ArrayList<>();
        Cursor cursor = new DBOpenHelperdao(mActivity).getappdaily();
        while (cursor.moveToNext()) {
            String packname = cursor.getString(cursor.getColumnIndex("packname"));

            //将数据库按照packagename分类，运行时间相加
            if (!packNameList.contains(packname)) {
                packNameList.add(packname);

                String appname = cursor.getString(cursor.getColumnIndex("appname"));
                int runtime = cursor.getInt(cursor.getColumnIndex("runtime"));
                int starttime = cursor.getInt(cursor.getColumnIndex("starttime"));
                int clickcount = cursor.getInt(cursor.getColumnIndex("clickcount"));
                AppDailyUsage appDailyUsage = new AppDailyUsage(packname, appname, runtime, date, starttime, clickcount);

                totalTime = totalTime + runtime;
                totalCount = totalCount + clickcount;
                appDailyUsagesList.add(appDailyUsage);
//                Log.i("appUsageList1", appDailyUsage.toString());
            } else {
                for (AppDailyUsage usage : appDailyUsagesList) {
                    if (packname.equals(usage.getPackname())) {
                        int runtime = cursor.getInt(cursor.getColumnIndex("runtime"));
                        int count = cursor.getInt(cursor.getColumnIndex("clickcount"));
                        int totalruntime = usage.getRuntime() + runtime;
                        usage.setRuntime(totalruntime);
                        int totalclick = usage.getClickcount() + count;
                        usage.setClickcount(totalclick);
                        totalTime = totalTime + runtime;
                        totalCount = totalCount + count;
//                        Log.i("appUsageList2",usage.toString());
                    }

                }
            }

        }

//        for(int a = 0;a<packNameList.size();a++){
//            Log.i("appUsageList",packNameList.get(a));
//            Log.i("appUsageList",appDailyUsagesList.get(a).toString());
//        }

        setData(packNameList.size(), totalTime);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);




        setListData();

    }

    private void setListData() {
        lv_piepager_applist = (ListView) mView.findViewById(R.id.lv_piepager_applist);
        lv_piepager_applist.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return appDailyUsagesList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = null;
                ViewHolder viewHolder=null;
                if (convertView == null){
                    view = View.inflate(mActivity, R.layout.item_piepager_applist, null);
                    viewHolder =     new ViewHolder();
                    viewHolder.appicon = (ImageView) view.findViewById(R.id.iv_piepagerlist_icon);
                    viewHolder.appname = (TextView) view.findViewById(R.id.tv_piepagerlist_appname);
                    viewHolder.appusagetime = (TextView) view.findViewById(R.id.tv_piepagerlist_appusagetime);
                    viewHolder.appusagecount = (TextView) view.findViewById(R.id.tv_piepagerlist_appusagecount);
                    view.setTag(viewHolder);

                }else {
                    view = convertView;
                    viewHolder = (ViewHolder) view.getTag();
                }




                return view;
            }

            class ViewHolder{
                ImageView appicon;
                TextView appname;
                TextView appusagetime;
                TextView appusagecount;
            }
        });

    }

    //piechart中心显示内容
    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Today's APP Statistics\ndeveloped by Joe Chen");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 23, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 23, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 23, s.length() - 9, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 9, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 9, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 9, s.length(), 0);
        return s;
    }


    //绑定piechart数据
    private void setData(int size, float range) {

// id integer primary key autoincrement,
// date varchar(10),
// packname varchar(40),
// appname varchar(20),
// starttime long,
// runtime int,
// clickcount int
//        public Cursor getappdaily() {
        //所有数据的结果游标集
//            Cursor cursor = db.rawQuery("select * from appdaily;", null);
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            String date = cursor.getString(1);
//            String packname = cursor.getString(2);
//            String appname = cursor.getString(3);
//            long starttime = cursor.getLong(4);
//            int runtime = cursor.getInt(5);
//            int clickcount = cursor.getInt(6);
//        }
//            return cursor;
//        }


        //piechart每个分块代表的名字
        String[] mParties = new String[size];
        for (int i = 0; i < size; i++) {
            mParties[i] = appDailyUsagesList.get(i).getAppname();
        }


        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        for (int i = 0; i < size; i++) {
            //piechart每块所占比例
            yVals1.add(new Entry(appDailyUsagesList.get(i).getRuntime() / range, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < size; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, null);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);
        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }


}
