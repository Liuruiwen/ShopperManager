package com.rw.login

/**
 * Created by Amuse
 * Date:2021/4/14.
 * Desc:
 */
object HttpApi {
   const val HTTP_LOGIN_URL="/s1/index/login"//登录
   const val HTTP_GET_VERIFICATION_CODE="/s1/index/Verification"//获取验证码
   const val HTTP_VERIFICATION_CODE_LOGIN="/s1/index/VerificationCodeLogin"//验证码登录
   const val HTTP_ACCOUNT_REGISTERED="/s1/index/registered"//账号注册
   const val HTTP_UPDATE_PASSWORD="/s1/index/updatePassword"//修改密码
}