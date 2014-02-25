package test.db;

import java.lang.annotation.Annotation;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class BasePRoto
{
	public BasePRoto()
	{
		Annotation[] annotations = this.getClass().getAnnotations();
		final DMAPAnnotation dmapAnnotation;
		if(annotations.length == 1 && DMAPAnnotation.class.equals(annotations[0].annotationType()))
		{
			dmapAnnotation = (DMAPAnnotation) annotations[0];
		}
		else if(annotations.length > 1)
		{
			dmapAnnotation = (DMAPAnnotation) Iterables.find(Lists.newArrayList(annotations), Predicates.instanceOf(DMAPAnnotation.class));
		}
		else
		{
			throw new RuntimeException("No matching annotation found");
		}
		dmapAnnotation.type();
		if(dmapAnnotation.needsCalculation())
		{
			
		}
	}
}
