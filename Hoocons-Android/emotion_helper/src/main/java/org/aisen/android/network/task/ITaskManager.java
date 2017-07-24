//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.aisen.android.network.task;

import org.aisen.android.network.task.WorkTask;

public interface ITaskManager {
	void addTask(WorkTask var1);

	void removeTask(String var1, boolean var2);

	void removeAllTask(boolean var1);

	int getTaskCount(String var1);
}
