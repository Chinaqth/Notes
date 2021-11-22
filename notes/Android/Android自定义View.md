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

