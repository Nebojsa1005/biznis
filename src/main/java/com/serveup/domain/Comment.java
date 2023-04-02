package com.serveup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "comment")
    private String comment;

    @ManyToMany(mappedBy = "comments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jobs", "comments", "ratings" }, allowSetters = true)
    private Set<AppUser> appUsers = new HashSet<>();

    @ManyToMany(mappedBy = "comments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location", "employees", "comments", "ratings", "appUsers" }, allowSetters = true)
    private Set<Job> jobs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Comment id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComment() {
        return this.comment;
    }

    public Comment comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<AppUser> getAppUsers() {
        return this.appUsers;
    }

    public void setAppUsers(Set<AppUser> appUsers) {
        if (this.appUsers != null) {
            this.appUsers.forEach(i -> i.removeComment(this));
        }
        if (appUsers != null) {
            appUsers.forEach(i -> i.addComment(this));
        }
        this.appUsers = appUsers;
    }

    public Comment appUsers(Set<AppUser> appUsers) {
        this.setAppUsers(appUsers);
        return this;
    }

    public Comment addAppUser(AppUser appUser) {
        this.appUsers.add(appUser);
        appUser.getComments().add(this);
        return this;
    }

    public Comment removeAppUser(AppUser appUser) {
        this.appUsers.remove(appUser);
        appUser.getComments().remove(this);
        return this;
    }

    public Set<Job> getJobs() {
        return this.jobs;
    }

    public void setJobs(Set<Job> jobs) {
        if (this.jobs != null) {
            this.jobs.forEach(i -> i.removeComment(this));
        }
        if (jobs != null) {
            jobs.forEach(i -> i.addComment(this));
        }
        this.jobs = jobs;
    }

    public Comment jobs(Set<Job> jobs) {
        this.setJobs(jobs);
        return this;
    }

    public Comment addJob(Job job) {
        this.jobs.add(job);
        job.getComments().add(this);
        return this;
    }

    public Comment removeJob(Job job) {
        this.jobs.remove(job);
        job.getComments().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return id != null && id.equals(((Comment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
