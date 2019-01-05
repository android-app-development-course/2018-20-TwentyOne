package com.example.testing.tone;

import android.os.Bundle;
import android.animation.ArgbEvaluator;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements View.OnClickListener{
    private CustomViewPager mViewPager;
    private MyImageView mIvHome; // tab 消息的imageview
    private TextView mTvHome;   // tab 消息的imageview

    private MyImageView mIvCategory; // tab 通讯录的imageview
    private TextView mTvCategory;

    private MyImageView mIvSuccess;  // tab 发现的imageview
    private TextView  Success;

    private MyImageView mIvMine; // tab 我的imageview
    private TextView mTvMine;

    private ArrayList<Fragment> mFragments;
    private ArgbEvaluator mColorEvaluator;

    private int mTextNormalColor;// 未选中的字体颜色
    private int mTextSelectedColor;// 选中的字体颜色
    private LinearLayout mLinearLayoutHome;
    private LinearLayout mLinearLayoutCategory;
    private LinearLayout mLinearLayoutSuccess;
    private LinearLayout mLinearLayoutMine;

    public Home() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initColor();//也就是选中未选中的textview的color
        initView();// 初始化控件
        initData(); // 初始化数据(也就是fragments)
        initSelectImage();// 初始化渐变的图片
        aboutViewpager(); // 关于viewpager
        setListener(); // viewpager设置滑动监听
    }
    private void initSelectImage() {
        mIvHome.setImages(R.drawable.home_normal, R.drawable.home_selected);
        mIvCategory.setImages(R.drawable.category_normal, R.drawable.category_selected);
        mIvSuccess.setImages(R.drawable.success_normal, R.drawable.success_selected);
        mIvMine.setImages(R.drawable.about_normal, R.drawable.about_selected);
    }

    private void initColor() {
        mTextNormalColor = getResources().getColor(R.color.main_bottom_tab_textcolor_normal);
        mTextSelectedColor = getResources().getColor(R.color.main_bottom_tab_textcolor_selected);
    }

    private void setListener() {
        //下面的tab设置点击监听
        mLinearLayoutHome.setOnClickListener(this);
        mLinearLayoutCategory.setOnClickListener(this);
        mLinearLayoutSuccess.setOnClickListener(this);
        mLinearLayoutMine.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPs) {
                setTabTextColorAndImageView(position,positionOffset);// 更改text的颜色还有图片
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setTabTextColorAndImageView(int position, float positionOffset) {
        mColorEvaluator = new ArgbEvaluator();  // 根据偏移量 来得到
        int  evaluateCurrent =(int) mColorEvaluator.evaluate(positionOffset,mTextSelectedColor , mTextNormalColor);//当前tab的颜色值
        int  evaluateThe =(int) mColorEvaluator.evaluate(positionOffset, mTextNormalColor, mTextSelectedColor);// 将要到tab的颜色值
        switch (position) {
            case 0:
                mTvHome.setTextColor(evaluateCurrent);  //设置消息的字体颜色
                mTvCategory.setTextColor(evaluateThe);  //设置通讯录的字体颜色

                mIvHome.transformPage(positionOffset);  //设置消息的图片
                mIvCategory.transformPage(1-positionOffset); //设置通讯录的图片
                break;
            case 1:
                mTvCategory.setTextColor(evaluateCurrent);
                Success.setTextColor(evaluateThe);

                mIvCategory.transformPage(positionOffset);
                mIvSuccess.transformPage(1-positionOffset);
                break;
            case 2:
                Success.setTextColor(evaluateCurrent);
                mTvMine.setTextColor(evaluateThe);

                mIvSuccess.transformPage(positionOffset);
                mIvMine.transformPage(1-positionOffset);
                break;

        }
    }

    private void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new OneFragment());
        mFragments.add(new TwoFragment());
        mFragments.add(new ThreeFragment());
        mFragments.add(new FourFragment());
    }

    private void aboutViewpager() {
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), mFragments);// 初始化adapter
        mViewPager.setAdapter(myAdapter); // 设置adapter
    }

    private void initView() {
        mLinearLayoutHome = (LinearLayout) findViewById(R.id.ll_home);
        mLinearLayoutCategory = (LinearLayout) findViewById(R.id.ll_categroy);
        mLinearLayoutSuccess = (LinearLayout) findViewById(R.id.ll_find);
        mLinearLayoutMine = (LinearLayout) findViewById(R.id.ll_mine);
        mViewPager = (CustomViewPager) findViewById(R.id.vp);
        mViewPager.setScanScroll(false);
        mIvHome = (MyImageView) findViewById(R.id.iv1);  // tab 首页 imageview
        mTvHome = (TextView) findViewById(R.id.rb1);  //  tab  首页 字

        mIvCategory = (MyImageView) findViewById(R.id.iv2); // tab 日程 imageview
        mTvCategory = (TextView) findViewById(R.id.rb2);  // tab   日程 字

        mIvSuccess = (MyImageView) findViewById(R.id.iv3); // tab 成就 imageview
        Success = (TextView) findViewById(R.id.rb3);   //  tab  成就 字

        mIvMine = (MyImageView) findViewById(R.id.iv4);   // tab 我 imageview
        mTvMine = (TextView) findViewById(R.id.rb4);    // tab   我 字
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.ll_categroy:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.ll_find:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.ll_mine:
                mViewPager.setCurrentItem(3);
                break;
        }
    }



    public interface MyTouchListener {
        public boolean onTouchEvent(MotionEvent event);
    }

    // 保存MyTouchListener接口的列表
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<Home.MyTouchListener>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove( listener );
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }


}
