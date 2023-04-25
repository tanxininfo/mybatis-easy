package com.mybatiseasy.core.keygen;

import com.mybatiseasy.core.keygen.IKeyGenerator;
import org.apache.ibatis.type.TypeException;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class KeyGeneratorFactory {

    private static final Map<Class<?>, IKeyGenerator> allIKeyGeneratorMap = new HashMap<>();
    
    static {
        register(UUIDKeyGenerator.class);
    }

    public static boolean hasKeyGenerator(Class<? extends IKeyGenerator> clazz) {
        return allIKeyGeneratorMap.containsKey(clazz);
    }

    public static IKeyGenerator getInstance(Class<? extends IKeyGenerator> clazz){
        if(hasKeyGenerator(clazz)) return allIKeyGeneratorMap.get(clazz);
        return register(clazz);
    }


    private static IKeyGenerator register(Class<? extends IKeyGenerator> clazz) {
        if (clazz == null) return null;
        if (hasKeyGenerator(clazz)) return allIKeyGeneratorMap.get(clazz);
        IKeyGenerator instance = createInstance(clazz);
        allIKeyGeneratorMap.put(clazz, instance);
        return instance;

    }

    private static IKeyGenerator createInstance(Class<?> clazz) {
        Constructor<?> c;
        try {
            c = clazz.getConstructor();
            return (IKeyGenerator) c.newInstance();
        } catch (Exception ex) {
            throw new TypeException("Unable to find a usable constructor for " + clazz, ex);
        }
    }

}
