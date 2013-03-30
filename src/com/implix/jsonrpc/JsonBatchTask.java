package com.implix.jsonrpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class JsonBatchTask implements Runnable {
    private Integer timeout;
    private List<JsonRequest> requests;
    private Thread thread = new Thread(this);
    private List<JsonResponseModel2> response = null;
    private Exception ex = null;
    private JsonRpcImplementation rpc;

    public JsonBatchTask(JsonRpcImplementation rpc, Integer timeout, List<JsonRequest> requests) {
        this.rpc = rpc;
        this.timeout = timeout;
        this.requests = requests;
    }

    public List<JsonResponseModel2> getResponse() {
        return this.response;
    }

    public Exception getEx() {
        return ex;
    }

    @Override
    public void run() {
        try {
            this.response = rpc.getJsonConnector().callBatch(this.requests, this.timeout);
        } catch (Exception e) {
            this.ex = e;
        }
    }

    public void execute() {
        thread.start();
    }

    public void join() throws InterruptedException {
        thread.join();
    }


    public static List<List<JsonRequest>> timeAssignRequests(List<JsonRequest> list, final int partsNo) {
        List<List<JsonRequest>> parts = new ArrayList<List<JsonRequest>>(partsNo);
        long[] weights = new long[partsNo];
        for (int i = 0; i < partsNo; i++) {
            parts.add(new ArrayList<JsonRequest>());
        }
        Collections.sort(list);
        for (JsonRequest req : list) {
            int i = getSmallestPart(weights);
            weights[i] += req.getWeight();
            parts.get(i).add(req);
        }

        return parts;
    }

    public static List<List<JsonRequest>> simpleAssignRequests(List<JsonRequest> list, final int partsNo) {
        int i = 0;
        List<List<JsonRequest>> parts = new ArrayList<List<JsonRequest>>(partsNo);
        for (i = 0; i < partsNo; i++) {
            parts.add(new ArrayList<JsonRequest>());
        }
        i = 0;
        for (JsonRequest elem : list) {
            parts.get(i).add(elem);
            i++;
            if (i == partsNo) {
                i = 0;
            }
        }

        return parts;
    }

    private static int getSmallestPart(long[] parts) {
        int res = 0;

        for (int i = 0; i < parts.length; i++) {
            if (parts[i] < parts[res]) {
                res = i;
            }
        }
        return res;
    }

}