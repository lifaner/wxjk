package com.xcx.lt.actor

import akka.actor.Props
import com.xcx.lt.actor.base.BizActorWorker
import com.xcx.lt.modal.com.egfbank.prm.actor.base.{AppQueryResult, LoginResultResponse}
import com.xcx.lt.modal.{EventRequest, ExecuteResult}
import com.xcx.lt.service.Anonymity.AnonymityMsgService
import com.xcx.lt.utils.LtUtil
import akka.pattern.ask
import scala.concurrent.duration._

import scala.concurrent.Await

class SearchAnonymityMsgActor extends BizActorWorker {
    override def execute(request: EventRequest) = {
        val res = AnonymityMsgService.searchCountry()
        Some(ExecuteResult(request.id, AppQueryResult(true, "查询成功！", 1, res)))
    }
}

class SearchMsgListActor extends BizActorWorker {
    override def execute(request: EventRequest) = {
        val userId = request.param("userId")
        val username = request.param("username")
        val index = request.param("index")
        val size = request.param("size")
        var list = AnonymityMsgService.searchUserInfoByUserId(userId)
        if (list.size == 0) {
            AnonymityMsgService.saveAnonymitMsg(userId, username)
            list = AnonymityMsgService.searchUserInfoByUserId(userId)
        }
        val msgList = AnonymityMsgService.findMsgListByUserId(list(0)("id"), index, size)
        Some(ExecuteResult(request.id, AppQueryResult(true, "保存成功！", 1, msgList)))
    }
}

class SaveUserInfoActor extends BizActorWorker {
    override def execute(request: EventRequest) = {
        //id,sys_id,username,status,create_date
        val sys_id = request.param("sys_id")
        val username = request.param("username")
        val res = AnonymityMsgService.saveAnonymitMsg(sys_id, username)
        Some(ExecuteResult(request.id, AppQueryResult(true, "保存成功！", 1, res)))
    }
}

class SaveMsgActor extends BizActorWorker {
    override def execute(request: EventRequest) = {
        val userId = request.param("userId")
        val content = request.param("content")
        val res = AnonymityMsgService.saveMsg(userId, content)
        Some(ExecuteResult(request.id, AppQueryResult(true, "保存成功！", 1, res)))
    }
}
