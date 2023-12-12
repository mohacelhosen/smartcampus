package com.smartcampus.common;

import java.time.LocalDate;

public class IdGenerator {
	public static String academicIdGenerator(String semester, String studentIdStart) {
		int semester1Digit = 0;
		if (semester.equalsIgnoreCase("SPRING")) {
			semester1Digit = 1;
		} else if (semester.equalsIgnoreCase("SUMMER")) {
			semester1Digit = 2;
		} else {
			semester1Digit = 3;
		}

		int year = LocalDate.now().getYear();
		String last2DigitOfYear = String.valueOf(year).substring(2);
		return last2DigitOfYear + "0" + semester1Digit + studentIdStart;
	}

	public static String academicIdGenerator(String semester, String departmentCode3Digit, String lastStudentId) {
		String last2digitOfYear = String.valueOf(LocalDate.now().getYear()).substring(2);
		String semesterValue2Digit = null;
		if (semester.equalsIgnoreCase("fall")) {
			semesterValue2Digit = "09";
		} else if (semester.equalsIgnoreCase("winter")) {
			semesterValue2Digit = "01";
		} else {
			semesterValue2Digit = "05";
		}
		return last2digitOfYear + semesterValue2Digit + departmentCode3Digit + (Integer.parseInt(lastStudentId) + 1);
	}
}
