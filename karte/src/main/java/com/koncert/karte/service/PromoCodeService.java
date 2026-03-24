package com.koncert.karte.service;

import com.koncert.karte.model.PromoCode;
import com.koncert.karte.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    public List<PromoCode> getAll() {
        return promoCodeRepository.findAll();
    }

    public PromoCode getById(Long id) {
        return promoCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promo code not found"));
    }

    public PromoCode getByCode(String code) {
        return promoCodeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Promo code not found"));
    }

    public boolean isValid(String code) {
        return promoCodeRepository.findByCode(code)
                .map(pc -> pc.getStatus().equals("ACTIVE"))
                .orElse(false);
    }

    public PromoCode markAsUsed(String code) {
        PromoCode promoCode = getByCode(code);
        promoCode.setStatus("USED");
        promoCode.setUsedAt(LocalDateTime.now());
        return promoCodeRepository.save(promoCode);
    }

    public PromoCode save(PromoCode promoCode) {
        return promoCodeRepository.save(promoCode);
    }

    public void invalidate(Long ticketId) {
        promoCodeRepository.findByTicketId(ticketId).ifPresent(pc -> {
            pc.setStatus("INVALID");
            promoCodeRepository.save(pc);
        });
    }
}