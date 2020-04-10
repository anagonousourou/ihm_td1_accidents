package ihm.accidents.adapters;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class BindingAdapters {
    @BindingAdapter("imageResource")
    public static void setImageUrl(ImageView view, Bitmap bitmap) {
        Glide.with(view.getContext()).load(bitmap).into(view);
    }

    @BindingAdapter("imageResource")
    public static void setImageUrl(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }

}
