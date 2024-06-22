package com.kit.grs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EsMovement implements Serializable {

    String action;
    String assigned_role;
    Long complaint_id;
    Date created_at;
    String current_status;
    Date deadline_date;
    String from_employee_designation_bng;
    String from_employee_name_bng;
    String from_employee_name_eng;
    Long from_employee_record_id;
    String from_employee_unit_name_bng;
    String from_employee_username;
    Long from_office_id;
    String from_office_name_bng;
    Long from_office_unit_id;
    Long from_office_unit_organogram_id;
    Long id;
    Boolean is_cc;
    Boolean is_committee_head;
    Boolean is_committee_member;
    Boolean is_current;
    Boolean is_seen;
    Date modified_at;
    String note;
    String to_employee_designation_bng;
    String to_employee_name_bng;
    String to_employee_name_eng;
    Long to_employee_record_id;
    String to_employee_unit_name_bng;
    Long to_office_id;
    String to_office_name_bng;
    Long to_office_unit_id;
    Long to_office_unit_organogram_id;
    Long modified_by;
    @JsonProperty(value = "@version")
    Long version;
    @JsonProperty(value = "@timestamp")
    Date timestamp;
    String status;
    String created_by;
}
