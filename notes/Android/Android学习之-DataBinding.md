# DataBinding

记得再一次不经意之间打开了AS自动创建的activity时候惊讶的发现这里面居然没有我们常用的“findViewById”的方法，结果调查后发现其实是用到了JetPack里面的DataBinding，使用了DataBinding之后我们就不需要每次都是用findViewById，一定程度上分离了activity和view，我们这次使用传统方式和DataBinding分别看看两者的区别吧。

## 传统

在这两种用法之中我们都是用JetPack的ViewModel和LiveData来分离Activity数据和View

MyViewModel：

```java
public class MyViewModel extends ViewModel {

    MutableLiveData<Integer> number;

    public MutableLiveData<Integer> getNumber(){
        if (number == null){
         number = new MutableLiveData<>();
         number.setValue(0);
        }
        return number;
    }

    public void add(){
        number.setValue(number.getValue() + 1);
    }

}
```

MainActivity：

```java
public class MainActivity extends AppCompatActivity {
    private  TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.Button);
        MyViewModel myViewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(MyViewModel.class);

        myViewModel.getNumber().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textView.setText(String.valueOf(integer));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewModel.add();
            }
        });

    }
}
```

可以看见，只要我们的布局文件中View有id我们就要使用findViewById来将Activity和View绑定，像Button这样的按键我们还要写点击事件。那么让我们来看看使用DataBinding会带来哪些便捷之处呢？

## DataBinding

layout.xml:

```java
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/textView"
            android:textSize="24sp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:id="@+id/Button"
            android:text="@string/add"
            app:layout_constraintTop_toBottomOf="@+id/textView" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

首先我们可以看到我们的布局文件的格式发生了变化。并且，如果一个xml文件使用了DataBinding的格式，那么就会生成一个名为“xml文件名Binding”的类，我们所有的View都在这个类里可以拿来使用，使用binding = DataBindingUtil.setContentView(this,R.layout.activity_main);来绑定binding和activity。

```java
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        MyViewModel myViewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(MyViewModel.class);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(this);

    }
}
```

在xml文件的data中加入：

```java
<data>
    <variable
        name="data"
        type="com.example.mydatabinding.MyViewModel" />
</data>
```

修改textView和Button

```java
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:id="@+id/textView"
    android:textSize="24sp"
    android:text="@{String.valueOf(data.number)}"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent" />

<Button
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:id="@+id/Button"
    android:text="@string/add"
    android:onClick="@{() -> data.add()}"
    app:layout_constraintTop_toBottomOf="@+id/textView" />
```

我们在TextView中绑定了data的number，在Button中绑定了点击事件。

这样就方便了许多。

