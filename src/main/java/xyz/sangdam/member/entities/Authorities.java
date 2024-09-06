package xyz.sangdam.member.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.sangdam.member.constants.Authority;

@Data
@Entity
@Builder
@IdClass(AuthoritiesId.class)
@NoArgsConstructor
@AllArgsConstructor
public class Authorities {
    @Id
    @JsonIgnore
    @ManyToOne(fetch= FetchType.LAZY)
    private Member member;

    @Id
    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private Authority authority;
}