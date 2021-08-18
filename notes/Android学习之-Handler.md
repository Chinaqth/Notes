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

