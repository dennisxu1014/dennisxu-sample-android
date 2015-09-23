package com.dennisxu.lib.core.datasource;

import com.dennisxu.lib.core.config.Configuration;
import com.dennisxu.lib.core.entity.Entity;
import com.dennisxu.lib.core.exception.ParseException;
import com.dennisxu.lib.core.net.HttpEngine;
import com.dennisxu.lib.core.net.NetworkExecption;
import com.dennisxu.lib.core.net.Request;
import com.dennisxu.lib.core.net.Request.ParserType;
import com.dennisxu.lib.core.net.Response;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;


public class CommonNetEngine {
    private static final String TAG = "HTTP";
    private static CommonNetEngine instance;
    private Configuration mConfiguration;

    private CommonNetEngine(Configuration configuration) {
        this.mConfiguration = configuration;
    }

    public synchronized static CommonNetEngine getInstance(Configuration configuration) {
        if (instance == null) {
            instance = new CommonNetEngine(configuration);
        }
        return instance;
    }

    public <T> Response<T> doRequest(Request request) throws ParseException, NetworkExecption {
        HttpEngine httpEngine = HttpEngine.getInstance();
        httpEngine.setConfiguration(mConfiguration);

        String resultStr = "";
        final String resolutionPath = mConfiguration.getHost() + request.getPath();
        if (request.getType() == Request.GET)
            resultStr = httpEngine.httpGet(resolutionPath, request.getParams());
        else if (request.getType() == Request.POST) {
            resultStr = httpEngine.httpPost(resolutionPath, request.getParams());
        }
        if (islegalJson(resultStr)) {
            return parser(request.getParserType(), resultStr, request.getExpectType());
        } else {
            return null;
        }
    }

    /**
     * 解析
     *
     * @param parserType
     * @param json
     * @param <T>
     * @return
     * @throws ParseException
     */
    private <T> Response<T> parser(ParserType parserType, String json, Class<?> expectType) throws ParseException {
        Response<T> response = new Response<T>();
        response.setResultStr(json);

        String dataString = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("errno"))
                response.setErrno(jsonObject.optInt("errno"));

            if (jsonObject.has("msg"))
                response.setMsg(jsonObject.optString("msg"));

            if (jsonObject.has("data")) {
                if (jsonObject.optJSONObject("data") != null) {
                    dataString = jsonObject.optJSONObject("data").toString();
                } else if (jsonObject.optJSONArray("data") != null) {
                    dataString = jsonObject.optJSONArray("data").toString();
                } else {
                    dataString = "";
                }
            }
        } catch (JSONException e) {
            response.setError(e);
            throw new ParseException(e);
        }

        if (islegalJson(dataString)) {
            Entity<T> entity = null;
            if (parserType.equals(ParserType.CUSTOM)) {
                try {
                    entity = (Entity<T>) expectType.newInstance();
                } catch (InstantiationException e) {
                    response.setError(e);
                    throw new ParseException(e);
                } catch (IllegalAccessException e) {
                    response.setError(e);
                    throw new ParseException(e);
                }
                entity.fromJson(dataString);
            } else if (parserType.equals(ParserType.GSON)) {
                entity = (Entity<T>) new Gson().fromJson(dataString, expectType);
            } else if (parserType.equals(ParserType.NONE)) {
                entity = null;
            }
            response.setData(entity);
        }

        return response;
    }

    /**
     * 判断是否为json
     *
     * @param jsonStr
     * @throws ParseException
     */
    private boolean islegalJson(String jsonStr) throws ParseException {
        if (jsonStr == null || jsonStr.length() == 0 || "".equalsIgnoreCase(jsonStr) || "null".equalsIgnoreCase(jsonStr))
            return false;
        try {
            new JsonParser().parse(jsonStr);
            return true;
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new ParseException(e);
        }
    }
}
