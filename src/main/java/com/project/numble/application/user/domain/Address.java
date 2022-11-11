package com.project.numble.application.user.domain;

import com.project.numble.application.common.entity.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_address")
@NoArgsConstructor
public class Address extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "address_name", nullable = false)
    private String addressName;

    @Column(name = "region_depth_1", nullable = false)
    private String regionDepth1;

    @Column(name = "region_depth_2", nullable = false)
    private String regionDepth2;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String addressName, String regionDepth1, String regionDepth2, User user) {
        this.addressName = addressName;
        this.regionDepth1 = regionDepth1;
        this.regionDepth2 = regionDepth2;
        this.user = user;
    }
}
