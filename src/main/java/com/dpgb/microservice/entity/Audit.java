package com.dpgb.microservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Table(name = "tbl_audit")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "Action can't be empty")
    private String action;
    @NotNull(message = "Entity id can't be empty")
    private Integer entityId;
    @NotNull(message = "Action date can't be empty")
    private LocalTime actionDate;
    //@NotNull(message = "User ID can't be empty")
    private Integer userId;

    public Audit(@NotNull(message = "Action can't be empty") String action, @NotNull(message = "Entity id can't be empty") Integer entityId, @NotNull(message = "Action date can't be empty") LocalTime actionDate, @NotNull(message = "User ID can't be empty") Integer userId) {
        this.action = action;
        this.entityId = entityId;
        this.actionDate = actionDate;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalTime actionDate) {
        this.actionDate = actionDate;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Audit)) return false;

        Audit audit = (Audit) o;

        return id.equals(audit.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
