/*
 * Copyright (c) 2012-present NAVER Corp.
 *
 * This file is part of The nGrinder software distribution. Refer to
 * the file LICENSE which is part of The nGrinder distribution for
 * licensing details. The nGrinder distribution is available on the
 * Internet at https://naver.github.io/ngrinder
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ngrinder.http.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.python.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.util.Map;

public class JsonUtils {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	private JsonUtils() {
	}

	public static String serialize(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Map<?, ?> deserialize(byte[] bytes) {
		if (bytes.length == 0) {
			return ImmutableMap.of();
		}

		try {
			return objectMapper.readValue(bytes, new TypeReference<Map<?, ?>>() {
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
