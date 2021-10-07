# Android进阶之光—View

## View坐标系

在深入了解View之前我们来说说View的坐标系。![img](https://img2.baidu.com/it/u=347646891,243698573&fm=26&fmt=auto)

​													**view坐标系**

我们可以看到有很多的方法，我们可以利用这些方法获得View的高度、宽度，点击的视图坐标和绝对坐标。

### View获取自身的高宽

> **weigh = getRight（） - getLeft（）；**
>
> **high = getBottom（） - getTop（）；**

我们通过程序来验证一下：

```java
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="156dp"
        android:layout_marginLeft="156dp"
        android:layout_marginTop="292dp"
        android:text="button"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

```java
public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int left = button.getLeft();
        int right = button.getRight();
        int bottom = button.getBottom();
        int top = button.getTop();
        Log.d("QQQ", "left: " + left + ", right" + right + ", bottom: " + bottom
                + ",top: " + top + "weigh " + button.getWidth() + ", high: " + button.getHeight());
    }
}
```

注意不要在onCreate中打印View的各项值，应为这个状态下View的还没有绘制完成，可以使用view的post或者onWindowFoucsChanged等方法中打印输出。

~~~java
 D/QQQ: left: 429, right 691, bottom: 935, top: 803, weigh 262, high: 132
~~~

我们可以看出，View的宽度就等于getRight-getLeft，高度就等于getBottom-getTop，当然也可以直接使用封装好的方法:getWidth和getHeight。

### MotionEvent方法

在onTouchEvent中提供的方法可以让我们判断出触摸的点到控件和到屏幕的距离，他们分别是getX、getY、getRawX和getRawY，前两个方法负责测量到控件的距离，后两个是测出到屏幕的距离。

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    button = findViewById(R.id.button);

    button.setOnTouchListener((v, event) -> {

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN){
            Log.d("QQQ", "getX: " + event.getX() + ", getY: " + event.getY() + ", getRawX: " + event.getRawX() + ", getRawY: "
                    + event.getRawY());
        }


        return false;
    });
}
```

~~~java
 D/QQQ: getX: 39.97339, getY: 27.922852, getRawX: 468.9734, getRawY: 1050.9229
 D/QQQ: getX: 41.983887, getY: 89.91321, getRawX: 470.9839, getRawY: 1112.9132
 D/QQQ: getX: 130.97314, getY: 93.9104, getRawX: 559.97314, getRawY: 1116.9104
 D/QQQ: getX: 195.96826, getY: 58.951904, getRawX: 624.96826, getRawY: 1081.9519
~~~

## View的移动

我们可以使用6种不同的方法达到移动一个View的效果。

### layout

在View布局的时候会调用layout子方法来将我们的View布局，所以，我们在onTouchEvent中的ACTION_MOVE下使用layout方法对View进行重新布局就可一达到移动View的效果了。

```java
@Override
public boolean onTouchEvent(MotionEvent event) {
    int x = (int) event.getX();
    int y = (int) event.getY();
    switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
            lastX = x;
            lastY = y;
            break;
        case MotionEvent.ACTION_MOVE:
            int offsetX = x - lastX;
            int offsetY = y - lastY;
            //方法1
            layout(getLeft() + offsetX,getTop() + offsetY,getRight() + offsetX,getBottom() + offsetY);
            break;
    }
    return true;
}
```

### offsetLeftAndRight和offsetTopAndBottom

使用这两个方法也可以实现和layout一样的效果。

~~~java
@Override
public boolean onTouchEvent(MotionEvent event) {
    int x = (int) event.getX();
    int y = (int) event.getY();
    switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
            lastX = x;
            lastY = y;
            break;
        case MotionEvent.ACTION_MOVE:
            int offsetX = x - lastX;
            int offsetY = y - lastY;
            //方法2
            offsetLeftAndRight(offsetX);
            offsetTopAndBottom(offsetY);
            break;
    }
    return true;
}
~~~

 
