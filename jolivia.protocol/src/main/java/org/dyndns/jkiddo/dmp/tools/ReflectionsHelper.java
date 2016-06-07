package org.dyndns.jkiddo.dmp.tools;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;

public final class ReflectionsHelper
{

	private ReflectionsHelper() throws InstantiationException {
		throw new InstantiationException("This class is not created for instantiation");
	}

	public static ImmutableSet<Class<? extends Object>> getClasses(final String packageName, final Class<? extends Annotation> annotation)
	{
		try
		{
			return ImmutableSet.copyOf(FluentIterable.from(getClasses(packageName)).filter(new Predicate<Class<? extends Object>>() {

				@Override
				public boolean apply(Class<? extends Object> input)
				{
					return input.getAnnotation(annotation) != null ? true : false;

				}
			}).filter(Predicates.notNull()));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	/**
	 * http://snippets.dzone.com/posts/show/4831 Scans all classes accessible from the context class loader which belong to the given package and subpackages.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws Exception
	 * @throws ClassNotFoundException
	 */
	private static List<Class<? extends Object>> getClasses(final String packageName) throws ClassNotFoundException, IOException
	{
		final ArrayList<Class<? extends Object>> classes = new ArrayList<>();
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		final String path = packageName.replace('.', '/');
		final Enumeration<URL> resources = classLoader.getResources(path);
		while(resources.hasMoreElements())
		{

			final URL resource = resources.nextElement();
			final String protocol = resource.getProtocol();
			if(protocol.equals("jar"))
			{
				classes.addAll(handleJar(resource));
			}
			if(protocol.equals("file"))
			{
				classes.addAll(handleFile(resource, packageName));
			}
		}
		return classes;
	}

	private static Collection<Class<? extends Object>> handleJar(final URL resource)
	{
		final Set<Class<? extends Object>> result = new HashSet<>();
		final String fileName = resource.getFile();
		final File file = new File(fileName.split("!")[0].replace("file:", ""));
		if(file.exists())
		{
			JarFile jarFile = null;
			try
			{
				jarFile = new JarFile(file);
				final Enumeration<JarEntry> entries = jarFile.entries();
				while(entries.hasMoreElements())
				{
					String name = entries.nextElement().getName();
					if(name.endsWith(".class"))
					{
						name = name.replace(".class", "");
						name = name.replaceAll("/", "."); // replace / with .
						final Class<?> clazz = Class.forName(name);
						result.add(clazz);
					}
				}
			}
			catch(final Exception e)
			{
				throw new RuntimeException(e);
			}
			finally
			{
				try
				{
					jarFile.close();
				}
				catch(final IOException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		return result;

	}
	private static Collection<Class<? extends Object>> handleFile(final URL resource, final String packageName) throws UnsupportedEncodingException, ClassNotFoundException
	{
		final String fileName = resource.getFile();
		final String fileNameDecoded = URLDecoder.decode(fileName, "UTF-8");
		final File file = new File(fileNameDecoded);
		return findClasses(file, packageName);
	}
	/**
	 * http://snippets.dzone.com/posts/show/4831 Recursive method used to find all classes in a given directory and subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class<? extends Object>> findClasses(final File directory, final String packageName) throws ClassNotFoundException
	{
		final List<Class<? extends Object>> classes = new ArrayList<>();
		if(!directory.exists())
		{
			return classes;
		}
		final File[] files = directory.listFiles();
		for(final File file : files)
		{
			final String fileName = file.getName();
			if(file.isDirectory())
			{
				assert !fileName.contains(".");
				classes.addAll(findClasses(file, packageName + "." + fileName));
			}
			else if(fileName.endsWith(".class") && !fileName.contains("$"))
			{
				Class<? extends Object> _class;
				try
				{
					_class = Class.forName(packageName + '.' + fileName.substring(0, fileName.length() - 6));
				}
				catch(final ExceptionInInitializerError e)
				{
					// happen, for example, in classes, which depend on
					// Spring to inject some beans, and which fail,
					// if dependency is not fulfilled
					_class = Class.forName(packageName + '.' + fileName.substring(0, fileName.length() - 6), false, Thread.currentThread().getContextClassLoader());
				}
				classes.add(_class);
			}
		}
		return classes;
	}
}
