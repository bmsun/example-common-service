package com.moxie.commons;

import com.google.common.base.Preconditions;
import com.moxie.commons.model.Criteria;
import com.moxie.commons.model.JdbcResult;
import com.moxie.commons.model.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.*;
import java.util.stream.IntStream;

/**
 * 生成sql语句及相应参数的帮助类.
 * 内部有大量的重复方法, 如参数类型为Criteria及List<Criteria>, 之所以不使用Criteria...就是要强迫使用者在有多个Criterion的情况下使用
 * 更可读的List<Criteria>
 */
@Slf4j
public class BaseJdbcUtils {
    public static JdbcResult getInsert(String table, Object entity, Map<String, String> dbMapping) {
        Preconditions.checkArgument(entity != null, "数据库插入数据时entity对象不能为空");
        return insertSql(table, Arrays.asList(entity), dbMapping, false);
    }

    public static JdbcResult getInsertIgnore(String table, Object entity, Map<String, String> dbMapping) {
        Preconditions.checkArgument(entity != null, "数据库插入数据时entity对象不能为空");
        return insertSql(table, Arrays.asList(entity), dbMapping, true);
    }

    public static JdbcResult getBatchInsert(String table, List<? extends Object> entities, Map<String, String> dbMapping) {
        return insertSql(table, entities, dbMapping, false);
    }

    public static JdbcResult getBatchInsertIgnore(String table, List<? extends Object> entities, Map<String, String> dbMapping) {
        return insertSql(table, entities, dbMapping, true);
    }

    public static JdbcResult getSelect(String table, Criteria criterion) {
        List<Criteria> criteria = criterion == null ? Collections.emptyList() : Arrays.asList(criterion);
        return getSelect(table, criteria, null);
    }

    public static JdbcResult getSelect(String table, List<Criteria> criteria) {
        return getSelect(table, criteria, null);
    }

    public static JdbcResult getSelect(String table, Criteria criterion, PageRequest pageRequest) {
        List<Criteria> criteria = criterion == null ? Collections.emptyList() : Arrays.asList(criterion);
        return getSelect(table, criteria, pageRequest);
    }

    public static JdbcResult getSelect(String table, List<Criteria> criteria, PageRequest pageRequest) {
        JdbcResult jdbcResult;
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(table);
        JdbcResult whereSql = whereSql(criteria);
        jdbcResult = new JdbcResult(sql.append(whereSql.getSql()).toString(), whereSql.getParams());

        //添加分页参数
        if (pageRequest != null && pageRequest.getPage() != null) {
            String pageSql = pageSql(pageRequest);
            jdbcResult.setSql(jdbcResult.getSql() + pageSql);
        }
        return jdbcResult;
    }

    public static JdbcResult getSelectForCount(String table, Criteria criterion) {
        List<Criteria> criteria = criterion == null ? Collections.emptyList() : Arrays.asList(criterion);
        return getSelectForCount(table, criteria);
    }

    public static JdbcResult getSelectForCount(String table, List<Criteria> criteria) {
        Preconditions.checkArgument(StringUtils.isNotBlank(table), "表名不能为空");
        StringBuilder sql = new StringBuilder("SELECT count(*) FROM ").append(table);
        JdbcResult whereSql = whereSql(criteria);
        return new JdbcResult(sql.append(whereSql.getSql()).toString(), whereSql.getParams());
    }

    public static JdbcResult getSelectForSum(String table, String dbColumn, Criteria criterion) {
        List<Criteria> criteria = criterion == null ? Collections.emptyList() : Arrays.asList(criterion);
        return getSelectForSum(table, dbColumn, criteria);
    }

