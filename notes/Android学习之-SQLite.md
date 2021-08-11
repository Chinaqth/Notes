# Android学习之-SQLite

SQLite是一款轻量级数据库，Android Studio中已经为我们配置好，支持SQL语句，用起来非常的方便。

## 创建类

首先，要定义一个类继承SQLiteOpenHelper，并重写两种方法。

~~~java
public class SQLDataBase extends SQLiteOpenHelper {
    Context mContext;
    private static final String CREATE = "create table user("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "num text)";
    public SQLDataBase( Context context,  String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
        Toast.makeText(mContext,"DataBase Created",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
~~~

可以看到这里面两个方法分别是：onCreate，onUpdate。

在刚刚开始时我们定义了一个静态final的sql语句，用于创建数据库，构造方法的参数有必要说明一下，第一个当然就是要获取上下文，第二个则是你要创建的数据库的名称，第三个一般为null，而最后一个则是版本，每次升级数据库后版本就要改变。我们在onCreate方法中使用execSQL方法执行刚刚的sql语句创建一张为user的表。onUpdate用于更新数据库（添加表等操作）。

## 创建数据库

接下来我们看到Activity中，这里有四个按钮，分别为创建、添加、更新、查询按钮，首先我们实例化数据库类:

~~~java
dataBase = new SQLDataBase(this,"user.db",null,1);
~~~

在创建数据库按钮中写下：

~~~java
dataBase.getWritableDatabase();
~~~

这样就会去获取一个可写的数据库，如果没有则创建一个。

## 添加操作

在来看看添加操作：

```java
public long insert(String table,
                   String nullColumnHack,
                   android.content.ContentValues values)
```

第一个参数为表名，第二个为null，第三个为你要添加的value值。

~~~java
ContentValues values = new ContentValues();
values.put("name","XXX");
values.put("num","No.Xx");
~~~

创建values对象，利用value.put的方法将键值对存入，用insert插入到数据库中

完整代码块：

~~~java
case R.id.insert:
                SQLiteDatabase db0 = dataBase.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name","Alex");
                values.put("num","No.01");
                db0.insert("user",null,values);
                break;
~~~

## 更新操作

和添加操作差不多，利用db对象的updat方法

~~~java
public int update(String table,//表名
                  android.content.ContentValues values,//更新的值
                  String whereClause,//在哪里更新
                  String[] whereArgs)//具体到哪里
~~~

完整代码块：

~~~java
case R.id.update:
                SQLiteDatabase db1 = dataBase.getWritableDatabase();
                ContentValues values1 = new ContentValues();
                values1.put("name","Tj");
                values1.put("num","No.002");
                db1.update("user",values1,"name = ?",new String[]{"XXX"});
~~~

注意到，这里第三个参数有个**“？”**，一开始可能不知道是啥，你看到第四个参数时就该明白了，第四个参数就是指？具体是个什么，方便精准更新。

## 查询操作

查询操作最为重要

~~~java
public android.database.Cursor query(String table,//表名
                                     String[] columns,//列名
                                     String selection,//where约束
                                     String[] selectionArgs,//具体where
                                     String groupBy,//分组
                                     String having,
                                     String orderBy)
~~~

查询操作会返回一个Cursor的对象，用来get到具体的数据。

那么怎么拿到具体的值呢？

Cursor有一类方法为getXxx()，但是里面的传入的参数类型为int，那么这个int类型是哪里来的呢？

其实，Cursor中还有个方法getColumnIndex()通过传入列名来获取值并以int返回。

~~~java
case R.id.query:
                SQLiteDatabase db2 = dataBase.getReadableDatabase();
                Cursor user = db2.query("user", null, null, null, null, null, null);
                while (user.moveToNext()){
                    String name = user.getString(user.getColumnIndex("name"));
                    String num = user.getString(user.getColumnIndex("num"));
                    Log.d("Name:",name);
                    Log.d("Num:",num);
                }
                break;
~~~



