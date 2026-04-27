package com.example.redditusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.redditusers.model.UserDetails
import com.example.redditusers.ui.theme.RedditUsersTheme
import com.example.redditusers.ui.utils.UIState
import com.example.redditusers.users.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val usersViewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RedditUsersTheme {
                RedditUsersApp(usersViewModel)
            }
        }
    }
}

@Composable
fun RedditUsersApp(viewModel: UsersViewModel) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding)
        ) {
            UserScreen(viewModel)
        }
    }
}

@Composable
fun UserScreen(viewModel: UsersViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getUsers()
    }

   when(state) {
       is UIState.Loading -> {
           Column(
               modifier = Modifier.fillMaxSize(),
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally
           ) {
               CircularProgressIndicator()
           }
       }
       is UIState.Success -> {
            UsersList(((state as UIState.Success).result)) { userId, isSelected ->
                viewModel.toggleFavourite(userId, isSelected)
            }
       }
       is UIState.Error -> {

       }
   }
}

@Composable
fun UsersList(users: List<UserDetails>, onSelected: (userId: String, isSelected: Boolean) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(users) { user ->
            UserItem(user, onSelected)
        }
    }
}

@Composable
fun UserItem(user: UserDetails, onSelected: (userId: String, isSelected: Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { onSelected(user.userId, user.isFollowed) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = user.displayName)
                Text(text = "${user.reputation}")
            }
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Favourite",
                tint = if (user.isFollowed) Color.Yellow else Color.Gray
            )
        }
    }
}