package com.longingfuture.parval.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.longingfuture.parval.util.FileCopyUtils;

/**
 * 容器管理,多线程安全
 * @author chenglei
 *
 */
public class HttpRequestExecutor {

    private static final Log log = LogFactory.getLog(HttpRequestExecutor.class);

    private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (60 * 1000);
    private static final int DEFAULT_CONNECT_TIMEOUT_MILLISECONDS = 3 * 1000;
    private static final int DEFAULT_MAX_CONNECTIONS_PER_HOST = 5;
    private static final String DEFAULT_ENCODING = "utf-8";
    private static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

    private int connectTimeoutMilliseconds = DEFAULT_CONNECT_TIMEOUT_MILLISECONDS;
    private int readTimeoutMilliseconds = DEFAULT_READ_TIMEOUT_MILLISECONDS;
    private int maxConnectionsPerHost = DEFAULT_MAX_CONNECTIONS_PER_HOST;
    private String encoding = DEFAULT_ENCODING;

    private HttpClient httpClient;

    public HttpRequestExecutor() {
        this.httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        this.httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(this.connectTimeoutMilliseconds);
        this.httpClient.getHttpConnectionManager().getParams().setSoTimeout(this.readTimeoutMilliseconds);
        this.httpClient.getHttpConnectionManager().getParams()
                .setDefaultMaxConnectionsPerHost(this.maxConnectionsPerHost);
    }

    public void destroy() {
        ((MultiThreadedHttpConnectionManager) this.httpClient.getHttpConnectionManager()).shutdown();
    }

    public void setConnectTimeoutMilliseconds(int connectTimeoutMilliseconds) {
        this.connectTimeoutMilliseconds = connectTimeoutMilliseconds;
    }

    public void setReadTimeoutMilliseconds(int readTimeoutMilliseconds) {
        this.readTimeoutMilliseconds = readTimeoutMilliseconds;
    }

    public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public byte[] executeGet(String url, Map<String, String> params) {
        GetMethod getMethod = null;
        try {
            getMethod = createGetMethod(url, params);
            executeHttpMethod(getMethod);
            validateResponse(getMethod);
            return getResponseBody(getMethod);
        } catch (Exception e) {
            if (e instanceof HttpInvokeException) {
                throw (HttpInvokeException) e;
            }
            throw new HttpInvokeException("http invoke,url:[" + url + "],params:[" + params + "] error!", e);
        } finally {
            // Need to explicitly release because it might be pooled.
            this.releaseMethod(getMethod);
        }
    }

    public byte[] executePost(String url, Map<String, String> params) {
        PostMethod postMethod = null;
        try {
            postMethod = this.createPostMethod(url, params);
            executeHttpMethod(postMethod);
            validateResponse(postMethod);
            return getResponseBody(postMethod);
        } catch (Exception ex) {
            if (ex instanceof HttpInvokeException) {
                throw (HttpInvokeException) ex;
            }
            throw new HttpInvokeException("http invoke,url:[" + url + "],params:[" + params + "] error!", ex);
        } finally {
            // Need to explicitly release because it might be pooled.
            this.releaseMethod(postMethod);
        }
    }

    protected GetMethod createGetMethod(String url, Map<String, String> params) throws IOException {
        GetMethod getMethod = new GetMethod(url);

        getMethod.getParams().setContentCharset(encoding);

        if (params != null && params.size() > 0) {
            NameValuePair[] pairs = new NameValuePair[params.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = URIUtil.encodeQuery(entry.getValue(), this.encoding);
                NameValuePair pair = new NameValuePair(key, value);
                pairs[i] = pair;
                i += 1;
            }
            getMethod.setQueryString(pairs);
        }

        return getMethod;
    }

    protected PostMethod createPostMethod(String url, Map<String, String> params) throws IOException {
        PostMethod postMethod = new PostMethod(url);

        postMethod.getParams().setContentCharset(encoding);
        postMethod.setRequestHeader(HTTP_HEADER_CONTENT_TYPE, PostMethod.FORM_URL_ENCODED_CONTENT_TYPE + ";charset="
                + this.encoding);

        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                postMethod.addParameter(entry.getKey(), entry.getValue());
            }
        }

        return postMethod;
    }

    protected void executeHttpMethod(HttpMethod httpMethod) throws IOException {
        httpClient.executeMethod(httpMethod);
    }

    protected void validateResponse(HttpMethod httpMethod) throws IOException {
        if (httpMethod.getStatusCode() >= 300) {
            throw new HttpException("Did not receive successful HTTP response: status code = "
                    + httpMethod.getStatusCode() + ", status message = [" + httpMethod.getStatusText() + "]");
        }
    }

    protected byte[] getResponseBody(HttpMethod httpMethod) throws IOException {
        // return postMethod.getResponseBodyAsString();

        InputStream in = httpMethod.getResponseBodyAsStream();
        byte[] byteBuf = FileCopyUtils.copyToByteArray(in);
        // return new String(byteBuf, this.encoding);

        return byteBuf;
    }

    protected void releaseMethod(HttpMethod httpMethod) {
        if (httpMethod != null) {
            try {
                httpMethod.releaseConnection();
            } catch (Throwable ex) {
                log.error("postMethod.releaseConnection error!", ex);
            }
        }
    }

}
