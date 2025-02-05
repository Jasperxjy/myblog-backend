package com.myblog.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
@Data
public class AddTagsToEssayDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String essayId;
    private List<String> tagIds;

}
