package com.tm.timemanager.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tm.timemanager.Activity.HomeActivity;
import com.tm.timemanager.R;
import com.tm.timemanager.Utils.DateUtil;
import com.tm.timemanager.bean.AppDailyUsage;
import com.tm.timemanager.dao.DBOpenHelperdao;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by CHENQIAO on 2016/4/22.
 */

public  class TrendFragment extends Fragment {
        public   String[] apps ;

        public   String[] details = new String[]{"00:00","01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00","10:00","11:00","12:00","13:00","14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

        private LineChartView chartTop;
        private ColumnChartView chartBottom;

        private LineChartData lineData;
        private ColumnChartData columnData;
    private ArrayList<AppDailyUsage> appDailyUsagesList;
    private ArrayList<String> packNameList;

    public TrendFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_trend, container, false);

            ImageButton ibn_trend_menu = (ImageButton) rootView.findViewById(R.id.ibn_trend_menu);
            ibn_trend_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SlidingMenu slidingMenu = ((HomeActivity)getActivity()).getSlidingMenu();
                    slidingMenu.toggle();
                }
            });

            dataInitial();

            // *** TOP LINE CHART ***
            chartTop = (LineChartView) rootView.findViewById(R.id.cv_trend_charttop);

            // Generate and set data for line chart
            generateInitialLineData();

            // *** BOTTOM COLUMN CHART ***

            chartBottom = (ColumnChartView) rootView.findViewById(R.id.cv_trend_chartbottom);

            generateColumnData();

            return rootView;
        }

    private void dataInitial() {
        String date = DateUtil.getDate();
        appDailyUsagesList = new ArrayList<>();
        packNameList = new ArrayList<>();
        int totalTime = 0;
        int totalCount = 0;
        Cursor cursor = new DBOpenHelperdao(getActivity()).getappdaily(date);
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
                Log.i("appUsageList1", appDailyUsage.toString());
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
                        Log.i("appUsageList2",usage.toString());
                    }

                }
            }

        }

        apps = new String[appDailyUsagesList.size()];
        for (int i=0;i<appDailyUsagesList.size();i++){
            apps[i] = appDailyUsagesList.get(i).getAppname();
            Log.i("dataInitial",apps[i]);
        }

    }

    private void generateColumnData() {

            int numSubcolumns = 1;
            int numColumns = apps.length;

        Log.i("apps.length",apps.length+"");

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue((float) appDailyUsagesList.get(i).getRuntime(), ChartUtils.pickColor()));
                }

                Log.i("generateColumnData",apps[i]);
                axisValues.add(new AxisValue(i).setLabel(apps[i]));

                columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
            }

            columnData = new ColumnChartData(columns);

            columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));

            chartBottom.setColumnChartData(columnData);

            // Set value touch listener that will trigger changes for chartTop.
            chartBottom.setOnValueTouchListener(new ValueTouchListener());

            // Set selection mode to keep selected month column highlighted.
            chartBottom.setValueSelectionEnabled(true);

            chartBottom.setZoomType(ZoomType.HORIZONTAL);

//             chartBottom.setOnClickListener(new View.OnClickListener() {
//
//             @Override
//             public void onClick(View v) {
//             SelectedValue sv = chartBottom.getSelectedValue();
//             if (!sv.isSet()) {
//             generateInitialLineData();
//             }
//
//             }
//             });

        }

        /**
         * Generates initial data for line chart. At the begining all Y values are equals 0. That will change when user
         * will select value on column chart.
         */
        private void generateInitialLineData() {
            int numValues = details.length;
            Log.i("details.length",details.length+"");

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i < numValues; ++i) {
                values.add(new PointValue(i, 0));
                axisValues.add(new AxisValue(i).setLabel(details[i]));
                Log.i("generateInitialLineData",details[i]+"");
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            lineData = new LineChartData(lines);
            lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

            chartTop.setLineChartData(lineData);

            // For build-up animation you have to disable viewport recalculation.
            chartTop.setViewportCalculationEnabled(false);

            // And set initial max viewport and current viewport- remember to set viewports after data.
            Viewport v = new Viewport(0, 100, 23, 0);
            chartTop.setMaximumViewport(v);
            chartTop.setCurrentViewport(v);

            chartTop.setZoomType(ZoomType.HORIZONTAL);
        }

        private void generateLineData(int color, float range) {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();

            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            line.setColor(color);
            for (PointValue value : line.getValues()) {
                // Change target only for Y value.
                value.setTarget(value.getX(), (float) Math.random() * range);
            }

            // Start new data animation with 300ms duration;
            chartTop.startDataAnimation(300);
        }

        private class ValueTouchListener implements ColumnChartOnValueSelectListener {

            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                generateLineData(value.getColor(), 100);
            }

            @Override
            public void onValueDeselected() {

                generateLineData(ChartUtils.COLOR_GREEN, 0);

            }
        }
    }