    /**
     * @param dbColumn 需要进行sum操作的数据库列名
     */
    public static JdbcResult getSelectForSum(String table, String dbColumn, List<Criteria> criteria) {
        Preconditions.checkArgument(StringUtils.isNotBlank(table), "表名不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(dbColumn), "表[" + table + "]进行sum运算的列名不能为空");
        StringBuilder sql = new StringBuilder("SELECT sum(").append(dbColumn).append(") FROM ").append(table);
        JdbcResult whereSql = whereSql(criteria);
        return new JdbcResult(sql.append(whereSql.getSql()).toString(), whereSql.getParams());
    }

    public static JdbcResult getPatch(String table, Object entity, Map<String, String> dbMapping, String dbColumn) {
        Preconditions.checkArgument(StringUtils.isNotBlank(dbColumn), "更新数据时必须指定dbColumn条件");
        return getPatch(table, entity, dbMapping, Arrays.asList(dbColumn));
    }

    public static JdbcResult getPatch(String table, Object entity, Map<String, String> dbMapping, Criteria criterion) {
        Preconditions.checkArgument(criterion != null, "更新数据时criteria不能为空");
        return getPatch(table, entity, dbMapping, Arrays.asList(criterion));
    }

    /**
     * 只更新entity中不为null的属性
     */
    public static JdbcResult getPatch(String table, Object entity, Map<String, String> dbMapping, List<? extends Object> criteria) {
        Preconditions.checkArgument(StringUtils.isNotBlank(table), "表名不能为空");
        Preconditions.checkArgument(entity != null, "数据库插入数据时entity对象不能为空");
        Preconditions.checkArgument(dbMapping != null && dbMapping.size() > 0, "数据库字段名和实体属性名的对应关系不能为空");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(criteria), "更新数据时criteria不能为空");

        StringBuilder sql = new StringBuilder("UPDATE ").append(table).append(" SET ");
        Map<String, Object> colValueMap = colValues(entity, dbMapping, true);
        List<String> colNames = new ArrayList<>(colValueMap.keySet());

        //col1=?,col2=?,..
        IntStream.range(0, colNames.size()).forEach(index -> {
            if (index != 0) {
                sql.append(",");
            }
            sql.append(colNames.get(index)).append("=?");
        });

        //where sql
        JdbcResult whereSql = whereSql(criteria, colValueMap);
        sql.append(whereSql.getSql());

        //组装params
        List<Object> params = new ArrayList<>();
        IntStream.range(0, colNames.size()).forEach(index ->
                params.add(colValueMap.get(colNames.get(index)))
        );
        IntStream.range(0, whereSql.getParams().length).forEach(index ->
                params.add(whereSql.getParams()[index])
        );

        return new JdbcResult(sql.toString(), params.toArray());
    }

    public static JdbcResult getUpdate(String table, Object entity, Map<String, String> dbMapping, Criteria criterion) {
        Preconditions.checkArgument(criterion != null, "更新数据时criteria不能为空");
        return getUpdate(table, entity, dbMapping, Arrays.asList(criterion));
    }

    public static JdbcResult getUpdate(String table, Object entity, Map<String, String> dbMapping, String dbColumn) {
        Preconditions.checkArgument(StringUtils.isNotBlank(dbColumn), "更新数据时必须指定dbColumn条件");
        return getUpdate(table, entity, dbMapping, Arrays.asList(dbColumn));
    }

    public static JdbcResult getUpdate(String table, Object entity, Map<String, String> dbMapping, List<? extends Object> criteria) {
        return getBatchUpdate(table, Arrays.asList(entity), dbMapping, criteria);
    }

    public static JdbcResult getBatchUpdate(String table, List<? extends Object> entities, Map<String, String> dbMapping, String dbColumn) {
        Preconditions.checkArgument(StringUtils.isNotBlank(dbColumn), "更新数据时必须指定dbColumn条件");
        return getBatchUpdate(table, entities, dbMapping, Arrays.asList(dbColumn));
    }

    /**
     * @param criteria 如果Object类型为String, 则表示实体类字段; 如果为Criteria表示数据库字段
     */
    public static JdbcResult getBatchUpdate(String table, List<? extends Object> entities, Map<String, String> dbMapping, List<? extends Object> criteria) {
        Preconditions.checkArgument(StringUtils.isNotBlank(table), "表名不能为空");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(entities), "数据库插入数据时entity对象不能为空");
        Preconditions.checkArgument(dbMapping != null && dbMapping.size() > 0, "数据库字段名和实体属性名的对应关系不能为空");

        StringBuilder sql = new StringBuilder("UPDATE ").append(table).append(" SET ");
        List<String> fieldNames = new ArrayList<>(dbMapping.keySet());

        //col1=?,col2=?,..
        IntStream.range(0, dbMapping.size()).forEach(index -> {
            if (index != 0) {
                sql.append(",");
            }
            sql.append(dbMapping.get(fieldNames.get(index))).append("=?");
        });

