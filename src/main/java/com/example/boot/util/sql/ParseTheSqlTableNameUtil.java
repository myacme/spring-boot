package com.example.boot.util.sql;


import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.Set;

/**
 * 高级SQL表名提取器
 * <p>
 *
 * <dependency>
 * <groupId>com.github.jsqlparser</groupId>
 * <artifactId>jsqlparser</artifactId>
 * <version>4.9</version>
 * </dependency>
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/10/30 17:10
 */
public class ParseTheSqlTableNameUtil {

    /**
     * 获取SQL中的表名
     *
     * @param sql SQL
     * @return 表名
     *
     */
    public static Set<String> getTableNames(String sql) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        return tablesNamesFinder.getTables(statement);
    }

    public static void main(String[] args) {
        String selectSql = "WITH cte1 AS (SELECT * FROM table1), " +
                "cte2 AS (SELECT * FROM table2 JOIN table3 ON table2.id=table3.id) " +
                "SELECT a.* FROM cte1 a JOIN cte2 b ON a.id=b.id " +
                "LEFT JOIN table4 c ON b.id=c.id WHERE EXISTS (SELECT 1 FROM table5)";
        String insertSql = "INSERT INTO table1 (id, name) select id, name from table2 ";
        String updateSql = "UPDATE table1 SET name='John' WHERE id=1";
        String deleteSql = "DELETE FROM table1 WHERE id = (select id from table2 where id = 1)";
        try {
            Set<String> selectTables = getTableNames(selectSql);
            Set<String> insertTables = getTableNames(insertSql);
            Set<String> updateTables = getTableNames(updateSql);
            Set<String> deleteTables = getTableNames(deleteSql);
            System.out.println("SELECT: " + selectTables);
            System.out.println("INSERT: " + insertTables);
            System.out.println("UPDATE: " + updateTables);
            System.out.println("DELETE: " + deleteTables);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }
}


