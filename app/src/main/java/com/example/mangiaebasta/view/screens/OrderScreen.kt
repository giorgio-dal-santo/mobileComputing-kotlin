package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.example.mangiaebasta.view.styles.signUpButtonModifier
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
    onGoToProfile: () -> Unit,
    onGoToOrder: () -> Unit
) {
    val scrollState = rememberScrollState()

    val appState by viewModel.appState.collectAsState()
    val orderState by viewModel.lastOrderState.collectAsState()
    val userState by viewModel.userState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    if (appState.isLoading || userState.user == null && userState.isUserRegistered) {
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

    if (!userState.isUserRegistered) {
        return Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 25.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row {
                Text(
                    text = "Sign up to start ordering",
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Row {
                Text(
                    text = "You need to sign up to place an order. Get started and enjoy delicious meals delivered quickly to your location.",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StyledButton(
                    text = "Go to profile",
                    modifier = signUpButtonModifier,
                    textStyle = buttonTextWhiteStyle,
                    onClick = { onGoToProfile() },
                )
            }
        }
    }

    if (orderState.lastOrder == null || orderState.lastOrderMenu == null) {
        return Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 25.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No order yet",
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StyledButton(
                    text = "Order now",
                    modifier = orderButtonModifier,
                    textStyle = buttonTextWhiteStyle,
                    onClick = { onGoToOrder() },
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(GlobalDimensions.DefaultPadding)
    ) {
        if (orderState.lastOrder!!.status == OrderStatus.ON_DELIVERY) {
            val instant = Instant.parse(orderState.lastOrder?.expectedDeliveryTimestamp)
            val zoneId = ZoneId.of("UTC+1")
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
                .withZone(zoneId)
            val formattedDate = formatter.format(instant)

            Text(
                "Your order will arrive at: $formattedDate",
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            val instant = Instant.parse(orderState.lastOrder?.deliveryTimestamp)
            val zoneId = ZoneId.of("UTC+1")
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
                .withZone(zoneId)
            val formattedDate = formatter.format(instant)

            Text(
                "Your order has been delivered at: $formattedDate",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

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
                    camera, EdgeInsets(100.0, 100.0, 100.0, 100.0),
                    maxZoom, offset, result
                )

                mapView.mapboxMap.easeTo(cameraOptions)
            }
        }
        MenuCard(orderState.lastOrderMenu!!)
    }
}