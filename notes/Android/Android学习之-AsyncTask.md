# AsyncTask

我们上次了解了Handler的具体用法，今天我们来学习另一种异步通信的工具AsyncTask。

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
                publishProgress(progress);
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