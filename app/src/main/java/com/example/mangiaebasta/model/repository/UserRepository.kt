package com.example.mangiaebasta.model.repository

import androidx.core.app.Person
import com.example.mangiaebasta.model.dataClasses.User
import com.example.mangiaebasta.model.dataClasses.UserDetail
import com.example.mangiaebasta.model.dataSource.CommunicationController
import com.example.mangiaebasta.model.dataSource.DBController
import com.example.mangiaebasta.model.dataSource.PreferencesController

class UserRepository (
    private val communicationController : CommunicationController,
    private val dbController: DBController,
    private val preferencesController: PreferencesController
) {
    companion object {
        private val TAG = UserRepository::class.simpleName
    }


    suspend fun isFirstRun() : Boolean {
        return preferencesController.isFirstRun()
    }

    suspend fun isRegistered() : Boolean {
        if  (preferencesController.get(PreferencesController.IS_REGISTERED) != null) {
            return true
        }
        return false
    }

    suspend fun getUserSession() : User {
        val isFirstRun = preferencesController.isFirstRun()
        if (!isFirstRun) {
            val sid = preferencesController.get(PreferencesController.SID)
            val uid = preferencesController.get(PreferencesController.UID)
            if (sid != null && uid != null) {
                return User(sid, uid)
            }
        }

        val user : User = communicationController.registerUser()
        preferencesController.memorizeSessionKeys(user.sid, user.uid)
        return user
    }


    suspend fun getUserData(sid: String ,uid: Int) : UserDetail {
        return communicationController.getUserData(sid,uid)
    }

    suspend fun putUserData(uid: Int, updateData: UserDetail) {
        communicationController.putUserData(uid, updateData)
        preferencesController.set(PreferencesController.IS_REGISTERED, true)
    }

    //NAV STACK

}