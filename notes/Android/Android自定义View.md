# Android自定义View

## 自定义View的种类

自定义View有三种：

- 通过继承View自定义View
- 通过继承ViewGroup自定义ViewGroup
- 自定义控件

## 通过继承View自定义View

最基本的自定义View分为几个步骤：

1.获取属性（如果有自定义属性的话），在value下定义自定义View的属性文件，在构造方法中利用context的obtainStyledAttributes方法获取TypedArray对象，利用该对象的getXxx方法获取到对应的属性值，**但是,在使用完之后一定要记得回收（recycle）**

~~~java
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="MyCustomView">
        <attr name="custom_color" format="color"/>
    </declare-styleable>
</resources>
~~~

~~~java
TypedArray typedArray = context.obtainStyledAttributes(R.styleable.MyCustomView);
mColor = typedArray.getColor(R.styleable.MyCustomView_custom_color, Color.YELLOW);
typedArray.recycle();
~~~

2.初始化画笔Paint

在构造方法中获取到了自定义属性之后就可以初始化画笔了，你可以定义画笔的颜色，粗细，风格（描边或者填充）等等。

~~~java
private void initPaint(){
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(1.5f);
    }
~~~

**3.重写onMeasure、onLayout方法：**

在比较高级的自定义View中常常伴随着这两个方法的重写，他们决定了View的大小和位置。

**4.重写onDraw方法：**

在这个方法中我们使用画笔配合canvas对象来绘制出我们想要的图形（基本图形如线条、圆、矩形等等）。

~~~java
@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        canvas.drawRect(0,0,width,height,mPaint);
    }
~~~

### 运用

这里写一个自定义View，实现一个textView在wrap_content的居中显示：

~~~java
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="MyTextCustomView">
        <attr name="android:textSize"/>
        <attr name="text_color" format="color"/>
        <attr name="custom_text" format="string"/>
    </declare-styleable>
</resources>
~~~

~~~java
package com.example.mycustomview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class MyTextCustomView extends View {

    private int mColor;
    private String mText;
    private int mTextSize;
    private  Paint mPaint;
    public MyTextCustomView(Context context) {
        super(context);
        //1.获取自定义属性
        TypedArray array = context.obtainStyledAttributes(R.styleable.MyTextCustomView);
        mColor = array.getColor(R.styleable.MyTextCustomView_text_color, Color.BLUE);
        mText = array.getString(R.styleable.MyTextCustomView_custom_text);
        mTextSize = array.getDimensionPixelSize(R.styleable.MyTextCustomView_android_textSize,24);
        array.recycle();
        //2.初始化画笔
        initPaint();

    }

    public MyTextCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //1.获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.MyTextCustomView);
        mColor = array.getColor(R.styleable.MyTextCustomView_text_color, Color.BLUE);
        mText = array.getString(R.styleable.MyTextCustomView_custom_text);
        mTextSize = array.getDimensionPixelSize(R.styleable.MyTextCustomView_android_textSize,24);
        array.recycle();
        //2.初始化画笔
        initPaint();
    }

    public MyTextCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //1.获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.MyTextCustomView);
        mColor = array.getColor(R.styleable.MyTextCustomView_text_color, Color.BLUE);
        mText = array.getString(R.styleable.MyTextCustomView_custom_text);
        mTextSize = array.getDimensionPixelSize(R.styleable.MyTextCustomView_android_textSize,24);
        array.recycle();

        //2.初始化画笔
        initPaint();
    }

    private void initPaint(){
        mPaint = new TextPaint();
        mPaint.setStrokeWidth(10f);
        mPaint.setColor(mColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);
    }


    //3.重写onMeasure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽高的Mode
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取宽高的Size
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //最终的测量值
        int resultW = widthSpecSize;
        int resultH = heightSpecSize;

        //内容值
        int contentW = 0;
        int contentH = 0;


        //设定wrap_content下的值，也就是AT_MOST
        if (widthSpecMode == MeasureSpec.AT_MOST){
            if (!TextUtils.isEmpty(mText)){
                //测量text的宽度
                contentW = (int) mPaint.measureText(mText);
                //考虑到padding值
                contentW += getPaddingLeft() + getPaddingRight();
                resultW = Math.min(contentW,widthSpecSize);
            }
        }
        if (heightSpecMode == MeasureSpec.AT_MOST){
            if (!TextUtils.isEmpty(mText)){
                //获取text的高度
                contentH = mTextSize;
               	//考虑到padding值
                contentH += getPaddingTop() + getPaddingBottom();
                resultH = Math.min(contentH,heightSpecSize);
            }
        }
		
        setMeasuredDimension(resultW,resultH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        //获取View的中心点
        int cx = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        int cy = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;

        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        cy += metrics.descent;

        //绘制text
        canvas.drawText(mText,cx,cy,mPaint);
    }
}

~~~



## 通过继承ViewGroup自定义ViewGroup

自定义View Group和自定义View的区别就在于**自定义ViewGroup必须重写onLayout方法**，原因是：由于ViewGroup里面包含的子View是不确定的，所以Android不没有实现onLayout方法，而是把它交给我们去处理，各个子View的分布。

onMeasure：和自定义View一样，唯一不同的地方同样在于子View的测量，在warp_content下ViewGroup的大小就是自身padding加上子View的宽高加上子View的margin。

### 运用

~~~java
package com.example.mycustomview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyViewGroup extends ViewGroup {
    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMod = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMod = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int resultW = widthSize;
        int resultH = heightSize;

        int contentW = getPaddingLeft() + getPaddingRight();
        int contentH = getPaddingTop() + getPaddingBottom();
        //测量子View，必不可少
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams layoutParams = null;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            layoutParams = (MarginLayoutParams) child.getLayoutParams();

            if (child.getVisibility() == GONE) {
                continue;
            }

            contentW += child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            contentH += child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

        }

        if (widthMod == MeasureSpec.AT_MOST) {
            resultW = Math.min(contentW, widthSize);
        }

        if (heightMod == MeasureSpec.AT_MOST) {
            resultH = Math.min(contentH, heightSize);
        }

        setMeasuredDimension(resultW, resultH);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int topStart = getPaddingTop();
        int leftStart = getPaddingLeft();

        int childW = 0;
        int childH = 0;
        MarginLayoutParams layoutParams = null;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            layoutParams = (MarginLayoutParams) child.getLayoutParams();
            if (child.getVisibility() == GONE) {
                continue;
            }

            childW = child.getMeasuredWidth();
            childH = child.getMeasuredHeight();

            leftStart += layoutParams.leftMargin;
            topStart += layoutParams.topMargin;

            child.layout(leftStart, topStart, leftStart + childW, topStart + childH);

            leftStart += childW + layoutParams.rightMargin;
            topStart += childH + layoutParams.bottomMargin;
        }


    }

}


~~~

