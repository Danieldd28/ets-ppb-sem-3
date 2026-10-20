package com.example.libraryreg.ui.screens.memberform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.libraryreg.data.MEMBERSHIP_TYPES
import com.example.libraryreg.data.Member
import com.example.libraryreg.data.MemberErrors
import com.example.libraryreg.data.MemberViewModel
import com.example.libraryreg.data.STUDY_PROGRAMS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberFormScreen(
    viewModel: MemberViewModel,
    memberId: String?,
    onDone: () -> Unit
) {
    val existingMember: Member? = remember(memberId) {
        memberId?.let { viewModel.getMemberById(it) }
    }
    val isEditMode = existingMember != null

    var name by rememberSaveable { mutableStateOf(existingMember?.name ?: "") }
    var memberNumber by rememberSaveable { mutableStateOf(existingMember?.memberNumber ?: "") }
    var studyProgram by rememberSaveable { mutableStateOf(existingMember?.studyProgram ?: "") }
    var address by rememberSaveable { mutableStateOf(existingMember?.address ?: "") }
    var phone by rememberSaveable { mutableStateOf(existingMember?.phone ?: "") }
    var email by rememberSaveable { mutableStateOf(existingMember?.email ?: "") }
    var membershipType by rememberSaveable { mutableStateOf(existingMember?.membershipType ?: "") }
    var errors by remember { mutableStateOf(MemberErrors()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Anggota" else "Formulir Pendaftaran") },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FormTextField("Nama Lengkap", name, errors.name, KeyboardType.Text) { name = it }
            FormTextField("NIM / NIP", memberNumber, errors.memberNumber, KeyboardType.Number) { memberNumber = it }
            FormDropdownField("Program Studi", studyProgram, STUDY_PROGRAMS, errors.studyProgram) { studyProgram = it }
            FormTextField("Alamat", address, errors.address, KeyboardType.Text, singleLine = false) { address = it }
            FormTextField("Nomor Telepon", phone, errors.phone, KeyboardType.Phone) { phone = it }
            FormTextField("Email", email, errors.email, KeyboardType.Email) { email = it }
            FormDropdownField("Jenis Keanggotaan", membershipType, MEMBERSHIP_TYPES, errors.membershipType) {
                membershipType = it
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onDone, modifier = Modifier.weight(1f)) {
                    Text("Batal")
                }
                Button(
                    onClick = {
                        val member = (existingMember ?: Member(
                            name = "", memberNumber = "", studyProgram = "",
                            address = "", phone = "", email = "", membershipType = ""
                        )).copy(
                            name = name.trim(),
                            memberNumber = memberNumber.trim(),
                            studyProgram = studyProgram,
                            address = address.trim(),
                            phone = phone.trim(),
                            email = email.trim(),
                            membershipType = membershipType
                        )
                        errors = viewModel.validate(member)
                        if (!errors.hasError) {
                            viewModel.saveMember(member)
                            onDone()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isEditMode) "Simpan" else "Daftar")
                }
            }
        }
    }
}

@Composable
private fun FormTextField(
    label: String,
    value: String,
    error: String?,
    keyboardType: KeyboardType,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormDropdownField(
    label: String,
    value: String,
    options: List<String>,
    error: String?,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            isError = error != null,
            supportingText = { error?.let { Text(it) } },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
