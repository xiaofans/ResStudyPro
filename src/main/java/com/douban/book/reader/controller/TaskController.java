package com.douban.book.reader.controller;

public class TaskController extends AbsTaskController {

    private static final class SingletonHolder {
        static final TaskController INSTANCE = new TaskController();

        private SingletonHolder() {
        }
    }

    public static TaskController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static void run(Runnable runnable) {
        getInstance().execute(runnable, runnable);
    }
}
