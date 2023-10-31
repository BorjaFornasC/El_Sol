package com.example.elsol

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainReal(navHostController: NavHostController) {
    val snackBarState = remember { SnackbarHostState() }
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {MyBottomAppBar(scope, drawerState)},
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackBarState) }
    ) {
        val items = listOf(
            Icons.Default.Build, Icons.Default.Info,
            Icons.Default.Email
        )
        val selectedItem = remember {
            mutableStateOf(items[0])
        }

        ModalNavigationDrawer(drawerContent = {
            ModalDrawerSheet {
                Image(
                    painter = painterResource(id = R.drawable.erupcionsolar),
                    contentDescription = "Imagen",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(imageVector = item, contentDescription = null) },
                        label = { Text(item.name.substringAfter(".")) },
                        selected = item == selectedItem.value,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem.value = item
                            navHostController.navigate(item.name)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }

        }, drawerState = drawerState, content = {

            Box(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
                SolView(snackbarHostState = snackBarState)
            }
        })
    }
}

data class Sol(
    var nombreSol: String,
    @DrawableRes var photo: Int
)

fun getSol(): List<Sol> {
    return listOf(
        Sol(
            "Corona Solar",
            R.drawable.corona_solar
        ),
        Sol(
            "Erupción Solar",
            R.drawable.erupcionsolar
        ),
        Sol(
            "Espiculas",
            R.drawable.espiculas
        ),
        Sol(
            "Filamentos",
            R.drawable.filamentos
        ),
        Sol(
            "Magnetosfera",
            R.drawable.magnetosfera
        ),
        Sol(
            "Mancha Solar",
            R.drawable.manchasolar
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSol(sol: Sol, snackbarHostState: SnackbarHostState) {
    var imagenMenu by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    Card(modifier =
    Modifier
        .padding(10.dp)
        .fillMaxSize(), elevation = CardDefaults.cardElevation(10.dp),
        onClick = {scope.launch { snackbarHostState.showSnackbar(sol.nombreSol) }}) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = sol.photo),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(200.dp),
                contentScale = ContentScale.Crop
            )

            Divider()

            BottomAppBar(modifier = Modifier.height(55.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()) {

                    Text(text = sol.nombreSol, modifier = Modifier.padding(start = 10.dp))
                    IconButton(onClick = { imagenMenu = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menú")
                    }
                }

                DropdownMenu(expanded = imagenMenu, onDismissRequest = { imagenMenu = false }, offset = DpOffset(0.dp, ((-40).dp))) {
                    DropdownMenuItem(text = { Text(text = "Copiar") }, onClick = { imagenMenu = false },
                        leadingIcon = { Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        })
                    DropdownMenuItem(text = { Text(text = "Eliminar") }, onClick = { imagenMenu = false },
                        leadingIcon = { Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        })
                }
            }
        }
    }
}

@SuppressLint("ResourceType")
@Composable
fun SolView(snackbarHostState: SnackbarHostState) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
        items(getSol()) { sol ->
            ItemSol(sol = sol, snackbarHostState)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomAppBar(scope : CoroutineScope, drawerState: DrawerState){
    var showMenu by remember { mutableStateOf(false) }
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary,
        actions = {
            IconButton(onClick = { scope.launch { drawerState.open()} }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            MyBadge()

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                Modifier.width(150.dp)
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Compartir", color = Color.Black) },
                    onClick = { /*TODO*/ },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = null, tint = Color.Black
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Album", color = Color.Black) },
                    onClick = { /*TODO*/ },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null, tint = Color.Black
                        )
                    }
                )
            }
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBadge() {
    var likes by remember {
        mutableStateOf(0)
    }
    BadgedBox(badge = { Badge { Text(text = likes.toString()) } }, modifier =
    Modifier
        .padding(10.dp)
        .clickable { likes++ }) {
        Icon(imageVector = Icons.Default.Favorite, contentDescription
        = "", tint = Color.White)
    }
}