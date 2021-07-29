# Android学习之-Activity

什么是Activity？什么是Activity？如果你想知道什么是Activity的话我现在就带你研究。

## 啥是Activity？

说的通俗一点，当你打开手机应用的一瞬间，你就多多少少沾点Activity，手机应用通过UI让我们可以感知到Internet的美好，UI设计的好你看的舒服，用的也舒服，UI就包含在Activity之中，**Activity就是你和应用交互的那一种东西**，**一个程序包含一个或多个Activity**，你打开应用总不可能只有一个界面吧,那这个应用也太拉了。

## 怎么创建一个Activity？

最简单的方法就是你打开Android Studio，新建一个项目，选择Empty Activity，然后狂点next，最后finish，那么一个Activity就被你拿下了，如果你想要手动整一个Activity的话，你要去你的app目录下的AndroidManifest文件下注册你的Activity，你不注册那你就是“黑户“啊，没法使用，如果你用的是AndroidStudio添加的Activity，那你就不用操心注册流程了，应为这款软件已经帮你注册好啦!

![image-20210729223518483](C:\Users\Chinaqth\AppData\Roaming\Typora\typora-user-images\image-20210729223518483.png)

` <action android:name="android.intent.action.MAIN" />`

`<category android:name="android.intent.category.LAUNCHER" />`

这两段标志着这个Activity是你的Activity入口，你的App一旦开启首界面就是这个Activty，

`<activity android:name=".MainActivity">`这个代表这你Activity的名字。

## UI和逻辑控制

Ok啦！，现在你已经拥有了一个全新的Activity。这个时候你可能会问：我看到的界面是在哪里实现的呀？我和界面操作交互又是怎么实现的呀？

Activity可以分为**UI和逻辑**，UI也就是你所看到的界面，大多东西都被放在了main中的src中的一个**layout**的文件下，这个文件就是”布局文件“，在这里你将用xml格式去完善一个Activity的UI，而逻辑方面的操作则在main中的src的java/kotlin中。

## Activity的生命周期

