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
package com.workoss.boot.storage.web.vo;

import com.workoss.boot.storage.model.ThirdPlatformType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 返回STS token 临时
 *
 * @author workoss
 */
@Data
public class STSTokenVO {

	private ThirdPlatformType storageType;

	/**
	 * accessKey accessId
	 */
	private String accessKey;

	/**
	 * secretKey
	 */
	private String secretKey;

	/**
	 * sts token
	 */
	private String stsToken;

	/**
	 * 过期时间
	 */
	@DateTimeFormat(pattern = "")
	private LocalDateTime expiration;

	/**
	 * endpoint
	 */
	private String endpoint;

}