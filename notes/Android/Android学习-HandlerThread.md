# HandlerThread

## 什么是HandlerThread？

HandlerThread看起来和handler有关又看起来和线程有关，那么他到底是个啥呢？**HandlerThread本质是线程**，他继承了Thread类，有自己的run方法，也是用start方法启动线程，那么他和Handler有什么关系呢？其实在HandlerThread中已经帮我们准备好了Looper和MessageQueue，不用我们手动的去调用原来的哪几种方法了，现在我们举个例子来看看HandlerThread是怎么用的。

## 举例

```java
public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView;
    private static final int HANDLE = 1;
    private static final int HANDLEThread = 2;
    Handler UiHandler;
    Handler workHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        //准备HandlerThread
        HandlerThread mHandleThread = new HandlerThread("mHandlerThread");
        //启动HandlerThread
        mHandleThread.start();
        //初始化子线程
        workHandler = new Handler(mHandleThread.getLooper())
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == HANDLEThread){
                    Message message = UiHandler.obtainMessage();
                    message.what = HANDLE;
                    message.obj = "Hello";
                    //发送消息给UI线程
                    UiHandler.sendMessage(message);
                    Log.d("UI", "handleMessage: ");
                }
            }
        };

        //修改UI
        UiHandler = new Handler(msg -> {
            Log.d("Receive", msg.what + "");
            if (msg.what == HANDLE){
                String str = (String) msg.obj;
                Log.d("handleMessage", str);
                textView.setVisibility(View.VISIBLE);
                textView.setText(str);
            }
            return true;
        });

        button.setOnClickListener(v -> {
            Message msg = workHandler.obtainMessage();
            msg.what = HANDLEThread;
            //发送消息给子线程
            workHandler.sendMessage(msg);
        });

    }
}
```

首先，我们定义了一个HandlerThread线程，并且直接start，然后创建了workHandler来代表工作线程中的Handler，又定义了一个UiHandler，在workThread中我们使用的是mHandleThread.getLooper()，从HandlerThread中直接获取到了Looper，在工作线程中使用workHandler发送消息通知主线程更新UI。

## 原理

在例子里面我们用到了HandlerThread的start方法，还有getLooper方法，我们来进一步分析一下HandlerThread的源码看看这其中的奥秘。

```java
@Override
public void run() {
    mTid = Process.myTid();
    Looper.prepare();
    synchronized (this) {
        mLooper = Looper.myLooper();
        notifyAll();
    }
    Process.setThreadPriority(mPriority);
    onLooperPrepared();
    Looper.loop();
    mTid = -1;
}
```

可以发现在run中调用了Looper.prepare();以及Looper.loop();方法，这两个方法我们在看过Handler的源码之后就很容易理解了。但是注意：

~~~java
synchronized (this) {
        mLooper = Looper.myLooper();
        notifyAll();
    }
~~~

在这个同步语句块中拿到了Looper之后还使用了notifyAll的方法，为什么要使用这个方法呢？要唤醒谁呢？我们接着看getLooper方法：

```java
public Looper getLooper() {
    if (!isAlive()) {
        return null;
    }
    
    // If the thread has been started, wait until the looper has been created.
    synchronized (this) {
        while (isAlive() && mLooper == null) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }
    return mLooper;
}
```

原来在这里，因为我们在主线程中使用了getLooper方法，所以要先等待，等待HandlerThread把Looper拿到之后才能正常的使用，所以两边就是用了wait和notifyAll方法。