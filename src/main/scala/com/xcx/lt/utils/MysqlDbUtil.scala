package com.xcx.lt.utils

import java.sql._
import javax.sql.DataSource

import org.apache.commons.dbcp2.BasicDataSourceFactory
import org.apache.commons.dbutils.QueryRunner
import xitrum.util.Loader

object MysqlDbUtil {
    /**
      * 初始化连接池
      */
    lazy val datasource: DataSource = BasicDataSourceFactory.createDataSource(Loader.propertiesFromClasspath("database.properties"))
    
    /**
      * 获取queryRunner
      *
      * @return
      */
    def getOracleQueryRunner(): QueryRunner = {
        new QueryRunner(datasource, true)
    }
    
    /**
      * 获取连接
      *
      * @return
      */
    def getConn(): Connection = {
        try {
            return datasource.getConnection();
        } catch {
            case ex: SQLException => {
                ex.printStackTrace()
            }
        }
        return null;
    }
    
    /**
      * 释放资源
      *
      * @param rs
      * @param ps
      * @param conn
      */
    def close(rs: ResultSet, ps: PreparedStatement, conn: Connection): Unit = {
        try {
            if (rs != null)
                try {
                    rs.close();
                } catch {
                    case ex: SQLException => {
                        ex.printStackTrace()
                    }
                }
        } finally {
            try {
                if (ps != null)
                    try {
                        ps.close();
                    } catch {
                        case ex: SQLException => {
                            ex.printStackTrace()
                        }
                    }
            } finally {
                if (conn != null)
                    try {
                        conn.close();
                    } catch {
                        case ex: SQLException => {
                            ex.printStackTrace()
                        }
                    }
            }
        }
    }
    
    /**
      * 关闭连接
      */
    def closeAll(rs: ResultSet, st: Statement, conn: Connection): Unit = {
        try {
            if (rs != null)
                try {
                    rs.close();
                } catch {
                    case ex: SQLException => {
                        ex.printStackTrace()
                    }
                }
        } finally {
            try {
                if (st != null)
                    try {
                        st.close();
                    } catch {
                        case ex: SQLException => {
                            ex.printStackTrace()
                        }
                    }
            } finally {
                if (conn != null)
                    try {
                        conn.close();
                    } catch {
                        case ex: SQLException => {
                            ex.printStackTrace()
                        }
                    }
            }
        }
    }
    
    
}
