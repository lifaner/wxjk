package com.xcx.lt.modal

package com.egfbank.prm.actor.base

/**
  * 返回结果封装
  * @author huxp
  */
sealed trait AppResponse


/**
  * 查询返回值封装类
  * 返回：isSucceeded:(TRUE操作成功,FALSE操作失败)；msg:返回消息；total:总记录数；data:数据
  */
case class AppQueryResult(isSucceeded: Boolean, msg: String, total: Int, data: Any)

/**
  * 更新操作返回值封装类
  * 返回：isSucceeded:(TRUE操作成功,FALSE操作失败)；msg:处理信息
  */
case class UpdateResult(isSucceeded: Boolean, msg: String)


/**
  * 登录校验结果封装
  */
case class LoginCheckResponse(flag: String, msg: String) extends AppResponse
/**
  * 登录结果
  */
case class LoginResultResponse(flag: String, msg: String, userId: String, username: String) extends AppResponse
/**
  * 异常结果封装
  */
case class ExceptionResponse(status: String, messages: String, errorCode: String) extends AppResponse