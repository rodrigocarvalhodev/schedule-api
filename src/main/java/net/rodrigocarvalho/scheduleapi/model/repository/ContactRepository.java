package net.rodrigocarvalho.scheduleapi.model.repository;

import net.rodrigocarvalho.scheduleapi.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
