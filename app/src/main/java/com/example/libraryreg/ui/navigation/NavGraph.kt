package com.example.libraryreg.ui.navigation

object Routes {
    const val SPLASH = "splash"
    const val MEMBER_LIST = "member_list"
    const val ADD_MEMBER = "add_member"
    const val MEMBER_DETAIL = "member_detail/{memberId}"
    const val EDIT_MEMBER = "edit_member/{memberId}"

    fun memberDetail(memberId: String) = "member_detail/$memberId"
    fun editMember(memberId: String) = "edit_member/$memberId"
}
