package com.back.domain.post.postComment.dto

import com.back.domain.post.postComment.entity.PostComment
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl.createDate
import lombok.Getter
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.time.LocalDateTime

class PostCommentDto(
    val id: Int,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val authorId: Int,
    val authorName: String,
    val postId: Int,
    val content: String,
) {
    constructor(postComment: PostComment) : this(
        id = postComment.id,
        createDate = postComment.createDate,
        modifyDate = postComment.modifyDate,
        authorId = postComment.author.id,
        authorName = postComment.author.name,
        postId = postComment.post.id,
        content = postComment.content,
    )
}