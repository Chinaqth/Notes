# Android学习之-ContentProvider

在Android四大组件之中，ContentProvider是负责进程和进程，进程内部的数据传输的，一个ContentProvider可以看作是一个中介，不同的进程来来去去，在一个中介中获取或者更新数据，所以ContentProvider中存在数据库，SQLite的学习就显得很有必要了。

## 准备

一个SQLite数据库，创建一张或多张表

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
}
```

## 自定义一个ContentProvider

在ContentProvider中经常利用到的一个类为Uri，Uri被称为**“统一资源定位符”**，依靠这个定位符才能获取到指定的数据，Uri有自己的语法规则

“**content://Authority/table/1**”

- content：//是固定的语法。
- 你的ContentProvider靠Authority来和其他的ContentProvider区分，一般的规则为包名.provider。
- table就是具体的表名。
- 1为table中的第一条。

**content://Authority/table/#**表示表中的任意一行数据

**content://Authority/***表示任意一张表

我们用UriMatcher类的match来匹配我们传入的Uri，匹配成功后返回一个int类型的数据，我们可以在ContentProvider中先设定好我们预期的整数值来对应不同的Uri。







