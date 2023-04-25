package com.mybatiseasy.core.keygen;

public class UUIDKeyGenerator implements IKeyGenerator{
    @Override
    public Object generateId() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
