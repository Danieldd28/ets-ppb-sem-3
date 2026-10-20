package com.example.libraryreg.data

data class MemberErrors(
    val name: String? = null,
    val memberNumber: String? = null,
    val studyProgram: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val membershipType: String? = null
) {
    val hasError: Boolean
        get() = listOf(name, memberNumber, studyProgram, address, phone, email, membershipType)
            .any { it != null }
}

private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

fun validateMember(member: Member, others: List<Member>): MemberErrors = MemberErrors(
    name = requiredError(member.name, "Nama lengkap"),
    memberNumber = memberNumberError(member, others),
    studyProgram = requiredError(member.studyProgram, "Program studi"),
    address = requiredError(member.address, "Alamat"),
    phone = phoneError(member.phone),
    email = emailError(member.email),
    membershipType = requiredError(member.membershipType, "Jenis keanggotaan")
)

private fun requiredError(value: String, label: String): String? =
    if (value.isBlank()) "$label wajib diisi" else null

private fun memberNumberError(member: Member, others: List<Member>): String? {
    val number = member.memberNumber
    return when {
        number.isBlank() -> "NIM/NIP wajib diisi"
        !number.all { it.isDigit() } -> "NIM/NIP hanya boleh angka"
        number.length !in 8..18 -> "NIM/NIP harus 8-18 digit"
        others.any { it.id != member.id && it.memberNumber == number } -> "NIM/NIP sudah terdaftar"
        else -> null
    }
}

private fun phoneError(phone: String): String? = when {
    phone.isBlank() -> "Nomor telepon wajib diisi"
    !phone.all { it.isDigit() } -> "Nomor telepon hanya boleh angka"
    phone.length !in 10..13 -> "Nomor telepon harus 10-13 digit"
    else -> null
}

private fun emailError(email: String): String? = when {
    email.isBlank() -> "Email wajib diisi"
    !EMAIL_REGEX.matches(email) -> "Format email tidak valid"
    else -> null
}
