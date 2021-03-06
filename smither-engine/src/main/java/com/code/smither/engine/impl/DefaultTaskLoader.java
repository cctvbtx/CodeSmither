package com.code.smither.engine.impl;

import com.code.smither.engine.api.IFileFilter;
import com.code.smither.engine.api.ITask;
import com.code.smither.engine.api.ITaskLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认的任务加载实现
 * Created by SCWANG on 2016/8/18.
 */
public class DefaultTaskLoader implements ITaskLoader {

    @Override
    public List<ITask> loadTask(File templates, File target, IFileFilter filter) {
        List<ITask> tasks = new ArrayList<>();
        File src = templates;
        if (src.isFile()) {
            if (filter == null || !filter.isNeedFilterFile(src)) {
                tasks.add(new DefaultTask(src, templates, target));
            }
            return tasks;
        }
        return loadTask(templates, target, filter, src, tasks);
    }


    protected List<ITask> loadTask(File templates, File target, IFileFilter filter,File path, List<ITask> tasks) {
        File[] files = path.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (filter == null || !filter.isNeedFilterFile(file)) {
                        tasks.add(new DefaultTask(file, templates, target));
                    }
                } else if (file.isDirectory()) {
                    if (filter == null || !filter.isNeedFilterPath(file)) {
                        loadTask(templates, target, filter, file, tasks);
                    }
                }
            }
        }
        return tasks;
    }
}
