package com.example.alohaandroid.utils.linphone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

import com.example.alohaandroid.R;

@SuppressLint("AppCompatCustomView")
public class AddressText extends EditText implements AddressType {
    private String mDisplayedName;
    private final Paint mTestPaint;
    private AddressChangedListener mAddressListener;

    public AddressText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTestPaint = new Paint();
        mTestPaint.set(this.getPaint());
        mAddressListener = null;
    }

    private void clearDisplayedName() {
        mDisplayedName = null;
    }

    public String getDisplayedName() {
        return mDisplayedName;
    }

    public void setDisplayedName(String displayedName) {
        this.mDisplayedName = displayedName;
    }

    private String getHintText() {
        String resizedText = getContext().getString(R.string.address_bar_hint);
        if (getHint() != null) {
            resizedText = getHint().toString();
        }
        return resizedText;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        clearDisplayedName();

        refitText(getWidth(), getHeight());

        if (mAddressListener != null) {
            mAddressListener.onAddressChanged();
        }

        super.onTextChanged(text, start, before, after);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        if (width != oldWidth) {
            refitText(getWidth(), getHeight());
        }
    }

    private float getOptimizedTextSize(String text, int textWidth, int textHeight) {
        int targetWidth = textWidth - getPaddingLeft() - getPaddingRight();
        int targetHeight = textHeight - getPaddingTop() - getPaddingBottom();
        float hi = 90;
        float lo = 2;
        final float threshold = 0.5f;

        mTestPaint.set(getPaint());

        while ((hi - lo) > threshold) {
            float size = (hi + lo) / 2;
            mTestPaint.setTextSize(size);
            if (mTestPaint.measureText(text) >= targetWidth || size >= targetHeight) {
                hi = size;
            } else {
                lo = size;
            }
        }

        return lo;
    }

    private void refitText(int textWidth, int textHeight) {
        if (textWidth <= 0) {
            return;
        }

        float size = getOptimizedTextSize(getHintText(), textWidth, textHeight);
        float entrySize = getOptimizedTextSize(getText().toString(), textWidth, textHeight);
        if (entrySize < size) size = entrySize;
        setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = getMeasuredHeight();

        refitText(parentWidth, height);
        setMeasuredDimension(parentWidth, height);
    }

    public void setAddressListener(AddressChangedListener listener) {
        mAddressListener = listener;
    }

    public interface AddressChangedListener {
        void onAddressChanged();
    }
}