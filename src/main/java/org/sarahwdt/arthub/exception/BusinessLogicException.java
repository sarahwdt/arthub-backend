package org.sarahwdt.arthub.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public abstract class BusinessLogicException extends RuntimeException implements ErrorResponse {
    private ProblemDetail body;

    protected String messageCodeQualifier() {
        return this.getClass().getSimpleName();
    }

    protected abstract String messageCodeCategory();

    @Override
    public ProblemDetail getBody() {
        if (body == null) {
            body = ProblemDetail.forStatus(getStatusCode());
        }
        return body;
    }

    @Override
    public String getTypeMessageCode() {
        return "problemDetails.type." + messageCodeCategory() + "." + messageCodeQualifier();
    }

    @Override
    public String getTitleMessageCode() {
        return "problemDetails.title." + messageCodeCategory() + "." + messageCodeQualifier();
    }

    @Override
    public String getDetailMessageCode() {
        return "problemDetails.detail." + messageCodeCategory() + "." + messageCodeQualifier();
    }
}
