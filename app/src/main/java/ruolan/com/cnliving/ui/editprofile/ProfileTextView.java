package ruolan.com.cnliving.ui.editprofile;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by wuyinlei on 2017/11/14.
 */

public class ProfileTextView extends ProfileEdit {
    public ProfileTextView(Context context) {
        super(context);
        disableEdit();
    }

    public ProfileTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        disableEdit();
    }
    public ProfileTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        disableEdit();
    }
}
