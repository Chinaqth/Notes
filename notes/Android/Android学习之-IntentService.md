# IntentService

前面的篇章中我们学习了Handler，HandlerThread以及Service，今天我们来看看这个IntentService是啥。

## IntentService是啥？

我们知道Service可以在后台长时间的运行，Activity也可以绑定Service的形式来控制Service做一些耗时操作。我们知道如果要在Service中完成一系列操作你需要开启这个Service并且在任务完成之后停止该活动，使用stopService或者stopSelf来停止，但是自己完成这一系列操作比较麻烦，于是就有了IntentService，IntentService也是Service，它可以帮忙处理一些任务，并且在结束之后自动停止，不需要你的操心。

## 举例分析

我们现在创建了一个IntentService类，继承了IntentService，重写了onHandleIntent方法。我们希望在这个方法中可以做一些操作。

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