package com.ashish.grpc.constant;

import io.grpc.Context;

public class Constant {
    public static final Context.Key<String> USER_ID_CTX_KEY = Context.key("userId");
}
