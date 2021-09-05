# Fragment

Fragment翻译过来就是碎片，Fragment依附于Activity，所以Fragment也有自己的生命周期。

## Fragment的生命周期

fragment的生命周期中的方法有：

onAttach（）、onCreate（）、onCreateView（）、onActivityCreated（），onStart（），onResume（）、onPause（）、onStop（）、onDestroyView（）、onDestroy（）、onDetach（）。

其中有很多的方法我们在学Activity中也看到过就不多介绍了，其中onAttach和onDetach方法是和Activity绑定时调用的，当Activity创建完成时会调用onActivityCreated（），在观察生命周期前我们还要知道Fragment是怎么使用的。

## Fragment的使用

我们在学习广播的时候知道有静态加载和动态加载之分，Fragment也一样。

- 静态加载：

  所谓的静态加载就是指在xml文件中直接指定一个fragment，待会我们观测其生命周期时就会采用这种方法。

- 动态加载：

  这个时候我们要获取FragmentManager对象，并使用其beginTransaction方法开启事物，通过add的方式将Fragment动态加载。

## Fragment的生命周期

我们使用静态加载的方式来观察一个fragment的生命周期：

我们先得制定一个自己的Fragment，这就意味着需要分别创建xml来布局fragment和java来将布局导入。

Fragment.xml:

```java
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:text="@string/fragment_one"
        android:id="@+id/textView"
        android:gravity="center"/>

</RelativeLayout>
```

在布局文件中我们简单的添加了一段TextView来显示我们的Fragment。

Fragment.java:

```java
public class FragmentOne extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        return view;
    }
}
```

在其中我们重写了onCreateView方法，在里面使用inflater.inflate加载我们写好的Fragment布局并返回即可。

现在我们只需要在activity_main.xml中添加一个fragment标签，并且在其中导入我们刚刚写的Fragment.java

activity_main.xml:

```java
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"

    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <fragment
        android:name="com.example.myfragment.FragmentOne"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/fragment_one"/>
</RelativeLayout>
```

这样运行以后，我们就可以看到我们写好的Fragment了。

在拥有一个fragment之后我们重写他的生命周期会调用到的方法，连带着Activity一起来观察整个过程中方法的调用顺序。

Fragment:

```java
package com.example.myfragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentOne extends Fragment {

    private final String TAG = "QTH";
    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "F_onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "F_onCreate: ");
        super.onCreate(savedInstanceState);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "F_onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "F_onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "F_onStart: ");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "F_onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "F_onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "F_onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "F_onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "F_onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "F_onDetach: ");
    }
}

```

MainActivity:

```java
package com.example.myfragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "QTH";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "A_onCreate: ");
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "A_onStart: ");
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "A_onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "A_onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "A_onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "A_onDestroy: ");
    }
}
```

