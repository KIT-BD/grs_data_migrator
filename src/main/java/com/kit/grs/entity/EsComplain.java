package com.kit.grs.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EsComplain implements Serializable {

    Long id;
    String current_status;
    Long custom_layer;
    String grievance_type;
    Long layer_level;
    String medium_of_submission;
    Long office_id;
    Long office_origin;
    Long self_motivated;
    String tracking_number;
}
