package br.com.fabianoss.errors;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.fabianoss.errors.dto.ErrorDTO;
import br.com.fabianoss.errors.mapper.ErrorMapper;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LogManager.getLogger(RestExceptionHandler.class);

	@Autowired
	private HttpServletRequest reqHttp;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorDTO details = ErrorMapper.INSTANCE.toErrorDto(HttpStatus.BAD_REQUEST, "Formato inválido");
		details.setMessage(ex.getLocalizedMessage());
		details.setPath(request.getContextPath());
		return handleExceptionInternal(ex, details, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorDTO details = ErrorMapper.INSTANCE.toErrorDto(HttpStatus.BAD_REQUEST, "Formato inválido");
		details.setMessage(ex.getLocalizedMessage());
		details.setPath(request.getContextPath());
		return handleExceptionInternal(ex, details, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		ErrorDTO error = createListOfErrors(ex.getBindingResult());
		logger.error("{} | DevMessage: {}", error.toString(), ExceptionUtils.getRootCauseMessage(ex));
		error.setPath(reqHttp.getRequestURI());
		return ResponseEntity.badRequest().body(error);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
		ErrorDTO details = ErrorMapper.INSTANCE.toErrorDto(HttpStatus.BAD_REQUEST, "Validation Failed");
		details.setMessage(ex.getLocalizedMessage());
		details.setPath(request.getRequestURI());
		ex.getConstraintViolations().forEach(violation -> {
			details.addViolation(violation.getPropertyPath().toString(), violation.getMessage());
		});
		return details;
	}

	@ExceptionHandler({ SQLException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorDTO handleInternalException(Exception ex, HttpServletRequest request) {
		ErrorDTO details = ErrorMapper.INSTANCE.toErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno");
		details.setMessage(ex.getMessage());
		details.setPath(request.getRequestURI());
		return details;
	}
	
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorDTO handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
		ErrorDTO details = ErrorMapper.INSTANCE.toErrorDto(HttpStatus.UNAUTHORIZED, "Unauthorized");
		details.setMessage(ex.getMessage());
		details.setPath(request.getContextPath());
		return details;
	}
	
	private ErrorDTO createListOfErrors(BindingResult bindingResult) {
		ErrorDTO error = ErrorMapper.INSTANCE.toErrorDto(HttpStatus.BAD_REQUEST, "Erro de campo obrigatório");
		for (FieldError fe : bindingResult.getFieldErrors()) {
			error.addViolation(fe.getField(), fe.getDefaultMessage());
		}
		return error;
	}
	

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String,String> responseBody = new HashMap<>();
        responseBody.put("path",request.getContextPath());
        responseBody.put("message","The URL you have reached is not in service at this time (404).");
        return new ResponseEntity<Object>(responseBody,HttpStatus.NOT_FOUND);
    }

}
