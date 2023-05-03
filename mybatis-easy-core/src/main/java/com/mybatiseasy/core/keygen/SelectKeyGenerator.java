/*
 *
 *  *
 *  *  * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */
package com.mybatiseasy.core.keygen;

import com.mybatiseasy.keygen.IKeyGenerator;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 *
 */
public class SelectKeyGenerator implements IKeyGenerator {

  private final Executor keyExecutor;
  private final Configuration configuration;

  private final MappedStatement keyStatement;

  private final Object parameter;

  private final String keyProperty;

  public SelectKeyGenerator(Configuration configuration,
                            Executor keyExecutor,
                            Object parameter,
                            String keyStatementId,
                            String keyProperty) {
    this.configuration = configuration;
    this.keyStatement = configuration.getMappedStatement(keyStatementId);
    this.keyExecutor = keyExecutor;
    this.parameter = parameter;
    this.keyProperty = keyProperty;
  }

  @Override
  public Object generateId() {
    try {
      List<Object> values = keyExecutor.query(keyStatement, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
      if (values.size() == 0) {
        throw new ExecutorException("SelectKey returned no data.");
      }
      if (values.size() > 1) {
        throw new ExecutorException("SelectKey returned more than one value.");
      } else {
        MetaObject metaResult = configuration.newMetaObject(values.get(0));
        return metaResult.getOriginalObject();
      }
    } catch (ExecutorException e) {
      throw e;
    } catch (Exception e) {
      throw new ExecutorException("Error selecting key or setting result to parameter object. Cause: " + e, e);
    }
  }
}
