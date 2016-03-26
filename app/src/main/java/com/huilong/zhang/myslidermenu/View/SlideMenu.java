package com.huilong.zhang.myslidermenu.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by Mario on 3/21/16.
 */
public class SlideMenu extends FrameLayout {
    private View menuView,mainView;
    private int menuWidth = 0;
    private Scroller scroller;

    public SlideMenu(Context context) {
        super(context);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) ( ev.getX()- downX);

                if(Math.abs(deltaX)>8){
                    return true;                      //默认返回false,拦截话返回true,在onTouchEvent事件里面处理
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

  /*  @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //		//测量所有子view的宽高
        //通过getLayoutParams方法可以获取到布局文件中指定宽高
		menuView.measure(menuView.getLayoutParams().width, heightMeasureSpec);
		//直接使用SlideMenu的测量参数，因为它的宽高都是充满父窗体
		mainView.measure(widthMeasureSpec, heightMeasureSpec);
    }*/
    /**
     * l: 当前子view的左边在父view的坐标系中的x坐标
     * t: 当前子view的顶边在父view的坐标系中的y坐标
     */

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("MAIN", "L: "+l+"   t: "+t  +"  r: "+r  + "   b: "+b);

        menuView.layout(-menuWidth, 0, 0, menuView.getMeasuredHeight());
        mainView.layout(0, 0, r, b);

    }
    private int downX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int deltaX = (int) ( moveX- downX);
                int newScrollX = getScrollX() - deltaX;

                if(newScrollX<-menuWidth)newScrollX = -menuWidth;
                if(newScrollX>0)newScrollX = 0;

                Log.e("Main", "scrollX: "+getScrollX());
                scrollTo(newScrollX, 0);
                downX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                if(getScrollX()>-menuWidth/2){
//				//关闭菜单
                    closeMenu();
                }else {
                    //打开菜单
                    openMenu();
                }
                break;
            default:
                break;

        }
        return true;
    }
    /**
     * Scroller不主动去调用这个方法
     * 而invalidate()可以掉这个方法
     * invalidate->draw->computeScroll
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){//返回true,表示动画没结束
            scrollTo(scroller.getCurrX(), 0);
            invalidate();
        }
    }

    private void closeMenu(){
        scroller.startScroll(getScrollX(), 0, 0-getScrollX(), 0, 400);
        invalidate();
    }

    private void openMenu(){
        scroller.startScroll(getScrollX(), 0, -menuWidth-getScrollX(), 0, 400);
        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menuView = getChildAt(0);
        mainView = getChildAt(1);
        menuWidth = menuView.getLayoutParams().width;
    }
    private void init(){
        scroller = new Scroller(getContext());
    }
    public void switchMenu() {
        if(getScrollX()==0){
            //需要打开
            openMenu();
        }else {
            //需要关闭
            closeMenu();
        }
    }
}
