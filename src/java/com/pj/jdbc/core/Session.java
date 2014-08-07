/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pj.jdbc.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据库会话接口
 * @author PENGJU
 */
public interface Session {
    /**
     * 批量执行 sql 脚本
     * @param sql 可带参数的SQL语句
     * @param values 参数值列表
     * @return 影响行数
     * @throws SQLException 
     */
    public int executeBatch(String sql,List<Object[]> values) throws SQLException;
    
    /**
     * 执行更新操作
     * @param sql
     * @param paramVals 参数值
     * @return
     * @throws SQLException 
     */
    public int executeUpdate(String sql,Object[] paramVals) throws SQLException;
    /**
     * 执行更新
     * @param sql
     * @return
     * @throws SQLException 
     */
    public int executeUpdate(String sql) throws SQLException;
    /**
     * 按参数执行查询
     * @param sql
     * @param paramVals
     * @return
     * @throws SQLException 
     */
    public ResultList<ResultRow> executeQuery(String sql,Object[] paramVals) throws SQLException;
    /**
     * 执行查询
     * @param sql
     * @return
     * @throws SQLException 
     */
    public ResultList<ResultRow> executeQuery(String sql) throws SQLException;
    /**
     * 按参数执行有限数据行查询
     * @param sql
     * @param paramVals
     * @param startRow 起始行下标 从0开始
     * @param rowCount 需要的结果行数
     * @return 结果列表
     * @throws SQLException 
     */
    public ResultList<ResultRow> executeQuery(String sql,Object[] paramVals,int startRow,int rowCount) throws SQLException;

    /**
     * 按参数执行有限数据行查询,并将每一行结果行交给filter包装
     * @param sql
     * @param paramVals
     * @param startRow 起始行下标 从0开始
     * @param rowCount 需要的结果行数
     * @param filter 结果集过滤器,可以在这里对记录行进行包装,返回包装的对象即可
     * @return
     * @throws SQLException 
     */
    public <T> ResultList<T> executeQuery(String sql,Object[] paramVals,int startRow,int rowCount,QueryFilter<T> filter) throws SQLException;
    /**
     * 按参数执行数据查询,并将每一行结果行交给filter包装
     * @param <T> 动态类型
     * @param sql
     * @param paramVals
     * @param filter 结果集过滤器,可以在这里对记录行进行包装,返回包装的对象即可
     * @return
     * @throws SQLException 
     */
    public <T> ResultList<T> executeQuery(String sql,Object[] paramVals,QueryFilter<T> filter) throws SQLException;
    /**
     * 按参查询,并将每一行结果行交给filter包装
     * @param <T> 动态类型
     * @param sql
     * @param filter 结果集过滤器,可以在这里对记录行进行包装,返回包装的对象即可
     * @return
     * @throws SQLException 
     */
    public <T> ResultList<T> executeQuery(String sql,QueryFilter<T> filter) throws SQLException;
    
    /**
     * 开始事务
     * @throws SQLException 
     */
    public void beginTransaction() throws SQLException;
    /**
     * 回滚
     * @throws SQLException 
     */
    public void rollback() throws SQLException;
    /**
     * 提交更改
     * @throws SQLException 
     */
    public void commit() throws SQLException;
    /**
     * 停止事务
     * @throws SQLException 
     */
    public void endTransaction() throws SQLException;
    /**
     * 返回当前会话的连接对象
     * @return 
     */
    public Connection getConnection();
    /**
     * 关闭会话
     * @throws SQLException 
     */
    public void close() throws SQLException;
    /**
     * 保存一行数据
     * 数据的类型必须要和数据库的对应类型对应
     * @param table 表名
     * @param row 数据行
     * @return
     * @throws SQLException 
     */
    public int save(String table,ResultRow row) throws SQLException;
    /**
     * 保存一个对象,必须用@Table对类注解,并且对每一个需要存到数据库的实例变量也要用@Column注解
     * 在此只能保存自己的属性,不能保存继承来的属性
     * 如果没有对类注解则用类名做表名,没有注解的实例变量将不存到数据库中
     * 变量的类型必须和数据库中的类型一一对应
     * @param object
     * @return
     * @throws SQLException 
     */
    public int save(Object object) throws SQLException;
    /**
     * 查询数据库,并将结果行转换为指定的clazz对象
     * @param <T> 目标类型
     * @param sql sql语句
     * @param paramVals 参数值
     * @param startRow 起始行下标,从0开始
     * @param rowCount 结果行数
     * @param clazz 类型
     * @return
     * @throws SQLException 
     */
    public <T> ResultList<T> executeQuery(String sql,Object[] paramVals,int startRow,int rowCount,Class<T> clazz) throws SQLException;
    /**
     * 查询数据库,并将结果行转换为指定的clazz对象
     * @param <T> 目标类型
     * @param sql sql语句
     * @param paramVals 参数值
     * @param clazz 类型
     * @return
     * @throws SQLException 
     */
    public <T> ResultList<T> executeQuery(String sql,Object[] paramVals,Class<T> clazz) throws SQLException;
    /**
     * 查询数据库,并将结果行转换为指定的clazz对象
     * @param <T> 目标类型
     * @param sql sql语句
     * @param clazz 类型
     * @return
     * @throws SQLException 
     */
    public <T> ResultList<T> executeQuery(String sql,Class<T> clazz) throws SQLException;
    
    /**
     * 执行查询，并返回一个结果(即使结果有多个)，无结果返回null
     * @param <T> 指定的返回类型
     * @param sql SQL语句
     * @param clazz 指定的类
     * @return T
     */
    public <T> T querySingle(String sql,Class<T> clazz) throws SQLException ;
    /**
     * 执行查询，并返回一个结果(即使结果有多个)，无结果返回null
     * @param <T> 指定的返回类型
     * @param sql SQL语句,参数用问号表示
     * @param paramVals 参数值
     * @param clazz 指定的类
     * @return T
     */
    public <T> T querySingle(String sql,Object[] paramVals,Class<T> clazz) throws SQLException ;
    /**
     * 执行查询，并返回一个结果(即使结果有多个)，无结果返回null
     * @param sql SQL语句
     * @return ResultRow
     */
    public ResultRow querySingle(String sql) throws SQLException ;
    /**
     * 执行查询，并返回一个结果(即使结果有多个)，无结果返回null
     * @param sql SQL语句,参数用问号表示
     * @param paramVals 参数值
     * @return ResultRow
     */
    public ResultRow querySingle(String sql,Object[] paramVals) throws SQLException ;
    
    /**
     * 列出表格所有数据
     * @param table
     * @return 
     */
    public ResultList<ResultRow> list(String table) throws SQLException;
    
    /**
     * 列出表格中从startRow行开始的rowCount条数据
     * @param table 表格名称
     * @param startRow 起始行下标,从0开始。第一行就是0，第二行就是1……
     * @param rowCount
     * @return
     * @throws SQLException 
     */
    public ResultList<ResultRow> list(String table, int startRow, int rowCount) throws SQLException;
}
