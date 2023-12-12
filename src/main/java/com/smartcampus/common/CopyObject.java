package com.smartcampus.common;

import org.springframework.beans.BeanUtils;

public class CopyObject {
	public static void copyObject(Object source, Object target) {
		BeanUtils.copyProperties(source, target);
	}
}
