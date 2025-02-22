package com.myblog.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EssayBriefDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String essayId;
    private String essayTitle;
    private Integer status;
    private String classId;
}

