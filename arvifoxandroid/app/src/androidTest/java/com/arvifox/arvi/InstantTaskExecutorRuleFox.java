package com.arvifox.arvi;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.executor.TaskExecutor;

/**
 * A JUnit Test Rule that swaps the background executor used by the Architecture Components with a
 * different one which executes each task synchronously.
 * <p>
 * You can use this rule for your host side tests that use Architecture Components.
 */
public class InstantTaskExecutorRuleFox extends TestWatcher {
    @Override
    protected void starting(Description description) {
        super.starting(description);
        ArchTaskExecutor.getInstance().setDelegate(new TaskExecutor() {
            @Override
            public void executeOnDiskIO(Runnable runnable) {
                runnable.run();
            }

            @Override
            public void postToMainThread(Runnable runnable) {
                runnable.run();
            }

            @Override
            public boolean isMainThread() {
                return true;
            }
        });
    }

    @Override
    protected void finished(Description description) {
        super.finished(description);
        ArchTaskExecutor.getInstance().setDelegate(null);
    }
}
