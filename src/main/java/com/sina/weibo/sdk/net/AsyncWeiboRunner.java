package com.sina.weibo.sdk.net;

import android.content.Context;
import android.os.AsyncTask;
import com.sina.weibo.sdk.cmd.WbAppActivator;
import com.sina.weibo.sdk.exception.WeiboException;

public class AsyncWeiboRunner {
    private Context mContext;

    private static class AsyncTaskResult<T> {
        private WeiboException error;
        private T result;

        public T getResult() {
            return this.result;
        }

        public WeiboException getError() {
            return this.error;
        }

        public AsyncTaskResult(T result) {
            this.result = result;
        }

        public AsyncTaskResult(WeiboException error) {
            this.error = error;
        }
    }

    static class RequestRunner extends AsyncTask<Void, Void, AsyncTaskResult<String>> {
        private final Context mContext;
        private final String mHttpMethod;
        private final RequestListener mListener;
        private final WeiboParameters mParams;
        private final String mUrl;

        public RequestRunner(Context context, String url, WeiboParameters params, String httpMethod, RequestListener listener) {
            this.mContext = context;
            this.mUrl = url;
            this.mParams = params;
            this.mHttpMethod = httpMethod;
            this.mListener = listener;
        }

        protected AsyncTaskResult<String> doInBackground(Void... params) {
            try {
                return new AsyncTaskResult(HttpManager.openUrl(this.mContext, this.mUrl, this.mHttpMethod, this.mParams));
            } catch (WeiboException e) {
                return new AsyncTaskResult(e);
            }
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(AsyncTaskResult<String> result) {
            WeiboException exception = result.getError();
            if (exception != null) {
                this.mListener.onWeiboException(exception);
            } else {
                this.mListener.onComplete((String) result.getResult());
            }
        }
    }

    public AsyncWeiboRunner(Context context) {
        this.mContext = context;
    }

    @Deprecated
    public void requestByThread(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
        final String str = url;
        final String str2 = httpMethod;
        final WeiboParameters weiboParameters = params;
        final RequestListener requestListener = listener;
        new Thread() {
            public void run() {
                try {
                    String resp = HttpManager.openUrl(AsyncWeiboRunner.this.mContext, str, str2, weiboParameters);
                    if (requestListener != null) {
                        requestListener.onComplete(resp);
                    }
                } catch (WeiboException e) {
                    if (requestListener != null) {
                        requestListener.onWeiboException(e);
                    }
                }
            }
        }.start();
    }

    public String request(String url, WeiboParameters params, String httpMethod) throws WeiboException {
        WbAppActivator.getInstance(this.mContext, params.getAppKey()).activateApp();
        return HttpManager.openUrl(this.mContext, url, httpMethod, params);
    }

    public void requestAsync(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
        WbAppActivator.getInstance(this.mContext, params.getAppKey()).activateApp();
        new RequestRunner(this.mContext, url, params, httpMethod, listener).execute(new Void[1]);
    }
}
