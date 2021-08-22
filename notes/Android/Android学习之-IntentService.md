# IntentService

前面的篇章中我们学习了Handler，HandlerThread以及Service，今天我们来看看这个IntentService是啥。

## IntentService是啥？

我们知道Service可以在后台长时间的运行，Activity也可以绑定Service的形式来控制Service做一些耗时操作。我们知道如果要在Service中完成一系列操作你需要开启这个Service并且在任务完成之后停止该活动，使用stopService或者stopSelf来停止，但是自己完成这一系列操作比较麻烦，于是就有了IntentService，IntentService也是Service，它可以帮忙处理一些任务，并且在结束之后自动停止，不需要你的操心。

## 举例分析

我们现在创建了一个IntentService类，继承了IntentService，重写了onHandleIntent方法。我们希望在这个方法中可以做一些UI操作。

```java
public class MIntentService extends IntentService {
    private static final String TAG = "MyIntentService";
    private static final String ACTION = "From_MyIntentService";

    public MIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: ");
        if (intent != null){
            Intent mIntent = new Intent(ACTION);
            Log.d(TAG, mIntent.getAction());
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread());
                    mIntent.putExtra("intent",count);
                    sendBroadcast(mIntent);
                    ++count;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "send: ");
        }

    }


<-----------------------------------------------**查看生命周期**--------------------------------------------->

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.d(TAG, "onStart: ");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return super.onBind(intent);
    }
}
```

我们看到在onHandleIntent方法中接受的参数为intent，我们拿到了这个intent之后我们new一个Intent对象，使用putExtra方法加入数据，每一秒加一次，最后使用广播的方式将intent发送出去。

来看看Activity方法：

```java
public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("From_MyIntentService");
        registerReceiver(broadcastReceiver,intentFilter);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,MIntentService.class);
            startService(intent);
        });

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            if (intent.getAction().equals("From_MyIntentService")){
                int extra = intent.getIntExtra("intent",0);
                Log.d(TAG, "onReceive: " + extra);
                String count = extra + "";
                textView.setText(count);
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
```

我们在Activity中定义了intentFilter，在其中添加我们的Action，定义了广播接收器broadcastReceiver，在onReceive方法中将从onHandleIntent传来的intent的数据提取出来然后更新UI，最后我们使用registerReceiver动态注册了这个广播，在onDestroy方法中使用unregisterReceiver来取消注册。

## 源码分析

```java
private volatile Looper mServiceLooper;
@UnsupportedAppUsage
private volatile ServiceHandler mServiceHandler;
private String mName;
private boolean mRedelivery;

private final class ServiceHandler extends Handler {
    public ServiceHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        onHandleIntent((Intent)msg.obj);
        stopSelf(msg.arg1);
    }
}
```

来看看onCreate方法：

```java
@Override
public void onCreate() {
    // TODO: It would be nice to have an option to hold a partial wakelock
    // during processing, and to have a static startService(Context, Intent)
    // method that would launch the service & hand off a wakelock.

    super.onCreate();
    HandlerThread thread = new HandlerThread("IntentService[" + mName + "]");
    thread.start();

    mServiceLooper = thread.getLooper();
    mServiceHandler = new ServiceHandler(mServiceLooper);
}
```

我们看到在onCreate方法中定义了一个HandlerThread对象并启动，将mServiceLooper初始化，用的是HandlerThread的Looper，接着初始化自定义的Handler对象。

接着来看看onStartCommand方法：

```java
@Override
public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
    onStart(intent, startId);
    return mRedelivery ? START_REDELIVER_INTENT : START_NOT_STICKY;
}
```

其中调用了onStart方法，那么让我们来看看onStart方法吧：

```java
@Override
public void onStart(@Nullable Intent intent, int startId) {
    Message msg = mServiceHandler.obtainMessage();
    msg.arg1 = startId;
    msg.obj = intent;
    mServiceHandler.sendMessage(msg);
}
```

在onStart方法中我们从前面mServiceHandler对象中取出消息，将传入的startId，和intent对象数据传给拿到的消息，再把消息发送到mServiceHandler里面，这个处理的过程是在HandlerThread线程中完成的，应为我们的mServiceHandler持有的是HandlerThread对象的Looper	。那么我们要来看看mServiceHandler的handleMessage方法了。

```java
private final class ServiceHandler extends Handler {
    public ServiceHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        onHandleIntent((Intent)msg.obj);
        stopSelf(msg.arg1);
    }
}
```

我们看到，handleMessage中拿到了消息把消息的ojb转为Intent类型，交给onHandleIntent方法处理，最后调用stopSelf（msg.arg1）来停止任务。注意，这里使用的是**stopSelf（msg.arg1）**，而不是stopSelf（），因为使用stopSelf（）会立即停止，而stopSelf（msg.arg1）会等到消息处理完毕后再停止。

