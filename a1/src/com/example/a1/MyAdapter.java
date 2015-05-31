/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.example.a1;

import java.util.HashMap;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import cn.sharesdk.framework.authorize.AuthorizeAdapter;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * 一个用于演示{@link AuthorizeAdapter}的例子。
 * <p>
 * 本demo将在授权页面底部显示一个“关注官方微博”的提示框，
 *用户可以在授权期间对这个提示进行控制，选择关注或者不关
 *注，如果用户最后确定关注此平台官方微博，会在授权结束以
 *后执行关注的方法。
 */
public class MyAdapter extends AuthorizeAdapter implements OnClickListener,
		PlatformActionListener {
	private CheckedTextView ctvFollow;
	private PlatformActionListener backListener;
	private boolean stopFinish;

	public void onCreate() {
		// 隐藏标题栏右部的ShareSDK Logo
		hideShareSDKLogo();

//		TitleLayout llTitle = getTitleLayout();
//		llTitle.getTvTitle().setText("xxxx");

		String platName = getPlatformName();
		if ("SinaWeibo".equals(platName)
				|| "TencentWeibo".equals(platName)) {
			initUi(platName);
			interceptPlatformActionListener(platName);
			return;
		}

	}

	private void initUi(String platName) {

	}

	private void interceptPlatformActionListener(String platName) {
		Platform plat = ShareSDK.getPlatform(platName);
		// 备份此前设置的事件监听器
		backListener = plat.getPlatformActionListener();
		// 设置新的监听器，实现事件拦截
		plat.setPlatformActionListener(this);
	}

	
	public void onError(Platform plat, int action, Throwable t) {
		if (action == Platform.ACTION_AUTHORIZING) {
			// 授权时即发生错误
			plat.setPlatformActionListener(backListener);
			if (backListener != null) {
				backListener.onError(plat, action, t);
			}
		}
		else {
			// 关注时发生错误
			plat.setPlatformActionListener(backListener);
			if (backListener != null) {
				backListener.onComplete(plat, Platform.ACTION_AUTHORIZING, null);
			}
		}
	}

	public void onComplete(Platform plat, int action,
			HashMap<String, Object> res) {
		if (action == Platform.ACTION_FOLLOWING_USER) {
			// 当作授权以后不做任何事情
			plat.setPlatformActionListener(backListener);
			if (backListener != null) {
				backListener.onComplete(plat, Platform.ACTION_AUTHORIZING, null);
			}
		}
		else {
			// 如果没有标记为“授权并关注”则直接返回
			plat.setPlatformActionListener(backListener);
			if (backListener != null) {
				// 关注成功也只是当作授权成功返回
				backListener.onComplete(plat, Platform.ACTION_AUTHORIZING, null);
			}
		}
	}

	public void onCancel(Platform plat, int action) {
		plat.setPlatformActionListener(backListener);
		if (action == Platform.ACTION_AUTHORIZING) {
			// 授权前取消
			if (backListener != null) {
				backListener.onCancel(plat, action);
			}
		}
		else {
			// 当作授权以后不做任何事情
			if (backListener != null) {
				backListener.onComplete(plat, Platform.ACTION_AUTHORIZING, null);
			}

		}
	}

	public void onClick(View v) {
		CheckedTextView ctv = (CheckedTextView) v;
		ctv.setChecked(!ctv.isChecked());
	}

	public void onResize(int w, int h, int oldw, int oldh) {
		if (ctvFollow != null) {
			if (oldh - h > 100) {
				ctvFollow.setVisibility(View.GONE);
			}
			else {
				ctvFollow.setVisibility(View.VISIBLE);
			}
		}
	}

	public boolean onFinish() {
		// 下面的代码演示如何设置自定义的授权页面退出动画
		if ("Douban".equals(getPlatformName())) {
			final View rv = (View) getBodyView().getParent();
			rv.clearAnimation();

			TranslateAnimation ta = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
			ta.setDuration(500);
			ta.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {

				}

				public void onAnimationRepeat(Animation animation) {

				}

				public void onAnimationEnd(Animation animation) {
					stopFinish = false;
					getActivity().finish();
				}
			});
			rv.setAnimation(ta);
		}
		return stopFinish;
	}

}
