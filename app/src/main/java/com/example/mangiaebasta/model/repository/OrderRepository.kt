package com.example.mangiaebasta.model.repository

import com.example.mangiaebasta.model.dataClasses.APILocation
import com.example.mangiaebasta.model.dataClasses.Order
import com.example.mangiaebasta.model.dataSource.CommunicationController

class OrderRepository(
    private val communicationController: CommunicationController,
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