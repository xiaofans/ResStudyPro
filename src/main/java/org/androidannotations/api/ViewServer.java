package org.androidannotations.api;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ViewServer implements Runnable {
    private static final String BUILD_TYPE_USER = "user";
    private static final String COMMAND_PROTOCOL_VERSION = "PROTOCOL";
    private static final String COMMAND_SERVER_VERSION = "SERVER";
    private static final String COMMAND_WINDOW_MANAGER_AUTOLIST = "AUTOLIST";
    private static final String COMMAND_WINDOW_MANAGER_GET_FOCUS = "GET_FOCUS";
    private static final String COMMAND_WINDOW_MANAGER_LIST = "LIST";
    private static final String LOG_TAG = "ViewServer";
    private static final String VALUE_PROTOCOL_VERSION = "4";
    private static final String VALUE_SERVER_VERSION = "4";
    private static final int VIEW_SERVER_DEFAULT_PORT = 4939;
    private static final int VIEW_SERVER_MAX_CONNECTIONS = 10;
    private static ViewServer sServer;
    private final ReentrantReadWriteLock mFocusLock;
    private View mFocusedWindow;
    private final List<WindowListener> mListeners;
    private final int mPort;
    private ServerSocket mServer;
    private Thread mThread;
    private ExecutorService mThreadPool;
    private final Map<View, String> mWindows;
    private final ReentrantReadWriteLock mWindowsLock;

    private static class UncloseableOutputStream extends OutputStream {
        private final OutputStream mStream;

        UncloseableOutputStream(OutputStream stream) {
            this.mStream = stream;
        }

        public void close() throws IOException {
        }

        public boolean equals(Object o) {
            return this.mStream.equals(o);
        }

        public void flush() throws IOException {
            this.mStream.flush();
        }

        public int hashCode() {
            return this.mStream.hashCode();
        }

        public String toString() {
            return this.mStream.toString();
        }

        public void write(byte[] buffer, int offset, int count) throws IOException {
            this.mStream.write(buffer, offset, count);
        }

        public void write(byte[] buffer) throws IOException {
            this.mStream.write(buffer);
        }

        public void write(int oneByte) throws IOException {
            this.mStream.write(oneByte);
        }
    }

    private interface WindowListener {
        void focusChanged();

        void windowsChanged();
    }

    private static final class NoopViewServer extends ViewServer {
        private NoopViewServer() {
            super();
        }

        public boolean start() throws IOException {
            return false;
        }

        public boolean stop() {
            return false;
        }

        public boolean isRunning() {
            return false;
        }

        public void addWindow(Activity activity) {
        }

        public void removeWindow(Activity activity) {
        }

        public void addWindow(View view, String name) {
        }

        public void removeWindow(View view) {
        }

        public void setFocusedWindow(Activity activity) {
        }

        public void setFocusedWindow(View view) {
        }

        public void run() {
        }
    }

    private class ViewServerWorker implements Runnable, WindowListener {
        private Socket mClient;
        private final Object[] mLock;
        private boolean mNeedFocusedWindowUpdate;
        private boolean mNeedWindowListUpdate;

        private ViewServerWorker(Socket client) {
            this.mLock = new Object[0];
            this.mClient = client;
            this.mNeedWindowListUpdate = false;
            this.mNeedFocusedWindowUpdate = false;
        }

        public void run() {
            IOException e;
            Throwable th;
            BufferedReader in = null;
            try {
                BufferedReader in2 = new BufferedReader(new InputStreamReader(this.mClient.getInputStream()), 1024);
                try {
                    String command;
                    String parameters;
                    boolean result;
                    String request = in2.readLine();
                    int index = request.indexOf(32);
                    if (index == -1) {
                        command = request;
                        parameters = "";
                    } else {
                        command = request.substring(0, index);
                        parameters = request.substring(index + 1);
                    }
                    if (ViewServer.COMMAND_PROTOCOL_VERSION.equalsIgnoreCase(command)) {
                        result = ViewServer.writeValue(this.mClient, "4");
                    } else if (ViewServer.COMMAND_SERVER_VERSION.equalsIgnoreCase(command)) {
                        result = ViewServer.writeValue(this.mClient, "4");
                    } else if (ViewServer.COMMAND_WINDOW_MANAGER_LIST.equalsIgnoreCase(command)) {
                        result = listWindows(this.mClient);
                    } else if (ViewServer.COMMAND_WINDOW_MANAGER_GET_FOCUS.equalsIgnoreCase(command)) {
                        result = getFocusedWindow(this.mClient);
                    } else if (ViewServer.COMMAND_WINDOW_MANAGER_AUTOLIST.equalsIgnoreCase(command)) {
                        result = windowManagerAutolistLoop();
                    } else {
                        result = windowCommand(this.mClient, command, parameters);
                    }
                    if (!result) {
                        Log.w(ViewServer.LOG_TAG, "An error occurred with the command: " + command);
                    }
                    if (in2 != null) {
                        try {
                            in2.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (this.mClient != null) {
                        try {
                            this.mClient.close();
                            in = in2;
                            return;
                        } catch (IOException e22) {
                            e22.printStackTrace();
                            in = in2;
                            return;
                        }
                    }
                } catch (IOException e3) {
                    e22 = e3;
                    in = in2;
                    try {
                        Log.w(ViewServer.LOG_TAG, "Connection error: ", e22);
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        if (this.mClient != null) {
                            try {
                                this.mClient.close();
                            } catch (IOException e2222) {
                                e2222.printStackTrace();
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e22222) {
                                e22222.printStackTrace();
                            }
                        }
                        if (this.mClient != null) {
                            try {
                                this.mClient.close();
                            } catch (IOException e222222) {
                                e222222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    in = in2;
                    if (in != null) {
                        in.close();
                    }
                    if (this.mClient != null) {
                        this.mClient.close();
                    }
                    throw th;
                }
            } catch (IOException e4) {
                e222222 = e4;
                Log.w(ViewServer.LOG_TAG, "Connection error: ", e222222);
                if (in != null) {
                    in.close();
                }
                if (this.mClient != null) {
                    this.mClient.close();
                }
            }
        }

        private boolean windowCommand(Socket client, String command, String parameters) {
            Exception e;
            Throwable th;
            boolean success = true;
            BufferedWriter out = null;
            try {
                int index = parameters.indexOf(32);
                if (index == -1) {
                    index = parameters.length();
                }
                int hashCode = (int) Long.parseLong(parameters.substring(0, index), 16);
                if (index < parameters.length()) {
                    parameters = parameters.substring(index + 1);
                } else {
                    parameters = "";
                }
                if (findWindow(hashCode) != null) {
                    Method dispatch = ViewDebug.class.getDeclaredMethod("dispatchCommand", new Class[]{View.class, String.class, String.class, OutputStream.class});
                    dispatch.setAccessible(true);
                    dispatch.invoke(null, new Object[]{window, command, parameters, new UncloseableOutputStream(client.getOutputStream())});
                    if (!client.isOutputShutdown()) {
                        BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                        try {
                            out2.write("DONE\n");
                            out2.flush();
                            out = out2;
                        } catch (Exception e2) {
                            e = e2;
                            out = out2;
                            try {
                                Log.w(ViewServer.LOG_TAG, "Could not send command " + command + " with parameters " + parameters, e);
                                success = false;
                                if (out != null) {
                                    try {
                                        out.close();
                                    } catch (IOException e3) {
                                        success = false;
                                    }
                                }
                                return success;
                            } catch (Throwable th2) {
                                th = th2;
                                if (out != null) {
                                    try {
                                        out.close();
                                    } catch (IOException e4) {
                                    }
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            out = out2;
                            if (out != null) {
                                out.close();
                            }
                            throw th;
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e5) {
                            success = false;
                        }
                    }
                    return success;
                } else if (out == null) {
                    return false;
                } else {
                    try {
                        out.close();
                        return false;
                    } catch (IOException e6) {
                        return false;
                    }
                }
            } catch (Exception e7) {
                e = e7;
                Log.w(ViewServer.LOG_TAG, "Could not send command " + command + " with parameters " + parameters, e);
                success = false;
                if (out != null) {
                    out.close();
                }
                return success;
            }
        }

        private View findWindow(int hashCode) {
            if (hashCode == -1) {
                View view = null;
                ViewServer.this.mWindowsLock.readLock().lock();
                try {
                    view = ViewServer.this.mFocusedWindow;
                    return view;
                } finally {
                    ViewServer.this.mWindowsLock.readLock().unlock();
                }
            } else {
                ViewServer.this.mWindowsLock.readLock().lock();
                try {
                    for (Entry<View, String> entry : ViewServer.this.mWindows.entrySet()) {
                        if (System.identityHashCode(entry.getKey()) == hashCode) {
                            View view2 = (View) entry.getKey();
                            return view2;
                        }
                    }
                    ViewServer.this.mWindowsLock.readLock().unlock();
                    return null;
                } finally {
                    ViewServer.this.mWindowsLock.readLock().unlock();
                }
            }
        }

        private boolean listWindows(Socket client) {
            Throwable th;
            BufferedWriter out = null;
            try {
                ViewServer.this.mWindowsLock.readLock().lock();
                BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()), 8192);
                try {
                    for (Entry<View, String> entry : ViewServer.this.mWindows.entrySet()) {
                        out2.write(Integer.toHexString(System.identityHashCode(entry.getKey())));
                        out2.write(32);
                        out2.append((CharSequence) entry.getValue());
                        out2.write(10);
                    }
                    out2.write("DONE.\n");
                    out2.flush();
                    ViewServer.this.mWindowsLock.readLock().unlock();
                    if (out2 != null) {
                        try {
                            out2.close();
                            out = out2;
                            return true;
                        } catch (IOException e) {
                            out = out2;
                            return false;
                        }
                    }
                    return true;
                } catch (Exception e2) {
                    out = out2;
                    ViewServer.this.mWindowsLock.readLock().unlock();
                    if (out != null) {
                        return false;
                    }
                    try {
                        out.close();
                        return false;
                    } catch (IOException e3) {
                        return false;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    out = out2;
                    ViewServer.this.mWindowsLock.readLock().unlock();
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e4) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                ViewServer.this.mWindowsLock.readLock().unlock();
                if (out != null) {
                    return false;
                }
                out.close();
                return false;
            } catch (Throwable th3) {
                th = th3;
                ViewServer.this.mWindowsLock.readLock().unlock();
                if (out != null) {
                    out.close();
                }
                throw th;
            }
        }

        private boolean getFocusedWindow(Socket client) {
            Throwable th;
            BufferedWriter out = null;
            try {
                BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()), 8192);
                try {
                    ViewServer.this.mFocusLock.readLock().lock();
                    View focusedWindow = ViewServer.this.mFocusedWindow;
                    ViewServer.this.mFocusLock.readLock().unlock();
                    if (focusedWindow != null) {
                        ViewServer.this.mWindowsLock.readLock().lock();
                        String focusName = (String) ViewServer.this.mWindows.get(ViewServer.this.mFocusedWindow);
                        ViewServer.this.mWindowsLock.readLock().unlock();
                        out2.write(Integer.toHexString(System.identityHashCode(focusedWindow)));
                        out2.write(32);
                        out2.append(focusName);
                    }
                    out2.write(10);
                    out2.flush();
                    if (out2 != null) {
                        try {
                            out2.close();
                            out = out2;
                            return true;
                        } catch (IOException e) {
                            out = out2;
                            return false;
                        }
                    }
                    return true;
                } catch (Exception e2) {
                    out = out2;
                    if (out != null) {
                        return false;
                    }
                    try {
                        out.close();
                        return false;
                    } catch (IOException e3) {
                        return false;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    out = out2;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e4) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                if (out != null) {
                    return false;
                }
                out.close();
                return false;
            } catch (Throwable th3) {
                th = th3;
                if (out != null) {
                    out.close();
                }
                throw th;
            }
        }

        public void windowsChanged() {
            synchronized (this.mLock) {
                this.mNeedWindowListUpdate = true;
                this.mLock.notifyAll();
            }
        }

        public void focusChanged() {
            synchronized (this.mLock) {
                this.mNeedFocusedWindowUpdate = true;
                this.mLock.notifyAll();
            }
        }

        private boolean windowManagerAutolistLoop() {
            Exception e;
            Throwable th;
            ViewServer.this.addWindowListener(this);
            BufferedWriter bufferedWriter = null;
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(this.mClient.getOutputStream()));
                while (!Thread.interrupted()) {
                    try {
                        boolean needWindowListUpdate = false;
                        boolean needFocusedWindowUpdate = false;
                        synchronized (this.mLock) {
                            while (!this.mNeedWindowListUpdate && !this.mNeedFocusedWindowUpdate) {
                                this.mLock.wait();
                            }
                            if (this.mNeedWindowListUpdate) {
                                this.mNeedWindowListUpdate = false;
                                needWindowListUpdate = true;
                            }
                            if (this.mNeedFocusedWindowUpdate) {
                                this.mNeedFocusedWindowUpdate = false;
                                needFocusedWindowUpdate = true;
                            }
                        }
                        if (needWindowListUpdate) {
                            out.write("LIST UPDATE\n");
                            out.flush();
                        }
                        if (needFocusedWindowUpdate) {
                            out.write("FOCUS UPDATE\n");
                            out.flush();
                        }
                    } catch (Exception e2) {
                        e = e2;
                        bufferedWriter = out;
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedWriter = out;
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e3) {
                    }
                }
                ViewServer.this.removeWindowListener(this);
                bufferedWriter = out;
            } catch (Exception e4) {
                e = e4;
                try {
                    Log.w(ViewServer.LOG_TAG, "Connection error: ", e);
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                        } catch (IOException e5) {
                        }
                    }
                    ViewServer.this.removeWindowListener(this);
                    return true;
                } catch (Throwable th3) {
                    th = th3;
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                        } catch (IOException e6) {
                        }
                    }
                    ViewServer.this.removeWindowListener(this);
                    throw th;
                }
            }
            return true;
        }
    }

    public static ViewServer get(Context context) {
        ApplicationInfo info = context.getApplicationInfo();
        if (!BUILD_TYPE_USER.equals(Build.TYPE) || (info.flags & 2) == 0) {
            sServer = new NoopViewServer();
        } else {
            if (sServer == null) {
                sServer = new ViewServer((int) VIEW_SERVER_DEFAULT_PORT);
            }
            if (!sServer.isRunning()) {
                try {
                    sServer.start();
                } catch (IOException e) {
                    Log.d(LOG_TAG, "Error:", e);
                }
            }
        }
        return sServer;
    }

    private ViewServer() {
        this.mListeners = new CopyOnWriteArrayList();
        this.mWindows = new HashMap();
        this.mWindowsLock = new ReentrantReadWriteLock();
        this.mFocusLock = new ReentrantReadWriteLock();
        this.mPort = -1;
    }

    private ViewServer(int port) {
        this.mListeners = new CopyOnWriteArrayList();
        this.mWindows = new HashMap();
        this.mWindowsLock = new ReentrantReadWriteLock();
        this.mFocusLock = new ReentrantReadWriteLock();
        this.mPort = port;
    }

    public boolean start() throws IOException {
        if (this.mThread != null) {
            return false;
        }
        this.mThread = new Thread(this, "Local View Server [port=" + this.mPort + "]");
        this.mThreadPool = Executors.newFixedThreadPool(10);
        this.mThread.start();
        return true;
    }

    public boolean stop() {
        if (this.mThread != null) {
            this.mThread.interrupt();
            if (this.mThreadPool != null) {
                try {
                    this.mThreadPool.shutdownNow();
                } catch (SecurityException e) {
                    Log.w(LOG_TAG, "Could not stop all view server threads");
                }
            }
            this.mThreadPool = null;
            this.mThread = null;
            try {
                this.mServer.close();
                this.mServer = null;
                return true;
            } catch (IOException e2) {
                Log.w(LOG_TAG, "Could not close the view server");
            }
        }
        this.mWindowsLock.writeLock().lock();
        try {
            this.mWindows.clear();
            this.mFocusLock.writeLock().lock();
            try {
                this.mFocusedWindow = null;
                return false;
            } finally {
                this.mFocusLock.writeLock().unlock();
            }
        } finally {
            this.mWindowsLock.writeLock().unlock();
        }
    }

    public boolean isRunning() {
        return this.mThread != null && this.mThread.isAlive();
    }

    public void addWindow(Activity activity) {
        String name = activity.getTitle().toString();
        if (TextUtils.isEmpty(name)) {
            name = activity.getClass().getCanonicalName() + "/0x" + System.identityHashCode(activity);
        } else {
            name = name + "(" + activity.getClass().getCanonicalName() + ")";
        }
        addWindow(activity.getWindow().getDecorView(), name);
    }

    public void removeWindow(Activity activity) {
        removeWindow(activity.getWindow().getDecorView());
    }

    public void addWindow(View view, String name) {
        this.mWindowsLock.writeLock().lock();
        try {
            this.mWindows.put(view.getRootView(), name);
            fireWindowsChangedEvent();
        } finally {
            this.mWindowsLock.writeLock().unlock();
        }
    }

    public void removeWindow(View view) {
        this.mWindowsLock.writeLock().lock();
        try {
            View rootView = view.getRootView();
            this.mWindows.remove(rootView);
            this.mFocusLock.writeLock().lock();
            try {
                if (this.mFocusedWindow == rootView) {
                    this.mFocusedWindow = null;
                }
                this.mFocusLock.writeLock().unlock();
                fireWindowsChangedEvent();
            } catch (Throwable th) {
                this.mFocusLock.writeLock().unlock();
            }
        } finally {
            this.mWindowsLock.writeLock().unlock();
        }
    }

    public void setFocusedWindow(Activity activity) {
        setFocusedWindow(activity.getWindow().getDecorView());
    }

    public void setFocusedWindow(View view) {
        this.mFocusLock.writeLock().lock();
        try {
            this.mFocusedWindow = view == null ? null : view.getRootView();
            fireFocusChangedEvent();
        } finally {
            this.mFocusLock.writeLock().unlock();
        }
    }

    public void run() {
        try {
            this.mServer = new ServerSocket(this.mPort, 10, InetAddress.getLocalHost());
        } catch (Exception e) {
            Log.w(LOG_TAG, "Starting ServerSocket error: ", e);
        }
        while (this.mServer != null && Thread.currentThread() == this.mThread) {
            try {
                Socket client = this.mServer.accept();
                if (this.mThreadPool != null) {
                    this.mThreadPool.submit(new ViewServerWorker(client));
                } else {
                    try {
                        client.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                Log.w(LOG_TAG, "Connection error: ", e3);
            }
        }
    }

    private static boolean writeValue(Socket client, String value) {
        Throwable th;
        BufferedWriter out = null;
        try {
            BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()), 8192);
            try {
                out2.write(value);
                out2.write("\n");
                out2.flush();
                if (out2 != null) {
                    try {
                        out2.close();
                        out = out2;
                        return true;
                    } catch (IOException e) {
                        out = out2;
                        return false;
                    }
                }
                return true;
            } catch (Exception e2) {
                out = out2;
                if (out != null) {
                    return false;
                }
                try {
                    out.close();
                    return false;
                } catch (IOException e3) {
                    return false;
                }
            } catch (Throwable th2) {
                th = th2;
                out = out2;
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            if (out != null) {
                return false;
            }
            out.close();
            return false;
        } catch (Throwable th3) {
            th = th3;
            if (out != null) {
                out.close();
            }
            throw th;
        }
    }

    private void fireWindowsChangedEvent() {
        for (WindowListener listener : this.mListeners) {
            listener.windowsChanged();
        }
    }

    private void fireFocusChangedEvent() {
        for (WindowListener listener : this.mListeners) {
            listener.focusChanged();
        }
    }

    private void addWindowListener(WindowListener listener) {
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    private void removeWindowListener(WindowListener listener) {
        this.mListeners.remove(listener);
    }
}
