package com.example.mangiaebasta.model.repository

import android.util.Log
import com.example.mangiaebasta.model.dataClasses.Menu
import com.example.mangiaebasta.model.dataClasses.MenuDetails
import com.example.mangiaebasta.model.dataClasses.MenuImage
import com.example.mangiaebasta.model.dataClasses.MenuImageWithVersion
import com.example.mangiaebasta.model.dataSource.CommunicationController
import com.example.mangiaebasta.model.dataSource.DBController
import com.example.mangiaebasta.model.dataSource.PreferencesController

class MenuRepository (
    private val communicationController : CommunicationController,
    private val dbController: DBController,
    private val preferencesController: PreferencesController
) {
    companion object {
        private val TAG = MenuRepository::class.simpleName
    }

    suspend fun getNearbyMenus(lat : Double, lng : Double, sid : String) : List<Menu> {
        return communicationController.getNearbyMenus(lat, lng ,sid)
    }

    suspend fun getMenuImage(mid: Int, sid: String, imageVersion: Int): MenuImage {
        val imageInStore = dbController.dao.getMenuImageByVersion(mid, imageVersion)
        if (imageInStore != null) {
            Log.d(TAG, "Image found in store")
            return MenuImage(imageInStore.image)
        }

        val imageFromServer = communicationController.getMenuImage(mid, sid)
        if (imageFromServer.base64.startsWith("data:image/jpeg;base64,")) {
            imageFromServer.base64 = imageFromServer.base64.substring(23)
        }

        dbController.dao.insertMenuImage(
            MenuImageWithVersion(mid, imageVersion, imageFromServer.base64)
        )

        Log.d(TAG, "Image found in server and inserted in store")
        return imageFromServer
    }

    suspend fun getMenuDetail(mid: Int, lat: Double, lng: Double, sid: String): MenuDetails {
        return communicationController.getMenuDetail(mid,lat,lng,sid)
    }


}