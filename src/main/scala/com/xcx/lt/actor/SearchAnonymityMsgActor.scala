package com.xcx.lt.actor

import com.xcx.lt.actor.base.BizActorWorker
import com.xcx.lt.modal.com.egfbank.prm.actor.base.AppQueryResult
import com.xcx.lt.modal.{EventRequest, ExecuteResult}
import com.xcx.lt.service.Anonymity.AnonymityMsgService

class SearchMsgListActor extends BizActorWorker {
    override def execute(request: EventRequest) = {
        val userId = request.param("userId")
        val index = request.param("index")
        val size = request.param("size")
        var list = AnonymityMsgService.searchUserInfoByUserId(userId)
        if (list.size == 0) {
            Some(ExecuteResult(request.id, AppQueryResult(true, "查询成功！", 0, List())))
        } else {
            val res = AnonymityMsgService.findMsgListByUserId(list(0)("id"), index, size)
            Some(ExecuteResult(request.id, AppQueryResult(true, "查询成功！", res.totalCount, res.dataList)))
        }
    }
}

class SaveUserInfoActor extends BizActorWorker {
    override def execute(request: EventRequest) = {
        //id,sys_id,username,status,create_date
        val sys_id = request.param("userId")
        val username = request.param("username")
        val list = AnonymityMsgService.searchUserInfoByUserId(sys_id)
        var res = 0
        if (list.size == 0) {
            Some(ExecuteResult(request.id, AppQueryResult(true, "保存成功！", res, res)))
        } else {
            res = AnonymityMsgService.saveUserinfo(sys_id, username)
            Some(ExecuteResult(request.id, AppQueryResult(true, "保存成功！", res, res)))
        }
    }
}

class SaveMsgActor extends BizActorWorker {
    override def execute(request: EventRequest) = {
        val userId = request.param("wx_id")
        val content = request.param("content")
        val res = AnonymityMsgService.saveMsg(userId, content)
        Some(ExecuteResult(request.id, AppQueryResult(true, "保存成功！", 1, res)))
    }
}
