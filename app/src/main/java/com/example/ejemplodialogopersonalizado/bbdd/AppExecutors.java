package com.example.ejemplodialogopersonalizado.bbdd;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecutors {
    private static final Object LOCK = new Object();
    private static AppExecutors sIntance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private static class MainThreadExecutor implements Executor
    {
        private final Handler mainThreadHandler = new android.os.Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {

            mainThreadHandler.post(command);
        }

    }
    private AppExecutors(Executor diskIO, Executor mainThread, Executor networkIO)
    {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;

    }
    public static AppExecutors getInstance()
    {
        if (sIntance == null) {
            synchronized (LOCK)
            {
                //yo tengo 3 exceutors, voy a permitir 3 hilos, y en red un hilo
                sIntance = new AppExecutors(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3), new MainThreadExecutor());
            }
        }
        return sIntance;
    }
    public Executor getDiskIO()
    {
        return this.diskIO;

    }
    public Executor getMainThread() {
        return this.mainThread;
    }

    public Executor getNetworkIO() {
        return this.networkIO;
    }



}



































































