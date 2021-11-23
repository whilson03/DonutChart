package com.example.donutchart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {

    private int[] values = {30, 60, 90, 100, 150};
    private int c[] = {Color.MAGENTA, Color.BLUE, Color.RED, Color.CYAN, Color.YELLOW};
    private int valuesLength = values.length;
    private RectF rectF;
    private Paint slicePaint, textPaint;
    private Path path;

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        valuesLength = values.length;
        slicePaint = new Paint();
        slicePaint.setAntiAlias(true);
        slicePaint.setDither(true);
        slicePaint.setStyle(Paint.Style.FILL);

        path = new Path();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (values != null) {
            int startTop = 0;
            int startLeft = 0;
            int endBottom = getHeight();
            int endRight = endBottom;// This makes an equal square.

            rectF = new RectF(startLeft, startTop, endRight, endBottom);

            float[] scaledValues = scale();
            float sliceStartPoint = 0;
            path.addCircle(rectF.centerX(), rectF.centerY(), 125, Path.Direction.CW);
            canvas.clipPath(path, Region.Op.DIFFERENCE);

            for (int i = 0; i < valuesLength; i++) {
                slicePaint.setColor(c[i]);
                path.reset();
                path.addArc(rectF, sliceStartPoint, scaledValues[i]);
                path.lineTo(rectF.centerX(), rectF.centerY());
                canvas.drawPath(path, slicePaint);
                sliceStartPoint += scaledValues[i];//This updates the starting point of next slice.
            }
        }
    }

    private float[] scale() {
        float[] scaledValues = new float[this.values.length];
        float total = getTotal(); //Total all values supplied to the chart
        for (int i = 0; i < this.values.length; i++) {
            scaledValues[i] = (this.values[i] / total) * 360; //Scale each value
        }
        return scaledValues;
    }

    private float getTotal() {
        float total = 0;
        for (float val : this.values)
            total += val;
        return total;
    }

}