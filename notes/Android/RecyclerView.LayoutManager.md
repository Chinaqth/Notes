# RecyclerView.LayoutManager

 # LayoutManager

- layoutmanager

  **`LayoutManager`**是用于在 **`RecyclerView`**中控制子项视图的布局方式的类。它决定了子项视图的位置和大小，以及在滚动时如何展示和回收视图。

- generateDefaultLayoutParams

  **`generateDefaultLayoutParams`**是 **`RecyclerView.LayoutManager`\**类中的一个方法，用于生成默认的布局参数（\**`LayoutParams`**）

- onLayoutChildren

  用于测量并布局RecyclerView的子视图。当RecyclerView需要显示新的内容或需要重新布局时，该方法将被调用。具体来说，**`onLayoutChildren()`**方法的主要作用是：

  1. 测量每个子视图的大小和位置，以便RecyclerView能够正确显示它们。
  2. 根据RecyclerView的滚动位置和子视图的大小和位置，决定哪些子视图需要显示在屏幕上，以及它们应该出现的位置。
  3. 对RecyclerView的所有子视图进行布局，以便它们出现在正确的位置，并准备好处理用户的交互。

  该方法在 **RecyclerView 初始化或者数据集改变时（比如添加或删除子项）被调用**

- detachAndScrapAttachedViews

  用于将所有已经添加到 **`RecyclerView`**上的 **`View`**先从 **`RecyclerView`**上 detach 掉，然后再把它们放到 **`Recycler`**缓存起来，以便在后续使用时能够快速复用。