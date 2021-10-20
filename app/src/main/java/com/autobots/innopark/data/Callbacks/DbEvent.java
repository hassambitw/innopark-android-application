package com.autobots.innopark.data.Callbacks;

import java.util.HashMap;

public class DbEvent {
    private onDbEventListener mOnDbEventListener;

    // First class constructor instead of setter method:
    public void setOnDbEvent(onDbEventListener listener) {
        mOnDbEventListener = listener;
    }

    public void doEvent() {
        /*
         * code code code
         */

        // and in the end

//        if (mOnDbEventListener != null)
//            mOnDbEventListener.onEvent(eventResult); // event result object :)
    }
}
