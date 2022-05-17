package ssi.pai5.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ssi.pai5.server.model.Certificado;

public interface CertificadoRepository extends JpaRepository<Certificado,Long> {

}