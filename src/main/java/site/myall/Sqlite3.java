package site.myall;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Map;

public class Sqlite3 {
    private static Sqlite3 sqlite3 = null;
    private static Connection connection = null;
    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private static Level dataInfo = Level.getLevel("DATAINFO");
    private Sqlite3(String dataBase){
        try {
            Class.forName("org.sqlite.JDBC");// 加载驱动,连接sqlite的jdbc
            connection= DriverManager.getConnection("jdbc:sqlite:"+dataBase);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static Sqlite3 getInstance(String dataBase){
        if (sqlite3 == null||connection == null)
            sqlite3 = new Sqlite3(dataBase);
        return sqlite3;
    }

    public void release(){
        if(connection!=null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean execute(String sql){
        Statement statement = null;
        boolean isSuccess = true;
        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            isSuccess = false;
        }finally {
            if (statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return  isSuccess;
    }



    public void createTable(String tableName)  {
        String sql = "SELECT COUNT(*) FROM "+ tableName ;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeQuery(sql);
        } catch (SQLException e) {
            if(tableName == "results"){
                try {
                    statement.executeUpdate("create table results(id INTEGER PRIMARY KEY AUTOINCREMENT, title varchar(255),baisi_url varchar(255) UNIQUE ,cat_url varchar(128) not null UNIQUE " +
                            ",image_urls varchar(512),real_url VARCHAR (1024),file_txt VARCHAR (3072),is_download VARCHAR (4))");
                } catch (SQLException e1) {
                    logger.warn(e1.getMessage());
                }
            }else if (tableName=="urls"){
                try {
                    statement.executeUpdate("create table urls(url varchar(255) not null PRIMARY KEY )");
                } catch (SQLException e1) {
                    logger.warn(e1.getMessage());
                }
            }
        }finally {
            if (statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public ResultSet getUrls_catUrs(){
        ResultSet resultSet = null;
        Statement statement = null;
        String sql = "Select baisi_url , cat_url from results";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT COUNT(*) as CNT FROM sqlite_master WHERE type = 'table' AND name = 'results'");
            int resultsTable = 0;
            if (resultSet.next()){
                resultsTable = Integer.parseInt(resultSet.getString("CNT"));
            }
            if (resultsTable != 0)
                resultSet = statement.executeQuery(sql);
            else
                resultSet = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    return resultSet;
    }

    public void insert(String tableName, Map<String,String> key_values){
        StringBuilder left = new StringBuilder("insert into "+tableName+" (");
        StringBuilder right = new StringBuilder(") values (");
        for (String key:key_values.keySet()){
            left.append(key+',');
            right.append("'"+key_values.get(key)+"'"+",");
        }
        left.deleteCharAt(left.length()-1);
        right.deleteCharAt(right.length()-1);
        right.append(")");
        String sql = left.toString()+right.toString();
        if(execute(sql))
            logger.log(dataInfo,key_values.get("title"));
    }
}
