package com.mad.hippo.codes.messaging.presentation.auth.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import com.mad.hippo.codes.messaging.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mad.hippo.codes.messaging.presentation.auth.AuthViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.compose.getViewModel

@Composable
@InternalCoroutinesApi
fun AuthContent(
    viewModel: AuthViewModel = getViewModel(),
    onGoogleLoginClicked : () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 48.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column() {

            Button(
                onClick = {
                    viewModel.signIn()
                }
            ) {
                Text(
                    text = "SIGN_IN",
                    fontSize = 18.sp
                )
            }
            OutlinedButton(
                border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                onClick = {
                    onGoogleLoginClicked()
                },
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            Icon(
                                tint = Color.Unspecified,
                                painter = painterResource(id = R.drawable.ic_launcher_background),
                                contentDescription = null,
                            )
                            Text(
                                style = MaterialTheme.typography.button,
                                color = MaterialTheme.colors.onSurface,
                                text = "Google"
                            )
                            Icon(
                                tint = Color.Transparent,
                                imageVector = Icons.Default.MailOutline,
                                contentDescription = null,
                            )
                        }
                    )
                }
            )
        }

    }
}
