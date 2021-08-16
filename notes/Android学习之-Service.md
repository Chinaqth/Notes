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