package de.ifcore.argue.domain.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueAccessRightValidator.class)
public @interface UniqueAccessRight
{
	String message() default "{constraints.uniqueAccessRight}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
