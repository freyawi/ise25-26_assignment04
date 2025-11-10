package de.seuhd.campuscoffee.domain.model;

import lombok.Builder;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class CashierAccount {
    private Long id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private BigDecimal tipBalance;
    private LocalDateTime lastLogin;
    private boolean active;
    @NonNull
    private CashierRole role;
}