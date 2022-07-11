package com.example.mypayments.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    private String name;
    private String surname;
    @OneToMany(mappedBy = "holder", cascade = CascadeType.ALL)
    private List<BankAccount> bankAccounts;
    @OneToMany(mappedBy = "holder", cascade = CascadeType.ALL)
    private List<BankCard> bankCards;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Role> roles;
}
