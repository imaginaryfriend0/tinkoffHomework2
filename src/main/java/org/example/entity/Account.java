package org.example.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="accounts")
@Setter
@Getter
@RequiredArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private long userId;
    private long balance;

    public Account(long userId, long balance) {
        this.userId = userId;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userID='" + userId + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}
