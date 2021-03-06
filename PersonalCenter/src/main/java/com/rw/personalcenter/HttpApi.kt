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
    const val HTTP_ADD_EMPLOYEES="/s1/index/employees/registered"//注册员工
    const val HTTP_DELETE_EMPLOYEES="/s1/index/deleteEmployees"//删除员工
    const val HTTP_EDIT_USER="/s1/index/updateUserInfo"//编辑用户
    const val HTTP_GET_REQUEST_CARD="/s1/index/getCardList"//获取员工缺勤列表
    const val HTTP_DELETE_REQUEST_CARD="/s1/index/deleteCard"//删除员工补卡申请
    const val HTTP_AGREE_REQUEST_CARD="/s1/index/configCard"//同意员工补卡申请
    const val HTTP_UPLOAD_IMAGE="/s1/index/uploadAvatar"
}