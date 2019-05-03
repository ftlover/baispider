package site.myall.dao;

import org.apache.ibatis.session.SqlSession;
import site.myall.bean.Result;

import java.util.List;

public class Sqlite {

    private static volatile Sqlite sqlite = null;

    private Sqlite(){

    }

    public static Sqlite getInstance(){
        if (sqlite == null)
            sqlite = new Sqlite();
        return sqlite;
    }

    public boolean existTable(String tableName){
        SqlSession sqlSession = JDBCUtil.getSqlSession();
        List<Integer> list = sqlSession.selectList("Mapper.existTable",tableName);
        sqlSession.close();
        return list==null||list.get(0)!=0;
    }

    public void createTable(String tableName){
        SqlSession sqlSession = JDBCUtil.getSqlSession();
        sqlSession.update("Mapper.createTable",tableName);
        sqlSession.close();
    }

    public void insertResult(Result result){
        SqlSession sqlSession = JDBCUtil.getSqlSession();
        sqlSession.insert("Mapper.insertResult",result);
        sqlSession.close();
    }

}
