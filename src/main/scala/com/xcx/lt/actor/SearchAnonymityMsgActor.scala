package com.xcx.lt.actor

import com.xcx.lt.actor.base.BizActorWorker
import com.xcx.lt.modal.com.egfbank.prm.actor.base.AppQueryResult
import com.xcx.lt.modal.{EventRequest, ExecuteResult}
import com.xcx.lt.service.Anonymity.AnonymityMsgService

class SearchAnonymityMsgActor extends BizActorWorker {
    override def execute(request: EventRequest) = {
        val res = AnonymityMsgService.searchCountry()
        Some(ExecuteResult(request.id, AppQueryResult(true, "查询成功！", 1, res)))
    }
}
