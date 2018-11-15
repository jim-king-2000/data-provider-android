package com.bytebilling.location;

import org.json.JSONException;
import org.json.JSONObject;

public interface DataProviderInterface {
    JSONObject getData() throws JSONException;
}
