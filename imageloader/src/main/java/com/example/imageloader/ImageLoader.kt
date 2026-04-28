package com.example.imageloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.imageloader.api.getImageAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ImageLoader(url: String, contentDescription: String) {

    val context = LocalContext.current

    val image = produceState<UIState>(UIState.Loading, url) {
        withContext(Dispatchers.IO) {
            try {
                getImageAPIService(context).downloadImage(url).run {
                    value = if(isSuccessful) {
                        body()!!.byteStream().use { inputStream ->
                            UIState.Success(BitmapFactory.decodeStream(inputStream))
                        }
                    } else UIState.Error(message())
                }
            } catch (e: Exception) {
                value = UIState.Error(e.message ?: "An error occurred")
            }
        }
    }

    Box {
        when(image.value) {
            is UIState.Loading -> {
                CircularProgressIndicator()
            }
            is UIState.Error -> {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = stringResource(R.string.error_content_description)
                )
            }
            is UIState.Success -> {
                Image(
                    bitmap = ((image.value as UIState.Success).result).asImageBitmap(),
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
            }
        }
    }
}

sealed class UIState {
    object Loading: UIState()
    data class Success(val result: Bitmap): UIState()
    data class Error(val message: String?): UIState()
}