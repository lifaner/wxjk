package com.xcx.lt.modal

import akka.actor.ActorRef

/**
  * 请求类型封装
 *
  * @author lifan
  */
sealed trait AppRequest

/**
  * @param id 全局唯一标识ID
  * @param param 请求参数体
  * @param sender 请求发送者
  */
case class EventRequest(id: String, param: Map[String, String], sender: Option[ActorRef]) extends AppRequest
/**
  * @param id 全局唯一标识ID
  * @param result 返回结果
  */
case class ExecuteResult[T](id: String, result: T) extends AppRequest