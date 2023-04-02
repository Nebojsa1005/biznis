package com.serveup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "postal_code")
    private Long postalCode;

    @JsonIgnoreProperties(value = { "location", "employees", "comments", "ratings", "appUsers" }, allowSetters = true)
    @OneToOne(mappedBy = "location")
    private Job job;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Location id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Location latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Location longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public Location streetAddress(String streetAddress) {
        this.setStreetAddress(streetAddress);
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Long getPostalCode() {
        return this.postalCode;
    }

    public Location postalCode(Long postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(Long postalCode) {
        this.postalCode = postalCode;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job) {
        if (this.job != null) {
            this.job.setLocation(null);
        }
        if (job != null) {
            job.setLocation(this);
        }
        this.job = job;
    }

    public Location job(Job job) {
        this.setJob(job);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", postalCode=" + getPostalCode() +
            "}";
    }
}
