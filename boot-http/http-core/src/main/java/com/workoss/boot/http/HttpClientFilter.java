/*
 * Copyright © 2020-2021 workoss (WORKOSS)
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
package com.workoss.boot.http;

import com.workoss.boot.util.Assert;

@FunctionalInterface
public interface HttpClientFilter {

	HttpClientResponse filter(HttpClientRequest request, HttpClientHandler nextHandler);

	default HttpClientFilter andThen(HttpClientFilter afterFilter) {
		Assert.notNull(afterFilter, "afterFilter 不能为空");
		return (request, next) -> filter(request, afterRequest -> afterFilter.filter(afterRequest, next));
	}

	default HttpClientHandler apply(HttpClientHandler handler) {
		Assert.notNull(handler, "HttpClientHandler 不能为空");
		return request -> this.filter(request, handler);
	}

}
