package com.smartcampus.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ModelLocalDateTime {
	private LocalDateTime localDateTime;
	private String localDateTimeString;
	private String localDateTimeStringAMPM;
	private long localDateTimeLong;
	private String dateString;
	private long dateLong;
	private String timeString;
	private long timeLong;

	public String getLocalDateTimeStringAMPM() {
		return localDateTimeStringAMPM;
	}

	public void setLocalDateTimeStringAMPM(String localDateTimeStringAMPM) {
		this.localDateTimeStringAMPM = localDateTimeStringAMPM;
	}

	public ModelLocalDateTime() {
		super();
		this.localDateTime = LocalDateTime.now();
	}

	public ModelLocalDateTime(LocalDateTime paramLocalDateTime) {
		super();

		this.localDateTime = paramLocalDateTime == null ? LocalDateTime.now() : paramLocalDateTime;

		DateTimeFormatter localDateTimeStringFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.localDateTimeString = this.localDateTime.format(localDateTimeStringFormatter);
		this.localDateTimeLong = Long.parseLong(localDateTimeString.replace("-", "").replace(" ", "").replace(":", ""));

		DateTimeFormatter localDateTimeStringFormatterAP = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
		this.localDateTimeStringAMPM = this.localDateTime.format(localDateTimeStringFormatterAP);

		DateTimeFormatter localDateStringFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.dateString = this.localDateTime.format(localDateStringFormatter);
		this.dateLong = Long.parseLong(dateString.replace("-", "").replace(" ", "").replace(":", ""));

		DateTimeFormatter localTimeStringFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		this.timeString = this.localDateTime.format(localTimeStringFormatter);
		this.timeLong = Long.parseLong(timeString.replace("-", "").replace(" ", "").replace(":", ""));
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public String getLocalDateTimeString() {
		return localDateTimeString;
	}

	public void setLocalDateTimeString(String localDateTimeString) {
		this.localDateTimeString = localDateTimeString;
	}

	public long getLocalDateTimeLong() {
		return localDateTimeLong;
	}

	public void setLocalDateTimeLong(long localDateTimeLong) {
		this.localDateTimeLong = localDateTimeLong;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public long getDateLong() {
		return dateLong;
	}

	public void setDateLong(long dateLong) {
		this.dateLong = dateLong;
	}

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public long getTimeLong() {
		return timeLong;
	}

	public void setTimeLong(long timeLong) {
		this.timeLong = timeLong;
	}

	public boolean isParamDateFuture(ModelLocalDateTime param) {
		if (param == null)
			return false;
		if (param.getDateLong() > this.dateLong)
			return true;
		return false;
	}

	public boolean isParamDateTodayOrFuture(ModelLocalDateTime param) {
		if (param == null)
			return false;
		if (param.getDateLong() >= this.dateLong)
			return true;
		return false;
	}

	public boolean isParamTimeFuture(ModelLocalDateTime param) {
		if (param == null)
			return false;
		if (param.getDateLong() > this.dateLong)
			return true;
		return false;
	}

	public boolean isParamTimeTodayOrFuture(ModelLocalDateTime param) {
		if (param == null)
			return false;
		if (param.getTimeLong() >= this.timeLong)
			return true;
		return false;
	}

	public boolean isParamDateTimeFuture(ModelLocalDateTime param) {
		if (param == null)
			return false;
		if (param.getLocalDateTimeLong() > this.localDateTimeLong)
			return true;
		return false;
	}

	public boolean isParamDateTimeTodayOrFuture(ModelLocalDateTime param) {
		if (param == null)
			return false;
		if (param.getLocalDateTimeLong() >= this.localDateTimeLong)
			return true;
		return false;
	}

	public boolean isParamDateTimeFullValueFuture(ModelLocalDateTime param) {
		if (param == null)
			return false;
		if ((this.localDateTime.compareTo(param.getLocalDateTime())) < 0)
			return true;
		return false;
	}

	public boolean isParamDateTimeFullValueTodayOrFuture(ModelLocalDateTime param) {
		if (param == null)
			return false;
		if ((this.localDateTime.compareTo(param.getLocalDateTime())) <= 0)
			return true;
		return false;
	}
}
