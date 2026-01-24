package br.com.booksaas.book_reader.user.entity;

import br.com.booksaas.book_reader.readingprogress.entity.ReadingProgress;
import br.com.booksaas.book_reader.book.entity.Book;
import br.com.booksaas.book_reader.usersetting.entity.UserSetting;
import br.com.booksaas.book_reader.order.entity.Order;
import br.com.booksaas.book_reader.role.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name = "Users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "tinyint", length = 1)
    private Boolean isPremium;

    @CreatedDate
    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @ManyToMany
    @JoinTable(
            name = "UserRoles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> userRoleRoles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Book> userBooks = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<ReadingProgress> userReadingProgresses = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Order> userOrders = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserSetting> userUserSettings = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userRoleRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return getEmail();
    }
}
