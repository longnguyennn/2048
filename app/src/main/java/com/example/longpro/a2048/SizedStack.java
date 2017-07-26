package com.example.longpro.a2048;

import java.util.Stack;

/**
 * Created by longpro on 7/26/17.
 */

public class SizedStack<String> extends Stack<String> {
    private int maxSize;

    public SizedStack(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    public String push(String object) {
        while ( this.size() >= this.maxSize ) {
            this.remove(0);
        }
        return super.push(object);
    }
}
