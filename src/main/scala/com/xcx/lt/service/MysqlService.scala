package com.xcx.lt.service

import java.sql.{Connection, PreparedStatement, ResultSet}

import com.xcx.lt.modal.{PageDataResult, RequestPage}
import com.xcx.lt.utils.MysqlDbUtil
import xitrum.Log


/**
  * @author lf
  */
trait MysqlService extends Log {
    
    def ltLog = log
    
    /*var conn: Connection = null
    var ps: PreparedStatement = null
    var st: Statement = null
    var rs: ResultSet = null*/
    /**
      * 查询SQL
      */
    def query(sql: String, v: Seq[String]): List[Map[String, String]] = {
        ltLog.debug("【 SQL INFO】 execute sql：[" + sql + "]; parameters: [" + v + "]")
        var list: scala.collection.mutable.ArrayBuffer[Map[String, String]] = scala.collection.mutable.ArrayBuffer[Map[String, String]]()
        
        var conn: Connection = null
        var ps: PreparedStatement = null
        var rs: ResultSet = null
        try {
            conn = MysqlDbUtil.getConn()
            ps = conn.prepareStatement(sql)
            var i: Int = 1
            if (v != null) v.foreach { x => {
                ps.setString(i, x); i = i + 1
            }
            }
            rs = ps.executeQuery()
            while (rs.next()) {
                val dm = scala.collection.mutable.Map[String, String]()
                for (i <- 1 to rs.getMetaData.getColumnCount) {
                    dm.put(rs.getMetaData.getColumnName(i), rs.getString(i))
                }
                list += dm.toMap
            }
        } catch {
            case ex: Exception => ltLog.error(ex.getMessage); ex.printStackTrace(); null
        } finally {
            MysqlDbUtil.close(rs, ps, conn)
        }
        list.toList
    }
    
    /**
      * 单记录值查询
      */
    def singleQuery(sql: String, v: Seq[String]): String = {
        ltLog.debug("【 SQL INFO】 execute sql：[" + sql + "]; parameters: [" + v + "]")
        var rst: String = "0"
        var conn: Connection = null
        var ps: PreparedStatement = null
        var rs: ResultSet = null
        try {
            conn = MysqlDbUtil.getConn()
            ps = conn.prepareStatement(sql)
            var i: Int = 1
            if (v != null && v.length > 0) v.foreach { x => {
                ps.setString(i, x); i = i + 1
            }
            }
            rs = ps.executeQuery()
            if (rs.next())
                rst = rs.getString(1)
        } catch {
            case ex: Exception => ltLog.error(ex.getMessage); ex.printStackTrace(); null
        } finally {
            MysqlDbUtil.close(rs, ps, conn)
        }
        rst
    }
    
    /**
      * 执行更新或写入
      * 单条语句事物处理
      */
    def executeUpdate(sql: String, v: Seq[Object]): Int = {
        ltLog.debug("【 SQL INFO】 execute sql：[" + sql + "]; parameters: [" + v + "]")
        var rtn = 0
        var conn: Connection = null
        var ps: PreparedStatement = null
        var rs: ResultSet = null
        try {
            conn = MysqlDbUtil.getConn()
            ps = conn.prepareStatement(sql)
            var i: Int = 1
            if (v != null) v.foreach { x => {
                ps.setObject(i, x); i = i + 1
            }
            }
            rtn = ps.executeUpdate()
        } catch {
            case ex: Exception => ltLog.error(ex.getMessage); ex.printStackTrace()
        } finally {
            MysqlDbUtil.close(rs, ps, conn)
        }
        rtn
    }
    
    
    /**
      * 查询分页数据
      * 首页page为 ： 1
      */
    def queryByPage(sql: String, params: Seq[String], page: RequestPage): PageDataResult = {
        ltLog.debug("【 SQL INFO】 execute sql：[" + sql + "]; parameters: [" + params + "]")
        if (sql != null && !sql.isEmpty()) {
            
            var number = 1
            var size = 50
            if (page != null) {
                if (page.pageNumber > 0) number = page.pageNumber
                if (page.pageSize > 0) size = page.pageSize
            }
            
            //拼接查询sql
//            val searchSql = "select * from (select row_.*, rownum rownum_ from (" + sql + " " +
//              ") row_ ) where  rownum_ >" + ((number - 1) * size) +
//              " and rownum_<= " + (number * size) + "";
            val searchSql = sql + " limit " + ((number - 1) * size) + "," + size
            
            //拼接总数sql
            val countSql = "select count(*) count_info_1 from (" + sql + " " + ")";
            
            /*val count = this.singleQuery(countSql, v).toInt
            val list = this.query(searchSql, v)*/
            //执行查询结果
            PageDataResult(this.singleQuery(countSql, params).toInt, this.query(searchSql, params))
        } else {
            PageDataResult(0, List())
        }
    }
    
}

object test extends MysqlService {
    
    def main(args: Array[String]): Unit = {
        val sql = "select * from country "
        val result = this.query(sql,Seq())
        println(result)
    }
}