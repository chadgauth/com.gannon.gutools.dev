package com.gannon.gutools.activities;

import java.io.File;

import org.holoeverywhere.ThemeManager;

import com.gannon.gutools.dev.R;
import com.gannon.gutools.fragments.*;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import org.holoeverywhere.addon.AddonSlidingMenu;
import org.holoeverywhere.addon.AddonSlidingMenu.AddonSlidingMenuA;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.app.Activity.Addons;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
 
@Addons(Activity.ADDON_SLIDING_MENU)
public class HomeActivity extends Activity implements OnBackStackChangedListener {
	private int backpress;
	public static interface OnMenuClickListener {
	    public void onMenuClick(int position);
	}

	public StreamService serviceBinder;
	IntentFilter intentFilter;
	Intent i;
	public static final String KEY_DISABLE_MUSIC = "disableMusic";
	private static final String KEY_PAGE = "page";
	private boolean mCreatedByThemeManager = false;
	private int mCurrentPage = -1;
	public boolean mIsPlaying = false;
	private Handler mHandler;
	private boolean mStaticSlidingMenu;
	
	private ServiceConnection connection = new ServiceConnection(){
    	public void onServiceConnected(ComponentName className, IBinder service){
    		serviceBinder = ((StreamService.MyBinder)service).getService();
    		startService(i);
    	}
    	public void onServiceDisconnected(ComponentName className) {
    		serviceBinder=null;
    	}
    };
	
	public void startService() {
        i = new Intent(HomeActivity.this, StreamService.class);
        bindService(i, connection, Context.BIND_AUTO_CREATE);    	
    }
    
    public void stopService() {
    	serviceBinder.onDestroy();
    	serviceBinder=null;
    }

	public AddonSlidingMenuA addonSlidingMenu() {
	    return addon(AddonSlidingMenu.class);
	}

	private int computeMenuWidth() {
		return (int) getResources().getDimension(R.dimen.demo_menu_width);
	     //getResources().getFraction(R.dimen.demo_menu_width,
	   //         getResources().getDisplayMetrics().widthPixels, 1);
	}

	public boolean isPlaying() {
	    return mIsPlaying;
	}

	@Override
	@SuppressLint("NewApi")
	public void onBackPressed() {
	   // if (!getSupportFragmentManager().popBackStackImmediate()) {
	   //     stopService();
	  //      finish();
	  //  }
	    backpress = (backpress + 1);

	    if (backpress>1) {
	    	Intent startMain = new Intent(Intent.ACTION_MAIN);
	        startMain.addCategory(Intent.CATEGORY_HOME);
	        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(startMain);
	        finish();
	    }
	    else
	    	Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onBackStackChanged() {
	    if (mStaticSlidingMenu) {
	        getSupportActionBar().setDisplayHomeAsUpEnabled(
	                getSupportFragmentManager().getBackStackEntryCount() > 0);
	    }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    // This line restore instance state when we are change theme and
	    // activity restarts
		startService();
	    savedInstanceState = instanceState(savedInstanceState);
	    super.onCreate(savedInstanceState);
	
	    backpress=0;
	    mCreatedByThemeManager = getIntent().getBooleanExtra(
	            ThemeManager.KEY_CREATED_BY_THEME_MANAGER, false);
	    if (mCreatedByThemeManager) {
	    }
	
	    if (savedInstanceState != null) {
	        mIsPlaying = savedInstanceState.getBoolean(KEY_DISABLE_MUSIC, false);
	        mCurrentPage = savedInstanceState.getInt(KEY_PAGE, 0);
	    }
	
	    final ActionBar ab = getSupportActionBar();
	    ab.setTitle(R.string.home);
	
	    setContentView(R.layout.content);
	
	    final AddonSlidingMenuA addonSM = addonSlidingMenu();
	    final SlidingMenu sm = addonSM.getSlidingMenu();
	
	    View menu = findViewById(R.id.menu);
	    if (menu == null) {
	        // Phone
	        mStaticSlidingMenu = false;
	        ab.setDisplayHomeAsUpEnabled(true);
	        addonSM.setBehindContentView(R.layout.menu_frame);
	        addonSM.setSlidingActionBarEnabled(true);
	        sm.setBehindWidth(computeMenuWidth());
	        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	        sm.setSlidingEnabled(true);
	    } else {
	        // Tablet
	        mStaticSlidingMenu = true;
	        addonSM.setBehindContentView(R.layout.menu_frame); // dummy view
	        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	        sm.setSlidingEnabled(false);
	    }
	
	    getSupportFragmentManager().addOnBackStackChangedListener(this);
	   
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new NavigationFragment())
		.commit();
	}

	@Override
	protected void onDestroy() {
	    stopService();
	    super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
		}

//	public boolean onOptionsItemSelected(MenuItem item) {
        //This uses the imported MenuItem from ActionBarSherlock
 //       Toast.makeText(this, "Got click: " + item.toString(), Toast.LENGTH_SHORT).show();
//        return true;
 //   }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            if (!mStaticSlidingMenu
	                    && getSupportFragmentManager().getBackStackEntryCount() == 0) {
	                addonSlidingMenu().toggle();
	            } else {
	                onBackPressed();
	            }
	            break;
	        case R.id.logout:
	        	//Toast.makeText(this.getApplicationContext(), "Logout Pressed", Toast.LENGTH_LONG).show();
	        	File file = getDatabasePath("courses.db");
	        	if(file.exists())
	        	  file.delete();
	        	file = getDatabasePath("preferences.db");
	        	if(file.exists())
		        	  file.delete();
	        	Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
			    startActivity(intent);
	        	break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    return true;
	}

	@Override
	protected void onPause() {
	    super.onPause();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	    savedInstanceState = instanceState(savedInstanceState);
	    if (mCreatedByThemeManager && savedInstanceState != null) {
	        savedInstanceState.putBoolean("SlidingActivityHelper.open", false);
	        savedInstanceState.putBoolean("SlidingActivityHelper.secondary", false);
	    }
	    super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
	    super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putBoolean(KEY_DISABLE_MUSIC, mIsPlaying);
	    outState.putInt(KEY_PAGE, mCurrentPage);
	}

	public void postDelayed(Runnable runnable, long delay) {
	    if (mHandler == null) {
	        mHandler = new Handler(getMainLooper());
	    }
	    mHandler.postDelayed(runnable, delay);
	}

	public void replaceFragment(Fragment fragment) {
		ActionBar ab = getSupportActionBar();
		if(fragment instanceof WERGFragment)
			ab.setTitle(R.string.WERGStream);
		else if(fragment instanceof EventFragment)
			ab.setTitle(R.string.events);
		else if(fragment instanceof ScheduleFragment)
			ab.setTitle(R.string.classes);
		else if(fragment instanceof AssignmentFragment)
			ab.setTitle(R.string.assignments);
		else
			ab.setTitle(R.string.home);
		replaceFragment(fragment, null);
	    addonSlidingMenu().showContent();
	    
	}

	public void replaceFragment(Fragment fragment,
	        String backStackName) {
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    ft.replace(R.id.content, fragment);
	    if (backStackName != null) {
	        ft.addToBackStack(backStackName);
	    }
	    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	    ft.commit();
	    addonSlidingMenu().showContent();
	}
 
}