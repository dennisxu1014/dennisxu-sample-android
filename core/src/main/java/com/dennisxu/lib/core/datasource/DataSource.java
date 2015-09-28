package com.dennisxu.lib.core.datasource;

import com.dennisxu.lib.core.config.Configuration;
import com.dennisxu.lib.core.exception.ParseException;
import com.dennisxu.lib.core.net.NetworkExecption;
import com.dennisxu.lib.core.net.Request;
import com.dennisxu.lib.core.net.Response;

public class DataSource {
    private Configuration mConfiguration;

    public DataSource(Configuration configuration) {
        this.mConfiguration = configuration;
    }

    public Configuration getConfiguration() {
        return mConfiguration;
    }

    public <T> Response<T> handleNetRequest(Request request) {
        Response<T> response = new Response<T>();
        try {
            response = CommonNetEngine.getInstance(mConfiguration).doRequest(request);
        } catch (ParseException e) {
            response.setError(e);
            e.printStackTrace();
        } catch (NetworkExecption e) {
            response.setError(e);
            e.printStackTrace();
        }
        return response;
    }
}
