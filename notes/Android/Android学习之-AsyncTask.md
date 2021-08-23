# AsyncTask

我们上次了解了Handler的具体用法，今天我们来学习另一种异步通信的工具AsyncTask。

## AsyncTask是什么？

为了更加方便我们在子线程中更新UI元素，Android从1.5版本就引入了一个AsyncTask类，使用它就可以非常灵活方便地从子线程切换到UI线程这就是AsyncTask了。

AsyncTask是抽象类，所以要实现它必须定义一个类继承它，并且，你会发现AsyncTask有三个泛型参数：

AsyncTask<**Params,Progress, Result**>

- Params

  在执行AsyncTask时需要传入的参数，可用于在后台任务中使用。

- Progress

  后台任务执行时，如果需要在界面上显示当前的进度，则使用这里指定的泛型作为进度单位。

- Result

  当任务执行完毕后，如果需要对结果进行返回，则使用这里指定的泛型作为返回值类型。

这三个参数还和需要重写的方法有关：

- onPreExecute()

  这个方法会在后台任务开始执行之间调用，用于进行一些界面上的初始化操作，比如显示一个进度条对话框等。

- doInBackground(Params...)

  这个方法中的所有代码都会在子线程中运行，我们应该在这里去处理所有的耗时任务。任务一旦完成就可以通过return语句来将任务的执行结果进行返回，如果AsyncTask的第三个泛型参数指定的是Void，就可以不返回任务执行结果。注意，在这个方法中是不可以进行UI操作的，如果需要更新UI元素，比如说反馈当前任务的执行进度，可以调用**publishProgress(Progress...)**方法来完成。

- onProgressUpdate(Progress...)

  当在后台任务中调用了publishProgress(Progress...)方法后，这个方法就很快会被调用，方法中携带的参数就是在后台任务中传递过来的。在这个方法中可以对UI进行操作，利用参数中的数值就可以对界面元素进行相应的更新。

- onPostExecute(Result)

  当后台任务执行完毕并通过return语句进行返回时，这个方法就很快会被调用。返回的数据会作为参数传递到此方法中，可以利用返回的数据来进行一些UI操作，比如说提醒任务执行的结果，以及关闭掉进度条对话框等。

## 举例说明

```java
public class MAsyncTask extends AsyncTask<Void,Integer,Boolean> {
    private static final String TAG = "MAsyncTask";
    private  Context context;
    UiChange uiChange;

    public MAsyncTask(Context context) {
        this.context = context;
    }

    public void setIUiChange(UiChange uiChange) {
        this.uiChange = uiChange;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: ");
        int progress = 0;
        for (int i = 0; i < 10; i++) {
            try {
                progress += 10;
                publishProgress(progress); //更新UI，调用onProgressUpdate
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: ");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        Log.d(TAG, "onPostExecute: ");
        Toast.makeText(context,"Finish",Toast.LENGTH_SHORT).show();
        super.onPostExecute(aBoolean);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d(TAG, "onProgressUpdate: ");
        uiChange.changeUi(values[0]);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        Log.d(TAG, "onCancelled: ");
        Toast.makeText(context,"Canceled",Toast.LENGTH_SHORT).show();
        super.onCancelled(aBoolean);
    }

    interface UiChange{
        void changeUi(Integer progress);
    }
}
```

Activity

```java
public class MainActivity extends AppCompatActivity  {
    public Button button;
    public Button button1;
    public ProgressBar progressBar;
    MAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.cancel);
        progressBar = findViewById(R.id.progressBar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new MAsyncTask(MainActivity.this);
                task.execute();
                task.setIUiChange(new MAsyncTask.UiChange() {
                    @Override
                    public void changeUi(Integer progress) {
                        progressBar.setProgress(progress);
                    }
                });
                button.setEnabled(false);
                button1.setEnabled(true);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.cancel(true);
                progressBar.setProgress(0);
                button.setEnabled(true);
                button1.setEnabled(false);
            }
        });

    }
}
```

## 深入

在看这些方法之前，我们要知道AsyncTask有三种状态：

```java
public enum Status {
    /**
     * Indicates that the task has not been executed yet.
 	 * 等待
     */
    PENDING,
    /**
     * Indicates that the task is running.
     * 运行
     */
    RUNNING,
    /**
     * Indicates that {@link AsyncTask#onPostExecute} has finished.
     * 结束
     */
    FINISHED,
}
```

我们在Activity中要开启AsyncTask就要new一个AsyncTask对象，并且调用其execute方法，我们来看一看：

```java
public final AsyncTask<Params, Progress, Result> execute(Params... params) {
    return executeOnExecutor(sDefaultExecutor, params);
}
```

我们看到，这里面调用了executeOnExecutor方法，参数除了params之外，还有一个sDefaultExecutor，分别来看看这两个东西吧

```java
@Deprecated
public static final Executor SERIAL_EXECUTOR = new SerialExecutor();

@UnsupportedAppUsage
private static volatile Executor sDefaultExecutor = SERIAL_EXECUTOR;
```

sDefaultExecutor是SERIAL_EXECUTOR赋给的，SERIAL_EXECUTOR是new了一个 SerialExecutor得到的。

```java
private static class SerialExecutor implements Executor {
    final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
    Runnable mActive;

    public synchronized void execute(final Runnable r) {
        mTasks.offer(new Runnable() {
            public void run() {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            }
        });
        if (mActive == null) {
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if ((mActive = mTasks.poll()) != null) {
            THREAD_POOL_EXECUTOR.execute(mActive);
        }
    }
}
```

