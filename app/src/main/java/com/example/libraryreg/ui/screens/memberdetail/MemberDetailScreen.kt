package com.example.libraryreg.ui.screens.memberdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.libraryreg.data.MemberViewModel
import com.example.libraryreg.ui.components.DeleteMemberDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailScreen(
    viewModel: MemberViewModel,
    memberId: String,
    onEdit: () -> Unit,
    onBack: () -> Unit
) {
    val members by viewModel.members.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val member = members.find { it.id == memberId } ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Anggota") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { padding ->
        Card(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DetailRow("Nama Lengkap", member.name)
                DetailRow("NIM / NIP", member.memberNumber)
                DetailRow("Program Studi", member.studyProgram)
                DetailRow("Alamat", member.address)
                DetailRow("Nomor Telepon", member.phone)
                DetailRow("Email", member.email)
                DetailRow("Jenis Keanggotaan", member.membershipType)
            }
        }
    }

    if (showDeleteDialog) {
        DeleteMemberDialog(
            memberName = member.name,
            onConfirm = {
                viewModel.deleteMember(member.id)
                onBack()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Text(text = label, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
    Text(text = value, style = MaterialTheme.typography.titleLarge)
    Spacer(modifier = Modifier.height(12.dp))
}
