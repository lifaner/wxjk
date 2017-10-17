package com.xcx.lt.action

import akka.actor.Props
import com.xcx.lt.actor.SearchAnonymityMsgActor
import com.xcx.lt.modal.com.egfbank.prm.actor.base.AppQueryResult
import com.xcx.lt.modal.{EventRequest, ExecuteResult}
import com.xcx.lt.utils.LtUtil
import xitrum.ActorAction
import xitrum.annotation.GET

@GET("/anonyMsg/:userId")
class SearchAnonymityMsgAction extends ActorAction {
    override def execute(): Unit = {
        val userId = param("userId")
        val params = Map("userId" -> userId)
        println(userId)
        context.actorOf(Props[SearchAnonymityMsgActor]) ! EventRequest(LtUtil.getUUID(),params,Some(self))
        context.become {
            case data: ExecuteResult[_] => {
                data.result match {
                    case rs: AppQueryResult => {
                        respondJson(rs)
                    }
                    case _ => { respondJson(AppQueryResult(true, "successed", -1, null)) }
                }
            }
            case _ => {
                respondJson(AppQueryResult(false, "fail", 0, null))
            }
        }
    }
}
