package com.xcx.lt.modal

import scala.beans.BeanProperty

/**
  * 后台分页查询结果封装类
  */
case class PageDataResult(@BeanProperty totalCount: Int, @BeanProperty dataList: List[Map[String, String]])
case class RequestPage(@BeanProperty pageNumber: Int, @BeanProperty pageSize: Int)
