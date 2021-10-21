package com.autobots.innopark;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class FontFitTextView extends androidx.appcompat.widget.AppCompatTextView {

    public FontFitTextView(Context context) {
        super(context);
    }

    public FontFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FontFitTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // start decreasing font size from 100
        setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);

        // calculate dimensions. don't forget about padding
        final float maxWidth = getWidth()-(getPaddingLeft()+getPaddingRight());
        final float maxHeight = getHeight()-(getPaddingTop()+getPaddingBottom());

        if (maxWidth < 1.0f || maxHeight < 1.0f) {
            return;
        }

        CharSequence text = getText();
        int lineCount = getLineCount(maxWidth, text);
        float height = getHeight(lineCount);
        // keep decreasing font size until it fits
        while (height > maxHeight) {
            final float textSize = getTextSize();
            setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize - 1));
            height = getHeight(getLineCount(maxWidth, getText()));
        }
        // show fitted textView
        requestLayout();
    }

    private float getHeight(int lineCount) {
        return lineCount * getLineHeight() + (lineCount > 0 ? (lineCount - 1) * getPaint().getFontSpacing() : 0);
    }

    private int getLineCount(float maxWidth, CharSequence text) {
        int lineCount = 0;
        int index = 0;
        final TextPaint paint = getPaint();
        while (index < text.length()) {
            index += paint.breakText(text, index, text.length(), true, maxWidth, null);
            lineCount++;
        }
        return lineCount;
    }
}