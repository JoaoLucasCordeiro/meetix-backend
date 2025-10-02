package com.meetix.meetix_api.dto.request;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class EventRequest {

    private UUID id_event;
    private String title;
    private String description;
    private String imgUrl;
    private String eventUrl;
    private Boolean remote;
    private Date date;
    private String address;
}