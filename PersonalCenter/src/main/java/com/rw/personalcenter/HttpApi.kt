package com.rw.personalcenter

/**
 * Created by Amuse
 * Date:2021/8/26.
 * Desc:
 */
object HttpApi {
    const val ERROR_TOKEN_CODE=1009
    const val HTTP_GET_USER_INFO="/s1/index/getUserInfo"//获取用户中心数据
    const val HTTP_LOGIN_OUT="/s1/index/loginOut"//退出登录
    const val HTTP_EMPLOYEES_LIST="/s1/index/selectListEmployees"//获取员工列表
    const val HTTP_EMPLOYEES_LEVEL_LIST="/s1/index/selectEmployeeLevel"//员工级别列表
}