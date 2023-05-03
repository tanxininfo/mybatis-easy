/*
 *
 *  * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (soft@tanxin.info).
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.mybatiseasy.core.keygen;

import com.mybatiseasy.keygen.IKeyGenerator;
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
