package com.xcx.lt.actor.base

import akka.actor.Actor
import com.xcx.lt.modal.{EventRequest, ExecuteResult}

/**
  * @author cyq
  *         定义统一抽象方法
  */
trait BizActorWorker extends Actor {
    override def receive = {
        case request: EventRequest => {
            try {
                val result = execute(request)
                result.map { rt =>
                    request.sender.map { x =>
                        x ! rt
                    }
                }
            } catch {
                case t: Exception => {
                    t.printStackTrace()
                    request.sender.map { x => x ! "服务器处理异常!" }
                    // t.getStackTrace.map { x => egflog.error(x.toString()) }
                }
            }
            //killSelf
        }
    }
    
    def execute(event: EventRequest): Option[ExecuteResult[_]]
    
    def killSelf = context stop self
    
}