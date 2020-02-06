package personal.project.springinfra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.project.springinfra.database.repository.CredentialRepository;
import personal.project.springinfra.exception.NoSuchRecordException;
import personal.project.springinfra.model.domain.Credential;

import java.util.Optional;

@Service
public class CredentialService extends BaseService {

    @Autowired
    private CredentialRepository credentialRepository;

    public Credential find(long id) {
        Optional<Credential> optional = this.credentialRepository.findById(id);
        return optional.orElseThrow(() -> new NoSuchRecordException(String.format("User with id [%d] doesn't exist", id)));
    }
}
