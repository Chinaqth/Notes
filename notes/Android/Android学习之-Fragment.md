# Fragment

Fragment翻译过来就是碎片，Fragment依附于Activity，所以Fragment也有自己的生命周期。

## Fragment的生命周期

fragment的生命周期中的方法有：

onAttach（）、onCreate（）、onCreateView（）、onActivityCreated（），onStart（），onResume（）、onPause（）、onStop（）、onDestroyView（）、onDestroy（）、onDetach（）。

其中有很多的方法我们在学Activity中也看到过就不多介绍了，其中onAttach和onDetach方法是和Activity绑定时调用的，当Activity创建完成时会调用onActivityCreated（），在观察生命周期前我们还要知道Fragment是怎么使用的。

## Fragment的使用

我们在学习广播的时候知道有静态加载和动态加载之分，Fragment也一样。

- 静态加载：

  所谓的静态加载就是指在xml文件中直接指定一个fragment，待会我们观测其生命周期时就会采用这种方法。

- 动态加载：

  这个时候我们要获取FragmentManager对象，并使用其beginTransaction方法开启事物，通过add的方式将Fragment动态加载。

