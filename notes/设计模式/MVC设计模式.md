# MVC设计模式

所谓的MVC就是M（Model）V（View）C（Controller），运用于应用的分层开发。

![img](https://www.runoob.com/wp-content/uploads/2014/08/1200px-ModelViewControllerDiagram2.svg_.png)

- Model：代表的是一个存储数据的对象类。
- View：将数据以可见的方式呈现给用户。
- Controller：控制数据，操作View和Model，当数据改变时可以更新视图。

下面以一个简单的例子来实现MVC设计模式：

存储数据的Students类（Model）

~~~java
public class Students {
    private String name;
    private int Id;

    public Students(String name, int id) {
        this.name = name;
        this.Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }
}

~~~

视图View类（View）

~~~java
public class View {
    public void showSt(String name,int Id){
        System.out.println("Student:");
        System.out.println("Name:" + name);
        System.out.println("Id:" + Id);
    }
}
~~~

控制类Controller（Controller）

~~~java
public class Controller {
    Students students;
    View view;

    public Controller(Students students, View view) {
        this.students = students;
        this.view = view;
    }

    public String getStName(){
        return students.getName();
    }
    public void setStName(String name){
        students.setName(name);
    }
    public int getStId(){
        return students.getId();
    }
    public void setStId(int Id){
        students.setId(Id);
    }
    public void updateSt(){
        view.showSt(students.getName(),students.getId());
    }
}
~~~

测试类

~~~java
public class TEST {
    public static void main(String[] args) {
        Students students = new Students("Alex",1);
        View view = new View();
        Controller controller = new Controller(students,view);
        controller.updateSt();

        controller.setStName("Tj");
        controller.setStId(2);
        controller.updateSt();
    }
}
~~~

结果

~~~java
Student:
Name:Alex
Id:1
Student:
Name:Tj
Id:2
~~~

