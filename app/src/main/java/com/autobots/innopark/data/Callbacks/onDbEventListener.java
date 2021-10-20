package com.autobots.innopark.data.Callbacks;

import java.util.HashMap;

public interface onDbEventListener {
    void onEvent(String collection, String doc, Object data);
}
