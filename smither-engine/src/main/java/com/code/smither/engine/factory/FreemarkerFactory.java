package com.code.smither.engine.factory;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;

import java.io.File;
import java.io.IOException;

/**
 * 模板引擎工厂
 * Created by SCWANG on 2015-07-04.
 */
public class FreemarkerFactory {

	private static Configuration cfg;

	public static Configuration getConfiguration(String url) {
		cfg = new Configuration();
		File file = new File(url);
		try {
			cfg.setDirectoryForTemplateLoading(file);
			cfg.setObjectWrapper(new DefaultObjectWrapper());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cfg;
	}

	public static Template getTemplate(File file, String encoding) throws IOException {
		String path = file.getParentFile().getAbsolutePath();
		Configuration configuration = getConfiguration(path);
		return configuration.getTemplate(file.getName(), encoding);
	}

	public static Template getTemplate(File file) throws IOException {
		String path = file.getParentFile().getAbsolutePath();
		Configuration configuration = getConfiguration(path);
		return configuration.getTemplate(file.getName());
	}

	public static Template getTemplate(String template) throws IOException {
		Configuration cfg = new Configuration();
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("template",template);
		cfg.setTemplateLoader(stringLoader);
		return cfg.getTemplate("template");
	}

}


