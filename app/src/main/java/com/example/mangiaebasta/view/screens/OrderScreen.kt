package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.mangiaebasta.R
import com.example.mangiaebasta.model.dataClasses.OrderStatus
import com.example.mangiaebasta.view.styles.GlobalDimensions
import com.example.mangiaebasta.view.styles.buttonTextWhiteStyle
import com.example.mangiaebasta.view.styles.orderButtonModifier
import com.example.mangiaebasta.view.utils.Header
import com.example.mangiaebasta.view.utils.button.StyledButton
import com.example.mangiaebasta.view.utils.cards.MenuCard
import com.example.mangiaebasta.viewmodel.MainViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.animation.easeTo
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun OrderScreen(
    viewModel: MainViewModel,
    onOrderNowCLick: () -> Unit
) {

    val scrollState = rememberScrollState()

    val appState by viewModel.appState.collectAsState()
    val orderState by viewModel.lastOrderState.collectAsState()
    val userState by viewModel.userState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    if (appState.isLoading || userState.user == null) {
        return Column {
            Text("Loading...", style = MaterialTheme.typography.titleSmall)
        }
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (true) {
                viewModel.fetchLastOrderDetail()
                kotlinx.coroutines.delay(5000)
            }
        }
    }

    if (orderState.lastOrder == null || orderState.lastOrderMenu == null) {
        return Column {
            Header("Order")
            Text("No order yet", style = MaterialTheme.typography.titleSmall)
            StyledButton(
                text = "Order Now",
                modifier = orderButtonModifier,
                textStyle = buttonTextWhiteStyle,
                onClick = { onOrderNowCLick() },
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Header("Order")

        if (orderState.lastOrder!!.status == OrderStatus.ON_DELIVERY) {
            val instant = Instant.parse(orderState.lastOrder?.expectedDeliveryTimestamp)
            val zoneId = ZoneId.of("UTC+1")
            val formatter = DateTimeFormatter.ofPattern("HH:mm, dd MMMM yyyy")
                .withZone(zoneId)
            val formattedDate = formatter.format(instant)

            Text(
                "your order will arrive at: $formattedDate",
                style = MaterialTheme.typography.titleSmall
            )
        } else {
            val instant = Instant.parse(orderState.lastOrder?.deliveryTimestamp)
            val zoneId = ZoneId.of("UTC+1")
            val formatter = DateTimeFormatter.ofPattern("HH:mm, dd MMMM yyyy")
                .withZone(zoneId)
            val formattedDate = formatter.format(instant)

            Text(
                "Your order has been delivered at: $formattedDate",
                style = MaterialTheme.typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

        // mappa
        val deliveryLocation = orderState.lastOrder!!.deliveryLocation
        val menuLocation = orderState.lastOrderMenu!!.menuDetails.location
        val droneLocation = orderState.lastOrder!!.currentPosition

        MapboxMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        ) {
            val menuLocationMarker = rememberIconImage(
                key = R.drawable.menu,
                painter = painterResource(R.drawable.menu)
            )
            if (orderState.lastOrder!!.status == OrderStatus.ON_DELIVERY) {
                PointAnnotation(point = Point.fromLngLat(menuLocation.lng, menuLocation.lat)) {
                    iconImage = menuLocationMarker
                    iconSize = 0.25
                }
            }
            val deliveryLocationMarker = rememberIconImage(
                key = R.drawable.user,
                painter = painterResource(R.drawable.user)
            )
            PointAnnotation(point = Point.fromLngLat(deliveryLocation.lng, deliveryLocation.lat)) {
                iconImage = deliveryLocationMarker
                iconSize = 0.25
            }
            val droneMarker = rememberIconImage(
                key = R.drawable.drone,
                painter = painterResource(R.drawable.drone)
            )

            if (orderState.lastOrder!!.status == OrderStatus.ON_DELIVERY) {
                PointAnnotation(point = Point.fromLngLat(droneLocation.lng, droneLocation.lat)) {
                    iconImage = droneMarker
                    iconSize = 0.25
                }
            }

            MapEffect(orderState.lastOrder) { mapView ->

                val points = listOf(
                    Point.fromLngLat(droneLocation.lng, droneLocation.lat),
                    Point.fromLngLat(menuLocation.lng, menuLocation.lat),
                    Point.fromLngLat(deliveryLocation.lng, deliveryLocation.lat)
                )

                val cameraOptions = mapView.mapboxMap.cameraForCoordinates(
                    points,
                    EdgeInsets(100.0, 100.0, 100.0, 100.0), // Padding
                    0.0, // Bearing (0 significa nessuna rotazione)
                    0.0  // Pitch (0 significa vista dall'alto)
                )

                // Animiamo la transizione della camera
                mapView.mapboxMap.easeTo(cameraOptions)
            }
        }

        MenuCard(orderState.lastOrderMenu!!)
    }

}