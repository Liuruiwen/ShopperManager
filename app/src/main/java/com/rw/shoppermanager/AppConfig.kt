package com.rw.shoppermanager

/**
 * Created by Amuse
 * Date:2020/8/9.
 * Desc:
 */
object AppConfig {
    //79:4B:0B:A9:3C:23:BD:EF:DD:2E:80:2E:E1:06:FF:46:13:23:AB:AF
   private const  val  LOGIN_APP="com.rw.login.LoginApp"
    private const  val  MAP_APP="com.rw.map.MapApp"
//   private const  val  PERSON_CENTER_APP="com.rw.person_center.PersonCenterApp"
    val moduleApps= listOf(LOGIN_APP, MAP_APP)
}