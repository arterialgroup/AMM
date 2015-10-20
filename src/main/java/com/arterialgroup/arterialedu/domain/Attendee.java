package com.arterialgroup.arterialedu.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Attendee.
 */
@Entity
@Table(name = "ATTENDEE")
public class Attendee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "attended")
    private Boolean attended;
    
    @Column(name = "points_awarded")
    private Boolean pointsAwarded;

	@Column(name = "certificate_path")
    private String certificatePath;
	
	@Column(name = "imported")
	private Boolean imported;

	@ManyToOne
    private Meeting meeting;

	@OneToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }
    
    public Boolean getPointsAwarded() {
		return pointsAwarded;
	}

	public void setPointsAwarded(Boolean pointsAwarded) {
		this.pointsAwarded = pointsAwarded;
	}
    
    public String getCertificatePath() {
		return certificatePath;
	}

	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}
	
	public Boolean getImported() {
		return imported;
	}

	public void setImported(Boolean imported) {
		this.imported = imported;
	}

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Attendee attendee = (Attendee) o;

        if ( ! Objects.equals(id, attendee.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Attendee{" +
                "id=" + id +
                ", attended='" + attended + "'" +
                '}';
    }
}
