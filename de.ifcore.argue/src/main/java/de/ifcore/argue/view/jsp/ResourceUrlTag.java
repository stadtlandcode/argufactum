package de.ifcore.argue.view.jsp;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.UrlTag;

public class ResourceUrlTag extends UrlTag
{
	private static final long serialVersionUID = 462773814921797820L;
	private static final Logger log = Logger.getLogger(ResourceUrlTag.class.getName());
	private static final Map<String, String> hashes = new ConcurrentHashMap<>();
	private String value;

	@Override
	public int doStartTagInternal() throws JspException
	{
		super.setValue(getMd5Value(value, 14));
		return super.doStartTagInternal();
	}

	String getMd5Value(String file, int length)
	{
		String md5Value = hashes.get(file);
		if (md5Value == null)
		{
			String md5 = null;
			try
			{
				WebApplicationContext webApplicationContext = getRequestContext().getWebApplicationContext();
				ServletContext servletContext = webApplicationContext.getServletContext();
				String realPath = servletContext.getRealPath(file);
				FileInputStream stream = new FileInputStream(new File(realPath));
				md5 = DigestUtils.md5Hex(stream).substring(0, length);
			}
			catch (Exception e)
			{
				md5 = RandomStringUtils.randomNumeric(length);
				log.warn("could not calculate md5 hash of resource " + file, e);
			}

			md5Value = file.substring(0, file.indexOf(".")) + "_" + md5 + file.substring(file.indexOf("."));
			hashes.put(file, md5Value);
		}
		return md5Value;
	}

	@Override
	public void setValue(String value)
	{
		this.value = value;
	}
}
