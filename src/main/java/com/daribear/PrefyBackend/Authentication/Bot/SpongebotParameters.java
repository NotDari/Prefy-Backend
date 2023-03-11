package com.daribear.PrefyBackend.Authentication.Bot;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@Table(name = "Spongebot")
@NoArgsConstructor
@AllArgsConstructor
public class SpongebotParameters {
    @Id
    private String botName;
    private Boolean botWorking;
    private Boolean botOn;
    private Boolean bothFeaPop;
    private Long allCounter;
    private Long featuredCounter;
    private Long popularCounter;
    private Integer stopCounter;
    private Integer botInterval;
}
