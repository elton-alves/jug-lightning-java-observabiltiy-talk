package erma.demo.exception;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    private final Tracer tracer;

    public GlobalExceptionHandler(Tracer tracer) {
        this.tracer = tracer;
    }

    private void enrichWithTraceInfo(ProblemDetail problem) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            problem.setProperty("traceId", currentSpan.context().traceId());
        }
    }

    /**
     * RFC 9457  Problem Details for HTTP APIs
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception ex) {
        log.error("Unhandled exception:", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        enrichWithTraceInfo(problemDetail);
        return ErrorResponse.builder(ex, problemDetail).build().getBody();
    }


}
