# Android学习之——Activity

什么是Activity？什么是Activity？如果你想知道什么是Activity的话我现在就带你研究。

## 啥是Activity？

说的通俗一点，当你打开手机应用的一瞬间，你就多多少少沾点Activity，手机应用通过UI让我们可以感知到Internet的美好，UI设计的好你看的舒服，用的也舒服，UI就包含在Activity之中，**Activity就是你和应用交互的那一种东西**，**一个程序包含一个或多个Activity**，你打开应用总不可能只有一个界面吧,那这个应用也太拉了。

## 怎么创建一个Activity？

最简单的方法就是你打开Android Studio，新建一个项目，选择Empty Activity，然后狂点next，最后finish，那么一个Activity就被你拿下了，如果你想要手动整一个Activity的话，你要去你的app目录下的AndroidManifest文件下注册你的Activity，你不注册那你就是“黑户“啊，没法使用，如果你用的是AndroidStudio添加的Activity，那你就不用操心注册流程了，应为这款软件已经帮你注册好啦!

![](C:\Users\Chinaqth\AppData\Roaming\Typora\typora-user-images\image-20210729223518483.png)

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

- 运行状态

  所谓的运行态最通俗的方式就是你能直接看到和操作界面的那个状态，当时的Activity正处于栈顶。

- 暂停状态

  你可以理解为这个界面被暂停了，不在处于返回栈的顶部，但没有被销毁，比如说界面突然弹出了一个对话框，你当前的界面被虚化了，而且你也无法在这个界面上进行点击交互，应为你现在要处理的是对话框中的内容。

- 停止状态

  Activity不在栈顶，而且完全不可见，这个时候它就进入了停止态，这个时候的Activity很有可能被系统回收，释放内存。

- 销毁状态

  Activity直接被返回栈移除了，系统就会前来回收这部分的空间。

### **Activity的生存期**

在Activity的生命周期中一共有7中方法，让你可以时时刻刻的了解Activity正处于哪一阶段。

- onCreate()

  你从Create这个单词就能看出来，这个就是指Activity刚刚被创建的时候调用的，它是用来加载你在layout.xml中编写好的布局，绑定你需要的时间（比如按下按钮就会巴拉巴拉之类的）.

- onStart()

  当你的Activity从可见变为不可见的时候就会调用.

- onResume()

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

### （插播）Intent

可能有人要问了，我们学的不是Activity吗？怎么半路闯出个Intent？谁啊？真不熟！别慌我们上面刚刚说到如果是两个Activity之间切换Activity的生命周期该如何变化，这不就来带你了解Activity之间是怎么切换的了嘛。

其实不仅仅是Activity，Intent几乎无时无刻不混迹于Android的四大组件之中



