package com.edu.zzti.assm.view;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.edu.zzti.assm.app.R;
import com.nineoldandroids.view.ViewHelper;


/**
 * Created by feng on 2015/3/5.
 */
public class SlidingMenu extends HorizontalScrollView {

    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;

    private int mSreenWidth; //屏幕的宽度

    private int mMenuRightPadding = 50; //菜单右侧的边距 50dp

    private int mMenuWidth;
    private boolean once = false;

    private boolean isOpen = false;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    /**
     * 未使用自定义属性时 调用
     *
     * @param context
     * @param attrs
     */
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);


    }

    /**
     * 使用了自定义属性值时 调用
     *
     * @param context
     * @param attrs
     * @param defStyleAttr 自定义属性数组
     */
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取我们定义的属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingMenu, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_rightPadding:
                    mMenuRightPadding = a.getDimensionPixelOffset(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.
                                    getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        a.recycle();

        //获得手机屏幕的服务
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //new屏幕参数类
        DisplayMetrics outMetrics = new DisplayMetrics();
        //把参数赋值过去
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mSreenWidth = outMetrics.widthPixels;

/*        //把dp转换为px
        mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.
                getResources().getDisplayMetrics());*/
    }


    /**
     * 设置子View 的宽和高 设置自己的宽和高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once) {
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);

            mMenuWidth = mMenu.getLayoutParams().width = mSreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mSreenWidth;
            once = true;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 通过设置偏移量 将menu隐藏
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        //判断用户手指的状态  此处为抬起状态 当抬起的判断宽度 来显示或隐藏Menu
        switch (action) {
            //pa
            case MotionEvent.ACTION_UP:
                //隐藏在左边的宽度
                int scrollX = getScrollX();
                if (scrollX >= mMenuWidth / 2) {
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isOpen) return;
        this.smoothScrollTo(0, 0);
        isOpen = true;

    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (!isOpen) return;
        this.smoothScrollTo(mMenuWidth, 0);
        isOpen = false;

    }

    public void toggle() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    /**
     * 滚动时发生
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
/*        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l* 1.0f/mMenuWidth;
        //调用属性动画，设置TranslationX
        ViewHelper.setTranslationX(mMenu, mMenuWidth*scale);*/

//        ViewHelper.setTranslationX(mMenu, l);  抽屉式

        float scale = l * 1.0f / mMenuWidth;
        float leftScale = 1 - 0.3f * scale;
        float rightScale = 0.8f + scale * 0.2f;

        ViewHelper.setScaleX(mMenu, leftScale);
        ViewHelper.setScaleY(mMenu, leftScale);
        ViewHelper.setAlpha(mMenu, 0.7f + 0.3f * (1 - scale));
        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.6f);

        ViewHelper.setPivotX(mContent, 0);
        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
        ViewHelper.setScaleX(mContent, rightScale);
        ViewHelper.setScaleY(mContent, rightScale);
    }
}
