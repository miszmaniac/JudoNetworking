package com.github.kubatatami.judonetworking.controllers;

import com.github.kubatatami.judonetworking.Request;
import com.github.kubatatami.judonetworking.exceptions.JudoException;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jbogacki
 * Date: 05.08.2013
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class GetOrPostTools {

    private GetOrPostTools() {
    }

    public static String createRequest(Request request, String apiKey, String apiKeyName) throws JudoException {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        int i = 0;
        if (apiKeyName != null && request.isApiKeyRequired()) {
            nameValuePairs.add(new BasicNameValuePair(apiKeyName, apiKey));
        }

        if (request.getArgs() != null) {
            if (request.getArgs().length != request.getParamNames().length) {
                throw new JudoException("Wrong param names.");
            }
            for (Object arg : request.getArgs()) {
                nameValuePairs.add(new BasicNameValuePair(request.getParamNames()[i], arg == null ? "" : arg.toString()));
                i++;
            }
        }


        return URLEncodedUtils.format(nameValuePairs, HTTP.UTF_8).replaceAll("\\+", "%20");
    }

}
