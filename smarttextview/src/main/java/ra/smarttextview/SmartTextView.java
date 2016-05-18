package ra.smarttextview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by rugvedambekar on 2016-04-10.
 */
public class SmartTextView extends TextView {

    private static final String TAG = SmartTextView.class.getSimpleName();

//    private static final float LINE_HEIGHT_ADJ = 0.95f;

    private static final HashMap<FontType, Typeface> mRegisteredFonts = new HashMap<>();

    public static void registerFont(FontType fType, Typeface fTypeface) {
        mRegisteredFonts.put(fType, fTypeface);
    }
    public static Typeface getFontTypeface(FontType fType) {
        return mRegisteredFonts.get(fType);
    }

    private int mId, mIndex;

    private boolean mJustified;
    private boolean mAllCaps;
    private FontType mFontType;

    private int mFirstLineTextHeight = 0;
    private Rect mLineBounds = new Rect();
    private float mDrawableWidth = 0;

    public SmartTextView(Context context) {
        super(context);
        initialize(null);
    }

    public SmartTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public SmartTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        int styleIndex = 0;

        if (attrs != null) {
            TypedArray attrArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SmartTextView, 0, 0);
            if (attrArray != null) {
                mJustified = attrArray.getBoolean(R.styleable.SmartTextView_justified, false);
                mFontType = FontType.values()[attrArray.getInt(R.styleable.SmartTextView_font, 1)];
                mAllCaps = attrArray.getBoolean(R.styleable.SmartTextView_android_textAllCaps, false);
                styleIndex = attrArray.getInt(R.styleable.SmartTextView_android_textStyle, 0);
            }
        }

        setTypeface(getFontTypeface(), styleIndex);
    }

    private Typeface getFontTypeface() {
        return !mRegisteredFonts.containsKey(mFontType) ? getTypeface() : mRegisteredFonts.get(mFontType);
    }

    public void setJustified(boolean justified) {
        mJustified = justified;
    }

    public void setFontType(FontType fType) {
        mFontType = fType;
        setTypeface(getFontTypeface());
    }
    public void setFontType(FontType fType, int style) {
        mFontType = fType;
        setTypeface(getFontTypeface(), style);
    }

    public void setId(int id) { mId = id; }
    public int getId() { return mId; }

    public void setIndex(int index) { mIndex = index; }
    public int getIndex() { return mIndex; }

    @Override
    public void setAllCaps(boolean allCaps) {
        mAllCaps = allCaps;
        super.setAllCaps(allCaps);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        if (tf == null) tf = getFontTypeface();
        super.setTypeface(tf, style);
    }

    public void switchText(int resId) {
        switchText(getResources().getString(resId));
    }
    public void switchText(final String newText) {
        animate().alpha(0).setDuration(150).withEndAction(new Runnable() {
            @Override public void run() {
                setText(newText);
                animate().alpha(1).setDuration(150).start();
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mJustified) {
            super.onDraw(canvas);
            return;
        }

        // Manipulations to mTextPaint found in super.onDraw()...
        getPaint().setColor(getCurrentTextColor());
        getPaint().drawableState = getDrawableState();

        // If word is too large to fit in line... must break it up OR Infinite Loop!?
        String fullText = mAllCaps ? getText().toString().toUpperCase() : getText().toString();

        float lineSpacing = getLineHeight(); // (getLineHeight() * LINE_HEIGHT_ADJ);
        float drawableWidth = getDrawableWidth();

        int lineNum = 1, lineStartIndex = 0;
        int lastWordEnd, currWordEnd = 0;

        String textHeights = "";

        if (fullText.indexOf(' ', 0) == -1) flushWord(canvas, getPaddingTop() + lineSpacing, fullText);
        else {
            while (currWordEnd >= 0) {
                lastWordEnd = currWordEnd + 1;
                currWordEnd = fullText.indexOf(' ', lastWordEnd);

                if (currWordEnd != -1) {
                    getPaint().getTextBounds(fullText, lineStartIndex, currWordEnd, mLineBounds);

                    if (mLineBounds.width() >= drawableWidth) {
                        flushLine(canvas, lineNum, fullText.substring(lineStartIndex, lastWordEnd));
                        textHeights += mLineBounds.height() + ".";
                        lineStartIndex = lastWordEnd;
                        lineNum++;
                    }

                } else {
                    getPaint().getTextBounds(fullText, lineStartIndex, fullText.length(), mLineBounds);

                    if (mLineBounds.width() >= drawableWidth) {
                        flushLine(canvas, lineNum, fullText.substring(lineStartIndex, lastWordEnd));
                        textHeights += mLineBounds.height() + ".";
                        rawFlushLine(canvas, ++lineNum, fullText.substring(lastWordEnd));
                        textHeights += mLineBounds.height() + ".";
                    } else {
                        if (lineNum == 1) {
                            rawFlushLine(canvas, lineNum, fullText);
                            textHeights += mLineBounds.height() + ".";
                        }
                        else {
                            rawFlushLine(canvas, lineNum, fullText.substring(lineStartIndex));
                            textHeights += mLineBounds.height() + ".";
                        }
                    }
                }

            }
        }

//        Log.d(TAG, "onDraw :: viewMeasuredHeight=" + getMeasuredHeight() + " firstLineTextHeight=" + mFirstLineTextHeight + " textHeights=" + textHeights);
    }

    private float getDrawableWidth() {
        return mDrawableWidth == 0 ? (mDrawableWidth = getWidth() - getPaddingLeft() - getPaddingRight()) : mDrawableWidth;
    }

    private void setFirstLineTextHeight(String firstLine) {
        getPaint().getTextBounds(firstLine, 0, firstLine.length(), mLineBounds);
        mFirstLineTextHeight = mLineBounds.height();
    }

    private void rawFlushLine(Canvas canvas, int lineNum, String line) {
        if (lineNum == 1) setFirstLineTextHeight(line);

        float yLine = getPaddingTop() + mFirstLineTextHeight + (lineNum - 1) * getLineHeight();

//        Log.d(TAG, "FLUSH @ " + yLine + " :: " + line);

        canvas.drawText(line, getPaddingLeft(), yLine, getPaint());
    }

    private void flushLine(Canvas canvas, int lineNum, String line) {
        if (lineNum == 1) setFirstLineTextHeight(line);

        float yLine = getPaddingTop() + mFirstLineTextHeight + (lineNum - 1) * getLineHeight();

//        Log.d(TAG, "FLUSH @ " + yLine + " :: " + line);

        String[] words = line.split("\\s+");
        StringBuilder lineBuilder = new StringBuilder();

        for (String word : words) {
            lineBuilder.append(word);
        }

        float xStart = getPaddingLeft();
        float wordWidth = getPaint().measureText(lineBuilder.toString());
        float spacingWidth = (getDrawableWidth() - wordWidth) / (words.length - 1);

        for (String word : words) {
            canvas.drawText(word, xStart, yLine, getPaint());
            xStart += getPaint().measureText(word) + spacingWidth;
        }
    }

    private void flushWord(Canvas canvas, float yLine, String word) {
        float xStart = getPaddingLeft();
        float wordWidth = getPaint().measureText(word);
        float spacingWidth = (getDrawableWidth() - wordWidth) / (word.length() - 1);

        for (int i = 0; i < word.length(); i++) {
            canvas.drawText(word, i, i + 1, xStart, yLine, getPaint());
            xStart += getPaint().measureText(word, i, i + 1) + spacingWidth;
        }
    }

    public enum FontType {
        Thin, Reg, Thick
    }
}