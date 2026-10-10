package com.learnjavawithcrocsa.store.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "id")
    private Long id;
    @Column(nullable = false, name="name")
    private String name;
    @Column(nullable = false,name = "email")
    private String email;
    @Column(nullable = false, name = "password")
    private String password;


    @OneToMany(mappedBy = "user",cascade ={ CascadeType.PERSIST,CascadeType.REMOVE},orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses=new ArrayList<>();


    public void addAddress(Address address) {
        this.addresses.add(address);
        address.setUser(this);
    }

    public void removerAddress(Address address){
        this.addresses.remove(address);
        address.setUser(null);
    }

    public void addTag(String tagName) {
        var tag = new Tag(tagName);
        this.tags.add(tag);
        tag.getUsers().add(this);
    }
    @ManyToMany
    @JoinTable(
            name = "user_tags",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags=new HashSet<>();


    @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
    private Profile profile;


    @ManyToMany
    @JoinTable(
            name = "wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> favoriteProducts=new HashSet<>();

    public void addFavoriteProduct(Product product) {
        this.favoriteProducts.add(product);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "email = " + email + ")";
    }
}
