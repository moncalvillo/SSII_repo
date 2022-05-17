package ssi.pai5.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssi.pai5.server.model.Certificado;
import ssi.pai5.server.model.Peticion;
import ssi.pai5.server.repository.CertificadoRepository;
import ssi.pai5.server.repository.PeticionRepository;

@Service
public class ServerService {

    @Autowired
    CertificadoRepository certificadoRepository;

    @Autowired
    PeticionRepository peticionRepository;

    public Certificado getCertificado(Long id) {
        return this.certificadoRepository.getById(id);
    }

    public Peticion savePeticion(Peticion peticion) {
        return this.peticionRepository.save(peticion);
    }


    // public boolean userExists(User user) {
    //     User userRegistered = userRepository.findByUsername(user.getUsername()).orElse(null);
    //     return userRegistered != null;  
    // }

    // public boolean verifyUser(User user) {
    //     User userRegistered = userRepository.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    //     return userRegistered != null;  
    // }

    // public Message saveMessage(Message message){
    //     return messageRepository.save(message);
    // }

    // public void createUser(User user) {
    //     userRepository.save(user);
    // }
}
