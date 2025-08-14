package com.back.domain.post.post.dto

import com.back.domain.post.post.entity.Post

class PostWithContentDto(
    val content: String
) {
    constructor(post: Post) : this(
        content = post.content
    )
}