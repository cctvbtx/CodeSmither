package com.codesmither.engine;

import java.io.File;
import java.io.PrintStream;

import com.codesmither.factory.C3P0Factory;
import com.codesmither.factory.TableSourceFactory;
import com.codesmither.factory.api.DbFactory;
import com.codesmither.kernel.*;
import com.codesmither.kernel.api.Config;
import com.codesmither.kernel.api.Converter;
import com.codesmither.kernel.api.TableSource;
import com.codesmither.model.Model;

/**
 * 核心引擎
 * Created by SCWANG on 2015-07-04.
 */
public class Engine {

    private Config config;
    private String templates;
    private String target;

    public Engine(Config config) {
        this.config = config;
        target = config.getTargetPath();
        templates = config.getTemplatePath();
    }

    public Engine(String templates, String target) {
        this.templates = templates;
        this.target = target;
    }

    protected void doInBackground(PrintStream print) throws Exception {

        File ftemplates = new File(templates);
        File ftarget = new File(target);
        if (!ftemplates.exists()) {
            throw new Exception("源项目不存在！");
        }
        if (!ftarget.exists() && !ftarget.mkdirs()) {
            throw new Exception("创建目标项目失败！");
        }
        DbFactory factory = C3P0Factory.getInstance(config.getDbConfigName());
        Converter converter = new ConfigConverter(config);
        ConfigFileFilter filter = new ConfigFileFilter(config);
        ModelBuilder modelBuilder = new ModelBuilder();
        TableSource tableBuilder = TableSourceFactory.getInstance(factory, converter);
        Model model = modelBuilder.build(config, factory, tableBuilder.build());
        TaskTransfer transfer = new TaskTransfer(config, model, ftemplates, ftarget, filter);
        while (transfer.hasTask()) {
            print.println(transfer.doTask());
        }
        factory.close();
    }

    public void doHtmlTableInBackground(PrintStream out, HtmlTableConfig config) throws Exception {
        doHtmlTableInBackground(out, new HtmlTableSource(config.getHtmlTablePath(), config.getHtmlTableCharset(), new ConfigConverter(config)));
    }

    public void doHtmlTableInBackground(PrintStream out, TableSource tableBuilder) throws Exception {
        File ftemplates = new File(config.getTemplatePath());
        File ftarget = new File(config.getTargetPath());
        if (!ftemplates.exists()) {
            throw new Exception("源项目不存在！");
        }
        if (!ftarget.exists() && !ftarget.mkdirs()) {
            throw new Exception("创建目标项目失败！");
        }
        ConfigFileFilter filter = new ConfigFileFilter(config);
        ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.build(config, C3P0Factory.getInstance("null"), tableBuilder.build());
        TaskTransfer transfer = new TaskTransfer(config, model, ftemplates, ftarget, filter);
        while (transfer.hasTask()) {
            out.println(transfer.doTask());
        }
    }
}
