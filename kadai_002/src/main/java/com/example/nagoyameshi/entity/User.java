package com.example.nagoyameshi.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "mail_address", nullable = false, unique = true, length =40)
    private String mailAddress;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    
    @Column(name = "name", nullable = false, length = 30)
    private String name;
    
    @Column(name = "phone_number", nullable = false, length =30)
	private String phoneNumber;
    
    @Column(name = "postal_code", nullable = false, length =10)
    private String postalCode;
    
    @Column(name = "address", nullable = false, length =100)
    private String address;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true; // デフォルト値をtrueに設定
    
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "fk_users_roles"))
    private Role role;
    
    @Column(name = "subscription_id", length = 255)
    private String subscriptionId;
    
}
