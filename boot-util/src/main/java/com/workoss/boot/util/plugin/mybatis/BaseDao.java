/*
 * #%L
 * %%
 * Copyright (C) 2019 Workoss Software, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.workoss.boot.util.plugin.mybatis;


import java.util.List;
import java.util.Map;

/**
 * @author: workoss
 * @date: 2018-05-26 16:33
 * @version:
 */
public interface BaseDao<T,ID> {

    /**
     * 执行sql
     * @param sql
     * @return
     */
    List<Map<String, Object>> executeNativeSql(String sql);

}
