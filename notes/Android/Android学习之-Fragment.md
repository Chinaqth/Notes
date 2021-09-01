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
