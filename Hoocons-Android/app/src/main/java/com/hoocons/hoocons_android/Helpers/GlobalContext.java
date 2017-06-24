package com.hoocons.hoocons_android.Helpers;

import android.app.Application;
import android.os.Handler;

import java.util.concurrent.TimeUnit;

public class GlobalContext extends Application {

	private static GlobalContext _context;

	public final static int CONN_TIMEOUT = 30000;
	public final static int READ_TIMEOUT = 30000;

	static {

	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		_context = this;
	}

	public static GlobalContext getInstance() {
		return _context;
	}
	
	public Handler getHandler() {
		return mHandler;
	}

	Handler mHandler = new Handler() {
		
	};
}