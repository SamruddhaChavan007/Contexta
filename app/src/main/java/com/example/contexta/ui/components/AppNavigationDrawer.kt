package com.example.contexta.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.contexta.navigation.model.DrawerItem

@Composable
fun AppNavigationDrawer(
    drawerState: DrawerState,
    drawerItems: List<DrawerItem>,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
                    drawerItems.forEach { item ->
                        Text(
                            text = item.label,
                            modifier = Modifier
                                .clickable { item.onClick() }
                                .padding(12.dp)
                        )
                    }
                }
            }
        }
    ) {
        content()
    }
}
