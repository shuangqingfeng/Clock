package com.feng.customClock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author feng
 * @Description: 自定义仿挂壁时钟
 * @date 2017/1/9
 */
public class CustomClock extends View {

    private static final String TAG = "CustomClock";
    private Paint mCircleBackgroundPaint;
    private int angleHour;
    private int angleMinute;
    private int angleSecond;
    private Timer timer;
    private TimerTask timerTask;
    private int mRadius;
    private int mWidth;
    private Paint mTickMarkerShortPaint;
    private Paint mDrawHandPaint;
    private Paint mPointPaint;
    private RectF mRectHour;
    private RectF mRectMinute;
    private RectF mRectSecond;

    public CustomClock(Context context) {
        this(context, null);
    }

    public CustomClock(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CustomClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mCircleBackgroundPaint = new Paint();
        mCircleBackgroundPaint.setAntiAlias(true);
        mCircleBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
        mCircleBackgroundPaint.setStyle(Paint.Style.FILL);//设置实心并且填充
        mCircleBackgroundPaint.setStrokeWidth(1);//设置线宽
        mCircleBackgroundPaint.setColor(Color.WHITE);//设置颜色
        mCircleBackgroundPaint.setStrokeJoin(Paint.Join.ROUND);
        mCircleBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        mTickMarkerShortPaint = new Paint();
        mTickMarkerShortPaint.setAntiAlias(true);
        mTickMarkerShortPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
        mTickMarkerShortPaint.setStyle(Paint.Style.FILL);//设置实心并且填充
        mTickMarkerShortPaint.setStrokeJoin(Paint.Join.ROUND);
        mTickMarkerShortPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawHandPaint = new Paint();
        mDrawHandPaint.setAntiAlias(true);
        mDrawHandPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
        mDrawHandPaint.setStyle(Paint.Style.FILL);//设置实心并且填充
        mDrawHandPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawHandPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawHandPaint.setStrokeWidth(30);
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
//        mPointPaint.setStyle(Paint.Style.FILL);//设置实心并且填充
        mPointPaint.setStrokeJoin(Paint.Join.ROUND);
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointPaint.setColor(Color.RED);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mRadius = mWidth / 2;
        mRectHour = new RectF(mRadius-5, mRadius*0.3f, mRadius+5, mWidth*0.6f);
        mRectMinute = new RectF(mRadius -5, mRadius *0.2f, mRadius+5, mWidth*0.65f);
        mRectSecond = new RectF(mRadius -5, mRadius *0.2f, mRadius+5, mWidth*0.65f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircleBackground(canvas);
        drawTickMarkerOrTime(canvas);
        //画时钟分钟秒钟
        drawHand(canvas, Color.BLACK, angleHour, mRectHour);
        drawHand(canvas, Color.BLACK, angleMinute , mRectMinute);
        drawHand(canvas, Color.RED, angleSecond , mRectSecond);
        drawCenterPoint(canvas);
    }

    /**
     * 绘制中心的圆点
     */
    private void drawCenterPoint(Canvas canvas) {
        canvas.save();
        canvas.drawCircle(mRadius, mRadius, 20, mPointPaint);//画中心的小圆点
        canvas.restore();
    }

    /**
     * 绘制指针
     */
    private void drawHand(Canvas canvas, int black, int degrees, RectF rectF) {
        canvas.save();
        mDrawHandPaint.setColor(black);
        canvas.rotate(degrees, mRadius, mRadius);
        canvas.drawRoundRect(rectF, 10, 10, mDrawHandPaint);
        canvas.restore();
    }

    /**
     * 绘制刻度 or 时间标记
     */
    private void drawTickMarkerOrTime(Canvas canvas) {
        canvas.save();
        int lineWidth = 0;
        // 画刻度 和时间
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                Log.d(TAG, "onDraw:当前时钟 " + i);
                String clockPoint = i / 5 + "";
                if (clockPoint.equals("0"))
                    clockPoint = 12 + "";
                lineWidth = 30;
                mTickMarkerShortPaint.setStrokeWidth(2);
                mTickMarkerShortPaint.setColor(Color.argb(225, 0, 0, 0));
                canvas.save();
                Rect rect1 = new Rect();
                mTickMarkerShortPaint.setTextSize(24);
                mTickMarkerShortPaint.getTextBounds(clockPoint, 0, clockPoint.length(), rect1);
                canvas.translate(mRadius, lineWidth + rect1.height() + 10);
                canvas.rotate(-6 * i);
                canvas.drawText(clockPoint, -rect1.width() / 2, rect1.height() / 2, mTickMarkerShortPaint);
                canvas.restore();
            } else {
                Log.d(TAG, "onDraw:当前钟 " + i);
                mTickMarkerShortPaint.setStrokeWidth(1);
                mTickMarkerShortPaint.setColor(Color.argb(125, 0, 0, 0));
                lineWidth = 20;
            }
            canvas.drawLine(mRadius, 0, mRadius, lineWidth, mTickMarkerShortPaint);
            canvas.rotate(6, mRadius, mRadius);
        }
        canvas.restore();
    }

    /**
     * 绘制背景圆
     */
    private void drawCircleBackground(Canvas canvas) {
        canvas.drawCircle(mRadius, mRadius, mRadius, mCircleBackgroundPaint);//画外圆
    }


    public void setCalendar() {
        timeStart();
        timerStart();
    }

    private void timerStart() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handByAngle();
                Log.d(TAG, "setCalendar角度: " + angleHour + "--->" + angleMinute + "----->" + angleSecond);
                postInvalidate();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);
    }

    private void timeStart() {
        handByAngle();
        invalidate();

    }

    private void handByAngle() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); //时
        int minute = calendar.get(Calendar.MINUTE); //分
        int second = calendar.get(Calendar.SECOND); //秒
        Log.d(TAG, "setCalendar时间: " + hour + "--->" + minute + "----->" + second);
        angleHour = (hour % 12) * 360 / 12;
        Log.d(TAG, "run时钟当前角度: " + angleHour);
        angleHour = (int) (0.5f * minute) + angleHour;//时钟随着分钟转动而转动
        Log.d(TAG, "run: 分钟" + (int) (0.5f * minute));
        Log.d(TAG, "run:时钟旋转角度：" + angleHour);
        //分针转过的角度
        angleMinute = minute * 360 / 60;
        angleMinute = (int) (0.1f * second) + angleMinute;//分钟随这秒钟而转动
        Log.d(TAG, "run分钟: " + angleMinute + "--->" + 0.1 * second + "---->" + angleMinute);
//                angleHour = (int) (angleHour+0.5*angleMinute);
        //秒针转过的角度
        angleSecond = second * 360 / 60;
    }
}
