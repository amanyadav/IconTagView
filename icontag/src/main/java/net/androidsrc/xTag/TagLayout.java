package net.androidsrc.xTag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman on 17/7/16.
 */
public class TagLayout extends ViewGroup {
    private final int default_border_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_text_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_background_color = Color.WHITE;
    private final int default_icon_background_color = Color.WHITE;
    private final int default_icon_color = Color.WHITE;
    private final float default_border_stroke_width;
    private final float default_text_size;
    private final float default_horizontal_spacing;
    private final float default_vertical_spacing;
    private final float default_horizontal_padding;
    private final float default_vertical_padding;
    private final float default_circlePaddingFactor;
    private final float default_iconPadding;
    private final boolean default_randomIconBackgroundColor;

    /**
     * The tag outline border color.
     */
    private int borderColor;

    /**
     * The tag text color.
     */
    private int textColor;

    /**
     * The tag background color.
     */
    private int backgroundColor;

    /**
     * The tag background color.
     */
    private int iconBackgroundColor;
    /**
     * The tag icon color.
     */
    private int iconColor;

    /**
     * The tag outline border stroke width, default is 0.5dp.
     */
    private float borderStrokeWidth;

    /**
     * The tag text size, default is 13sp.
     */
    private float textSize;

    /**
     * The horizontal tag spacing, default is 8.0dp.
     */
    private int horizontalSpacing;

    /**
     * The vertical tag spacing, default is 4.0dp.
     */
    private int verticalSpacing;

    /**
     * The horizontal tag padding, default is 12.0dp.
     */
    private int horizontalPadding;

    /**
     * The vertical tag padding, default is 3.0dp.
     */
    private int verticalPadding;

    /**
     * The circlePaddingFactor , default is 1.9f.
     */
    private float circlePaddingFactor;

    /**
     * The iconPadding , default is 1.9f.
     */
    private int iconPadding;

    private boolean randomIconBackgroundColor;

    ColorGenerator generator = ColorGenerator.MATERIAL;

    /**
     * Listener used to dispatch tag click event.
     */
    private OnTagClickListener mOnTagClickListener;