        //where
        sql.append(" WHERE ");
        IntStream.range(0, criteria.size()).forEach(index -> {
            if (index != 0) {
                sql.append(" AND ");
            }

            Object field = criteria.get(index);
            if (field instanceof String) {
                sql.append(dbMapping.get(field)).append("=?");
            } else {
                Criteria criterion = (Criteria) field;
                sql.append(whereSql(criterion));
            }
        });

        //组装params
        List<Object[]> paramsList = new ArrayList<>();
        entities.forEach(entity -> {
            List<Object> params = new ArrayList();

            Map<String, Object> entityMap = BaseBeanUtils.beanToMap(entity);
            IntStream.range(0, dbMapping.size()).forEach(index ->
                    params.add(entityMap.get(fieldNames.get(index)))
            );

            //where params
            IntStream.range(0, criteria.size()).forEach(index -> {
                Object field = criteria.get(index);
                if (field instanceof String) {
                    params.add(entityMap.get(criteria.get(index)));
                } else if (field instanceof Criteria) {
                    Criteria criterion = (Criteria) field;
                    if (criterion.getType() == Criteria.Type.IN || criterion.getType() == Criteria.Type.NIN) {
                        params.addAll((List) criterion.getValue());
                    } else {
                        params.add(criterion.getValue());
                    }
                }
            });

            paramsList.add(params.toArray());
        });

