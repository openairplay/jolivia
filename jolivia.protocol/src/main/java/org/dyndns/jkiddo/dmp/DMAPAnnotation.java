package org.dyndns.jkiddo.dmp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DMAPAnnotation
{
	public boolean needsCalculation() default false;
	public IDmapProtocolDefinition.DmapProtocolDefinition type();

}