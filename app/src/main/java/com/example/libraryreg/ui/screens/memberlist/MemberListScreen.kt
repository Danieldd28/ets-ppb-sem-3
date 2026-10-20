package com.example.libraryreg.ui.screens.memberlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.libraryreg.data.Member
import com.example.libraryreg.data.MemberViewModel
import com.example.libraryreg.ui.components.DeleteMemberDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberListScreen(
    viewModel: MemberViewModel,
    onRegisterMember: () -> Unit,
    onOpenMember: (String) -> Unit,
    onEditMember: (String) -> Unit
) {
    val members by viewModel.filteredMembers.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    var memberToDelete by remember { mutableStateOf<Member?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("LibraryReg") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onRegisterMember) {
                Icon(Icons.Filled.Add, contentDescription = "Daftar Anggota")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Cari nama atau nomor induk...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Hapus pencarian")
                        }
                    }
                },
                singleLine = true
            )

            Text(
                text = "Jumlah anggota: ${members.size}",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodyLarge
            )

            if (members.isEmpty()) {
                EmptyState(isSearching = searchQuery.isNotBlank())
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(members, key = { it.id }) { member ->
                        MemberCard(
                            member = member,
                            onOpen = { onOpenMember(member.id) },
                            onEdit = { onEditMember(member.id) },
                            onDelete = { memberToDelete = member }
                        )
                    }
                }
            }
        }
    }

    memberToDelete?.let { member ->
        DeleteMemberDialog(
            memberName = member.name,
            onConfirm = {
                viewModel.deleteMember(member.id)
                memberToDelete = null
            },
            onDismiss = { memberToDelete = null }
        )
    }
}

@Composable
private fun EmptyState(isSearching: Boolean) {
    val message = if (isSearching) {
        "Tidak ada anggota yang cocok dengan pencarian"
    } else {
        "Tekan tombol + untuk mendaftarkan anggota pertama"
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.LibraryBooks,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Belum ada data anggota", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun MemberCard(
    member: Member,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onOpen)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = member.name, fontWeight = FontWeight.Bold)
                Text(text = member.memberNumber, style = MaterialTheme.typography.bodyLarge)
                Text(text = member.membershipType, style = MaterialTheme.typography.bodyLarge)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
