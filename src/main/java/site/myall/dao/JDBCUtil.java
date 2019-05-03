package site.myall.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import site.myall.core.DataPipline;

import java.io.IOException;

public class JDBCUtil {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(DataPipline.class);

    public static SqlSession getSqlSession() {
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-conf.xml"));
            sqlSession =  sqlSessionFactory.openSession(true);
        } catch (IOException e) {
            log.error("open session error",e);
        }
        return sqlSession;
    }
}