        return new JdbcResult(sql.toString(), paramsList);
    }

    public static JdbcResult getDelete(String table, Criteria criterion) {
        Preconditions.checkArgument(criterion != null, "数据库删除必须要指定条件");
        return getDelete(table, Arrays.asList(criterion));
    }

    public static JdbcResult getDelete(String table, List<Criteria> criteria) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(criteria), "数据库删除必须要指定条件");
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(table);
        JdbcResult whereSql = whereSql(criteria);
        return new JdbcResult(sql.append(whereSql.getSql()).toString(), whereSql.getParams());
    }

    public static <T> T dbRowToEntity(Map<String, Object> dbRow, Map<String, String> dbMapping, Class<T> entityClass) {
        if (dbRow == null || dbRow.size() == 0) {
            return null;
        }

        Object entity = BaseBeanUtils.newInstance(entityClass);
        if (entity == null) {
            return null;
        }

        Arrays.stream(entityClass.getDeclaredFields()).forEach(f -> {
            try {
                f.setAccessible(true);
                String col = dbMapping.get(f.getName());
                if (col != null) {
                    Object value = dbRow.get(col);
                    if (value != null) {
                        if (value instanceof java.sql.Date || value instanceof java.sql.Timestamp || value instanceof java.sql.Time) {
                            value = new Date(((Date) value).getTime());
                        }
                        if (f.getType() != String.class && value.getClass() == String.class) {
                            PropertyEditor editor = PropertyEditorManager.findEditor(f.getType());
                            editor.setAsText(value.toString());
                            value = editor.getValue();
                        }

                        f.set(entity, value);
                    }
                } else {
                    log.warn("类'{}'中属性'{}'没有对应的数据库字段", entityClass.getSimpleName(), f.getName());
                }
            } catch (Throwable e) {
                log.error("用数据库返回的数据设置类[{}]字段[{}]异常", entityClass.getSimpleName(), f.getName(), ExceptionUtils.getStackTrace(e));
            }
        });

        return (T) entity;
    }

    private static JdbcResult insertSql(String table, List<? extends Object> entities, Map<String, String> dbMapping, boolean ignore) {
        Preconditions.checkArgument(StringUtils.isNotBlank(table), "表名不能为空");
        Preconditions.checkArgument(entities != null && entities.size() > 0, "数据库插入数据时entity对象不能为空");
        Preconditions.checkArgument(dbMapping != null && dbMapping.size() > 0, "数据库字段名和实体属性名的对应关系不能为空");

        StringBuilder sql = new StringBuilder("INSERT ");
        if (ignore) {
            sql.append("IGNORE INTO ").append(table);
        } else {
            sql.append("INTO ").append(table);
        }

        //(col1,col2,...)
        List<String> fieldNames = new ArrayList<>(dbMapping.keySet());
        sql.append(" (");
        IntStream.range(0, dbMapping.size()).forEach(index -> {
            if (index != 0) {
                sql.append(",");
            }
            sql.append(dbMapping.get(fieldNames.get(index)));
        });

        //values(?,?,..)
        sql.append(") VALUES (");
        IntStream.range(0, dbMapping.size()).forEach(index -> {
            if (index != 0) {
                sql.append(",");
            }
            sql.append("?");
        });
        sql.append(")");

        List<Object[]> paramsList = new ArrayList<>();
        entities.forEach(entity -> {
            Object[] params = new Object[dbMapping.size()];
            paramsList.add(params);

            Map<String, Object> entityMap = BaseBeanUtils.beanToMap(entity);
            IntStream.range(0, dbMapping.size()).forEach(index -> {
                params[index] = entityMap.get(fieldNames.get(index));
            });
        });

        return new JdbcResult(sql.toString(), paramsList);
    }

    private static JdbcResult whereSql(List<? extends Object> criteria) {
        return whereSql(criteria, null);
    }

    private static JdbcResult whereSql(List<? extends Object> criteria, Map<String, Object> colValues) {
        if (CollectionUtils.isEmpty(criteria)) {
            return new JdbcResult("", new Object[]{});
        }

        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(" WHERE ");
        IntStream.range(0, criteria.size()).forEach(index -> {
            if (index != 0) {
                sql.append(" AND ");
            }

            Object criterion = criteria.get(index);
            if (criterion instanceof String) {
                sql.append(criterion).append("=?");
                params.add(colValues.get(criterion));
            } else {
                Criteria criterion1 = (Criteria) criterion;
                sql.append(whereSql((criterion1)));
                if (criterion1.getType() == Criteria.Type.IN || criterion1.getType() == Criteria.Type.NIN) {
                    params.addAll((List) criterion1.getValue());
                } else {
                    params.add(criterion1.getValue());
                }
            }
        });

        return new JdbcResult(sql.toString(), params.toArray());
    }

    private static String whereSql(Criteria criterion) {
        StringBuilder sql = new StringBuilder("");
        if (criterion.getType() == Criteria.Type.EQ) {
            sql.append(criterion.getColName()).append("=?");
        } else if (criterion.getType() == Criteria.Type.NE) {
            sql.append(criterion.getColName()).append("!=?");
        } else if (criterion.getType() == Criteria.Type.GT) {
            sql.append(criterion.getColName()).append(">?");
        } else if (criterion.getType() == Criteria.Type.GE) {
            sql.append(criterion.getColName()).append(">=?");
        } else if (criterion.getType() == Criteria.Type.LT) {
            sql.append(criterion.getColName()).append("<?");
        } else if (criterion.getType() == Criteria.Type.LE) {
            sql.append(criterion.getColName()).append("<=?");
        } else if (criterion.getType() == Criteria.Type.IN || criterion.getType() == Criteria.Type.NIN) {
            char[] temp = new char[((List) criterion.getValue()).size()];
            Arrays.fill(temp, '?');
            String innerSql = StringUtils.join(temp, ',');
            if (criterion.getType() == Criteria.Type.IN) {
                sql.append(criterion.getColName()).append(" IN(").append(innerSql).append(")");
            } else {
                sql.append(criterion.getColName()).append(" NOT IN(").append(innerSql).append(")");
            }
        }
        return sql.toString();
    }

    private static String pageSql(PageRequest pageRequest) {
        if (pageRequest == null) {
            return "";
        }
        Integer page = pageRequest.getPage();
        Integer pageSize = pageRequest.getPageSize();
        String pageSql = "";
        if (StringUtils.isNotBlank(pageRequest.getOrderedCol())) {
            pageSql = " ORDER BY " + pageRequest.getOrderedCol() + " " + pageRequest.getOrder().name();
        }
        return pageSql + " LIMIT " + (page - 1) * pageSize + "," + pageSize;
    }

    private static Map<String, Object> colValues(Object entity, Map<String, String> dbMapping, boolean noneNull) {
        Map<String, Object> entityMap;
        if (noneNull) {
            entityMap = BaseBeanUtils.beanToMapNonNull(entity);
        } else {
            entityMap = BaseBeanUtils.beanToMap(entity);
        }

        Map<String, Object> colValues = new HashMap<>();
        entityMap.entrySet().forEach(entry -> {
            if (dbMapping.containsKey(entry.getKey())) {
                colValues.put(dbMapping.get(entry.getKey()), entry.getValue());
            }
        });
        return colValues;
    }
}
