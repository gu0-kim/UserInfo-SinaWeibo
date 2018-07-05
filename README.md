# UserInfo-SinaWeibo

实现思路：
结构 viewpager+frontView
简单的说，在viewpager上盖了一层frontView，frontView就是新浪微博的个人主页的背景图。recyclerview滚动时，同步移动拉伸frontView。当然，由于需要处理viewpgaer中每一页滚动同步时和frontView的位置关系，所以问题并没有那么简单，具体实现还是看代码吧。
当然如果你说可以使用appbar去实现，but but -----虽然viewpager中不同页的滚动可以实现同步，但frontView的拉伸效果并不容易处理。

实现效果：
1.实现拉伸效果
2.能同步不同fragment的滚动位置，具体效果可以比对新浪微博。
3.实现效果frontView上面，可点击的view，也能拖动拉伸。（由于实现的结构，如果你直接加onclicklistener，则只能点击，不能拖动滚动）
4.由于frontView只是叠在后面下拉刷新recyclerview上面，所以会出现透点后面recyclerview的item现象。对于这种现象，我也做了处理判断，阻止了透点现象。