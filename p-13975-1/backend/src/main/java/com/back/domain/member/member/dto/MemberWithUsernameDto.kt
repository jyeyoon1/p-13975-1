package com.back.domain.member.member.dto

import com.back.domain.member.member.entity.Member

data class MemberWithUsernameDto(
    val username: String
) {
    constructor(member: Member): this(
        username = member.username
    )
}
