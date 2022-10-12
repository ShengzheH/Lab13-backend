package se331.rest.security.repository;



import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    Authority findByName(AuthorityName input);
}
