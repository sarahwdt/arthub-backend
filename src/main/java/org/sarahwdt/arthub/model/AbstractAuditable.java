package org.sarahwdt.arthub.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.sarahwdt.arthub.model.user.User;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AbstractAuditable<PK extends Serializable>
        extends AbstractPersistable<PK>
        implements Auditable<User, PK, Instant> {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    @Setter(onParam_ = @Nullable)
    private User createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @Setter(onParam_ = @NonNull)
    private Instant createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @Setter(onParam_ = @Nullable)
    private User lastModifiedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    @Setter(onParam_ = @NonNull)
    private Instant lastModifiedDate;

    @Override
    @NonNull
    public Optional<User> getCreatedBy() {
        return Optional.ofNullable(this.createdBy);
    }

    @Override
    @NonNull
    public Optional<Instant> getCreatedDate() {
        return Optional.ofNullable(this.createdDate);
    }

    @Override
    @NonNull
    public Optional<User> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }

    @Override
    @NonNull
    public Optional<Instant> getLastModifiedDate() {
        return Optional.ofNullable(this.lastModifiedDate);
    }
}
