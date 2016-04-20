package com.tm.timemanager.pager;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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

import java.util.ArrayList;

/**
 * Created by CHENQIAO on 2016/4/20.
 */
public class PiePager extends BasePager implements OnChartValueSelectedListener {


    public View mView;
    private PieChart mChart;

    private Typeface tf;

    public PiePager(Activity activity) {
        super(activity);
        initView();
}


    public void initView() {
        mView = View.inflate(mActivity, R.layout.pager_pie, null);
        mChart = (PieChart) mView.findViewById(R.id.chart1);

        int screenWidth = mActivity.getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽
        int screenHeight =mActivity.getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高


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

        setData(3, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

    }

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


    private void setData(int count, float range) {

         String[] mParties = new String[] {
                "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
                "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
                "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
                "Party Y", "Party Z"
        };

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < count; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count; i++)
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
        data.setValueTextColor(Color.WHITE);
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
