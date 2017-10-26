package com.xcx.lt.action

import akka.actor.Props
import com.xcx.lt.actor.{SaveMsgActor, SaveUserInfoActor, SearchMsgListActor}
import com.xcx.lt.modal.com.egfbank.prm.actor.base.AppQueryResult
import com.xcx.lt.modal.{EventRequest, ExecuteResult}
import com.xcx.lt.utils.LtUtil
import xitrum.annotation.{GET, POST}
import xitrum.{Action, SkipCsrfCheck}


@GET("/msg/:wx_id/:index/:size")
class SearchMsgAction extends BaseAppAction {
    
    override def execute(): Unit = {
        val userId = param("wx_id")
        val index = param("index")
        val size = param("size")
        val params = Map("userId" -> userId, "index" -> index, "size" -> size)
        context.actorOf(Props[SearchMsgListActor]) ! EventRequest(LtUtil.getUUID(), params, Some(self))
        context.become {
            case data: ExecuteResult[_] => {
                data.result match {
                    case rs: AppQueryResult => {
                        respondJson(rs)
                    }
                    case _ => {
                        respondJson(AppQueryResult(true, "succeed", 0, null))
                    }
                }
            }
            case _ => {
                respondJson(AppQueryResult(false, "fail", -1, null))
            }
        }
    }
}

@POST("/userInfo")
class SaveUserAction extends BaseAppAction with SkipCsrfCheck {
    
    override def execute(): Unit = {
        val userId = param("wx_id")
        val username = param("wx_name")
        val params = Map("userId" -> userId, "username" -> username)
        context.actorOf(Props[SaveUserInfoActor]) ! EventRequest(LtUtil.getUUID(), params, Some(self))
        context.become {
            case data: ExecuteResult[_] => {
                data.result match {
                    case rs: AppQueryResult => {
                        respondJson(rs)
                    }
                    case _ => {
                        respondJson(AppQueryResult(true, "succeed", 0, null))
                    }
                }
            }
            case _ => {
                respondJson(AppQueryResult(false, "fail", -1, null))
            }
        }
    }
}

@POST("/msg")
class SaveMsgAction extends BaseAppAction with SkipCsrfCheck {
    
    override def execute(): Unit = {
        //        val targetUserId = params("wx_id")
        //        val content = param("wx_msg")
        //        val params = Map("userId" -> targetUserId,"content"->content)
        context.actorOf(Props[SaveMsgActor]) ! EventRequest(LtUtil.getUUID(), params, Some(self))
        context.become {
            case data: ExecuteResult[_] => {
                data.result match {
                    case rs: AppQueryResult => {
                        respondJson(rs)
                    }
                    case _ => {
                        respondJson(AppQueryResult(true, "succeed", 0, null))
                    }
                }
            }
            case _ => {
                respondJson(AppQueryResult(false, "fail", -1, null))
            }
        }
    }
}

@GET("around_filter")
class MyAction extends Action {
    aroundFilter { action =>
        val begin = System.currentTimeMillis()
        log.info(s"The begin is $begin [ms]")
        action()
        val end = System.currentTimeMillis()
        log.info(s"The end is $end [ms]")
        val dt = end - begin
        log.info(s"The action took $dt [ms]")
    }
    
    def execute() {
        //Thread.sleep(1000)
        log.info(s"action?")
        respondText("Around filter should have been run, please check the log")
    }
}
