package uk.colessoft.android.hilllist;

import android.app.Application;
import org.acra.*;
import org.acra.annotation.*;

//@ReportsCrashes(formKey = "dE1qcVo2bTRVYmo5MnJPMnBnME1GaEE6MQ") 

public class BritishHills extends Application{

	@Override
	public void onCreate() {
		// The following line triggers the initialization of ACRA
        ACRA.init(this);
        super.onCreate();
	}

}
