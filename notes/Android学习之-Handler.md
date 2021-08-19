# Handler

今天，我们要开始学习Android中最最重要的内容——**Handler**

## 什么是Handler？

Handler是负责通信的类，具体一点是负责线程之间的通信，通过发送消息的方式给对应的handler，再利用handleMessage的方法来处理得到的消息。

```java
public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.Start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    for (int i = 0; i < 10; i++) {
                        try {
                            textView.setText(i + "");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                textView.setVisibility(View.GONE);
            }
        });

    }
}
```

以上述代码为例，你想在一个线程中每隔一秒设置一次UI，但是运行之后就会报错：

~~~java
android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
~~~

## 处理子线程更新UI

抛出的异常理由是只有主线程能更新视图，**在Android中只有主线程能更新UI**，那我们想要在线程中更新我们的UI怎么办呢？这个时候就要用到**Handler**了！

```java
public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.Start);
        Handler uiHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                        textView.setText(i + "");
                        i++;
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    for (int j = 0; j < 10; j++) {
                        try {
                            Message msg = uiHandler.obtainMessage();
                            uiHandler.sendMessage(msg);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }
}
```

可以看到我们在**主线程**中先创建了一个Handler实例，并且**重写了handleMessage方法**，这个方法是干什么的呢？把他翻译过来就知道啦！就是处理消息的逻辑操作，当我们的handler接收到发来的消息时就会去处理，并且通知主线程，再往下看，看看在子线程中我们做了那些事情呢？我们在子线程中**创建了一个消息对象**，有人可能要问为什么不直接new一个Message反而**用handler对象obtainMessage方法**呢?那是应为平凡的new 一个Message内存开销过大，容易造成内存抖动，使用obtainMessage方法会去现有的消息队列中取出消息，可以反复利用大大减轻了内存上的负担，最后调用主线程handler对象的sendMessage方法将这个消息发送出去，这个消息飘呀飘，飘到了主线程的消息队列之中，被Looper拿了出来最后在handleMessage中处理，怎么样？流程清楚了吧？

## 异步消息处理组成

前面我们简单的说了说Handle的基本用法，我们可以看到的是Handler，Message，但其实还有一些东西你们有看见。

整个异步消息处理的机制靠4样法宝组成：**Handler、Message、Looper、MessageQueue**。

- Handler ：用于接受消息（handleMessage）和发送消息（sendXxxMessage）。
- Message ：线程之间传递的消息，可以携带少量的信息。
- MessageQueue ：消息队列，用于存放消息的队列，**每个线程中有且仅有一个MessageQueue**。
- Looper ：有了Looper消息才能被取出来，Looper通过loop方法不断的从消息队列中取出消息，**每个线程有且仅有一个Looper**。

![img](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcxMTI0MTUxNzM3MDAw?x-oss-process=image/format,png)

## 深入Handler

当你想用线程A使用Handler向线程B发送消息时你可能会仿造上面的写法：

```java
button1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        new Thread(){
            @Override
            public void run() {
                Message msg = handler.obtainMessage(0);
                handler.sendMessage(msg);

            }
        }.start();
    }
});

Thread thread = new Thread(){
    @Override
    public void run() {

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                        Log.d("Handler", "Handle to A");
                        break;
                }
                return true;
            }
        });

    }
};
thread.start();
```

但你你会发现程序竟然直接报错了，我们康康他报的是什么错误：

**java.lang.RuntimeException: Can't create handler inside thread Thread[Thread-2,5,main] that has not called Looper.prepare()**

这里告诉你没用使用Looper.prepare()，你把这句话加上去重新运行程序，程序启动了，但是当你点击按钮时却接收不到Log里的信息，这是为什么呢？其实你不仅仅要加上prepare，还要加上Looper.loop()；这样在重新运行就可以了。

那么**为什么呢?**为什么要加上那两条语句呢？为什么我们前面就不用但是依然可以达到效果呢？这就要从源码中细细品尝了。

我们说过，一个线程只能有一个Looper和MessageQueue，我们在主线程中创建的Handler其实在源码里已经帮我们准备好了，这让我们无法察觉，但是子线程可不会提供给你，需要你手动创建才行，你要自己主备一个Looper,在开启loop方法从消息队列中把消息源源不断的拿出来。

### 准备Looper和MessageQueue

我们先来看看主线程里面的源码，我们知道main方法是程序的入口，在ActivityThread中就有程序的入口，在这个main方法中包我们准备好了Looper和消息队列。

```java
public static void main(String[] args) {
    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "ActivityThreadMain");

    // Install selective syscall interception
    AndroidOs.install();

    // CloseGuard defaults to true and can be quite spammy.  We
    // disable it here, but selectively enable it later (via
    // StrictMode) on debug builds, but using DropBox, not logs.
    CloseGuard.setEnabled(false);

    Environment.initForCurrentUser();

    // Make sure TrustedCertificateStore looks in the right place for CA certificates
    final File configDir = Environment.getUserConfigDirectory(UserHandle.myUserId());
    TrustedCertificateStore.setDefaultUserDirectory(configDir);

    // Call per-process mainline module initialization.
    initializeMainlineModules();

    Process.setArgV0("<pre-initialized>");

    Looper.prepareMainLooper();//准备Looper和MessageQueue

    // Find the value for {@link #PROC_START_SEQ_IDENT} if provided on the command line.
    // It will be in the format "seq=114"
    long startSeq = 0;
    if (args != null) {
        for (int i = args.length - 1; i >= 0; --i) {
            if (args[i] != null && args[i].startsWith(PROC_START_SEQ_IDENT)) {
                startSeq = Long.parseLong(
                        args[i].substring(PROC_START_SEQ_IDENT.length()));
            }
        }
    }
    ActivityThread thread = new ActivityThread();
    thread.attach(false, startSeq);

    if (sMainThreadHandler == null) {
        sMainThreadHandler = thread.getHandler();
    }

    if (false) {
        Looper.myLooper().setMessageLogging(new
                LogPrinter(Log.DEBUG, "ActivityThread"));
    }

    // End of event ActivityThreadMain.
    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
    Looper.loop();//不断取出消息

    throw new RuntimeException("Main thread loop unexpectedly exited");
}
```

