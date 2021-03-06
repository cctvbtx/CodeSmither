package com.code.smither.project;

import com.code.smither.project.base.ProjectEngine;
import com.code.smither.project.base.model.Table;
import com.code.smither.project.base.model.TableColumn;
import com.code.smither.project.htmltable.HtmlTableConfig;
import com.code.smither.project.htmltable.HtmlTableEngine;
import com.code.smither.project.database.factory.ConfigFactory;
import com.code.smither.project.database.impl.HtmlTableModelBuilder;
import com.code.smither.project.database.impl.HtmlTableSource;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.sql.Types;

/**
 *
 * Created by SCWANG on 2016/8/18.
 */
public class TempleteTest {

    @Test
    public void HtmlTableTemplate() {
        try {
            HtmlTableConfig config = ConfigFactory.loadConfig("config.properties");
            HtmlTableEngine engine = new HtmlTableEngine(config);
            engine.launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void HtmlTableTestCloudTemplate() {
        try {
            HtmlTableConfig config = ConfigFactory.loadConfig("config.properties");
            config.setHtmlTablePath("../htmltable/TestCloud.html");
            config.setTemplatePath("../templates/testcloud-model");
            config.setTemplateLang("C#");
            config.setTargetProjectPackage("YX.TestCloud.Data");
            config.setTargetPath("../testcloud-model");
            ProjectEngine engine = new ProjectEngine(config);

            HtmlTableSource source = new HtmlTableSource(config) {
                {
                    metaData = new HtmlTableMetaDataImpl(){
                        @Override
                        public String getColumnType(Elements columnMetaData) {
                            if (getColumnLength(columnMetaData) > 0) {
                                return "varchar";
                            }
                            return columnMetaData.get(3).text();
                        }

                        @Override
                        public int getColumnTypeInt(Elements columnMetaData) {
                            String text = columnMetaData.get(3).text();
                            if (text.endsWith("符串")) {
                                return Types.NVARCHAR;
                            } else if (text.contains("日期")){
                                return Types.DATE;
                            } else if (text.toLowerCase().contains("binary")){
                                return Types.BINARY;
                            } else if (text.toLowerCase().contains("bool")){
                                return Types.BIT;
                            } else if (text.toLowerCase().contains("float")){
                                return Types.FLOAT;
                            }
                            if (columnMetaData.get(2).text().endsWith("日期")) {
                                return Types.DATE;
                            } else if (columnMetaData.get(2).text().startsWith("是否")) {
                                return Types.BIT;
                            }
                            if (columnMetaData.get(1).text().endsWith("Date")) {
                                return Types.DATE;
                            }
                            return Types.NVARCHAR;
                        }

                        @Override
                        public int getColumnLength(Elements columnMetaData) {
                            try {
                                return Integer.parseInt(columnMetaData.get(4).text().replace(",",""));
                            } catch (NumberFormatException ignored) {
                            }
                            return -1;
                        }
                    };
                }

                @Override
                protected void buildTableMetaData(Element tableElement, Table table) {
                    Element element = tableElement.previousElementSibling().clone();
                    table.setRemark(element.select("strong").text());
                    table.setName(element.select("em").text());
                }

                @Override
                protected void buildColumnMetaData(TableColumn column, Elements columnMetaData) {
                    column.setName      (columnMetaData.get(1).text().replace(" ",""));
                    column.setType      (metaData.getColumnType     (columnMetaData));
                    column.setTypeInt   (metaData.getColumnTypeInt  (columnMetaData));
                    column.setLength    (metaData.getColumnLength   (columnMetaData));
                    column.setDefValue  ("");
                    column.setNullable  (true);
                    column.setRemark    (columnMetaData.get(2).text());
                    column.setAutoIncrement(false);
                }

            };
            engine.launch(new HtmlTableModelBuilder(config, source));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
