package dam.proyecto;

import android.os.Bundle;
import android.os.StrictMode;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import dam.proyecto.SieteGemas;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Para inhibir los mensajes de error procedentes de Internet y asi poder comunicar
		//con el servidor de IES Trassierra.
		if (android.os.Build.VERSION.SDK_INT > 9)
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
			StrictMode.setThreadPolicy(policy);
		}
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		initialize(new SieteGemas(), config);
	}
}
