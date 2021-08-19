# Android学习之——Activity

什么是Activity？什么是Activity？如果你想知道什么是Activity的话我现在就带你研究。

## 啥是Activity？

说的通俗一点，当你打开手机应用的一瞬间，你就多多少少沾点Activity，手机应用通过UI让我们可以感知到Internet的美好，UI设计的好你看的舒服，用的也舒服，UI就包含在Activity之中，**Activity就是你和应用交互的那一种东西**，**一个程序包含一个或多个Activity**，你打开应用总不可能只有一个界面吧,那这个应用也太拉了。

## 怎么创建一个Activity？

最简单的方法就是你打开Android Studio，新建一个项目，选择Empty Activity，然后狂点next，最后finish，那么一个Activity就被你拿下了，如果你想要手动整一个Activity的话，你要去你的app目录下的AndroidManifest文件下注册你的Activity，你不注册那你就是“黑户“啊，没法使用，如果你用的是AndroidStudio添加的Activity，那你就不用操心注册流程了，应为这款软件已经帮你注册好啦!

~~~java
<activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
~~~



` <action android:name="android.intent.action.MAIN" />`

`<category android:name="android.intent.category.LAUNCHER" />`

这两段标志着这个Activity是你的Activity入口，你的App一旦开启首界面就是这个Activty，

`<activity android:name=".MainActivity">`这个代表这你Activity的名字。

## UI和逻辑控制

Ok啦！现在你已经拥有了一个全新的Activity。这个时候你可能会问：我看到的界面是在哪里实现的呀？我和界面操作交互又是怎么实现的呀？

Activity可以分为**UI和逻辑**，UI也就是你所看到的界面，大多东西都被放在了main中的src中的一个**layout**的文件下，这个文件就是”布局文件“，在这里你将用xml格式去完善一个Activity的UI，而逻辑方面的操作则在main中的src的java/kotlin中。

## Activity的生命周期

### 返回栈

这里我们要探究的是当你的Activity在你打开界面，退出应用，切换其他应用或者是返回桌面时经历了什么，但是在这之前有必要让你知道**“返回栈”**这个概念，栈都是大家学数据结构时必不可少的知识点，当你启动了一个新Activity时，默认情况下它就会进入到这个返回栈中，如果此时栈中没有其他的Activity，那么你所创建的Activity就要到栈底，当你在当前的Activity上又跳转到了另一个Activity时，它也会进入到返回栈中，以此类推，处于最上面的Activity就是在栈顶，当时点击返回或者调用finish()去销毁一个Activity时，它就会出栈，在它下面的Activity就会变成栈顶的Activity。

### Activity的4种状态

- **运行状态**

  所谓的运行态最通俗的方式就是你能直接看到和操作界面的那个状态，当时的Activity正处于栈顶。

- 暂停状态

  你可以理解为这个界面被暂停了，不在处于返回栈的顶部，但没有被销毁，比如说界面突然弹出了一个对话框，你当前的界面被虚化了，而且你也无法在这个界面上进行点击交互，应为你现在要处理的是对话框中的内容。

- **停止状态**

  Activity不在栈顶，而且完全不可见，这个时候它就进入了停止态，这个时候的Activity很有可能被系统回收，释放内存。

- **销毁状态**

  Activity直接被返回栈移除了，系统就会前来回收这部分的空间。

### **Activity的生存期**

在Activity的生命周期中一共有7中方法，让你可以时时刻刻的了解Activity正处于哪一阶段。

> **正常情况**

- **onCreate()**

  你从Create这个单词就能看出来，这个就是指Activity刚刚被创建的时候调用的，它是用来加载你在layout.xml中编写好的布局，绑定你需要的时间（比如按下按钮就会巴拉巴拉之类的）.

- onStart()

  当你的Activity从可见变为不可见的时候就会调用.

