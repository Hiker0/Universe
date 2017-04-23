package com.allen.test.demon;

import com.allen.test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShortCutTest extends Activity {
	public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		processAddShortcut();
		this.setContentView(R.layout.activity_shortcut_test);
		Button addButton = (Button) this.findViewById(R.id.add_shotcut);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addShortcut();
			}
		});

		Button deleteButton = (Button) this.findViewById(R.id.delete_shotcut);
		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				removeShortCut();
			}
		});
	}

	void processAddShortcut() {
		Intent intent = getIntent();
		if (intent.getAction().equals(Intent.ACTION_CREATE_SHORTCUT)) {
			Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
			launcherIntent.setClassName(this.getPackageName(), ShortCutTest.class.getName());

			Intent addShortcutIntent = new Intent();
			// 不允许重复创建
			addShortcutIntent.putExtra("duplicate", false);
			// 名字
			addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "shortcut");

			// 图标
			addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
					Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));
			addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
			setResult(RESULT_OK, addShortcutIntent);
			finish();
		}
	}

	void addShortcut() {
		Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);

		// 不允许重复创建
		addShortcutIntent.putExtra("duplicate", false);
		// 名字
		addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "shortcut");

		// 图标
		addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));

		// 设置关联程序
		Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
		launcherIntent.setClass(this, ShortCutTest.class);

		addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

		// 发送广播
		sendBroadcast(addShortcutIntent);
	}

	void removeShortCut() {
		Intent intent = new Intent(ACTION_REMOVE_SHORTCUT);

		// 名字
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "shortcut");

		// 设置关联程序
		Intent launcherIntent = new Intent(this, ShortCutTest.class).setAction(Intent.ACTION_MAIN);
		launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

		// 发送广播
		sendBroadcast(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
