package com.xcx.lt.action

import com.xcx.lt.modal.com.egfbank.prm.actor.base.ExceptionResponse
import xitrum.ActorAction

import scala.util.parsing.json.JSON

private[action] trait BaseAppAction extends ActorAction{
    
    var params: Map[String, String] = null
    // var params: scala.collection.mutable.Map[String, String] = null
    // 验证是否登录
    private[action] def checkLogin {
        // todo Nothing
    }
    // 验证是否具有该功能权限
    private[action] def checkAuthorization {
        // todo Nothing
    }
    /**
      *  请求参数是否合法
      */
    private[action] def checkParam {
        // 请求参数空值检测
        /* if (null == this.requestContentString || this.requestContentString.length() < 1) {
           respondJson(ExceptionResponse("-1", "参数无效，服务器拒绝处理。", "parameter_error"))
         }*/
    }
    
    /**
      * 获取请求参数
      */
    private[action] def getParam {
        if (null != this.requestContentString && this.requestContentString.length() > 0) {
            // 获取参数
            JSON.parseFull(this.requestContentString).get match {
                case params: Map[_, _] => {
                    this.params = params.asInstanceOf[Map[String, String]]
                }
            }
        }
    }
    
    /**
      * 前置过滤器
      */
    beforeFilter {
        log.debug("进入Action前置过滤器。")
//        checkLogin
//        checkAuthorization
//        checkParam
        getParam
        log.debug("Action前置过滤器结束。")
    }
    
    /**
      * 前后过滤器
      */
    aroundFilter { action =>
        try {
            action()
        } catch {
            case ex: xitrum.exception.MissingParam => {
                val msg = ex.getMessage
                respondJson(ExceptionResponse("-1", "exception", "parameter $msg not exist"))
            }
            case ex: java.sql.SQLException => {
                val msg = ex.getMessage
                log.error(msg)
                respondJson(ExceptionResponse("-1", "SQLException", "$msg"))
            }
            case ex: java.util.concurrent.TimeoutException => {
                val msg = ex.getMessage
                log.error(msg)
                respondJson(ExceptionResponse("-1", "exception", "request timeout $msg"))
            }
            case ex: Exception => {
                val msg = ex.getMessage
                log.error("未知错误，请详查："+msg)
                log.error(msg)
                respondJson(ExceptionResponse("-1", "exception", msg))
            }
        }
    }
    
    /**
      * 获取当前用户的user_id
      */
    protected def getCurrentUserId(): String = {
        var userId = ""
        val loginUser: Option[Any] = session.get("LoginUser")
        if (loginUser.nonEmpty && loginUser.get.isInstanceOf[String]) {
            var value: Option[String] = loginUser.asInstanceOf[Option[String]]
            value match {
                case Some(value) => userId = value.toString()
                case None        =>
            }
        }
        userId
    }
}
