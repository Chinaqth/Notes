# Iterator模式

Iterator为迭代器，当我们在遍历数组的时候可以利用下标进行遍历，但是当我们要遍历的集合不再限于数组时该怎么办呢？利用迭代器就可以解决这个问题。

## 在Iterator模式需要定义的角色

>- Iterator（迭代器）（**interface**）：负责按顺序遍历集合
>- ConcreteIterator（具体的迭代器）：负责实现对具体集合遍历的实现类
>- Aggregate（集合）（**interface**）：负责创建出迭代器
>- ConcreteAggregate（具体集合）：负责具体实现Aggregate

## 举例说明

我们利用一个想象一下，在书架上我们要遍历上面所有书籍的场景，在这个场景中我们需要的有：

Iterator：遍历的接口

Aggregate：集合，负责生成Iterator的接口。

Book：书本类，有名字。

BookShelf：书架类，负责存储书籍，实现Aggregate。

BookShelfIterator：负责遍历书籍的迭代器，Iterator的实现类。

> Iterator.java

```java
public interface Iterator {

     Boolean hasNext();

     Object next();
}
```

> Aggregate.java

```java
public interface Aggregate {

    Iterator iterator();
}
```

> Book.java

```java
public class Book {
    private String name;

    public Book(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

> BookShelf.java

```java
public class BookShelf implements Aggregate {

    List<Book> books = new ArrayList<>();

    public BookShelf() {
    }

    public void addBook(Book book){
        books.add(book);
    }
    public Book getBookAt(int index){
        return books.get(index);
    }

    public int getLength(){
        return books.size();
    }

    @Override
    public Iterator iterator() {
        return new BookShelfIterator();
    }
}
```

> BookShelfIterator.java

```java
public class BookShelfIterator implements Iterator {
    BookShelf bookShelf;
    int index;

    public BookShelfIterator() {

        index = 0;
    }

    @Override
    public Boolean hasNext() {
        return index < bookShelf.getLength();

    }

    @Override
    public Object next() {
        Book book = bookShelf.getBookAt(index);
        index++;
        return book;
    }
}

```

BookShelf负责管理书籍，我们在里面使用了List来对Book进行统一管理，比如添加、删除和获取长度，除了这些功能之外还要利用重写的iterator方法返回一个iterator来遍历刚刚的List。

在iterator中我们传入管理书籍的List，通过初始值为0的标准和List的长度作比较，若大于List长度则遍历完成，遍历过程中遍历选择当前的book返回过后，index才继续移动。

在BookShelfIterator中我们发现，在对集合进行遍历操作都是利用BookShelf自身的方法，如果当我们使用的集合类型发生变化也不会影响的Iterator的正常使用，大大提高了灵活性。

所以我们要学习到要多利用接口，“面向接口编程”，尽量解耦，降低代码间的耦合性。