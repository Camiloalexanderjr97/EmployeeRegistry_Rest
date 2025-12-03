package com.example.usuariojwt.domain.repository;

import com.example.usuariojwt.domain.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Empleado, Long> {
    List<Empleado> findByDeletedFalse();
    Optional<Empleado> findByIdAndDeletedFalse(Long id);
    boolean existsByNumeroDocumentoAndDeletedFalse(String numeroDocumento);
    boolean existsByNumeroDocumentoAndIdNotAndDeletedFalse(String numeroDocumento, Long id);
}
