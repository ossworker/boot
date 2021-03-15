/*
 * Copyright 2019-2021 workoss (https://www.workoss.com)
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
package com.workoss.boot.plugin.mybatis;

import com.workoss.boot.util.Assert;
import com.workoss.boot.util.StringUtils;
import com.workoss.boot.util.concurrent.fast.FastThreadLocal;
import com.workoss.boot.util.reflect.ReflectUtils;

/**
 * SqlHelper
 *
 * @author workoss
 */
@SuppressWarnings("ALL")
public class SqlHelper {

	protected static final FastThreadLocal<SqlParam> LOCAL_SQL_PARAM = new FastThreadLocal<>();

	public static PageBuilder page() {
		return new PageBuilder();
	}

	public static PageBuilder page(Object pageParam){
		return new PageBuilder(pageParam);
	}

	public static PageBuilder page(int pageNo, int limit) {
		return new PageBuilder().page(pageNo, limit);
	}

	public static SortBuilder sort(String sortBy) {
		Assert.hasLength(sortBy, "排序不能为空");
		return new SortBuilder().sort(sortBy);
	}

	public static SqlParam getLocalSqlParam() {
		return LOCAL_SQL_PARAM.get();
	}

	public static void clearSqlParam() {
		LOCAL_SQL_PARAM.set(null);
	}


	protected static void start(SqlParam sqlParam) {
		if (StringUtils.isNotBlank(sqlParam.getSortBy())) {
			sqlParam.setSortBy(StringUtils.underscoreName(sqlParam.getSortBy()));
		}
		LOCAL_SQL_PARAM.set(sqlParam);
	}

	static class PageBuilder {

		private SqlParamBuilder sqlParamBuilder;

		public PageBuilder() {
			this.sqlParamBuilder = new SqlParamBuilder();
		}

		public PageBuilder(Object pageObject){
			if (pageObject == null) {
				this.sqlParamBuilder = new SqlParamBuilder();
				return;
			}
			String clazName = pageObject.getClass().getName();
			if (clazName.startsWith("java.lang.") || clazName.startsWith("java.math.")) {
				throw new RuntimeException("please input object");
			}
			if (pageObject instanceof SqlParam) {
				sqlParamBuilder = new SqlParamBuilder((SqlParam) pageObject);
				return;
			}
			this.sqlParamBuilder = new SqlParamBuilder();
			if (pageObject instanceof Integer) {

			} else {
				Object offset = ReflectUtils.getPropertyByInvokeMethod(pageObject, "offset");
				Object limit = ReflectUtils.getPropertyByInvokeMethod(pageObject, "limit");
				if (limit != null) {
					sqlParamBuilder.offset(offset == null ? 0 : Integer.parseInt(offset.toString()));
					sqlParamBuilder.limit(limit == null ? 10 : Integer.parseInt(limit.toString()));
				}
				sqlParamBuilder.sort((String) ReflectUtils.getPropertyByInvokeMethod(pageObject, "sortBy"));
			}
		}

		public PageBuilder page(int pageNo, int limit) {
			sqlParamBuilder.page(pageNo, limit);
			return this;
		}

		public PageBuilder sort(String sortBy) {
			sqlParamBuilder.sort(sortBy);
			return this;
		}

		public void start() {
			SqlParam sqlParam = sqlParamBuilder.build();
			if (sqlParam.getLimit() <= 0 || sqlParam.getOffset() < 0) {
				throw new RuntimeException("分页参数 limit >0 offset>=0");
			}
			SqlHelper.start(sqlParam);
		}
	}


	static class SortBuilder {

		private SqlParamBuilder sqlParamBuilder;

		public SortBuilder() {
			this.sqlParamBuilder = new SqlParamBuilder();
		}

		public SortBuilder sort(String sortBy) {
			sqlParamBuilder.sort(sortBy);
			sqlParamBuilder.shouldCount(false);
			sqlParamBuilder.shouldPage(false);
			return this;
		}

		public void start() {
			SqlParam sqlParam = sqlParamBuilder.build();
			SqlHelper.start(sqlParam);
		}
	}

	static class SqlParamBuilder {

		private SqlParam sqlParam;

		public SqlParamBuilder() {
			if (sqlParam == null) {
				this.sqlParam = new SqlParam();
			}
		}

		public SqlParamBuilder(SqlParam sqlParam) {
			this.sqlParam = sqlParam;
		}


		public SqlParamBuilder page(int pageNo, int limit) {
			sqlParam.setOffset((pageNo - 1) * limit);
			sqlParam.setLimit(limit);
			sqlParam.setShouldPage(true);
			return this;
		}

		public SqlParamBuilder offset(int offset) {
			sqlParam.setOffset(offset);
			return this;
		}

		public SqlParamBuilder limit(int limit) {
			sqlParam.setLimit(limit);
			return this;
		}

		public SqlParamBuilder shouldCount(boolean shouldCount) {
			sqlParam.setShouldCount(shouldCount);
			if (shouldCount) {
				this.sqlParam.setShouldPage(true);
			}
			return this;
		}

		public SqlParamBuilder shouldPage(boolean shouldPage) {
			sqlParam.setShouldPage(shouldPage);
			return this;
		}

		public SqlParamBuilder sort(String sortBy) {
			sqlParam.setSortBy(sortBy);
			return this;
		}

		public SqlParam build() {
			return sqlParam;
		}
	}

}