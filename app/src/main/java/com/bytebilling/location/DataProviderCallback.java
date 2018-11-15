package com.bytebilling.location;

import org.json.JSONObject;

interface DataProviderCallback {
    void accept(JSONObject param);
}
