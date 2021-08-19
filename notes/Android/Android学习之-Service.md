# Android学习之-Service

## 什么是Service？

Service是安卓的四大组件之一，它在进程存在时一直运行在后台，附于UI线程之上，不能进行耗时操作，否则会出现ANR的情况。

## Service和Thread的区别

从运行的线程上来看，Service运行在UI线程中，Thread运行在工作线程中，从广度上来看，一个Service可以和多个Activity进行连接，通过在onBind()可以配合Thread一起使用，使得Activity能让Service做一些耗时的操作，而Thread只能绑定一个Activity，而且其他的Activity并不能对其做出操作，很难控制线程。

![img](https://upload-images.jianshu.io/upload_images/944365-ad8ff95781d19451.png?imageMogr2/auto-orient/strip|imageView2/2/format/webp)

## Service的生命周期

在没有绑定Activity之前，Service的生命周期大致有3个阶段：

> **onCreate() -> onStartCommand(Intent intent, int flags, int startId) ->onDestroy()**

在Service刚刚被创建的时候会执行onCreate（），当执行完startService（）后会执行onStartCommand(Intent intent, int flags, int startId)，当手动调用stopService（）后则是onDestroy（），要注意，onCreate方法同Activity一样，被创建之后就不会再执行了，只会接着执行onStartCommand（）

在绑定了Activity之后，又变了

> **onCreate() ->onBind（）->onunbind（）->onDestroy()**

通过调用bindService（）的方法绑定Service，会直接运行前两个方法，通过unbindService（）来解绑Service时会调用onunbind（）。

## 创建自己的Service

添加一个Service并命名，Android Studio会自动帮我们配置好常规的Service参数。

```java
public class MyService extends Service {

    private static final String TAG = "MyService";


    public MyService() {
    }
    private  Binder mBinder = new mBinder();

    static class mBinder extends Binder {

        public void method1(){
            Log.d(TAG,"mBinder");
        }
    }
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}
```

在代码中会发现定义了一个Binder内部类，这个就是用来当Activity和Service绑定时，Activity可以通过Binder来操控Service做一些耗时操作，这个例子中我们用一个Log替代。

```java
public class MainActivity extends AppCompatActivity {
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    MyService.mBinder myBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.start);
        btn2 = findViewById(R.id.stop);
        btn3 = findViewById(R.id.bind);
        btn4 = findViewById(R.id.unbind);

        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (MyService.mBinder)service;
                myBinder.method1();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(MainActivity.this,MyService.class);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService(intent,connection,BIND_AUTO_CREATE);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(connection);
                Log.d("Service","Unbind!");
            }
        });
    }
}
```

简单的说明一下，启动和停止Service分别用startService和stopService方法，传入的参数为Intent，重点要说明的为bindService（）。

~~~java
public boolean bindService(android.content.Intent service,
                           android.content.ServiceConnection conn,
                           int flags)
~~~

第一个参数接受的和前面两种方法一样，都是intent,而第二个参数为一个ServiceConnection，我们要先获取它的对象，并且重写方法

```java
ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        myBinder = (MyService.mBinder)service;
        myBinder.method1();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
};
```

第一个方法在绑定成功调用，可以通过binder向下转型初始化对象，并且调用我们刚刚在MyService中定义的mBinder的方法，这样就达到了Activity可以控制Service的目的。

使用unbindService（）方法来解绑，传入的时刚刚的connection。

## Service的保活

怎么保证Service不会被系统杀掉呢?

- 提升Service的优先级

  ```
  优先级的排序为：
  1.前台进程( FOREGROUND_APP)
  2.可视进程(VISIBLE_APP )
  3.次要服务进程(SECONDARY_SERVER )
  4.后台进程 (HIDDEN_APP)
  5.内容供应节点(CONTENT_PROVIDER)
  6.空进程(EMPTY_APP)
  ```

  我们可以使Service变成前台进程，比如说以通知的方式呈现出来，这样它的优先级为最高，极大的保证不会被系统kill。

  定义一个前台的Service：

  ```java
  NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
  NotificationChannel channel = new NotificationChannel("1","前台Service",NotificationManager.IMPORTANCE_DEFAULT);
  manager.createNotificationChannel(channel);
  Intent intent = new Intent(this,MainActivity.class);
  PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
  NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"1");
  Notification notification = builder.setContentTitle("标题")
          .setContentText("这是一个前台Service")
          .setSmallIcon(R.drawable.ic_launcher_foreground)
          .setContentIntent(pendingIntent)
          .build();
  startForeground(1,notification);
  Log.d(TAG,"onCreate");
  ```

- 通过广播重新启动Service

  在我们的Service走onDestroy方法中发出一条广播，通知重新创建一个Service。

  在onDestroy中

  ```java
  @Override  
  public void onDestroy() {  
      Intent intent = new Intent("com.dbjtech.waiqin.destroy");  
      sendBroadcast(intent);  
      super.onDestroy();  
  }
  ```

  Broadcast：

  ```java
  public class BootReceiver extends BroadcastReceiver {  
    
      @Override  
      public void onReceive(Context context, Intent intent) {  
          if (intent.getAction().equals("com.dbjtech.waiqin.destroy")) {   
              //重启Service
          }  
      }  
  }
  ```

- 直接在onDestroy中重启Service。

- onStartCommand方法，返回START_STICKY

  ```java
  @Override  
  public int onStartCommand(Intent intent, int flags, int startId) {  
      flags = START_STICKY;  
      return super.onStartCommand(intent, flags, startId);  
  }
  ```

  **1、START_STICKY**
      在运行onStartCommand后service进程被kill后，那将保留在开始状态，但是不保留那些传入的intent。    不久后service就会再次尝试重新创建，因为保留在开始状态，在创建 service后将保证调用onstartCommand。如果没有传递任何开始命令给service，那将获取到null的intent。
  2、START_NOT_STICKY
      在运行onStartCommand后service进程被kill后，并且没有新的intent传递给它。Service将移出开始状态，并且直到新的明显的方法（startService）调用才重新创建。    因为如果没有传递任何未决定的intent那么service是不会启动，也就是期间onstartCommand不会接收到任何null的intent。
  3、START_REDELIVER_INTENT
      在运行onStartCommand后service进程被kill后，系统将会再次启动service，并传入最后一个intent给onstartCommand。直到调用stopSelf(int)才停止传递intent。    如果在被kill后还有未处理好的intent，那被kill后服务还是会自动启动。因此onstartCommand不会接收到任何null的intent。