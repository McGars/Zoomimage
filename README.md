# Zoomimage
Zoom image to front from everywhere 
Based on https://github.com/alexvasilkov/GestureViews

[![Video](https://i.ytimg.com/vi/liuGlyL95Gc/2.jpg?time=1477051509642)](https://youtu.be/liuGlyL95Gc "Video")

```gradle
implementation 'com.github.msgars:ZoomImage:0.1.0'
```

Simple use, first init

```kotlin
zoomImageController = ZoomImageController(UiContainer(findViewById(R.id.rootView)), object : Displayer {
          // load fullscreen image
          override fun displayImage(photo: IPhoto?, v: ZoomViewHolder) {
              // display image into v.image
          }

          override fun cancel(imageView: ImageView) {
              cancel load
          }
      })
```

Second set from and array of data
"From" can be
- RecyclerView
- ImageView
- List<ImageView>
- ListView

```kotlin
    zoomImageController.setPhotos(AnimatorBuilder.from(/*from*/), List<IPhoto>)
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