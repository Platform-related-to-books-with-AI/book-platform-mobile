package com.pwr.bookPlatform.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwr.bookPlatform.R
import com.pwr.bookPlatform.data.utils.DateUtils
import com.pwr.bookPlatform.ui.viewModels.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountView(
    modifier: Modifier = Modifier,
    accountViewModel: AccountViewModel,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }
    accountViewModel.loadUserData()
    val snackbarHostState = remember { SnackbarHostState() }
    var showEditDialog by remember { mutableStateOf(false) }

    accountViewModel.snackbarMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            accountViewModel.clearSnackbarMessage()
        }
    }

    if (showEditDialog) {
        EditAccountDialog(
            accountViewModel = accountViewModel,
            onDismiss = { showEditDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.account_management)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.global_back)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            accountViewModel.user?.let { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.account_info),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Divider(thickness = 1.dp)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 16.dp)
                            )
                            Text(
                                text = user.email,
                                fontSize = 20.sp
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 16.dp)
                            )
                            Text(
                                text = user.nickname,
                                fontSize = 20.sp
                            )
                        }

                        Divider(thickness = 1.dp)

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                shape = RoundedCornerShape(50),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (user.active) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                                )
                            ) {
                                Text(
                                    text = stringResource(
                                        if (user.active) R.string.account_active else R.string.account_inactive
                                    ),
                                    fontSize = 16.sp,
                                    color = if (user.active) Color(0xFF4CAF50) else Color(0xFFE57373),
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.account_created, DateUtils.formatDateTime(user.createdAt)),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            Text(
                                text = stringResource(R.string.account_updated, DateUtils.formatDateTime(user.updatedAt)),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = { showEditDialog = true },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(vertical = 16.dp)
                                .height(56.dp)
                                .align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                stringResource(R.string.account_edit_account),
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditAccountDialog(
    accountViewModel: AccountViewModel,
    onDismiss: () -> Unit
) {
    val user = accountViewModel.user

    var emailSectionActive by remember { mutableStateOf(false) }
    var nicknameSectionActive by remember { mutableStateOf(false) }
    var passwordSectionActive by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf(user?.email ?: "") }
    var nickname by remember { mutableStateOf(user?.nickname ?: "") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.account_edit_account)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SectionHeader(
                    title = stringResource(R.string.account_edit_email),
                    isActive = emailSectionActive,
                    onClick = { emailSectionActive = !emailSectionActive }
                )

                if (emailSectionActive) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(R.string.account_email)) },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                } else {
                    Text(
                        text = "${stringResource(R.string.account_current_email)}: ${user?.email}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                SectionHeader(
                    title = stringResource(R.string.account_edit_nickname),
                    isActive = nicknameSectionActive,
                    onClick = { nicknameSectionActive = !nicknameSectionActive }
                )

                if (nicknameSectionActive) {
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        label = { Text(stringResource(R.string.account_nickname)) },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        supportingText = {
                            Text(stringResource(R.string.account_nickname_requirements))
                        }
                    )
                } else {
                    Text(
                        text = "${stringResource(R.string.account_current_nickname)}: ${user?.nickname}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                SectionHeader(
                    title = stringResource(R.string.account_change_password),
                    isActive = passwordSectionActive,
                    onClick = { passwordSectionActive = !passwordSectionActive }
                )

                if (passwordSectionActive) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.account_new_password)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Text(
                                    text = if (passwordVisible)
                                        stringResource(R.string.account_hide_password)
                                    else
                                        stringResource(R.string.account_show_password),
                                    fontSize = 12.sp
                                )
                            }
                        },
                        supportingText = {
                            Text(stringResource(R.string.account_password_requirements))
                        }
                    )
                } else {
                    Text(
                        text = stringResource(R.string.account_password_unchanged),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedEmail = if (emailSectionActive) email else ""
                    val updatedNickname = if (nicknameSectionActive) nickname else ""
                    val updatedPassword = if (passwordSectionActive) password else ""

                    accountViewModel.saveProfileChanges(updatedEmail, updatedNickname, updatedPassword)
                    onDismiss()
                },
                enabled = (emailSectionActive || nicknameSectionActive || passwordSectionActive)
            ) {
                Text(stringResource(R.string.account_save_changes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.global_cancel))
            }
        }
    )
}

@Composable
private fun SectionHeader(
    title: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = if (isActive) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        tonalElevation = if (isActive) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurface
            )

            Icon(
                imageVector = if (isActive)
                    Icons.Filled.KeyboardArrowDown
                else
                    Icons.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer
                      else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

