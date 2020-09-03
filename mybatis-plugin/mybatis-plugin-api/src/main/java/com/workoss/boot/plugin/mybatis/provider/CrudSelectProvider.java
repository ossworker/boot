/*
 * Copyright © 2020-2021 workoss (workoss@icloud.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.workoss.boot.plugin.mybatis.provider;

import java.util.Map;

import org.apache.ibatis.builder.annotation.ProviderContext;

/**
 * CrudSelectProvider
 *
 * @author workoss
 */
public class CrudSelectProvider extends BaseProvider {

	public CharSequence selectById(Map<String, Object> parameter, ProviderContext context) {
		return executeSql(context, (tableColumnInfo -> {
			StringBuilder sqlBuilder = new StringBuilder(" select ");
			sqlBuilder.append(getSelectColumn(tableColumnInfo));
			sqlBuilder.append(" from ");
			sqlBuilder.append(tableColumnInfo.getTableName());
			sqlBuilder.append(" where ");
			sqlBuilder.append(tableColumnInfo.getIdColumnName());
			sqlBuilder.append("=");
			sqlBuilder.append(bindParameter("id"));
			return sqlBuilder.toString();
		}));
	}

	public CharSequence selectByIds(Map<String, Object> parameter, ProviderContext context) {
		return executeSql(context, (tableColumnInfo -> {
			StringBuilder sqlBuilder = new StringBuilder("<script> select ");
			sqlBuilder.append(getSelectColumn(tableColumnInfo));
			sqlBuilder.append(" from ");
			sqlBuilder.append(tableColumnInfo.getTableName());
			sqlBuilder.append(" where ");
			sqlBuilder.append(tableColumnInfo.getIdColumnName());
			sqlBuilder.append(" in (");

			sqlBuilder.append(
					"<foreach collection=\"ids\" index=\"index\" item=\"id\" open=\"\" separator=\",\" close=\"\">");
			sqlBuilder.append(bindParameter("id"));
			sqlBuilder.append("</foreach>");

			sqlBuilder.append(") </script>");
			return sqlBuilder.toString();
		}));
	}

	public CharSequence selectSelective(Map<String, Object> parameter, ProviderContext context) {
		return executeSql(context, (tableColumnInfo -> {
			StringBuilder sqlBuilder = new StringBuilder("<script> select ");
			sqlBuilder.append(getSelectColumn(tableColumnInfo));
			sqlBuilder.append(" from ");
			sqlBuilder.append(tableColumnInfo.getTableName());
			sqlBuilder.append(getWhereSelectColumn(tableColumnInfo));
			sqlBuilder.append(" </script>");
			return sqlBuilder.toString();
		}));
	}

	public CharSequence selectCountSelective(Map<String, Object> parameter, ProviderContext context) {
		return executeSql(context, (tableColumnInfo -> {
			StringBuilder sqlBuilder = new StringBuilder("<script> select ");
			sqlBuilder.append(" count( ");
			sqlBuilder.append(tableColumnInfo.getIdColumnName());
			sqlBuilder.append(" ) ");
			sqlBuilder.append(" from ");
			sqlBuilder.append(tableColumnInfo.getTableName());
			sqlBuilder.append(getWhereSelectColumn(tableColumnInfo));
			sqlBuilder.append(" </script>");
			return sqlBuilder.toString();
		}));
	}

}
