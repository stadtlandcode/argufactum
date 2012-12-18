package de.ifcore.argue.domain.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.hibernate.validator.constraints.NotBlank;

/**
 * constraint for a valid, existing username<br />
 * use this annotation together with {@link NotBlank}
 * 
 * @author Felix Ebert
 * 
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingUsernameValidator.class)
public @interface ExistingUsername
{
	String message() default "{constraints.existingUsername}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
