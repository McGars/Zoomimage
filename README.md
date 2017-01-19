# Zoomimage
Zoom image to front from everywhere 
Based on https://github.com/alexvasilkov/GestureViews

[![Video](https://i.ytimg.com/vi/liuGlyL95Gc/2.jpg?time=1477051509642)](https://youtu.be/liuGlyL95Gc "Video")

```gradle
compile 'com.github.msgars:ZoomImage:0.0.6'
```

Simple use, first init

```java
zoomImageController = new ZoomImageController(this, new Displayer() {
            // load fullscreen image
            @Override
            public void displayImage(ZoomPhotoPagerAdapter.IPhoto photo, ZoomPhotoPagerAdapter.ViewHolder v) {
                // Show image with your own libs or methods
            }

            @Override
            public void cancel(ImageView v) {
                // cancel loading
            }
        });
```

Second set from and array of data
"From" can be
- RecyclerView
- ImageView
- List<ImageView>
- ListView

```java
    zoomImageController.setPhotos(AnimatorBuilder.getInstance().from(/*from*/), List<IPhoto>)
                       .show(position);
```

If **From** is simple ImageView, then set **position** to 0
If **From** is simple **RecyclerView, ListView**, then implements ZoomHolder to your Holder

Don't forget 
```java
 @Override
    public void onBackPressed() {
        if(zoomImageController.onBackPressed())
            return;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        zoomImageController.onDestroy();
    }
```

See example for more details