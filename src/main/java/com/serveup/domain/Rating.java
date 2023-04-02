package com.serveup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Rating.
 */
@Entity
@Table(name = "rating")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "rating", precision = 21, scale = 2)
    private BigDecimal rating;

    @ManyToMany(mappedBy = "ratings")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jobs", "comments", "ratings" }, allowSetters = true)
    private Set<AppUser> appUsers = new HashSet<>();

    @ManyToMany(mappedBy = "ratings")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location", "employees", "comments", "ratings", "appUsers" }, allowSetters = true)
    private Set<Job> jobs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Rating id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getRating() {
        return this.rating;
    }

    public Rating rating(BigDecimal rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Set<AppUser> getAppUsers() {
        return this.appUsers;
    }

    public void setAppUsers(Set<AppUser> appUsers) {
        if (this.appUsers != null) {
            this.appUsers.forEach(i -> i.removeRating(this));
        }
        if (appUsers != null) {
            appUsers.forEach(i -> i.addRating(this));
        }
        this.appUsers = appUsers;
    }

    public Rating appUsers(Set<AppUser> appUsers) {
        this.setAppUsers(appUsers);
        return this;
    }

    public Rating addAppUser(AppUser appUser) {
        this.appUsers.add(appUser);
        appUser.getRatings().add(this);
        return this;
    }

    public Rating removeAppUser(AppUser appUser) {
        this.appUsers.remove(appUser);
        appUser.getRatings().remove(this);
        return this;
    }

    public Set<Job> getJobs() {
        return this.jobs;
    }

    public void setJobs(Set<Job> jobs) {
        if (this.jobs != null) {
            this.jobs.forEach(i -> i.removeRating(this));
        }
        if (jobs != null) {
            jobs.forEach(i -> i.addRating(this));
        }
        this.jobs = jobs;
    }

    public Rating jobs(Set<Job> jobs) {
        this.setJobs(jobs);
        return this;
    }

    public Rating addJob(Job job) {
        this.jobs.add(job);
        job.getRatings().add(this);
        return this;
    }

    public Rating removeJob(Job job) {
        this.jobs.remove(job);
        job.getRatings().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rating)) {
            return false;
        }
        return id != null && id.equals(((Rating) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rating{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            "}";
    }
}
