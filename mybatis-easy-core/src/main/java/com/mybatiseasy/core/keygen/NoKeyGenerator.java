package com.mybatiseasy.core.keygen;

public class NoKeyGenerator implements IKeyGenerator {

    @Override
    public Object generateId(Object entity){
        return null;
    }
}
