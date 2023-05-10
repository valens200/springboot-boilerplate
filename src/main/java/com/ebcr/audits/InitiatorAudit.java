package com.ebcr.audits;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.UUID;

public abstract class InitiatorAudit  extends TimeStampAudit {
    private static final Long serialVersionUID = 1L;
    @CreatedBy
    @Column(name = "created_by")
    private UUID createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private UUID updatedBy;
}
