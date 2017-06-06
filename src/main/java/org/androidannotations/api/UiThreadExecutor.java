package org.androidannotations.api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import java.util.HashMap;
import java.util.Map;

public final class UiThreadExecutor {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            Runnable callback = msg.getCallback();
            if (callback != null) {
                callback.run();
                UiThreadExecutor.decrementToken((Token) msg.obj);
                return;
            }
            super.handleMessage(msg);
        }
    };
    private static final Map<String, Token> TOKENS = new HashMap();

    private static final class Token {
        final String id;
        int runnablesCount;

        private Token(String id) {
            this.runnablesCount = 0;
            this.id = id;
        }
    }

    private UiThreadExecutor() {
    }

    public static void runTask(String id, Runnable task, long delay) {
        if ("".equals(id)) {
            HANDLER.postDelayed(task, delay);
            return;
        }
        HANDLER.postAtTime(task, nextToken(id), SystemClock.uptimeMillis() + delay);
    }

    private static Token nextToken(String id) {
        Token token;
        synchronized (TOKENS) {
            token = (Token) TOKENS.get(id);
            if (token == null) {
                token = new Token(id);
                TOKENS.put(id, token);
            }
            token.runnablesCount++;
        }
        return token;
    }

    private static void decrementToken(Token token) {
        synchronized (TOKENS) {
            int i = token.runnablesCount - 1;
            token.runnablesCount = i;
            if (i == 0) {
                String id = token.id;
                Token old = (Token) TOKENS.remove(id);
                if (old != token) {
                    TOKENS.put(id, old);
                }
            }
        }
    }

    public static void cancelAll(String id) {
        synchronized (TOKENS) {
            Token token = (Token) TOKENS.remove(id);
        }
        if (token != null) {
            HANDLER.removeCallbacksAndMessages(token);
        }
    }
}
