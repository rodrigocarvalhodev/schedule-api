package net.rodrigocarvalho.scheduleapi.controller;

import lombok.RequiredArgsConstructor;
import net.rodrigocarvalho.scheduleapi.model.entity.Contact;
import net.rodrigocarvalho.scheduleapi.model.repository.ContactRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ContactController {

    private final ContactRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contact save(@RequestBody @Valid Contact contact) {
        return repository.save(contact);
    }

    @GetMapping
    public Page<Contact> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repository.findAll(pageRequest);
    }

    @GetMapping("{id}")
    public Contact findById(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
    }

    @PutMapping("{id}")
    public Contact updateById(@PathVariable Integer id, @RequestBody @Valid Contact contact) {
        return repository.findById(id)
                .map(result -> repository.save(contact)).
                orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact with id " + id + " not found.")
                );
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    @PatchMapping("{id}/favorite")
    public void favorite(
            @PathVariable Integer id) {
        Optional<Contact> optionalContact = repository.findById(id);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            contact.setFavorite(!contact.getFavorite());
            repository.save(contact);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact with id " + id + " not found.");
        }
    }

    @PutMapping("{id}/photo")
    public byte[] addPhoto(@PathVariable Integer id, @RequestParam("photo") MultipartFile part) {
        Optional<Contact> optionalContact = repository.findById(id);
        return optionalContact.map(contact -> {
            try {
                InputStream inputStream = part.getInputStream();
                byte[] bytes = new byte[(int) part.getSize()];
                IOUtils.readFully(inputStream, bytes);
                contact.setPhoto(bytes);
                repository.save(contact);
                inputStream.close();
                return bytes;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).orElse(null);
    }
}