让我们来运行看看有什么结果(Mac运行结果）：

~~~java
2021-09-02 04:42:17.665 8745-8745/com.example.myfragment D/QTH: F_onAttach: 
2021-09-02 04:42:17.665 8745-8745/com.example.myfragment D/QTH: F_onCreate: 
2021-09-02 04:42:17.665 8745-8745/com.example.myfragment D/QTH: F_onCreateView: 
2021-09-02 04:42:17.668 8745-8745/com.example.myfragment D/QTH: A_onCreate: 
2021-09-02 04:42:17.670 8745-8745/com.example.myfragment D/QTH: A_onStart: 
2021-09-02 04:42:17.670 8745-8745/com.example.myfragment D/QTH: F_onActivityCreated: 
2021-09-02 04:42:17.670 8745-8745/com.example.myfragment D/QTH: F_onStart: 
2021-09-02 04:42:17.672 8745-8745/com.example.myfragment D/QTH: A_onResume: 
2021-09-02 04:42:17.672 8745-8745/com.example.myfragment D/QTH: F_onResume: 
~~~

在按下返回键时的调用：

~~~java
2021-09-02 04:42:17.672 8745-8745/com.example.myfragment D/QTH: A_onResume: 
2021-09-02 04:42:17.672 8745-8745/com.example.myfragment D/QTH: F_onResume: 
2021-09-02 04:46:24.355 8745-8745/com.example.myfragment D/QTH: F_onPause: 
2021-09-02 04:46:24.355 8745-8745/com.example.myfragment D/QTH: A_onPause: 
2021-09-02 04:46:25.049 8745-8745/com.example.myfragment D/QTH: F_onStop: 
2021-09-02 04:46:25.049 8745-8745/com.example.myfragment D/QTH: A_onStop: 
~~~

杀掉程序：

~~~java
2021-09-02 04:46:33.786 8745-8745/com.example.myfragment D/QTH: F_onDestroyView: 
2021-09-02 04:46:33.788 8745-8745/com.example.myfragment D/QTH: F_onDestroy: 
2021-09-02 04:46:33.788 8745-8745/com.example.myfragment D/QTH: F_onDetach: 
2021-09-02 04:46:33.789 8745-8745/com.example.myfragment D/QTH: A_onDestroy: 
~~~

那我们再来看看当Fragment之间切换时生命周期的变化吧，要实现Fragment的切换我们可以使用动态加载Fragment的方法，这里我们新建一个Fragment。将原来main.xml文件中的fragment替换为framelayout来承接fragment。

FragmentTwo.java:

```JAVA
public class FragmentTwo extends Fragment {


    private final String TAG = "QTH";
    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "FB_onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "FB_onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "FB_onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "FB_onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "FB_onStart: ");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "FB_onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "FB_onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "FB_onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "F_onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "F_onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "F_onDetach: ");
    }
}
```

我们在Activity中加入如下代码：

```java
FragmentManager manager = getSupportFragmentManager();
FragmentTransaction transaction = manager.beginTransaction();
transaction.add(R.id.frame,new FragmentOne());
transaction.commit();
button = findViewById(R.id.button);
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,new FragmentTwo());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
});
```

可以看到动态加载Fragment需要用到Fragment Manager，并且利用Manager对象来开启一个事务，在事物中常见的有add(),remove(),hide(),show()方法，其中我们看到的replace方法本质上就是先remove该Fragment在add一个新的Fragment。我们来看一下在使用replace方法下Fragment生命周期的变化吧：

~~~Java
2021-09-02 05:08:20.491 9391-9391/com.example.myfragment D/QTH: FB_onAttach: 
2021-09-02 05:08:20.492 9391-9391/com.example.myfragment D/QTH: FB_onCreate: 
2021-09-02 05:08:20.493 9391-9391/com.example.myfragment D/QTH: F_onPause: 
2021-09-02 05:08:20.493 9391-9391/com.example.myfragment D/QTH: F_onStop: 
2021-09-02 05:08:20.494 9391-9391/com.example.myfragment D/QTH: F_onDestroyView: 
2021-09-02 05:08:20.495 9391-9391/com.example.myfragment D/QTH: FB_onCreateView: 
2021-09-02 05:08:20.498 9391-9391/com.example.myfragment D/QTH: FB_onActivityCreated: 
2021-09-02 05:08:20.499 9391-9391/com.example.myfragment D/QTH: FB_onStart: 
2021-09-02 05:08:20.499 9391-9391/com.example.myfragment D/QTH: FB_onResume: 
~~~

我们看到，另一个Fragment先执行了onAttach和onCreate方法，原来的Fragment执行了onPause一直到onDestroyView方法，那我们来看下使用hide方法将第一个Fragment隐藏起来时的生命周期吧

~~~java
2021-09-02 05:14:15.558 9678-9678/com.example.myfragment D/QTH: FB_onAttach: 
2021-09-02 05:14:15.561 9678-9678/com.example.myfragment D/QTH: FB_onCreate: 
2021-09-02 05:14:15.562 9678-9678/com.example.myfragment D/QTH: FB_onCreateView: 
2021-09-02 05:14:15.565 9678-9678/com.example.myfragment D/QTH: FB_onActivityCreated: 
2021-09-02 05:14:15.569 9678-9678/com.example.myfragment D/QTH: FB_onStart: 
2021-09-02 05:14:15.569 9678-9678/com.example.myfragment D/QTH: FB_onResume: 
~~~

这个时候因为第一个Fragment为隐藏状态，所以不会执行onPause到onDestroyView。另外我们还用了一句fragmentTransaction.addToBackStack(null);来为Fragment得到了一个返回栈，如果没有这个语句，那么按下返回键会直接退出程序，有了这个我们按下返回键会返回到上个的Fragment。

Fragment的用法还有很多，可以结合ViewPager和TabLayout使用达到更加炫酷的效果。

