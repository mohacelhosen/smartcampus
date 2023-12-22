package com.smartcampus.Institution.service;

import com.smartcampus.exception.IncompleteDataException;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.Institution.model.Institution;
import com.smartcampus.Institution.repository.InstitutionRepository;
import com.smartcampus.exception.RegistrationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InstitutionService {
    @Autowired
    private InstitutionRepository institutionRepository;

    public Institution register(Institution institution) {
        if (institution.getInstitutionName() != null && !institution.getInstitutionName().isEmpty()) {
            String institutionCode = String.valueOf(UUID.randomUUID().getMostSignificantBits()).replace("-", "").substring(0, 7);
            Optional<Institution> optional = institutionRepository.findByInstitutionCode(institutionCode);
            if (optional.isPresent()){
                institutionCode = String.valueOf(UUID.randomUUID().getMostSignificantBits()).replace("-", "").substring(0, 7);
            }
            institution.setInstitutionCode(institutionCode);
            try {
                return institutionRepository.save(institution);
            } catch (Exception e) {
                throw new RegistrationFailedException("Failed to register institution" + e);
            }
        } else {
            throw new IncompleteDataException("Institution name can't be null");
        }
    }


    public Institution findByInstitutionByCode(String institutionCode) {
        if (institutionCode != null && !institutionCode.isEmpty()) {
            return institutionRepository.findByInstitutionCode(institutionCode).orElseThrow(() -> {
                throw new NotFoundException("Institution not found. ID: " + institutionCode);
            });
        } else {
            throw new IncompleteDataException("Institution institutionCode can't be null");
        }
    }

    public List<Institution> getAllInstitution() {
        return institutionRepository.findAll();
    }

    public String deleteInstitutionByCode(String institutionCode) {
        Optional<Institution> institution = institutionRepository.findByInstitutionCode(institutionCode);
        if (institution.isPresent()) {
            Institution dbInstitution = institution.get();
            institutionRepository.delete(dbInstitution);
            return institution.get().getInstitutionName() + ", Delete successfully💣🏫";
        } else {
            throw new IncompleteDataException("Institution institutionCode can't be null");
        }
    }
}
