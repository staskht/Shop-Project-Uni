package uni.project.entities;

public class CashRegister {
    private Cashier cashier;

    public CashRegister(Cashier cashier){
        ValidateCashier(cashier);
        this.cashier = cashier;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        ValidateCashier(cashier);
        this.cashier = cashier;
    }

    private void ValidateCashier(Cashier cashier){
        if (cashier == null){
            throw new IllegalArgumentException("Cashier cannot be null");

        }
    }
}
