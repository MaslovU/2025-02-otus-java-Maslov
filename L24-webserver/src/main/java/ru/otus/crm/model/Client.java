package ru.otus.crm.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.Collections;
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "client")
    @Fetch(FetchMode.SELECT)
    private List<Phone> phones = new ArrayList<>();

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.addAddress(address);
        this.addPhones(phones);
    }

    public Client(Long id, String name) {
        this(id, name, null, Collections.emptyList());
    }

    public Client(String name) {
        this(null, name, null, Collections.emptyList());
    }

    public void addAddress(Address address) {
        if (address != null) {
            var cloneAddress = address.clone();
            cloneAddress.setClient(this);
            this.address = cloneAddress;
        }
    }

    public void addPhone(Phone phone) {
        var clonePhone = phone.clone();
        this.phones.add(clonePhone);
        clonePhone.setClient(this);
    }

    public void addPhones(List<Phone> phone) {
        phone.forEach(this::addPhone);
    }

    @Override
    public Client clone() {
        var copyOfClient = new Client(this.id, this.name);
        phones.forEach(phone -> cloneAndLinkWithClient(phone, copyOfClient));
        if (nonNull(address)) {
            var copyOfAddress = address.clone();
            copyOfClient.addAddress(copyOfAddress);
        }
        return copyOfClient;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }


    private void cloneAndLinkWithClient(Phone phone, Client client) {
        var copyOfPhone = phone.clone();
        client.phones.add(copyOfPhone);
        copyOfPhone.setClient(client);
    }
}
