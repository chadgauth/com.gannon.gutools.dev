package com.gannon.gutools;

import android.app.Service;
import android.widget.Toast;

public abstract class ImportIntoDatabase extends Service{
	//Travis finish this piece of code
    public void setSchedule(String loaded)
    {
		Toast.makeText(getApplicationContext(), loaded, Toast.LENGTH_LONG).show();
    }
}