### 取出消息

可以看到，main里面使用了Looper.prepareMainLooper()和Looper.loop()方法，让我们进一步看看。

```java
public static void prepareMainLooper() {
    prepare(false);
    synchronized (Looper.class) {
        if (sMainLooper != null) {
            throw new IllegalStateException("The main Looper has already been prepared.");
        }
        sMainLooper = myLooper();
    }
}
```

在prepareMainLooper里面又调用了两个方法prepare(false)和myLooper()，我们先看prepare：

```java
private static void prepare(boolean quitAllowed) {
    if (sThreadLocal.get() != null) {
        throw new RuntimeException("Only one Looper may be created per thread");
    }
    sThreadLocal.set(new Looper(quitAllowed));
}
```

我们发现在prepare中先会去查看Looper对象是否已经存在，这样就保证了Looper的为一性，若存在则抛出异常，若不存在则创建一个新的Looper。

```java
private Looper(boolean quitAllowed) {
    mQueue = new MessageQueue(quitAllowed);
    mThread = Thread.currentThread();
}
```

在new Looper（）中我们看到MessageQueue的创建。

Ok，到这里为止main已经准备好了Looper和MessageQueue了。

让我们把目光放到Looper.loop()中：

```java
    ...// 仅贴出关键代码
public static void loop() {
    // 1. 获取当前Looper的消息队列
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        
        final MessageQueue queue = me.mQueue;
        // 获取Looper实例中的消息队列对象（MessageQueue）

    // 消息循环
        for (;;) {
        
        // 2.1 从消息队列中取出消息
        Message msg = queue.next(); 
        if (msg == null) {
            return;
        }

        //派发消息到对应的Handler
        msg.target.dispatchMessage(msg);

    // 3. 释放消息占据的资源
    msg.recycle();
 }
```
在loop方法中我们看到，Looper在不断的从消息队列中使用next（）方法取出消息，再通过dispatchMessage（）方法把消息发到Handler中，Handler收到消息在handleMessage中做出处理。

### MessageQueue

其实看到这里，Handler的运行机制已经基本上有一个大概的感觉了，但是我们还要看看消息的机制。

```java
Message next() {
    int nextPollTimeoutMillis = 0;

    for (;;) {
        if (nextPollTimeoutMillis != 0) {
            Binder.flushPendingCommands();
        }

    // nativePollOnce方法在native层，若是nextPollTimeoutMillis为-1，此时消息队列处于等待状态　
    nativePollOnce(ptr, nextPollTimeoutMillis);

    synchronized (this) {
 
        final long now = SystemClock.uptimeMillis();
        Message prevMsg = null;
        Message msg = mMessages;

        // 出队消息，即 从消息队列中取出消息：按创建Message对象的时间顺序
        if (msg != null) {
            if (now < msg.when) {
                nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
            } else {
                // 取出了消息
                mBlocked = false;
                if (prevMsg != null) {
                    prevMsg.next = msg.next;
                } else {
                    mMessages = msg.next;
                }
                msg.next = null;
                if (DEBUG) Log.v(TAG, "Returning message: " + msg);
                msg.markInUse();
                return msg;
            }
        } else {

            // 若 消息队列中已无消息，则将nextPollTimeoutMillis参数设为-1
            // 下次循环时，消息队列则处于等待状态
            nextPollTimeoutMillis = -1;
        }

        ......
    }
       .....
   }
}
```
在next方法中我们发现在MessageQueue为空的情况其实会一直等待，直到有消息到来，再利用无限循环的方式把消息从消息队列中拿出来，把链表的消息删除，并返回这条消息，如此一来Looper就可以不断的取出消息啦！

那么消息是怎么来到消息队列的呢？

```java
boolean enqueueMessage(Message msg, long when) {
    if (msg.target == null) {
        throw new IllegalArgumentException("Message must have a target.");
    }

    synchronized (this) {
        if (msg.isInUse()) {
            throw new IllegalStateException(msg + " This message is already in use.");
        }

        if (mQuitting) {
            IllegalStateException e = new IllegalStateException(
                    msg.target + " sending message to a Handler on a dead thread");
            Log.w(TAG, e.getMessage(), e);
            msg.recycle();
            return false;
        }

        msg.markInUse();
        msg.when = when;
        Message p = mMessages;
        boolean needWake;
        if (p == null || when == 0 || when < p.when) {
            // New head, wake up the event queue if blocked.
            msg.next = p;
            mMessages = msg;
            needWake = mBlocked;
        } else {
            // Inserted within the middle of the queue.  Usually we don't have to wake
            // up the event queue unless there is a barrier at the head of the queue
            // and the message is the earliest asynchronous message in the queue.
            needWake = mBlocked && p.target == null && msg.isAsynchronous();
            Message prev;
            for (;;) {
                prev = p;
                p = p.next;
                if (p == null || when < p.when) {
                    break;
                }
                if (needWake && p.isAsynchronous()) {
                    needWake = false;
                }
            }
            msg.next = p; // invariant: p == prev.next
            prev.next = msg;
        }

        // We can assume mPtr != 0 because mQuitting is false.
        if (needWake) {
            nativeWake(mPtr);
        }
    }
    return true;
}
```

消息会通过时间的先后顺序将消息插入到MessageQueue的链表当中。