    /**
     * Listener used to handle tag click event.
     */
    private InternalTagClickListener mInternalTagClickListener = new InternalTagClickListener();

    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.tagLayoutStyle);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        default_border_stroke_width = dp2px(0.5f);
        default_text_size = sp2px(13.0f);
        default_horizontal_spacing = dp2px(8.0f);
        default_vertical_spacing = dp2px(4.0f);
        default_horizontal_padding = dp2px(12.0f);
        default_vertical_padding = dp2px(3.0f);
        default_circlePaddingFactor = 1.90f;
        default_iconPadding = dp2px(5f);
        default_randomIconBackgroundColor = true;

        // Load styled attributes.
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagLayout, defStyleAttr, R.style.TagLayout);
        try {
            borderColor = a.getColor(R.styleable.TagLayout_net_borderColor, default_border_color);
            textColor = a.getColor(R.styleable.TagLayout_net_textColor, default_text_color);
            backgroundColor = a.getColor(R.styleable.TagLayout_net_backgroundColor, default_background_color);
            iconBackgroundColor = a.getColor(R.styleable.TagLayout_net_iconBackgroundColor, default_icon_background_color);
            iconColor = a.getColor(R.styleable.TagLayout_net_iconColor, default_icon_color);
            borderStrokeWidth = a.getDimension(R.styleable.TagLayout_net_borderStrokeWidth, default_border_stroke_width);
            textSize = a.getDimension(R.styleable.TagLayout_net_textSize, default_text_size);
            horizontalSpacing = (int) a.getDimension(R.styleable.TagLayout_net_horizontalSpacing, default_horizontal_spacing);
            verticalSpacing = (int) a.getDimension(R.styleable.TagLayout_net_verticalSpacing, default_vertical_spacing);
            horizontalPadding = (int) a.getDimension(R.styleable.TagLayout_net_horizontalPadding, default_horizontal_padding);
            verticalPadding = (int) a.getDimension(R.styleable.TagLayout_net_verticalPadding, default_vertical_padding);
            circlePaddingFactor = a.getFloat(R.styleable.TagLayout_net_circlePaddingFactor, default_circlePaddingFactor);
            iconPadding = (int) a.getDimension(R.styleable.TagLayout_net_iconPadding, default_iconPadding);
            randomIconBackgroundColor = a.getBoolean(R.styleable.TagLayout_net_randomIconBackgroundColor, default_randomIconBackgroundColor);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int row = 0; // The row counter.
        int rowWidth = 0; // Calc the current row width.
        int rowMaxHeight = 0; // Calc the max tag height, in current row.

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                rowWidth += childWidth;
                if (rowWidth > widthSize) { // Next line.
                    rowWidth = childWidth; // The next row width.
                    height += rowMaxHeight + verticalSpacing;
                    rowMaxHeight = childHeight; // The next row max height.
                    row++;
                } else { // This line.
                    rowMaxHeight = Math.max(rowMaxHeight, childHeight);
                }
                rowWidth += horizontalSpacing;
            }
        }
        // Account for the last row height.
        height += rowMaxHeight;

        // Account for the padding too.
        height += getPaddingTop() + getPaddingBottom();

        // If the tags grouped in one row, set the width to wrap the tags.
        if (row == 0) {
            width = rowWidth;
            width += getPaddingLeft() + getPaddingRight();
        } else {// If the tags grouped exceed one line, set the width to match the parent.
            width = widthSize;
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        int childLeft = parentLeft;
        int childTop = parentTop;

        int rowMaxHeight = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                if (childLeft + width > parentRight) { // Next line
                    childLeft = parentLeft;
                    childTop += rowMaxHeight + verticalSpacing;
                    rowMaxHeight = height;
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, height);
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);

                childLeft += width + horizontalSpacing;
            }
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.tags = getTags();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
    }

    /**
     * Returns the tag array in group, except the INPUT tag.
     *
     * @return the tag array.
     */
    public String[] getTags() {
        final int count = getChildCount();
        final List<String> tagList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final IconTagView tagView = getTagAt(i);
            tagList.add(tagView.getText().toString());
        }

        return tagList.toArray(new String[tagList.size()]);
    }

    public void setTags(List<TagData> tagList) {
        setTags(tagList.toArray(new TagData[tagList.size()]));
    }

    /**
     * Set the tags. It will remove all previous tags first.
     *
     * @param tags the tag list to set.
     */
    public void setTags(TagData... tags) {
        removeAllViews();
        for (final TagData tag : tags) {
            appendTag(tag);
        }
    }

    /**
     * Returns the tag view at the specified position in the group.
     *
     * @param index the position at which to get the tag view from.
     * @return the tag view at the specified position or null if the position
     * does not exists within this group.
     */
    protected IconTagView getTagAt(int index) {
        return (IconTagView) getChildAt(index);
    }

    /**
     * Append tag to this group.
     *
     * @param tag the tag to append.
     */
    protected void appendTag(TagData tag) {
        final IconTagView newTag = new IconTagView(getContext(), tag);
        newTag.setOnClickListener(mInternalTagClickListener);
        addView(newTag);
    }

    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new TagLayout.LayoutParams(getContext(), attrs);
    }

    /**
     * Register a callback to be invoked when a tag is clicked.
     *
     * @param l the callback that will run.
     */
    public void setOnTagClickListener(OnTagClickListener l) {
        mOnTagClickListener = l;
    }

    /**
     * Interface definition for a callback to be invoked when a tag is clicked.
     */
    public interface OnTagClickListener {
        /**
         * Called when a tag has been clicked.
         *
         * @param tag The tag text of the tag that was clicked.
         */
        void onTagClick(String tag);
    }

    /**
     * Per-child layout information for layouts.
     */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    /**
     * For {@link TagLayout} save and restore state.
     */
    static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        int tagCount;
        String[] tags;
        String input;

        public SavedState(Parcel source) {
            super(source);
            tagCount = source.readInt();
            tags = new String[tagCount];
            source.readStringArray(tags);
            input = source.readString();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            tagCount = tags.length;
            dest.writeInt(tagCount);
            dest.writeStringArray(tags);
            dest.writeString(input);
        }
    }

    /**
     * The tag view click listener for internal use.
     */
    class InternalTagClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            final IconTagView tag = (IconTagView) v;
            if (mOnTagClickListener != null) {
                mOnTagClickListener.onTagClick(tag.getText().toString());
            }
        }
    }

    /**
     * The Icon tag view
     */
    private class IconTagView extends TextView {

        String TAG = "IconTagView";

        Drawable icon;

        private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private Paint mIconBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        /**
         * The rect for the tag's left corner drawing.
         */
        private RectF mLeftCornerRectF = new RectF();

        /**
         * The rect for the tag's right corner drawing.
         */
        private RectF mRightCornerRectF = new RectF();

        /**
         * The rect for the tag's horizontal blank fill area.
         */
        private RectF mHorizontalBlankFillRectF = new RectF();

        /**
         * The rect for the tag's vertical blank fill area.
         */
        private RectF mVerticalBlankFillRectF = new RectF();

        /**
         * Used to detect the touch event.
         */
        private Rect mOutRect = new Rect();

        /**
         * The path for draw the tag's outline border.
         */
        private Path mBorderPath = new Path();

        ColorFilter colorFilter;
        RectF iconCircleRectF=new RectF();

        {
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(borderStrokeWidth);
            mBackgroundPaint.setStyle(Paint.Style.FILL);
            mIconBackgroundPaint.setStyle(Paint.Style.FILL);
            if (randomIconBackgroundColor)
                mIconBackgroundPaint.setColor(generator.getRandomColor());
            else
                mIconBackgroundPaint.setColor(iconBackgroundColor);
        }


        public IconTagView(Context context, TagData data) {
            super(context);
            setLayoutParams(new TagLayout.LayoutParams(TagLayout.LayoutParams.WRAP_CONTENT, TagLayout.LayoutParams.WRAP_CONTENT));

            setGravity(Gravity.CENTER);
            setText(data.getDisplayName());
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            //temp code
            Log.d(TAG, "text size " + getTextSize());
            setPadding(horizontalPadding + verticalPadding * 2 + (int) (1.1f * getTextSize()), verticalPadding, horizontalPadding, verticalPadding);

            icon = getResources().getDrawable(data.getImgId());

            //icon color matrix
            int red = (iconColor & 0xFF0000) / 0xFFFF;
            int green = (iconColor & 0xFF00) / 0xFF;
            int blue = iconColor & 0xFF;

            float[] matrix = {0, 0, 0, 0, red, 0, 0, 0, 0, green, 0, 0, 0, 0, blue, 0, 0, 0, 1, 0};

            colorFilter = new ColorMatrixColorFilter(matrix);

            // Interrupted long click event to avoid PAUSE popup.
            setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            invalidatePaint();
        }

        private void invalidatePaint() {
            mBorderPaint.setColor(borderColor);
            mBackgroundPaint.setColor(backgroundColor);
            setTextColor(textColor);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawArc(mRightCornerRectF, -90, 90, true, mBackgroundPaint);
            canvas.drawArc(mRightCornerRectF, 0, 90, true, mBackgroundPaint);
            canvas.drawRect(mHorizontalBlankFillRectF, mBackgroundPaint);
            canvas.drawRect(mVerticalBlankFillRectF, mBackgroundPaint);
            canvas.drawPath(mBorderPath, mBorderPaint);
            drawIcon(canvas);
            super.onDraw(canvas);
        }

        private void drawIcon(Canvas canvas) {
            icon.setColorFilter(colorFilter);
            canvas.drawArc(iconCircleRectF, -180, 360, true, mIconBackgroundPaint);
            icon.draw(canvas);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            int left = (int) borderStrokeWidth;
            int top = (int) borderStrokeWidth;
            int right = (int) (left + w - borderStrokeWidth * 2);
            int bottom = (int) (top + h - borderStrokeWidth * 2);

            int d = bottom - top;
            mLeftCornerRectF.set(left, top, left + d, top + d);
            mRightCornerRectF.set(right - d, top, right, top + d);

            //view is called initially
            mBorderPath.reset();
            mBorderPath.addArc(mLeftCornerRectF, -180, 90);
            mBorderPath.addArc(mLeftCornerRectF, -270, 90);
            mBorderPath.addArc(mRightCornerRectF, -90, 90);
            mBorderPath.addArc(mRightCornerRectF, 0, 90);

            int l = (int) (d / 2.0f);
            mBorderPath.moveTo(left + l, top);
            mBorderPath.lineTo(right - l, top);

            mBorderPath.moveTo(left + l, bottom);
            mBorderPath.lineTo(right - l, bottom);

            mBorderPath.moveTo(left, top + l);
            mBorderPath.lineTo(left, bottom - l);

            mBorderPath.moveTo(right, top + l);
            mBorderPath.lineTo(right, bottom - l);

            mHorizontalBlankFillRectF.set(left, top + l, right, bottom - l);
            mVerticalBlankFillRectF.set(left + l, top, right - l, bottom);
            //outer circle rect
            iconCircleRectF.set(mLeftCornerRectF.left - borderStrokeWidth / circlePaddingFactor, mLeftCornerRectF.top - borderStrokeWidth / circlePaddingFactor,
                    mLeftCornerRectF.right + borderStrokeWidth / circlePaddingFactor, mLeftCornerRectF.bottom + borderStrokeWidth / circlePaddingFactor);

            icon.setBounds((int) (iconCircleRectF.left + iconPadding), (int) (iconCircleRectF.top + iconPadding),
                    (int) (iconCircleRectF.right - iconPadding), (int) (iconCircleRectF.bottom - iconPadding));
        }
    }
}
