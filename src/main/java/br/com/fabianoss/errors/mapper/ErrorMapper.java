package br.com.fabianoss.errors.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;

import br.com.fabianoss.errors.dto.ErrorDTO;

@Mapper
public abstract class ErrorMapper {

	public static final ErrorMapper INSTANCE = Mappers.getMapper(ErrorMapper.class);

	public ErrorDTO toErrorDto(HttpStatus status, String path) {
		ErrorDTO dto = new ErrorDTO();
		dto.setStatus(status.value());
		dto.setError(status.getReasonPhrase());
		dto.setTimestamp(LocalDateTime.now());
		dto.setPath(path);
		return dto;
	}

	public ErrorDTO toErrorDto(HttpStatus status, String path, String message) {
		ErrorDTO dto = new ErrorDTO();
		dto.setStatus(status.value());
		dto.setError(status.getReasonPhrase());
		dto.setTimestamp(LocalDateTime.now());
		dto.setPath(path);
		dto.setMessage(message);
		return dto;
	}
}