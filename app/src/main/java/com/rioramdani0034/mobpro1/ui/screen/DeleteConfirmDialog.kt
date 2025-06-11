package com.rioramdani0034.mobpro1.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rioramdani0034.mobpro1.R
import com.rioramdani0034.mobpro1.model.Art

@Composable
fun DeleteConfirmDialog(
    art: Art,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.hapus_hewan_title))
        },
        text = {
            Text(text = stringResource(R.string.hapus_hewan_body, art.title))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.hapus))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.batal))
            }
        }
    )
}

