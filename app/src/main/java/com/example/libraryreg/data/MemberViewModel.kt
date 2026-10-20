package com.example.libraryreg.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MemberViewModel : ViewModel() {

    private val dummyMembers = listOf(
        Member(
            name = "Andi Pratama", memberNumber = "231001001", studyProgram = "Informatika",
            address = "Jl. Melati No. 12, Surabaya", phone = "081234567801",
            email = "andi.pratama@mail.com", membershipType = "Mahasiswa"
        ),
        Member(
            name = "Siti Aisyah", memberNumber = "231001002", studyProgram = "Sistem Informasi",
            address = "Jl. Kenanga No. 5, Sidoarjo", phone = "081234567802",
            email = "siti.aisyah@mail.com", membershipType = "Mahasiswa"
        ),
        Member(
            name = "Budi Santoso", memberNumber = "198501001", studyProgram = "Teknik Komputer",
            address = "Jl. Anggrek No. 20, Surabaya", phone = "081234567803",
            email = "budi.santoso@mail.com", membershipType = "Dosen"
        ),
        Member(
            name = "Rina Lestari", memberNumber = "231001003", studyProgram = "Manajemen",
            address = "Jl. Mawar No. 8, Gresik", phone = "081234567804",
            email = "rina.lestari@mail.com", membershipType = "Mahasiswa"
        )
    )

    private val _members = MutableStateFlow(dummyMembers)
    val members: StateFlow<List<Member>> = _members.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredMembers: StateFlow<List<Member>> = combine(_members, _searchQuery) { list, query ->
        if (query.isBlank()) {
            list
        } else {
            list.filter {
                it.name.contains(query, ignoreCase = true) ||
                    it.memberNumber.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), dummyMembers)

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun getMemberById(id: String): Member? = _members.value.find { it.id == id }

    fun validate(member: Member): MemberErrors = validateMember(member, _members.value)

    fun saveMember(member: Member) {
        val isExisting = _members.value.any { it.id == member.id }
        _members.value = if (isExisting) {
            _members.value.map { if (it.id == member.id) member else it }
        } else {
            _members.value + member
        }
    }

    fun deleteMember(id: String) {
        _members.value = _members.value.filterNot { it.id == id }
    }
}