- **onResume()**

  这个方法一旦被调用就代表你可以和你的界面进行交互啦，这里还有一个隐藏的信息：“你可以和界面交互”，还记得刚刚的提到的方法栈吗，对了！这就表明该Activity一定处于栈顶的位置。

- onPause()

  还是顾名思义，就是指Activity不处于栈顶了，被暂停了，在这个方法里常常要进行保存数据和资源的释放，要保证执行速度.

- onStop()

  Activity不可见了，停止了.

- onDestroy()

  表示当前的窗口要被返回栈移除了，释放资源。

- onRestart()

  就表示该窗口要重新启动了，这时怎么一回事呢？举一个最常见的例子，你从界面A点开界面B，再从B返回到界面A，这个时候界面A就会调用onRestart()方法重新启动了.

Ok，光说无用，我们可以用你的AndroidStudio来测试一下，我们先来体验一下单个Activity的生命周期，随便新建一个Empty Activity，你可别一个一个手打这7个方法嗷，那可就拉跨了，直接”唱跳rap篮球“（ctrl） + O，就可以找到你想要重写的方法啦！，在每个重写的方法体里Log一下，写上你想打印的话区分每个方法。

~~~java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity","onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity","onDestroy");
    }

}
~~~

然后，见证奇迹啦！（别忘了配置你的AVD）

你现在看到了如下：

```java
2021-08-01 10:42:32.632 20168-20168/com.example.notes_activity_test D/MainActivity: onCreate
2021-08-01 10:42:32.637 20168-20168/com.example.notes_activity_test D/MainActivity: onStart
2021-08-01 10:42:32.638 20168-20168/com.example.notes_activity_test D/MainActivity: onResume
```

看吧，当你的Activity第一次建立的时候就会调用这三个方法，那如果我们选择直接返回桌面呢？

~~~java
2021-08-01 10:46:04.067 20931-20931/com.example.notes_activity_test D/MainActivity: onPause
2021-08-01 10:46:04.511 20931-20931/com.example.notes_activity_test D/MainActivity: onStop
~~~

由于界面退出到了后台，不可见了所以会顺序执行onPause和onStop方法。那么如果我直接按下返回键呢？

~~~java
2021-08-01 10:47:43.867 20931-20931/com.example.notes_activity_test D/MainActivity: onPause
2021-08-01 10:47:44.757 20931-20931/com.example.notes_activity_test D/MainActivity: onStop
2021-08-01 10:47:44.758 20931-20931/com.example.notes_activity_test D/MainActivity: onDestroy
~~~

可一看到，界面不存在了，而且还调用了onDestroy方法销毁了这个Activity，这就表示它已经被移除返回栈了，系统将要回收资源了。

### （中间插入）Intent

可能有人要问了，我们学的不是Activity吗？怎么半路闯出个Intent？谁啊？真不熟！别慌我们上面刚刚说到是在两个Activity之间切换下Activity的生命周期该如何变化，这不就来带你了解Activity之间是怎么切换的了嘛。

其实不仅仅是Activity，Intent几乎无时无刻不混迹于Android的四大组件之中

Intent有两种分类：

> **显示Intent**

```public Intent(android.content.Context packageContext,Class<?> cls)```

显示Intent就是指当前的Activity跳转到你指名道姓的Activity，只要在Intent中传入当前Activity的Context，第二个参数就传入你想要跳转的Activity类名

> **隐式Intent**

你可以这么理解隐式intent,你想给一个人传达消息，但是不知道他的名字，你凭着这个人的身高、年龄、口音.....最终匹配到了你想找到的那个人并且把消息传给了他。这就是隐式Intent，这种Intent需要利用**”action“**和**”category“**来匹配你想要的Activity，你可以在AndroidManifest.xml文件中activity中的intent-filter来添加action和category。

~~~java
<activity android:name=".MainActivity2">
            <intent-filter>
                <action android:name="my_action"/>
                <category android:name="my_category"/>
                <category android:name="android.intent.category.DEFAULT"/>
            </intent-filter>
~~~

```public Intent(String action)```

