package com.xcx.lt.action

import akka.actor.Props
import com.xcx.lt.actor.{SaveMsgActor, SaveUserInfoActor, SearchAnonymityMsgActor, SearchMsgListActor}
import com.xcx.lt.modal.com.egfbank.prm.actor.base.{AppQueryResult, LoginResultResponse}
import com.xcx.lt.modal.{EventRequest, ExecuteResult}
import com.xcx.lt.utils.LtUtil
import xitrum.{Action, ActorAction, SkipCsrfCheck}
import xitrum.annotation.{GET, POST}

//@GET("/anonyMsg/:userId")
//class SearchAnonymityMsgAction extends ActorAction {
//    override def execute(): Unit = {
//        val userId = param("userId")
//        val params = Map("userId" -> userId)
//        println(userId)
//        context.actorOf(Props[SearchAnonymityMsgActor]) ! EventRequest(LtUtil.getUUID(),params,Some(self))
//        context.become {
//            case data: ExecuteResult[_] => {
//                data.result match {
//                    case rs: AppQueryResult => {
//                        respondJson(rs)
//                    }
//                    case _ => { respondJson(AppQueryResult(true, "successed", -1, null)) }
//                }
//            }
//            case _ => {
//                respondJson(AppQueryResult(false, "fail", 0, null))
//            }
//        }
//    }
//}

@POST("/anonyMsg")
class SearchMsgAction extends BaseAppAction with SkipCsrfCheck {
    
    override def execute(): Unit = {
        val userId = param("sysId")
        val username = param("username")
        val index = param("index")
        val size = param("size")
        val params = Map("userId" -> userId, "username" -> username, "index" -> index, "size" -> size)
        context.actorOf(Props[SearchMsgListActor]) ! EventRequest(LtUtil.getUUID(), params, Some(self))
        context.become {
            case data: ExecuteResult[_] => {
                data.result match {
                    case rs: AppQueryResult => {
                        respondJson(rs)
                    }
                    case _ => {
                        respondJson(AppQueryResult(true, "successed", -1, null))
                    }
                }
            }
            case _ => {
                respondJson(AppQueryResult(false, "fail", 0, null))
            }
        }
    }
}

@POST("/userInfo")
class SaveUserAction extends BaseAppAction with SkipCsrfCheck {
    
    override def execute(): Unit = {
        val userId = param("sysId")
        val username = param("username")
        val params = Map("userId" -> userId, "username" -> username)
        context.actorOf(Props[SaveUserInfoActor]) ! EventRequest(LtUtil.getUUID(), params, Some(self))
        context.become {
            case data: ExecuteResult[_] => {
                data.result match {
                    case rs: AppQueryResult => {
                        respondJson(rs)
                    }
                    case _ => {
                        respondJson(AppQueryResult(true, "successed", -1, null))
                    }
                }
            }
            case _ => {
                respondJson(AppQueryResult(false, "fail", 0, null))
            }
        }
    }
}

@POST("/msg")
class SaveMsgAction extends BaseAppAction with SkipCsrfCheck {
    
    override def execute(): Unit = {
        val targetUserId = param("targetUserId")
        val content = param("content")
        val params = Map("userId" -> targetUserId,"content"->content)
        context.actorOf(Props[SaveMsgActor]) ! EventRequest(LtUtil.getUUID(), params, Some(self))
        context.become {
            case data: ExecuteResult[_] => {
                data.result match {
                    case rs: AppQueryResult => {
                        respondJson(rs)
                    }
                    case _ => {
                        respondJson(AppQueryResult(true, "successed", -1, null))
                    }
                }
            }
            case _ => {
                respondJson(AppQueryResult(false, "fail", 0, null))
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
