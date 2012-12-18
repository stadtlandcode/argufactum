package de.ifcore.argue.domain.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegistrationPasswordVerificationValidator.class)
public @interface RegistrationPasswordVerification
{
	String message() default "{constraints.registrationPasswordVerification}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int minLength() default 6;

	int maxLength() default 64;
}
