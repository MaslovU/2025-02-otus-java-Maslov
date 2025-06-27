package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static java.util.Objects.nonNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "client")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;

        if (this.address != null) {
            this.address.setClient(this);
        }
        if (nonNull(this.phones)) {
            this.phones.forEach(p -> p.setClient(this));
        }
    }

    @Override
    public Client clone() {
        Address newAddress = null;
        List<Phone> newPhones = null;

        if (nonNull(this.address)) {
            newAddress = new Address(this.address.getId(), this.address.getStreet());
        }

        if (nonNull(this.phones)) {
            newPhones = this.phones.stream()
                    .map(p -> new Phone(p.getId(), p.getNumber()))
                    .toList();
        }

        return new Client(this.id, this.name, newAddress, newPhones);
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + ", address=" + address + ", phones=" + phones + "}";
    }
}
