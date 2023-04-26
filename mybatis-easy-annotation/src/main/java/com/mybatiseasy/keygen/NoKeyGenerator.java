package com.mybatiseasy.keygen;

public class NoKeyGenerator implements IKeyGenerator {


    @Override
    public Object generateId(){
        return null;
    }
}
