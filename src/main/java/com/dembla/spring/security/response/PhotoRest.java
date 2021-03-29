package com.dembla.spring.security.response;

import lombok.Data;

@Data
public class PhotoRest {

    private String userId;
    private String photoId;
    private String albumId;
    private String photoTitle;
    private String photoDescription;
    private String photoUrl;

}
