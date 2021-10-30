package br.com.fabianoss.errors.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)

public class ErrorDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
	private LocalDateTime timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
	private Map<String, Object> violations;

	public ErrorDTO() {
		super();
	}

	public ErrorDTO(Integer status) {
		if (timestamp == null) {
			timestamp = LocalDateTime.now();
		}
		HttpStatus httpStatus = HttpStatus.valueOf(status);
		status = httpStatus.value();
		error = httpStatus.getReasonPhrase();
	}

	public ErrorDTO(LocalDateTime timestamp, Integer status, String error) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, Object> getViolations() {
		return violations;
	}

	public void addViolation(String key, Object value) {
		if (violations == null) {
			violations = new HashMap<>();
		}
		violations.put(key, value);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}