```public Intent addCategory(String category)```

默认的category

```<category android:name="android.intent.category.DEFAULT"/>```

你可以自己自定义你的action和category。

将你的action和category传入到intent中，就能启动匹配到的activity。

> 利用Intent传递数据

```public Intent putExtra(String name,
public Intent putExtra(String name,String value)
public String getStringExtra(String name)
```

Intent除了开启界面之外，还可以传递指定的数据，利用**intent.putExtra（）**这一语句可以将指定的数据发送出去，在另一个activity中使用getIntent.getXxxExtra（）接收数据。要注意的是，putExtra使用的是传入键值对的方式，Key是你指定的键，利用这个唯一的键另外的Activity才能在其中提取出所需要的值。

那有了Intent具体怎么应用到Activity之间的切换的呢？这就要用到方法startActivity()了。

```public void startActivity(android.content.Intent intent)```

可见，startActivity方法中就传入了我们刚刚所说的intent，可以说这个方法就是开启了Activity之间交互大门的钥匙，intent就是告诉你开哪一个门，附带着数据。

其实，除了startActivity方法外还有一种方式叫做startActivityForResult，这个方法也是用于Activity之间的切换，但是这个方法会接收你开启Activity的返回值，在新开启的Activity中如果你想要在退出的时候返回一些数据，就可以在开始的Activity中使用这个方法，

```public void startActivityForResult(android.content.Intent intent,int requestCode)```

```protected void onActivityResult(int requestCode,int resultCode,Intent data)```

```public final void setResult(int resultCode,android.content.Intent data)```

利用setResult方法发送数据，在准备接收数据的Activity当中重写onActivityResult就可以接收数据啦,利用requestCode和resultCode来保证数据传递无误。

> Bundle

其实，应该在Intent之前就讲述Bundle的概念的，Bundle和Intent一样，都是Activity之间交换数据的一种工具，而且用法极其相似。

~~~java
Bundle bundle = new Bundle();
bundle.putExtra(Key key,Value value);
....
Intent intent = new Intent(Context context,Class <?> cls);
intent.putExtras(bundle);
startActivity(intent);
~~~

你会发现这个Bundle的用法和Intent几乎是一模一样的，同样是以键值对的方式传递数据，只是用Bundle时要把数据先给Bundle，再把bundle实例传到intent对象的PutExtras中。

### （续）Activity

我们刚刚初步学习了Intent的使用方法，就是为了更好的体验Activity的生命周期，我们在第一个Activity中使用Intent去启动另一个Activity，来观察A和B Activity来回切换时生存期的变化。

**A Activity**

