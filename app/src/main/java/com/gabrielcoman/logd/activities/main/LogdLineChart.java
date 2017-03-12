package com.gabrielcoman.logd.activities.main;

import android.content.Context;
import android.util.AttributeSet;

import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.LineChartView;
import com.gabrielcoman.logd.R;

import java.util.ArrayList;

public class LogdLineChart extends LineChartView {

    public LogdLineChart(Context context) {
        this(context, null);
    }

    public LogdLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) return;

        setXAxis(false);
        setYAxis(false);
        setLabelsColor(getResources().getColor(R.color.colorLightAccent));
        setYLabels(AxisRenderer.LabelPosition.NONE);
        setAxisBorderValues(-1, 1);
    }

    public void setData(String[] labels, float[] values) {
        LineSet set = new LineSet(labels, values);
        set.setSmooth(false);
        set.setThickness(5.0F);
        set.setDotsRadius(16.0F);
        set.setColor(getResources().getColor(R.color.colorTextHint));
        set.setDotsColor(getResources().getColor(R.color.colorAccent));
        set.setFill(getResources().getColor(R.color.colorPrimaryDark));

        ArrayList<ChartSet> list = new ArrayList<>();
        list.add(set);
        addData(list);
        show();
    }


}
