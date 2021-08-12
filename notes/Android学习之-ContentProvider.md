# ContentProvider

## SQLite:

```java
public class MySQL extends SQLiteOpenHelper {

    private static final String CREATE = "create table book("
            + "_id integer primary key autoincrement,"
            + "name text,"
            + "author text)";
    public MySQL( Context context, String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
```

## contentprovider：

~~~java
public class MyContentProvider extends ContentProvider {
    MySQL mySQL = null;
    SQLiteDatabase db = null;
    private static final int BOOKDIR = 1;
    private static final String authority = "com.example.mycontentprovider.provier";
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(authority,"book",BOOKDIR);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)){
            case BOOKDIR:
                long insert = db.insert("book", null, values);
                return Uri.parse("content://" + authority + "/book/" + insert);
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        mySQL = new MySQL(getContext(),"Book.db",null,1);
        db = mySQL.getWritableDatabase();
        Toast.makeText(getContext(),"DB Created!",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)){
            case BOOKDIR:
                return db.query("book",null,selection,selectionArgs,null,null,sortOrder);
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
~~~

## Activity

```java
public class MainActivity extends AppCompatActivity {
    Button btnInsert;
    Button btnQuery;
    MyContentProvider contentProvider;

    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContentResolver resolver = getContentResolver();
        btnInsert = findViewById(R.id.insert);
        btnQuery = findViewById(R.id.query);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = Uri.parse("content://com.example.mycontentprovider.provier/book");
                ContentValues values = new ContentValues();
                values.put("name","Love");
                values.put("author","Alex");
                resolver.insert(uri,values);
            }
        });

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = Uri.parse("content://com.example.mycontentprovider.provier/book");
                Cursor cursor = resolver.query(uri,null,null,null,null);
                while (cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String author = cursor.getString(cursor.getColumnIndex("author"));
                    Log.d("Name:",name);
                    Log.d("Author",author);
                }
            }
        });
    }
}
```

