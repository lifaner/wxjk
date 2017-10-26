package com.xcx.lt.service.Anonymity

import com.xcx.lt.modal.RequestPage
import com.xcx.lt.service.MysqlService
import com.xcx.lt.utils.LtUtil

object AnonymityMsgService extends MysqlService{
    def saveMsg(userId: String, content: String) = {
        val sql = " insert into lt_message(id ,user_id,content,create_date) value(?,?,?,SYSDATE()) "
        this.executeUpdate(sql, Seq(LtUtil.getUUID(),userId,content))
    }
    
    def findMsgListByUserId(userId: String,index:String,size:String) = {
        val sql = " select * from lt_message t where t.user_id = ? order by t.create_date desc"
        this.queryByPage(sql, Seq(userId),RequestPage(index.toInt,size.toInt))
    }
    
    def searchUserInfoByUserId(userId: String) = {
        val sql = " select * from lt_user_info t where t.sys_id = ? and t.status = 1"
        val seq = Seq(userId)
        this.query(sql,seq)
    }
    
    
    def saveUserinfo(sysId: String, username: String) = {
        val sql =
            """
              |insert into lt_user_info(id,sys_id,username,photo,status,create_date)
              |values(?,?,?,?,?,SYSDATE())
            """.stripMargin
        val seq:Seq[Object] = Seq(LtUtil.getUUID(),sysId,username,null,"1")
        this.executeUpdate(sql,seq)
    }
    
    
    def searchCountry() = {
        val sql = " select * from country "
        this.query(sql,Seq())
    }
}
