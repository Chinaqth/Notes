# Android进阶之光—View

## View坐标系

在深入了解View之前我们来说说View的坐标系。![img](https://img2.baidu.com/it/u=347646891,243698573&fm=26&fmt=auto)

​													**view坐标系**

我们可以看到有很多的方法，我们可以利用这些方法获得View的高度、宽度，点击的视图坐标和绝对坐标。

### View获取自身的高宽

> **weigh = getRight（） - getLeft（）；**
>
> **high = getBottom（） - getTop（）；**

我们通过程序来验证一下：

