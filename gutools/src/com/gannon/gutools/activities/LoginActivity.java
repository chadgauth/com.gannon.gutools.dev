package com.gannon.gutools.activities;

import java.io.File;

import net.sqlcipher.database.SQLiteDatabase;

import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.Toast;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.app.Activity;

import com.gannon.gutools.dev.R;
 
public class LoginActivity extends Activity {
	private SQLiteDatabase database;
	private Button button;
	private EditText ur;
	private EditText ps;
	private Handler mHandler = new Handler();
	private int backpress;
	public void onCreate(Bundle savedInstanceState) {
		backpress=0;
		final Context context = this;
		SQLiteDatabase.loadLibs(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		button = (Button) findViewById(R.id.button1);
		ur = (EditText)findViewById(R.id.editText1);
		ps = (EditText)findViewById(R.id.editText2);
		button.setVisibility(View.INVISIBLE);
		ur.setVisibility(View.INVISIBLE);
		ps.setVisibility(View.INVISIBLE);
		if(isLoggedIn()){
			Intent intent = new Intent(context, HomeActivity.class);
	        startActivity(intent);
		}else{
			button.setVisibility(View.VISIBLE);
			ur.setVisibility(View.VISIBLE);
			ps.setVisibility(View.VISIBLE);
			button.setOnClickListener(new OnClickListener() {
				
			  @Override
			  public void onClick(View arg0) {
				InitializeSQLCipher();
			    Intent intent = new Intent(context, DataPullActivity.class);
			    startActivity(intent);
			  }
	 
			});
		}
	}
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
		        this.finish();
		    }
		    else
		    	Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

		}
	private boolean isLoggedIn() {
		File databaseFile = getDatabasePath("preferences.db");
		if(!databaseFile.exists()){
			return false;
		}
		database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "gannon123", null);
        Cursor cr = database.query("person", null, null, null, null, null, null);
        cr.moveToFirst();
        Boolean result = cr.getInt(2)>0;
        cr.close();
        database.close();
        return result;
	}
	private void InitializeSQLCipher() {
        
        File databaseFile = getDatabasePath("preferences.db");
        databaseFile.mkdirs();
        databaseFile.delete();
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "gannon123", null);
        database.execSQL("create table person(user, pass, log)");
        database.execSQL("insert into person(user, pass, log) values(?, ?, ?)", new Object[]{ur.getText().toString(),
        		ps.getText().toString(), true});
        database.close();
    }
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
 
}