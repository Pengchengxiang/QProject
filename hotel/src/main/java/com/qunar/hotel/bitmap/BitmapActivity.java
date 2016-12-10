package com.qunar.hotel.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.qunar.hotel.R;

public class BitmapActivity extends AppCompatActivity {
    private int[] images = new int[]{R.drawable.p1, R.drawable.p2, R.drawable.p3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        Gallery gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new GalleryAdapter());
    }

    class GalleryAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return images[position];
        }

        @Override
        public long getItemId(int position) {
            return images[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(BitmapActivity.this);

            BitmapFactory.Options options = new BitmapFactory.Options();
            //inJustDecodeBounds为true，不返回bitmap，只返回这个bitmap的尺寸
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), images[position], options);

            //利用返回的原图片的宽高，我们就可以计算出缩放比inSampleSize，获取指定宽度为300像素，等长宽比的缩略图，减少图片的像素
            int toWidth = 300;
            int toHeight = options.outHeight * toWidth / options.outWidth;
            options.inSampleSize = options.outWidth / toWidth;
            options.outWidth = toWidth;
            options.outHeight = toHeight;

            //使用RGB_565减少图片大小
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //释放内存，共享引用（21版本后失效）
            options.inPurgeable = true;
            options.inInputShareable = true;

            //inJustDecodeBounds为false，返回bitmap
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images[position], options);
            imageView.setImageBitmap(bitmap);

            return imageView;
        }
    }

}
