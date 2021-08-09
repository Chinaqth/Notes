# BroadCast

BroadCast翻译为“广播”，故名思意了属于是，就像我们生活中所见到的广播一样，一个大喇叭，在社区或者村子里传播消息。

## 什么是BroadCast？

和我们所见的广播的作用差不多，BoradCast负责在应用之间、应用内部（LocalBroadCast）传递消息。

## BroadCast的种类

**BroadCast分为3种**

- 无序广播

  ```public void sendBroadcast(android.content.Intent intent)```

  无序广播是异步广播消息的，所有注册过的广播都可以收到对应的消息，没有先后顺序。使用Intent来发送你指定的广播

  ~~~java
  Intent intent = new Intent("MyBroadCast");
  sendBroadcast(intent);
  ~~~

- 有序广播

  ~~~java
  Intent intent = new Intent("MyBroadCast");
  sendOrderedBroadcast(intent,null);
  ~~~

  有序广播发出的广播是线性的，有顺序的，而且**可以被拦截**中断后续接收器的接受

  在你的onReceive方法中添加```abortBroadcast();```既可以阻断后续广播的发送了。

- 本地广播（LocalBroadCast)

  本地广播意在保护当前应用的数据仅仅在本地交流，不会暴露于外界，造成安全隐患。

  需要使用LocalBroadCastManagehuoqu获取实例对象

  ~~~java
  public static void sendLocalBroadcast(Context context,String action){
      Intent intent = new Intent(action);
      LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
      localBroadcastManager.sendBroadcast(intent);
  }
  ~~~

## BroadCast的注册

刚刚说明了如何发送各类广播，现在我们就来说说如何注册广播吧！

### 动态注册

动态注册的意思就是通过代码的方式将广播注册，首先你需要写一个类继承BroadCastReceiver，重写它的方法：onReceive()，这个方法就是当你的广播接收器接收到广播后会做的事情啦！

~~~java
public class MyBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Receive !",Toast.LENGTH_SHORT).show();
    }
}
~~~

在Activity种我们注册代码分为三部：

- **给你的广播接收器创建实例。**

- **创建IntentFilter实例并且使用其addAction方法添加你的广播标志。**

- **使用registerReceiver方法注册广播，参数就是刚刚上述两部的实例对象。**

  ~~~java
  myBroadCast = new MyBroadCast();
  IntentFilter filter = new IntentFilter();
  filter.addAction("MyBroadCast");
  registerReceiver(myBroadCast,filter);
  ~~~

记得要在**被销毁时取消注册**哦：unregisterReceiver().

### 静态注册

第一步还是要写一个继承BroadCastReceiver的类实现onReceive方法。接着要在AndroidManifest.xml文件中注册。

~~~java
<receiver 
     android:name=".BootCompleteReceiver" >
     <intent-filter>
     <action android:name="MyBroadCast" />
     </intent-filter>
</receiver>
~~~

静态注册还可以设定优先级：

~~~java
android:priority="100"
~~~

