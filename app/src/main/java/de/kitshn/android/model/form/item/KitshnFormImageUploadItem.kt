package de.kitshn.android.model.form.item

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import de.kitshn.android.R
import de.kitshn.android.ui.dialog.external.PhotoPickerDialog
import de.kitshn.android.ui.modifier.loadingPlaceHolder
import de.kitshn.android.ui.state.translateState
import de.kitshn.android.ui.theme.Typography

class KitshnFormImageUploadItem(
    val currentImage: @Composable () -> ImageRequest?,

    val value: () -> Uri?,
    val onValueChange: (uri: Uri?) -> Unit,

    val label: String
) : KitshnFormBaseItem() {

    @Composable
    override fun Render() {
        var imageLoadingState by remember {
            mutableStateOf<AsyncImagePainter.State>(
                AsyncImagePainter.State.Loading(null)
            )
        }

        var showPhotoPicker by remember { mutableStateOf(false) }
        PhotoPickerDialog(
            shown = showPhotoPicker,
            onSelect = {
                onValueChange(it)
            }
        ) { showPhotoPicker = false }

        Box {
            OutlinedCard(
                Modifier
                    .height(170.dp)
                    .clickable {
                        showPhotoPicker = true
                    }
            ) {
                if(currentImage() != null || value() != null) {
                    if(value() != null) {
                        AsyncImage(
                            model = value(),
                            onState = {
                                imageLoadingState = it
                            },
                            contentDescription = label,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .loadingPlaceHolder(imageLoadingState.translateState())
                        )
                    } else {
                        AsyncImage(
                            model = currentImage(),
                            onState = {
                                imageLoadingState = it
                            },
                            contentDescription = label,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .loadingPlaceHolder(imageLoadingState.translateState())
                        )
                    }
                } else {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .width(64.dp)
                                    .height(64.dp),
                                imageVector = Icons.Rounded.Image,
                                contentDescription = label,
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(
                                Modifier.padding(start = 8.dp, end = 8.dp)
                            )

                            Column {
                                Text(
                                    text = label,
                                    style = Typography.titleMedium
                                )

                                Text(
                                    text = stringResource(R.string.action_click_to_upload_image),
                                    style = Typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }

            if(currentImage() != null || value() != null) Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 8.dp, y = 8.dp)
            ) {
                if(value() != null) Box(
                    Modifier.size(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SmallFloatingActionButton(
                        onClick = { onValueChange(null) },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ) {
                        Icon(Icons.Rounded.Restore, stringResource(R.string.action_reset))
                    }
                }

                FloatingActionButton(
                    onClick = {
                        showPhotoPicker = true
                    }
                ) {
                    Icon(Icons.Rounded.Upload, stringResource(R.string.action_upload))
                }
            }
        }
    }

    override fun submit(): Boolean {
        return true
    }

}