package com.meetix.meetix_api.dto.response;

import com.meetix.meetix_api.model.Address;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@Data
public class EventResponse {

    private UUID id;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private Integer maxAttendees;
    private Boolean isPaid;
    private BigDecimal price;
    private Boolean remote;
    private Date date;
    private Address address;

}