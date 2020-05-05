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
package com.workoss.boot.plugin.mybatis;

import java.util.List;


import com.workoss.boot.plugin.mybatis.provider.CrudDeleteProvider;
import com.workoss.boot.plugin.mybatis.provider.CrudInsertProvider;
import com.workoss.boot.plugin.mybatis.provider.CrudSelectProvider;
import com.workoss.boot.plugin.mybatis.provider.CrudUpdateProvider;
import org.apache.ibatis.annotations.*;

public interface CrudDao<T, ID> {
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @SelectProvider(type = CrudSelectProvider.class, method = "selectById")
    T selectById(@Param("id") ID id);

    /**
     * 根据id批量查询
     * @param ids
     * @return
     */
    @SelectProvider(type = CrudSelectProvider.class, method = "selectByIds")
	List<T> selectByIds(@Param("ids") List<ID> ids);

    /**
     * 多条件查询
     *
     * @param record
     * @return
     */
    @SelectProvider(type = CrudSelectProvider.class, method = "selectSelective")
	List<T> selectSelective(@Param("record") T record);

    /**
     * 分页查询
     * @param record
     * @return
     */
    @SelectProvider(type = CrudSelectProvider.class, method = "selectSelective")
    PageResult<T> selectPageSelective(@Param("record") T record);

    /**
     * 多条件count
     * @param record
     * @return
     */
    @SelectProvider(type = CrudSelectProvider.class, method = "selectCountSelective")
    int selectCountSelective(@Param("record") T record);

    /**
     * 插入
     *
     * @param record
     * @return
     */
    @InsertProvider(type = CrudInsertProvider.class, method = "insert")
    int insert(@Param("record") T record);

    /**
     * 可选插入
     *
     * @param record
     * @return
     */
    @InsertProvider(type = CrudInsertProvider.class, method = "insertSelective")
    int insertSelective(@Param("record") T record);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    @InsertProvider(type = CrudInsertProvider.class, method = "insertBatch")
    int insertBatch(@Param("list") List<T> list);

    /**
     * 根据id 修改
     *
     * @param record
     * @param id
     * @return
     */
    @UpdateProvider(type = CrudUpdateProvider.class, method = "updateById")
    int updateById(@Param("record") T record, @Param("id") ID id);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @DeleteProvider(type = CrudDeleteProvider.class, method = "deleteById")
    int deleteById(@Param("id") ID id);

    /**
     * 根据ids 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteProvider(type = CrudDeleteProvider.class, method = "deleteByIds")
    int deleteByIds(@Param("ids") List<ID> ids);

    /**
     * 条件删除
     *
     * @param t
     * @return
     */
    @DeleteProvider(type = CrudDeleteProvider.class, method = "deleteSelective")
    int deleteSelective(@Param("record") T t);

}