package ssi.pai3.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssi.pai3.server.repository.ServerRepository;

@Service
public class ServerService {

    @Autowired
    ServerRepository repository;
}
