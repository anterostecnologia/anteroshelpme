package br.com.anteros.helpme.dialog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.swt.widgets.Display;

public class AsynchronousThreading {
	private long sleepingTime;

    private boolean synchronousDisplayExecution;

    private Runnable target;

    private Display display;

    private Thread thread;

    private static ExecutorService executor = Executors.newCachedThreadPool();

    public AsynchronousThreading(long sleepingTime, boolean synchronousDisplayExecution, Display display,
            Runnable target) {
        this.sleepingTime = sleepingTime;
        this.synchronousDisplayExecution = synchronousDisplayExecution;
        this.target = target;
        this.display = display;
    }

    public AsynchronousThreading(long sleepingTime, Runnable target) {
        this.sleepingTime = sleepingTime;
        this.target = target;
    }

    public void start() {

        executor.execute(new Runnable() {
            public void run() {
                if (sleepingTime > 0) {
                    try {
                        synchronized (this) {
                            this.wait(sleepingTime);
                        }
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                if (display == null) {
                    target.run();
                } else {
                    if (display.isDisposed()) {
                        return;
                    }
                    if (synchronousDisplayExecution) {
                        display.syncExec(new Runnable() {

                            public void run() {
                                target.run();
                            }
                        });
                    } else {
                        display.asyncExec(new Runnable() {

                            public void run() {
                                target.run();
                            }
                        });
                    }
                }
            }

        });
    }

    public void interrupt() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
    }

}