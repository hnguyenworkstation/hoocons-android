//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.aisen.android.network.task;

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.aisen.android.common.utils.Logger;
import org.aisen.android.network.task.ITaskManager;
import org.aisen.android.network.task.TaskException;

public abstract class WorkTask<Params, Progress, Result> {
	private static final String TAG = "WorkTask";
	private static final int CORE_IMAGE_POOL_SIZE = 10;
	private static final int CORE_POOL_SIZE = 5;
	private static final int MAXIMUM_POOL_SIZE = 128;
	private static final int KEEP_ALIVE = 1;
	private TaskException exception;
	private boolean cancelByUser;
	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask #" + this.mCount.getAndIncrement());
		}
	};
	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue(10);
	public static final Executor THREAD_POOL_EXECUTOR;
	public static final Executor IMAGE_POOL_EXECUTOR;
	public static final Executor SERIAL_EXECUTOR;
	private static final int MESSAGE_POST_RESULT = 1;
	private static final int MESSAGE_POST_PROGRESS = 2;
	private static final WorkTask.InternalHandler sHandler;
	private static volatile Executor sDefaultExecutor;
	private final WorkerRunnable mWorker;
	private final FutureTask mFuture;
	private volatile WorkTask.Status mStatus;
	private final AtomicBoolean mTaskInvoked;
	private String taskId;

	public static void init() {
		sHandler.getLooper();
	}

	private static void setDefaultExecutor(Executor exec) {
		sDefaultExecutor = exec;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public WorkTask(String taskId, ITaskManager taskManager) {
		this();
		this.taskId = taskId;
		taskManager.addTask(this);
	}

	public WorkTask() {
		this.mStatus = WorkTask.Status.PENDING;
		this.mTaskInvoked = new AtomicBoolean();
		this.mWorker = new WorkTask.WorkerRunnable() {
			public Result call() throws Exception {
				WorkTask.this.mTaskInvoked.set(true);
				Process.setThreadPriority(10);
				return WorkTask.this.postResult(WorkTask.this.doInBackground((Params[]) this.mParams));
			}
		};
		this.mFuture = new FutureTask(this.mWorker) {
			protected void done() {
				try {
					Object t = this.get();
					WorkTask.this.postResultIfNotInvoked((Result) t);
				} catch (InterruptedException var2) {
					Log.w("WorkTask", var2);
				} catch (ExecutionException var3) {
					throw new RuntimeException("An error occured while executing doInBackground()", var3.getCause());
				} catch (CancellationException var4) {
					WorkTask.this.postResultIfNotInvoked(null);
				} catch (Throwable var5) {
					throw new RuntimeException("An error occured while executing doInBackground()", var5);
				}

			}
		};
	}

	private void postResultIfNotInvoked(Result result) {
		boolean wasTaskInvoked = this.mTaskInvoked.get();
		if(!wasTaskInvoked) {
			this.postResult(result);
		}

	}

	private Result postResult(Result result) {
		Message message = sHandler.obtainMessage(1, new WorkTask.AsyncTaskResult(this, new Object[]{result}));
		message.sendToTarget();
		return result;
	}

	public final WorkTask.Status getStatus() {
		return this.mStatus;
	}

	protected void onPrepare() {
	}

	protected void onFailure(TaskException exception) {
	}

	protected void onSuccess(Result result) {
	}

	protected Params[] getParams() {
		return (Params[]) this.mWorker.mParams;
	}

	protected void onFinished() {
		Logger.d("WorkTask", String.format("%s --->onFinished()", new Object[]{TextUtils.isEmpty(this.taskId)?"run ":this.taskId + " run "}));
	}

	public abstract Result workInBackground(Params... var1) throws TaskException;

	private Result doInBackground(Params... params) {
		Logger.d("WorkTask", String.format("%s --->doInBackground()", new Object[]{TextUtils.isEmpty(this.taskId)?"run ":this.taskId + " run "}));

		try {
			return this.workInBackground(params);
		} catch (TaskException var3) {
			var3.printStackTrace();
			this.exception = var3;
			return null;
		}
	}

	protected final void onPreExecute() {
		Logger.d("WorkTask", String.format("%s --->onTaskStarted()", new Object[]{TextUtils.isEmpty(this.taskId)?"run ":this.taskId + " run "}));
		this.onPrepare();
	}

	protected final void onPostExecute(Result result) {
		if(this.exception == null) {
			Logger.d("WorkTask", String.format("%s --->onTaskSuccess()", new Object[]{TextUtils.isEmpty(this.taskId)?"run ":this.taskId + " run "}));
			this.onSuccess(result);
		} else if(this.exception != null) {
			Logger.d("WorkTask", String.format("%s --->onFailure(), \nError msg --->", new Object[]{TextUtils.isEmpty(this.taskId)?"run ":this.taskId + " run ", this.exception.getMessage()}));
			this.onFailure(this.exception);
		}

		this.onFinished();
	}

	protected void onProgressUpdate(Progress... values) {
	}

	protected void onCancelled(Result result) {
		this._onCancelled();
	}

	private void _onCancelled() {
		this.onCancelled();
		this.onFinished();
	}

	protected void onCancelled() {
		Logger.d("WorkTask", String.format("%s --->onCancelled()", new Object[]{TextUtils.isEmpty(this.taskId)?"run ":this.taskId + " run "}));
	}

	public final boolean isCancelled() {
		return this.mFuture.isCancelled();
	}

	public boolean isCancelByUser() {
		return this.cancelByUser;
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		this.cancelByUser = true;
		return this.mFuture.cancel(mayInterruptIfRunning);
	}

	public final Result get() throws InterruptedException, ExecutionException {
		return (Result) this.mFuture.get();
	}

	public final Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return (Result) this.mFuture.get(timeout, unit);
	}

	public final WorkTask<Params, Progress, Result> executeOnSerialExecutor(Params... params) {
		return this.executeOnExecutor(SERIAL_EXECUTOR, params);
	}

	public final WorkTask<Params, Progress, Result> executrOnImageExecutor(Params... params) {
		return this.executeOnExecutor(IMAGE_POOL_EXECUTOR, params);
	}

	public final WorkTask<Params, Progress, Result> execute(Params... params) {
		return this.executeOnExecutor(THREAD_POOL_EXECUTOR, params);
	}

	public final WorkTask<Params, Progress, Result> executeOnExecutor(Executor exec, Params... params) {
		this.mStatus = WorkTask.Status.RUNNING;
		this.onPreExecute();
		this.mWorker.mParams = params;
		exec.execute(this.mFuture);
		return this;
	}

	public static void execute(Runnable runnable) {
		sDefaultExecutor.execute(runnable);
	}

	protected final void publishProgress(Progress... values) {
		if(!this.isCancelled()) {
			sHandler.obtainMessage(2, new WorkTask.AsyncTaskResult(this, values)).sendToTarget();
		}

	}

	private void finish(Result result) {
		if(this.isCancelled()) {
			this.onCancelled(result);
		} else {
			this.onPostExecute(result);
		}

		this.mStatus = WorkTask.Status.FINISHED;
	}

	static {
		THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
		IMAGE_POOL_EXECUTOR = Executors.newFixedThreadPool(10, sThreadFactory);
		SERIAL_EXECUTOR = new WorkTask.SerialExecutor();
		sHandler = new WorkTask.InternalHandler();
		sDefaultExecutor = SERIAL_EXECUTOR;
	}

	private static class AsyncTaskResult<Data> {
		final WorkTask mTask;
		final Data[] mData;

		@SafeVarargs
		AsyncTaskResult(WorkTask task, Data... data) {
			this.mTask = task;
			this.mData = data;
		}
	}

	private abstract static class WorkerRunnable<Params, Result> implements Callable<Result> {
		Params[] mParams;

		private WorkerRunnable() {
		}
	}

	private static class InternalHandler extends Handler {
		private InternalHandler() {
		}

		public void handleMessage(Message msg) {
			WorkTask.AsyncTaskResult result = (WorkTask.AsyncTaskResult)msg.obj;
			switch(msg.what) {
				case 1:
					result.mTask.finish(result.mData[0]);
					break;
				case 2:
					result.mTask.onProgressUpdate(result.mData);
			}

		}
	}

	public static enum Status {
		PENDING,
		RUNNING,
		FINISHED;

		private Status() {
		}
	}

	private static class SerialExecutor implements Executor {
		final ArrayDeque<Runnable> mTasks;
		Runnable mActive;

		private SerialExecutor() {
			this.mTasks = new ArrayDeque<>();
		}

		public synchronized void execute(final Runnable r) {
			this.mTasks.offer(new Runnable() {
				public void run() {
					try {
						r.run();
					} finally {
						SerialExecutor.this.scheduleNext();
					}

				}
			});
			if(this.mActive == null) {
				this.scheduleNext();
			}

		}

		private synchronized void scheduleNext() {
			if((this.mActive = (Runnable)this.mTasks.poll()) != null) {
				WorkTask.THREAD_POOL_EXECUTOR.execute(this.mActive);
			}

		}
	}
}
