package uni.project.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class Cashier implements Serializable {
    private final UUID id;
    private String name;
    private BigDecimal monthlySalary;
    private static final long serialVersionUID = 1L;

    public Cashier(String name, BigDecimal monthlySalary){
        validateName(name);
        validateMonthlySalary(monthlySalary);

        id = UUID.randomUUID();
        this.name = name;
        this.monthlySalary = monthlySalary;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public BigDecimal getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(BigDecimal monthlySalary) {
        validateMonthlySalary(monthlySalary);
        this.monthlySalary = monthlySalary;
    }


    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Cashier name cannot be null or empty.");
        }
    }

    private void validateMonthlySalary(BigDecimal monthlySalary) {
        if (monthlySalary == null) {
            throw new IllegalArgumentException("Monthly salary cannot be null.");
        }
        if (monthlySalary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly salary cannot be negative.");
        }
    }

}