~~~java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("A","onCreate");
        EditText editText = (EditText)findViewById(R.id.output);
        Button button = (Button)findViewById(R.id.changeActivity);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("A","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("A","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("A","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("A","onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("A","onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("A","onDestroy");
    }

}
~~~

**B Activity**

~~~java
public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d("B","onCreate");
        Button button = (Button)findViewById(R.id.finish);
        button.setOnClickListener(v -> finish());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("B","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("B","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("B","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("B","onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("B","onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("B","onDestroy");
    }
}
~~~

运行之后结果为:

~~~java
D/A: onCreate
D/A: onStart
D/A: onResume
~~~

刚刚开始，A Activity初始化的三部曲，现在点击跳转按钮

~~~java
D/A: onPause
D/B: onCreate
D/B: onStart
D/B: onResume
D/A: onStop
~~~

你会发现，先是A暂停了（Pause），之后B开始初始化三部曲，等B可以交互（Resume）A被停止（Stop），接下来，我在B中定义了一个按钮使用了finish()来结束B，点击之后出现如下结果：

~~~java
D/B: onPause
D/A: onRestart
D/A: onStart
D/A: onResume
D/B: onStop
D/B: onDestroy
~~~

B的活动和A跳转到B的过程很像，都是先进入onPause，但是A多了一个onRestart阶段，这就印证了A还在返回栈的底部，没有被销毁，当又要使用到A的时候会调用onRestart方法->onStart->onResume来恢复A，有一点要注意，那就是A的重新恢复并没有使用到onCreate方法。最后A为栈顶，B被移除并销毁。

总结：

> 1. A启动B:A.onPause()→B.onCreate()→B.onStart()→B.onResume()→A.onStop
> 2. B返回A：B.onPause()→A.onRestart()→A.onStart()→A.onResume()→B.onStop()
> 3. 再按Back键：A.onpause()→A.onStop()→A.onDestroy()

------



> 非正常情况

什么是非正常情况呢？就是当手机由于屏幕旋转或者被系统杀掉等情况下不是用户自己选择退出Activity。

这种情况下会调用onSaveInstanceState和onRestoreInstanceState

还是开始的代码，运行之后我们旋转屏幕

~~~java
D/A: onPause
D/A: onStop
D/A: onSaveInstanceState
D/A: onDestroy
D/A: onCreate
D/A: onStart
D/A: onRestoreInstanceState
D/A: onResume
~~~

Activity竟然被销毁了，在销毁之前调用了onSaveInstanceState方法保存当前的数据，在交互状态前使用了onRestoreInstanceState来恢复数据。

### Activity的启动模式

Activity的启动模式一共有4种，分别是standard、singleTop、singleTask、singlinstance。在AndroidManifest文件中通过给<activity>标签指定android:launchMode来选择你需要的启动方式。

- standard

  Activity的默认启动模式，在这种模式下每当你启动一个Activity都会在返回栈中添加这个Activity，你可以试一试让一个Activity自己通过startActivity启动自己，并在onCreate方法中Log this.toString，你会发现同样的Activity会被反复创建，创建了多少次你就要点击多少次退出键。

- singleTop

  singleTop就处理这种情况，当前Activity为栈顶时就不会在重复添加，但是，如果通过其他的Activity在startActivity，这样的话返回栈中又会有两个相同的Activity。

- singleTask

  singleTask保证了一个返回栈中只存在一个某个Activity的实例，如果你以A Activity开始，开启了B Activity，在用B Activity开启 A Activity，那么这个A还是你开始的那个Activity，这个例子中singleTask通过直接从返回栈中将B移除的方式，使得A重新回到栈顶。

- singleInstance

  这个模式非常特殊，使用了这个模式的Activity在启动时会单独创建一个返回栈来管理这个Activity，实现了多个Activity共享一个Activity，但是使用的时候也需要注意，这里也用一个例子来说明，A启动了singleInstance的B，B启动了C，那么由于B是被另一个返回栈所控制的，所以当你在**C中按下返回键会直接到A**，在按下返回键则会退出。

## 问题

- Bundle和Intent有什么区别，这两个同样用的是键值对的形式，那为什么不用HashMap实现？

  1. Bundle和Intent的作用都是一样的，负责在Activity之间传递数据，用法几乎相同，打开Intent的源码：

     ~~~java
     public @NonNull Intent putExtra(String name, char value) {
             if (mExtras == null) {
                 mExtras = new Bundle();
             }
             mExtras.putChar(name, value);
             return this;
         }
     ~~~

     其中的一个putExtra,可以看到，其实在intent传入键值对的方法中创建了Bundle对象来传输数据。

  2. 首先，Activity中使用Intent或者Bundle传递的都是体积较小的数据，而HashMap底层的数据结构是利用数组、链表，而Bundle是利用ArrayMAP实现的，在少量数据面前HashMap显得过于庞大，消耗太多资源，小巧的Bundle则更加适合这种体型数据的传输；其次，在某些时刻Intent可以传输对象，这就要求对象能被序列化，Intent使用的时Parcelable，而HashMap则使用Serializable，在Android中，使用前者的性能要优于后者，所以更加推荐Intent。

- 在Activity中如果有DiaLog弹出，那么当前的Activity生命周期会调用那个函数？

  什么也不会发生。

  
