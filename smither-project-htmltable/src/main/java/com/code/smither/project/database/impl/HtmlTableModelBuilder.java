package com.code.smither.project.database.impl;

import com.code.smither.engine.api.IModelBuilder;
import com.code.smither.engine.api.IRootModel;
import com.code.smither.project.base.ProjectConfig;
import com.code.smither.project.base.api.TableSource;
import com.code.smither.project.base.model.DatabaseJdbc;
import com.code.smither.project.base.model.Model;

/**
 *
 * Created by SCWANG on 2016/8/18.
 */
public class HtmlTableModelBuilder implements IModelBuilder {

    private final TableSource source;
    private final ProjectConfig config;

    public HtmlTableModelBuilder(ProjectConfig config, TableSource source) {
        this.config = config;
        this.source = source;
    }

    @Override
    public IRootModel build() throws Exception {
        return build(config,source);
    }

    private Model build(ProjectConfig config, TableSource source) throws Exception {
        Model model = new Model();
        model.setAuthor(config.getTargetProjectAuthor());
        model.setCharset(config.getTargetCharset());
        model.setPackageName(config.getTargetProjectPackage());
        model.setProjectName(config.getTargetProjectName());
        model.setJdbc(new DatabaseJdbc());
        model.getJdbc().setUrl("");
        model.getJdbc().setDriver("");
        model.getJdbc().setUsername("");
        model.getJdbc().setPassword("");
        model.setTables(source.build());
        return model;
    }
}
