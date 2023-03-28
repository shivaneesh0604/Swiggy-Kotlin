package library.Users

import library.enums.Role

sealed class User(internal val name : String, internal val userID:String, private val role: Role) {

    fun getRole():Role{
        return role
    }
}