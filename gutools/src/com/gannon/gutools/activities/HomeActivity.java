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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.holoeverywhere.addon.AddonSlidingMenu;
import org.holoeverywhere.addon.AddonSlidingMenu.AddonSlidingMenuA;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.app.Activity.Addons;
import org.holoeverywhere.widget.DatePicker;
import org.holoeverywhere.widget.Spinner;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
 
@SuppressLint("NewApi")
@Addons(Activity.ADDON_SLIDING_MENU)
public class HomeActivity extends Activity implements OnBackStackChangedListener, ArticleListFragment.Callbacks {
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
	public int mCurrentPage;
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
	public void onRestoreInstanceState(Bundle savedInstanceState){
		mIsPlaying = savedInstanceState.getBoolean(KEY_DISABLE_MUSIC, false);
        mCurrentPage = savedInstanceState.getInt(KEY_PAGE);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    // This line restore instance state when we are change theme and
	    // activity restarts
		startService();
	    super.onCreate(savedInstanceState);
	    backpress=0;
	    mCreatedByThemeManager = getIntent().getBooleanExtra(
	            ThemeManager.KEY_CREATED_BY_THEME_MANAGER, false);
	    if (mCreatedByThemeManager) {
	    }
	
	    if (savedInstanceState != null) {
	        mIsPlaying = savedInstanceState.getBoolean(KEY_DISABLE_MUSIC, false);
	        mCurrentPage = savedInstanceState.getInt(KEY_PAGE);
	    } else {
	    	mCurrentPage = 1;
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
		if (mCurrentPage == 1){
	    	replaceFragment(new HomeFragment());
	    }else if (mCurrentPage == 2)
	    	replaceFragment(new ArticleListFragment());	
	    else if (mCurrentPage == 3)
	    	replaceFragment(new ScheduleFragment());
	    else if (mCurrentPage == 4)
	    	replaceFragment(new AssignmentFragment());
	    else
	    	replaceFragment(new WERGFragment());
	}

	@Override
	protected void onDestroy() {
	    stopService();
	    super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Inflates the ABS with the menu that includes a logout
		//getSupportMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
	}
	public boolean onPrepareOptionsMenu(Menu menu){
		if(mCurrentPage==4)
			getSupportMenuInflater().inflate(R.menu.assignment_menu, menu);
		else if (mCurrentPage==2)
			getSupportMenuInflater().inflate(R.menu.refresh_menu, menu);
		else
			getSupportMenuInflater().inflate(R.menu.settings_menu, menu);
		return super.onPrepareOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    //String[] array_spinner;
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
	        case R.id.add:
	        	AssignmentFragment af = (AssignmentFragment) this.getSupportFragmentManager()
	        	.findFragmentById(R.id.content);
	        	af.setVisibility(true);
	        	/*	Dialog dialog = new Dialog(this);
	        	dialog.setTitle("Add Assignment");
				dialog.setContentView(R.layout.assignment_dialog);
				//Calendar c = Calendar.getInstance(); 
				//int month = c.get(Calendar.MONTH);
				//int year = c.get(Calendar.YEAR);
				//Calendar c = Calendar.getInstance(); 
				//int month = c.get(Calendar.MONTH);
				//int year = c.get(Calendar.YEAR);
				//array_spinner=new String[12-month];
				//for (int i=month; i<12; i++){
				//	array_spinner[i-month] = new DateFormatSymbols().getMonths()[i];
				//}
				//Spinner s = (Spinner) dialog.findViewById(R.id.monthSpinner);
				//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				//		R.layout.simple_spinner_dropdown_item, array_spinner);
				//		s.setAdapter(adapter);
						
				dialog.show();
				*/
	        	break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    return true;
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
		if(fragment instanceof WERGFragment){
			ab.setTitle(R.string.WERGStream);
			mCurrentPage = 5;
		}
		else if(fragment instanceof ArticleListFragment){
			ab.setTitle(R.string.events);
			mCurrentPage = 2;
		}
		else if(fragment instanceof ScheduleFragment){
			ab.setTitle(R.string.classes);
			mCurrentPage = 3;
		}
			
		else if(fragment instanceof AssignmentFragment){
			ab.setTitle(R.string.assignments);
			mCurrentPage = 4;
		}
		else{
			ab.setTitle(R.string.home);
			mCurrentPage = 1;
		}
		invalidateOptionsMenu();
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

	@Override
	public void onItemSelected(String id) {
		Article selected = (Article) 
				((ArticleListFragment) getSupportFragmentManager().findFragmentById(R.id.article_list))
				.getListAdapter().getItem(Integer.parseInt(id));
        
        //mark article as read
        selected.setRead(true);
        ArticleListAdapter adapter = (ArticleListAdapter) ((ArticleListFragment) getSupportFragmentManager().findFragmentById(R.id.article_list)).getListAdapter();
        adapter.notifyDataSetChanged();
        
        Log.i("test", selected.getGuid());
        
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selected.getGuid()));
        startActivity(browserIntent);
	}
 
}