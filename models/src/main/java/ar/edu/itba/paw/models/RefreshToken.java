package ar.edu.itba.paw.models;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(
        name = "refresh_token",
        indexes = {
                @Index(columnList = "refresh_token_id", name = "refresh_token_id_uindex", unique = true),
        }
)
public class RefreshToken extends GenericModel<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_refresh_token_id_seq")
    @SequenceGenerator(sequenceName = "refresh_token_refresh_token_id_seq", name = "refresh_token_refresh_token_id_seq", allocationSize = 1)
    @Column(name = "refresh_token_id")
    private Integer id;
    @Column(name = "token")
    private String token;
    @Column(name = "created_date")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime createdDate;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    protected boolean isSameType(Object o) {
        return o instanceof RefreshToken;
    }
}
