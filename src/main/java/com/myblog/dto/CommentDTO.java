package com.myblog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CommentDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String id;
    private String essayId;
    private String content;
    private String senderUserId;
    private String senderUsername;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentTime;
    private Integer commentLikeNum;
    private String replyCommentId;
}
