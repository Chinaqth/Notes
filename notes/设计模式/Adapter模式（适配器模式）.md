# Adapter模式（适配器模式）

在日常生活中我们经常见到各种各样的适配器，他们保证了不一样规格设备的正常使用，在设计模式中，适配器模式又被称为“Wrapper模式”，可以理解为把某样东西包装起来，可以被其他东西使用。

## Adapter的种类

- 类适配器模式（使用继承）
- 对象适配器模式（使用委托）

## 举例说明

> 类适配器模式

在类适配器模式中，我们要简单模拟输入字符串，通过适配器来输出两种不同风格加工后的字符串。

Banner.java :

```java
public class Banner {
    private String string;
    
    public Banner(String string){
        this.string = string;
    }
    
    public void printfWeak(){
        System.out.println("(" + string + ")");
    }
    public void printfStrong()
    {
        System.out.println("*" + string + "*");
    }
}
```

这个Banner类就相当于最原始的事物，要经过适配器的适配之后才能正常使用。

接口：

```java
public interface Print {
    void showWeak();
    void showStrong();
}
```

接下来就是具体的适配器了，适配器要继承我们的Banner类，且要实现我们的接口。

```java
public class PrintBanner extends Banner implements Print {
    
    public PrintBanner(String string) {
        super(string);
    }

    @Override
    public void showWeak() {
        printfWeak();
    }

    @Override
    public void showStrong() {
        printfStrong();
    }
}
```

这样我们的适配器就设计好了，在Main方法里面就可以直接拿来用了。

```java
public class Main {
    public static void main(String[] args) {
        Print p = new PrintBanner("Hello");
        p.showStrong();
        p.showWeak();
    }
}
```

结果：

~~~java
*Hello*
(Hello)
~~~

我们看到，在Main方法中我们将PrintBanner的实例保留在了Print类型的变量里，这样Main方法根本就不需要去考虑最终的结果是怎么转换而来的，只需要拿来用就完事了，当我们需要修改适配器的时候就不用修改Main了。

> 委托模式

当我们的Print不在是接口而变成了类，我们就需要将它转为抽象类。

```java
public abstract class Class_Print {
    public abstract void showWeak();
    public abstract void showStrong();
}
```

在PrintBanner中继承这个抽象类，由于不能多继承所以我们在其中加入了Banner对象来调用方法。

```java
public class PrintBanner extends Class_Print {
    private Banner banner;

    public PrintBanner(String string) {
        this.banner = new Banner(string);
    }

    @Override
    public void showWeak() {
        banner.printfWeak();
    }

    @Override
    public void showStrong() {
        banner.printfStrong();
    }
}
```

在Main中可以正常使用。

