package com.twh.packagenamechecker;


import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	PackageManager pm;
	List<PackageInfo> appInfo;
	ArrayList<HashMap<String, Object>> appArray = null;
	ListView lv = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView) this.findViewById(R.id.listView);

		pm = this.getPackageManager();
		appInfo = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		// getInstalledPackages(flags):flage的参数有多种

		Iterator<PackageInfo> it = appInfo.iterator();
		while (it.hasNext()) {
			PackageInfo app = (PackageInfo) it.next();
			// if ((app.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==
			// 0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("icon", app.applicationInfo.loadIcon(pm));
			map.put("appName", app.applicationInfo.loadLabel(pm));
			map.put("packageName", app.packageName);
			if (appArray == null)
				appArray = new ArrayList<HashMap<String, Object>>();
			appArray.add(map);
			// }
		}

		SimpleAdapter adapter = new SimpleAdapter(this, appArray,
				R.layout.packitem, new String[] { "icon", "appName",
						"packageName" }, new int[] { R.id.icon, R.id.appName,
						R.id.packageName });
		lv.setAdapter((ListAdapter) adapter);

		adapter.setViewBinder(new ViewBinder() {
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (view instanceof ImageView && data instanceof Drawable) {
					ImageView iv = (ImageView) view;
					iv.setImageDrawable((Drawable) data);
					return true;
				} else if (view instanceof TextView && data instanceof String) {
					TextView tv = (TextView) view;
					tv.setText((String) data);

					return true;
				} else
					return false;
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String packageName = (String) appArray.get(arg2).get(
						"packageName");
				uninstallPackage(packageName);
//				Intent intent = pm.getLaunchIntentForPackage(packageName);
//				Log.i("PackageActivity", "open intent " + packageName);
//				if (intent != null) {
//					startActivity(intent);
//				} else {
//					Log.e("PackageActivity", "startActivity error2.");
//				}
			}
		});
	}
	
	private void uninstallPackage(String packageName)
	{
		Uri packageUri = Uri.parse("package:"+ packageName);
//		Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
		startActivity(uninstallIntent);
	}
	
}