executeOnExecutor：

```java
@MainThread
public final AsyncTask<Params, Progress, Result> executeOnExecutor(Executor exec,
        Params... params) {
    if (mStatus != Status.PENDING) {
        switch (mStatus) {
            case RUNNING:
                throw new IllegalStateException("Cannot execute task:"
                        + " the task is already running.");
            case FINISHED:
                throw new IllegalStateException("Cannot execute task:"
                        + " the task has already been executed "
                        + "(a task can be executed only once)");
        }
    }

    mStatus = Status.RUNNING;

    onPreExecute();

    mWorker.mParams = params;
    exec.execute(mFuture);

    return this;
}
```

可以看到，先对AsyncTask的状态进行了判断，如果状态部位Pending的话，根据剩余的两种状态抛出对应的异常，接着将状态置为Pending，然后调用**onPreExecute**方法，我们可以看到，还有两个没有见过变量，分别是：mWorker和mFuture，他们在AsyncTask的构造方法中就被初始化了。

```java
public AsyncTask(@Nullable Looper callbackLooper) {
    mHandler = callbackLooper == null || callbackLooper == Looper.getMainLooper()
        ? getMainHandler()
        : new Handler(callbackLooper);

    mWorker = new WorkerRunnable<Params, Result>() {
        public Result call() throws Exception {
            mTaskInvoked.set(true);
            Result result = null;
            try {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                //noinspection unchecked
                result = doInBackground(mParams);
                Binder.flushPendingCommands();
            } catch (Throwable tr) {
                mCancelled.set(true);
                throw tr;
            } finally {
                postResult(result);
            }
            return result;
        }
    };
	//在mFuture实例中,将会调用mWorker做后台任务,完成后会调用done方法
    mFuture = new FutureTask<Result>(mWorker) {
        @Override
        protected void done() {
            try {
                postResultIfNotInvoked(get());
            } catch (InterruptedException e) {
                android.util.Log.w(LOG_TAG, e);
            } catch (ExecutionException e) {
                throw new RuntimeException("An error occurred while executing doInBackground()",
                        e.getCause());
            } catch (CancellationException e) {
                postResultIfNotInvoked(null);
            }
        }
    };
}
```

在初始化mFuture的时候将mWorker作为参数传入。mWorker是一个Callable对象，mFuture是一个FutureTask对象，这两个变量会暂时保存在内存中，稍后才会用到它们。在 mWorker的call方法中我们看到首次调用了**doInBackground**。mWorker作为参数传入了FutureTask中，在FutureTask中调用了postResultIfNotInvoked方法。

```java
private void postResultIfNotInvoked(Result result) {
    final boolean wasTaskInvoked = mTaskInvoked.get();
    if (!wasTaskInvoked) {
        postResult(result);
    }
}
```

在postResultIfNotInvoked中又调用了**postResult**方法

```java
private Result postResult(Result result) {
    @SuppressWarnings("unchecked")
    Message message = getHandler().obtainMessage(MESSAGE_POST_RESULT,
            new AsyncTaskResult<Result>(this, result));
    message.sendToTarget();
    return result;
}
```

我们看到，在postResult里是一个消息的发送，发送的消息为MESSAGE_POST_RESULT，Handler对象为getHandler()

```java
private Handler getHandler() {
    return mHandler;
}
```

我们看到mHandler其实在构造方法中就被赋值了。

~~~java
mHandler = callbackLooper == null || callbackLooper == Looper.getMainLooper()
        ? getMainHandler()
        : new Handler(callbackLooper);
~~~

在这里mHandler为getMainHandler（）就会使用在里面定义的Handler类——InternalHandler：

```java
private static Handler getMainHandler() {
    synchronized (AsyncTask.class) {
        if (sHandler == null) {
            sHandler = new InternalHandler(Looper.getMainLooper());
        }
        return sHandler;
    }
}
```

```java
private static class InternalHandler extends Handler {
    public InternalHandler(Looper looper) {
        super(looper);
    }

    @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
    @Override
    public void handleMessage(Message msg) {
        AsyncTaskResult<?> result = (AsyncTaskResult<?>) msg.obj;
        switch (msg.what) {
            case MESSAGE_POST_RESULT:
                // There is only one result
                result.mTask.finish(result.mData[0]);
                break;
            case MESSAGE_POST_PROGRESS:
                result.mTask.onProgressUpdate(result.mData);
                break;
        }
    }
}
```

在这里对消息做出了处理，如果接受的为MESSAGE_POST_RESULT，表名已经结束任务，要调用finish方法。

```java
private void finish(Result result) {
    if (isCancelled()) {
        onCancelled(result);
    } else {
        onPostExecute(result);
    }
    mStatus = Status.FINISHED;
}
```

在finish中判断了是否自然结束还是使用了Cancel方法，如果不是Cancel方法则调用**onPostExecute**。

如果接受的是MESSAGE_POST_PROGRESS，那么就要调用**onProgressUpdate**方法。我们知道当使用了**publishProgress**方法就会调用onProgressUpdate方法，来看看是怎么实现的吧

```java
@WorkerThread
protected final void publishProgress(Progress... values) {
    if (!isCancelled()) {
        getHandler().obtainMessage(MESSAGE_POST_PROGRESS,
                new AsyncTaskResult<Progress>(this, values)).sendToTarget();
    }
}
```

可以看到在publishProgress方法中发送了MESSAGE_POST_PROGRESS消息，当handler收到消息后就会调用onProgressUpdate方法。

