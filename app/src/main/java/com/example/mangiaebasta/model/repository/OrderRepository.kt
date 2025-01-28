package com.example.mangiaebasta.model.repository

import com.example.mangiaebasta.model.dataClasses.APILocation
import com.example.mangiaebasta.model.dataClasses.Order
import com.example.mangiaebasta.model.dataSource.CommunicationController
import com.example.mangiaebasta.model.dataSource.DBController
import com.example.mangiaebasta.model.dataSource.PreferencesController

class OrderRepository(
    private val communicationController: CommunicationController,
    private val dbController: DBController,
    private val preferencesController: PreferencesController
) {
    companion object {
        private val TAG = OrderRepository::class.simpleName
    }


    suspend fun newOrder(sid: String, deliveryLocation: APILocation, mid: Int): Order {
        return communicationController.buyMenu(sid, deliveryLocation, mid)
    }

    suspend fun getOrderDetail(oid: Int, sid: String): Order {
        return communicationController.getOrderDetail(oid, sid)
    }

}