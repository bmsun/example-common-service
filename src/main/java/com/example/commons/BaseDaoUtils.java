package com.example.commons;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.google.common.base.Preconditions;
import com.example.commons.model.DbInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaseDaoUtils {
    private static final String NAME = "COLUMN_NAME";
    private static final String TYPE = "TYPE_NAME";
    private static final Map<String, String> typeMap = new HashMap<>();
    private static String basePath;
    private static Map<String, List<String>> primaryKeysMap = new HashMap<>();

    static {
        URL baseUrl = BaseDaoUtils.class.getClassLoader().getResource(".");
        try {
            basePath = URLDecoder.decode(baseUrl.getPath(), "UTF-8");
            basePath = StringUtils.substringBefore(basePath, "/target");
        } catch (Exception e) {
            log.error("获取项目路径异常", e);
        }

        typeMap.put("CHAR", "String");
        typeMap.put("VARCHAR", "String");
        typeMap.put("BLOB", "String");
        typeMap.put("MEDIUMBLOB", "String");
        typeMap.put("LONGBLOB", "String");
        typeMap.put("TEXT", "String");
        typeMap.put("TINYTEXT", "String");
        typeMap.put("MEDIUMTEXT", "String");
        typeMap.put("LONGTEXT", "String");
        typeMap.put("ENUM", "String");
        typeMap.put("SET", "String");
        typeMap.put("FLOAT", "BigDecimal");
        typeMap.put("REAL", "BigDecimal");
        typeMap.put("DOUBLE", "BigDecimal");
        typeMap.put("NUMERIC", "BigDecimal");
        typeMap.put("DECIMAL", "BigDecimal");
        typeMap.put("TINYINT", "Integer");
        typeMap.put("SMALLINT", "Integer");
        typeMap.put("MEDIUMINT", "Integer");
        typeMap.put("INT", "Integer");
        typeMap.put("INTEGER", "Integer");
        typeMap.put("BIGINT", "Long");
        typeMap.put("DATE", "String");
        typeMap.put("TIME", "Date");
        typeMap.put("DATETIME", "Date");
        typeMap.put("TIMESTAMP", "Date");
    }

    public static void generateCode(DbInfo dbInfo, List<String> tables, String basePackage) throws Exception {
        Preconditions.checkArgument(StringUtils.isNotBlank(dbInfo.getUrl()), "数据库url不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(dbInfo.getUser()), "数据库用户名不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(basePackage), "basePackage不能为空");

        DataSource ds = dataSource(dbInfo);
        Connection conn = null;
        try {
            conn = ds.getConnection();
            DatabaseMetaData meta = conn.getMetaData();

            //如果没有指定tables,将遍历当前schema下所有表
            if (tables == null || tables.isEmpty()) {
                tables = new ArrayList<>();
                ResultSet rs = meta.getTables(null, null, "%", null);
                if (rs != null) {
                    while (rs.next()) {
                        tables.add(rs.getString(3));
                    }
                } else {
                    log.error("当前数据库schema下未发现任务表存在");
                }
            }

            tables.forEach(table -> generateCode(meta, table, basePackage));
        } catch (Throwable e) {
            log.error("dao代码生成异常", e);
        } finally {
            try {
                conn.close();
            } catch (Throwable ex) {
            }
        }
    }

    private static void generateCode(DatabaseMetaData meta, String table, String basePackage) {
        try {
            ResultSet rs = meta.getColumns(null, null, table, null);
            if (rs != null) {
                Map<String, String> nameTypes = new HashMap<>();
                while (rs.next()) {
                    nameTypes.put(rs.getString(NAME), rs.getString(TYPE));
                }

                //获取表所有主键
                ResultSet primaryKeyRs = meta.getPrimaryKeys(null, null, table);
                while (primaryKeyRs.next()) {
                    List<String> primaryKeys = new ArrayList<>();
                    if (primaryKeysMap.containsKey(table)) {
                        primaryKeys = primaryKeysMap.get(table);
                    } else {
                        primaryKeysMap.put(table, primaryKeys);
                    }
                    primaryKeys.add(primaryKeyRs.getString("COLUMN_NAME"));
                }

                String entityName = entityName(table);
                generateEntityFile(entityName, basePackage, nameTypes);
                generateDaoFile(entityName, basePackage, table, nameTypes);
            }
        } catch (Throwable e) {
            log.error("为表[{}]生成dao代码时异常: {}", table, ExceptionUtils.getStackTrace(e));
        }
    }

    private static void generateEntityFile(String entityName, String basePackage, Map<String, String> nameTypes) throws Exception {
        File file = ensureEntityFile(entityName, basePackage);
        InputStream in = new ByteArrayInputStream(entityContent(basePackage, entityName, nameTypes).getBytes("UTF-8"));
        FileUtils.copyInputStreamToFile(in, file);
    }

    private static void generateDaoFile(String entityName, String basePackage, String table, Map<String, String> nameTypes) throws Exception {
        File file = ensureDaoFile(entityName, basePackage);
        InputStream in = new ByteArrayInputStream(daoContent(basePackage, entityName, table, nameTypes).getBytes("UTF-8"));
        FileUtils.copyInputStreamToFile(in, file);
    }

    private static File ensureEntityFile(String entityName, String basePackage) throws Exception {
        File dir = new File(basePath + "/src/main/java/" + basePackage.replace('.', '/') + "/entity");
        FileUtils.forceMkdir(dir);
        File file = new File(dir, entityName + "Entity.java");
        FileUtils.touch(file);
        return file;
    }

    private static File ensureDaoFile(String entityName, String basePackage) throws Exception {
        File dir = new File(basePath + "/src/main/java/" + basePackage.replace('.', '/') + "/dao");
        FileUtils.forceMkdir(dir);
        File file = new File(dir, entityName + "Dao.java");
        FileUtils.touch(file);
        return file;
    }

    private static String entityContent(String basePackage, String entityName, Map<String, String> nameTypes) {
        StringBuilder buf = new StringBuilder("package ").append(basePackage).append(".entity;").append("\n\n");
        buf.append("import lombok.Data;").append("\n");
        buf.append("import java.util.*;").append("\n\n");
        buf.append("@Data").append("\n");
        buf.append("public class ").append(entityName).append("Entity {").append("\n");
        nameTypes.entrySet().forEach(entry ->
                buf.append("    private ").append(javaType(entry.getValue())).append(" ").append(toCamel(entry.getKey(), false)).append(";\n")
        );
        buf.append("}");

        return buf.toString();
    }

    private static String daoContent(String basePackage, String entityName, String table, Map<String, String> nameTypes) {
        String entityCamelName = toCamel(entityName, false);
        String primaryCamelKey = toCamel(primaryKey(table), false);
        StringBuilder buf = new StringBuilder("package ").append(basePackage).append(".dao;").append("\n\n");
        buf.append("import com.example.commons.model.Criteria;").append("\n");
        buf.append("import com.example.commons.model.JdbcResult;").append("\n");
        buf.append("import com.example.commons.model.PageRequest;").append("\n");
        buf.append("import org.springframework.beans.factory.annotation.Autowired;").append("\n");
        buf.append("import org.springframework.dao.EmptyResultDataAccessException;").append("\n");
        buf.append("import org.springframework.jdbc.core.JdbcTemplate;").append("\n");
        buf.append("import org.springframework.stereotype.Repository;").append("\n");
        buf.append("import com.example.commons.BaseJdbcUtils;").append("\n");
        buf.append("import com.xb.dao.test.entity.").append(entityName).append("Entity;").append("\n");
        buf.append("import java.util.*;").append("\n");
        buf.append("import java.util.stream.Collectors;").append("\n");
        buf.append("import java.util.stream.IntStream;").append("\n");
        buf.append("import javax.annotation.PostConstruct;").append("\n\n");
        buf.append("@Repository").append("\n");
        buf.append("public class ").append(entityName).append("Dao {").append("\n");
        buf.append("    private final String TABLE_NAME = \"").append(table).append("\";\n");
        buf.append("    private Map<String, String> dbMapping = new HashMap<>();").append("\n");
        buf.append("    @Autowired").append("\n");
        buf.append("    private JdbcTemplate template;").append("\n\n");

        //init
        buf.append("    @PostConstruct").append("\n");
        buf.append("    public void init() {").append("\n");
        nameTypes.entrySet().forEach(entry ->
                buf.append("        dbMapping.put(\"").append(toCamel(entry.getKey(), false)).append("\", \"").append(entry.getKey()).append("\");\n")
        );
        buf.append("    }\n");

        //create
        buf.append("    public boolean create").append(entityName).append("(").append(entityName).append("Entity ").append(entityCamelName).append(") {\n");
        buf.append("        JdbcResult jdbcResult = BaseJdbcUtils.getInsert(TABLE_NAME, ").append(entityCamelName).append(", dbMapping);\n");
        buf.append("        return template.update(jdbcResult.getSql(), jdbcResult.getParams()) == 1;\n");
        buf.append("    }\n\n");

        //batch create
        buf.append("    public int create").append(entityName).append("s(List<").append(entityName).append("Entity> ").append(entityCamelName).append("s) {\n");
        buf.append("        JdbcResult jdbcResult = BaseJdbcUtils.getBatchInsert(TABLE_NAME, ").append(entityCamelName).append("s, dbMapping);\n");
        buf.append("        return IntStream.of(template.batchUpdate(jdbcResult.getSql(), jdbcResult.getBatchParams())).sum();\n");
        buf.append("    }\n\n");

        //update
        buf.append("    public boolean update").append(entityName).append("(").append(entityName).append("Entity ").append(entityCamelName).append(") {\n");
        buf.append("        JdbcResult jdbcResult = BaseJdbcUtils.getUpdate(TABLE_NAME, ").append(entityCamelName).append(", dbMapping, \"")
                .append(primaryKey(table)).append("\");\n");
        buf.append("        return template.update(jdbcResult.getSql(), jdbcResult.getParams()) == 1;\n");
        buf.append("    }\n\n");

        //patch
        buf.append("    public boolean patch").append(entityName).append("(").append(entityName).append("Entity ").append(entityCamelName).append(") {\n");
        buf.append("        JdbcResult jdbcResult = BaseJdbcUtils.getPatch(TABLE_NAME, ").append(entityCamelName).append(", dbMapping, \"")
                .append(primaryKey(table)).append("\");\n");
        buf.append("        return template.update(jdbcResult.getSql(), jdbcResult.getParams()) == 1;\n");
        buf.append("    }\n\n");

        //get one record
        buf.append("    public ").append(entityName).append("Entity get").append(entityName).append("(").append(primaryKeyArg(table, nameTypes)).append(") {\n");
        buf.append("        JdbcResult jdbcResult = BaseJdbcUtils.getSelect(TABLE_NAME, Criteria.column(\"").append(primaryKey(table)).append("\").eq(").append(primaryCamelKey).append("));\n");
        buf.append("        try {\n");
        buf.append("            Map<String, Object> dbRow = template.queryForMap(jdbcResult.getSql(), jdbcResult.getParams());");
        buf.append("            return BaseJdbcUtils.dbRowToEntity(dbRow, dbMapping, ").append(entityName).append("Entity.class);\n");
        buf.append("        } catch (EmptyResultDataAccessException e) {\n");
        buf.append("            return null;\n");
        buf.append("        }\n");
        buf.append("    }\n\n");

        //batch get with paging
        buf.append("    public List<").append(entityName).append("Entity> get").append(entityName).append("s(PageRequest pageRequest) {\n");
        buf.append("        JdbcResult jdbcResult = BaseJdbcUtils.getSelect(TABLE_NAME, (Criteria) null, pageRequest);\n");
        buf.append("        return  template.queryForList(jdbcResult.getSql(), jdbcResult.getParams()).stream()\n");
        buf.append("            .map(dbRow -> BaseJdbcUtils.dbRowToEntity(dbRow, dbMapping, ").append(entityName).append("Entity.class))\n");
        buf.append("            .collect(Collectors.toList());\n");
        buf.append("    }\n");
        buf.append("}");

        return buf.toString();
    }

    private static String javaType(String sqlType) {
        if (typeMap.containsKey(sqlType.toUpperCase())) {
            return typeMap.get(sqlType.toUpperCase());
        } else {
            log.warn("无法识别的mysql数据类型[{}]将映射为String", sqlType);
            return "String";
        }
    }

    private static String toCamel(String src, boolean firstUpper) {
        return BaseStringUtils.underScoreToCamel(src, firstUpper);
    }

    private static String entityName(String table) {
        int index = table.indexOf("t_");
        if (index == 0) {
            table = table.substring(2);
        }
        index = table.indexOf("T_");
        if (index == 0) {
            table = table.substring(2);
        }

        table = table.replaceAll("_\\d{1,}$", "");
        return toCamel(table, true);
    }

    private static DataSource dataSource(DbInfo dbInfo) throws Exception {
        Map<String, Object> datasourceMap = new HashMap<>();
        datasourceMap.put("url", dbInfo.getUrl());
        datasourceMap.put("username", dbInfo.getUser());
        if (StringUtils.isNotBlank(dbInfo.getPass())) {
            datasourceMap.put("password", dbInfo.getPass());
        }

        return DruidDataSourceFactory.createDataSource(datasourceMap);
    }

    private static String primaryKey(String table) {
        if (!primaryKeysMap.containsKey(table) || primaryKeysMap.get(table).size() != 1) {
            return "id";
        } else {
            return primaryKeysMap.get(table).get(0);
        }
    }

    private static String primaryKeyArg(String table, Map<String, String> nameTypes) {
        if (!primaryKeysMap.containsKey(table) || primaryKeysMap.get(table).size() != 1) {
            return "String id";
        } else {
            String colName = primaryKeysMap.get(table).get(0);
            String type = typeMap.get(nameTypes.get(colName));
            return type + " " + toCamel(colName, false);
        }
    }
}
