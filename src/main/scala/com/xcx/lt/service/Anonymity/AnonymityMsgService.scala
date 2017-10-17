package com.xcx.lt.service.Anonymity

import com.xcx.lt.service.MysqlService

object AnonymityMsgService extends MysqlService{
    
    def searchCountry() = {
        val sql = " select * from country "
        this.query(sql,Seq())
    }
}
