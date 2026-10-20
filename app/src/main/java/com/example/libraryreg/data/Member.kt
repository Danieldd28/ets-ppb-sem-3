package com.example.libraryreg.data

import java.util.UUID

data class Member(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val memberNumber: String,
    val studyProgram: String,
    val address: String,
    val phone: String,
    val email: String,
    val membershipType: String
)

val STUDY_PROGRAMS = listOf(
    "Informatika",
    "Sistem Informasi",
    "Teknik Komputer",
    "Desain Komunikasi Visual",
    "Manajemen",
    "Akuntansi"
)

val MEMBERSHIP_TYPES = listOf("Mahasiswa", "Dosen", "Staf")
