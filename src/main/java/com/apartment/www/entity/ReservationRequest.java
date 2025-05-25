package com.apartment.www.entity;

import lombok.Data;

@Data
public class ReservationRequest {
    private String date;
    private boolean reserved;
}
