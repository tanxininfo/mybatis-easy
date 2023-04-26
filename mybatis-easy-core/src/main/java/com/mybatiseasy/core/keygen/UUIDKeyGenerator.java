package com.mybatiseasy.core.keygen;

import com.mybatiseasy.keygen.IKeyGenerator;

public class UUIDKeyGenerator implements IKeyGenerator {
    @Override
    public Object generateId() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
