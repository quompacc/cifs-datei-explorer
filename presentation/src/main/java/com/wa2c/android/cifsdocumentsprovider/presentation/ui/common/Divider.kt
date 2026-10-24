package com.wa2c.android.cifsdocumentsprovider.presentation.ui.common

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun DividerWide() = HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

@Composable
fun DividerNormal() = HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

@Composable
fun DividerThin() = HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
