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
package com.workoss.boot.storage.web.controller;

import com.workoss.boot.storage.mapper.web.STSTokenToVOMapper;
import com.workoss.boot.storage.mapper.web.UploadSignToVOMapper;
import com.workoss.boot.storage.mapper.web.STSTokenParamToStorageModelMapper;
import com.workoss.boot.storage.mapper.web.UploadSignParamToStorageModelMapper;
import com.workoss.boot.storage.model.ThirdPlatformType;
import com.workoss.boot.storage.service.security.SecurityService;
import com.workoss.boot.storage.web.vo.STSTokenParam;
import com.workoss.boot.storage.web.vo.STSTokenVO;
import com.workoss.boot.storage.web.vo.UploadSignVO;
import com.workoss.boot.storage.web.vo.UploadSignParam;

import com.workoss.boot.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * storage controller
 *
 * @author workoss
 */
@SuppressWarnings("ALL")
@Validated
@RequestMapping("/security")
@RestController
public class SecurityController {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private STSTokenParamToStorageModelMapper stsTokenToStorageModelMapper;

	@Autowired
	private STSTokenToVOMapper stsTokenToVOMapper;

	@Autowired
	private UploadSignParamToStorageModelMapper uploadSignParamToStorageModelMapper;

	@Autowired
	private UploadSignToVOMapper uploadSignToVOMapper;

	@PostMapping("/sign")
	public Mono<UploadSignVO> generateWebSign(@Validated @RequestBody UploadSignParam param) {
		if (StringUtils.isBlank(param.getTenentId())) {
			param.setTenentId("default");
		}
		if (param.getStorageType() == null) {
			param.setStorageType(ThirdPlatformType.OSS);
		}
		return securityService
				.generateUploadSign(uploadSignParamToStorageModelMapper.toTarget(param), param.getKey(),
						param.getMimeType(), param.getSuccessActionStatus())
				.flatMap(uploadSign -> Mono.justOrEmpty(uploadSignToVOMapper.toTarget(uploadSign)));
	}

	@PostMapping("/stssign")
	public Mono<UploadSignVO> generateStsWebSign(@Validated @RequestBody UploadSignParam param) {
		if (StringUtils.isBlank(param.getTenentId())) {
			param.setTenentId("default");
		}
		if (param.getStorageType() == null) {
			param.setStorageType(ThirdPlatformType.OSS);
		}
		return securityService
				.generateUploadStsSign(uploadSignParamToStorageModelMapper.toTarget(param), param.getKey(),
						param.getMimeType(), param.getSuccessActionStatus())
				.flatMap(uploadSign -> Mono.justOrEmpty(uploadSignToVOMapper.toTarget(uploadSign)));
	}

	@PostMapping(value = "/ststoken", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Validated
	public Mono<STSTokenVO> generateStsToken(@RequestBody STSTokenParam param) {
		if (param.getStorageType() == null) {
			param.setStorageType(ThirdPlatformType.OSS);
		}
		if (StringUtils.isBlank(param.getTenentId())) {
			param.setTenentId("default");
		}
		return securityService
				.generateStsToken(stsTokenToStorageModelMapper.toTarget(param), param.getKey(), param.getAction())
				.flatMap(stsToken -> Mono.justOrEmpty(stsTokenToVOMapper.toTarget(stsToken)));
	}

}
